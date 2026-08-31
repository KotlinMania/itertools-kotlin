// port-lint: source groupbylazy.rs
package io.github.kotlinmania.itertools

/**
 * A trait to unify `FnMut` for `ChunkBy` with the chunk key in `IntoChunks`.
 */
interface KeyFunction<A, K> {
    fun callMut(_arg: A): K
}

/**
 * `ChunkIndex` acts like the grouping key function for `IntoChunks`.
 */
class ChunkIndex(
    val size: Int,
    var index: Int = 0,
    var key: Int = 0,
) : KeyFunction<Any?, Int> {
    override fun callMut(_arg: Any?): Int {
        if (index == size) {
            key += 1
            index = 0
        }
        index += 1
        return key
    }

    companion object {
        fun new(size: Int): ChunkIndex = ChunkIndex(size)
    }
}

/**
 * Internals for lazy grouping and chunking.
 */
internal class GroupInner<K, T>(
    private val keyFn: (T) -> K,
    private val iter: Iterator<T>,
) {
    private var currentKey: K? = null
    private var currentElt: T? = null

    /** Flag set if iterator is exhausted */
    private var done: Boolean = false

    /** Index of group we are currently buffering or visiting */
    private var topGroup: Int = 0

    /** Least index for which we still have elements buffered */
    private var oldestBufferedGroup: Int = 0

    /** Group index for buffer[0] */
    private var bottomGroup: Int = 0

    /** Buffered groups */
    private val buffer: MutableList<MutableList<T>> = mutableListOf()

    /** Index of last group iter that was dropped */
    private var droppedGroup: Int = -1

    /**
     * Take the next element from the iterator, and set the done flag if exhausted.
     */
    fun nextElement(): T? {
        if (done) return null
        return if (iter.hasNext()) {
            iter.next()
        } else {
            done = true
            null
        }
    }

    /**
     * `client`: Index of group that requests next element
     */
    fun step(client: Int): T? =
        if (client < oldestBufferedGroup) {
            null
        } else if (client < topGroup || (client == topGroup && buffer.size > topGroup - bottomGroup)) {
            lookupBuffer(client)
        } else if (done) {
            null
        } else if (topGroup == client) {
            stepCurrent()
        } else {
            stepBuffering(client)
        }

    fun lookupBuffer(client: Int): T? {
        val bufidx = client - bottomGroup
        if (bufidx in buffer.indices && buffer[bufidx].isNotEmpty()) {
            return buffer[bufidx].removeAt(0)
        }
        return null
    }

    fun stepBuffering(client: Int): T? {
        val group = mutableListOf<T>()
        currentElt?.let {
            if (topGroup != droppedGroup) {
                group.add(it)
            }
            currentElt = null
        }
        var firstElt: T? = null
        while (true) {
            val elt = nextElement() ?: break
            val key = keyFn(elt)
            val oldKey = currentKey
            if (oldKey != null && oldKey != key) {
                currentKey = key
                firstElt = elt
                break
            }
            currentKey = key
            if (topGroup != droppedGroup) {
                group.add(elt)
            }
        }
        if (topGroup != droppedGroup) {
            pushNextGroup(group)
        }
        if (firstElt != null) {
            topGroup += 1
        }
        return firstElt
    }

    fun pushNextGroup(group: MutableList<T>) {
        buffer.add(group)
    }

    /**
     * This is the immediate case, where we use no buffering.
     */
    fun stepCurrent(): T? {
        currentElt?.let {
            currentElt = null
            return it
        }
        val elt = nextElement() ?: return null
        val key = keyFn(elt)
        val oldKey = currentKey
        if (oldKey != null && oldKey != key) {
            currentKey = key
            currentElt = elt
            topGroup += 1
            return null
        }
        currentKey = key
        return elt
    }

    /**
     * Request the just started groups' key.
     * `client`: Index of group
     */
    fun groupKey(client: Int): K? {
        val oldKey = currentKey
        val elt = nextElement()
        if (elt != null) {
            val key = keyFn(elt)
            if (oldKey != null && oldKey != key) {
                topGroup += 1
            }
            currentKey = key
            currentElt = elt
        }
        return oldKey
    }

    /**
     * Called when a group is dropped.
     */
    fun dropGroup(client: Int) {
        if (droppedGroup == -1 || client > droppedGroup) {
            droppedGroup = client
        }
    }
}

/**
 * Deprecated alias for [ChunkBy].
 */
@Deprecated("Use ChunkBy instead", ReplaceWith("ChunkBy<K, T>"))
typealias GroupBy<K, T> = ChunkBy<K, T>

/**
 * `ChunkBy` is the storage for the lazy grouping operation.
 *
 * If the groups are consumed in their original order, or if each
 * group is dropped without keeping it around, then `ChunkBy` uses
 * no allocations. It needs allocations only if several group iterators
 * are alive at the same time.
 *
 * Each group is returned as a `Pair<K, List<T>>`.
 *
 * See [chunkBy] for more information.
 */
class ChunkBy<K, T>(
    private val iter: Iterator<T>,
    private val keySelector: (T) -> K,
) : Iterator<Pair<K, List<T>>> {
    private val pending: ArrayDeque<T> = ArrayDeque(1)

    override fun hasNext(): Boolean = pending.isNotEmpty() || iter.hasNext()

    override fun next(): Pair<K, List<T>> {
        if (!hasNext()) {
            throw NoSuchElementException("ChunkBy exhausted")
        }
        val first =
            if (pending.isNotEmpty()) {
                pending.removeFirst()
            } else {
                iter.next()
            }

        val key = keySelector(first)
        val group = mutableListOf(first)

        while (iter.hasNext()) {
            val item = iter.next()
            val itemKey = keySelector(item)
            if (itemKey == key) {
                group.add(item)
            } else {
                pending.addLast(item)
                break
            }
        }

        return Pair(key, group)
    }
}

/**
 * An iterator that yields the Group iterators.
 *
 * Iterator element type is `(K, Group)`:
 * the group's key `K` and the group's iterator.
 *
 * See [chunkBy] for more information.
 */
class Groups<K, T>(
    val parent: ChunkBy<K, T>,
) : Iterator<Pair<K, List<T>>> by parent

/**
 * An iterator for the elements in a single group.
 *
 * Iterator element type is `T`.
 */
class Group<K, T>(
    val key: K,
    val elements: List<T>,
) : Iterator<T> by elements.iterator()

/**
 * `IntoChunks` is the storage for a lazy chunking operation.
 *
 * `IntoChunks` behaves just like `ChunkBy`: it is iterable, and
 * it only buffers if several chunk iterators are alive at the same time.
 *
 * Iterator element type is `List<T>`, each chunk's elements.
 *
 * See [chunks] for more information.
 */
class IntoChunks<T>(
    private val iter: Iterator<T>,
    private val size: Int,
) : Iterator<List<T>> {
    init {
        require(size > 0) { "Chunk size must be positive" }
    }

    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("IntoChunks exhausted")
        }
        val chunk = mutableListOf<T>()
        while (chunk.size < size && iter.hasNext()) {
            chunk.add(iter.next())
        }
        return chunk
    }
}

/**
 * An iterator that yields the Chunk iterators.
 *
 * Iterator element type is `Chunk`.
 *
 * See [chunks] for more information.
 */
typealias Chunks<T> = IntoChunks<T>

/**
 * An iterator for the elements in a single chunk.
 *
 * Iterator element type is `T`.
 */
class Chunk<T>(
    val elements: List<T>,
) : Iterator<T> by elements.iterator()

/**
 * Create a new [ChunkBy] instance.
 */
fun <K, T> new(iter: Iterator<T>, f: (T) -> K): ChunkBy<K, T> =
    ChunkBy(iter, f)

/**
 * Create a new [IntoChunks] instance.
 */
fun <T> newChunks(iter: Iterator<T>, size: Int): IntoChunks<T> =
    IntoChunks(iter, size)

/**
 * Group consecutive elements of `iterable` that have the same key produced by `keySelector`.
 */
fun <K, T> chunkBy(iterable: Iterable<T>, keySelector: (T) -> K): ChunkBy<K, T> =
    ChunkBy(iterable.iterator(), keySelector)

/**
 * Group consecutive elements of `iterator` that have the same key produced by `keySelector`.
 */
fun <K, T> chunkBy(iterator: Iterator<T>, keySelector: (T) -> K): ChunkBy<K, T> =
    ChunkBy(iterator, keySelector)

/**
 * Group consecutive elements of `iterable` that have the same key produced by `keySelector`.
 */
fun <K, T> groupBy(iterable: Iterable<T>, keySelector: (T) -> K): ChunkBy<K, T> =
    ChunkBy(iterable.iterator(), keySelector)

/**
 * Group consecutive elements of `iterator` that have the same key produced by `keySelector`.
 */
fun <K, T> groupBy(iterator: Iterator<T>, keySelector: (T) -> K): ChunkBy<K, T> =
    ChunkBy(iterator, keySelector)

/**
 * Split elements of `iterable` into chunks of given `size`.
 */
fun <T> chunks(iterable: Iterable<T>, size: Int): IntoChunks<T> =
    IntoChunks(iterable.iterator(), size)

/**
 * Split elements of `iterator` into chunks of given `size`.
 */
fun <T> chunks(iterator: Iterator<T>, size: Int): IntoChunks<T> =
    IntoChunks(iterator, size)
