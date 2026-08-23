// port-lint: source src/groupbylazy.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that groups consecutive elements with the same key together.
 *
 * Each group is returned as a `Pair<K, List<T>>`.
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
        val first = if (pending.isNotEmpty()) {
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
 * An iterator that yields chunks of a given size.
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
