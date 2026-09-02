// port-lint: source lazy_buffer.rs
package io.github.kotlinmania.itertools

/**
 * Buffers the source iterator on demand. Each call to [getNext] pulls one
 * element from the source (if available) and appends it to the internal
 * buffer; previously buffered elements remain accessible via [get] and
 * [getAt].
 *
 * Used by the combinations/permutations adaptors that need stable, indexed
 * access to a prefix of an otherwise once-passable iterator.
 */
internal class LazyBuffer<T>(
    private val it: Iterator<T>,
    private val sourceHint: SizeHint,
) {
    private val buffer: MutableList<T> = mutableListOf()
    private var consumed: Int = 0
    private var sourceExhausted: Boolean = false

    constructor(iterable: Iterable<T>) : this(
        iterable.iterator(),
        when (iterable) {
            is Collection<*> -> SizeHint(iterable.size, iterable.size)
            is IntProgression -> {
                val count =
                    if (iterable.step > 0) {
                        if (iterable.first <= iterable.last) (iterable.last - iterable.first) / iterable.step + 1 else 0
                    } else {
                        if (iterable.first >= iterable.last) (iterable.first - iterable.last) / (-iterable.step) + 1 else 0
                    }
                SizeHint(count, count)
            }
            else -> SizeHint(0, null)
        },
    )

    /** Number of buffered (already-pulled) elements. */
    fun len(): Int = buffer.size

    /** Number of buffered (already-pulled) elements. */
    val length: Int
        get() = buffer.size

    /** Returns the size hint for the buffer. */
    fun sizeHint(): SizeHint = addScalar(subScalar(sourceHint, consumed), length)

    /**
     * Drain the remaining source and return total length.
     */
    fun count(): Int {
        while (getNext()) { /* drain */ }
        return length
    }

    /**
     * Pull one more element from the source into the buffer. Returns `true`
     * if an element was buffered, `false` if the source is exhausted.
     */
    fun getNext(): Boolean {
        if (sourceExhausted) return false
        return if (it.hasNext()) {
            val x = it.next()
            buffer.add(x)
            consumed += 1
            true
        } else {
            sourceExhausted = true
            false
        }
    }

    /**
     * Buffer up to [len] elements, pulling from the source as needed. After
     * this returns, [length] is at least `min(len, totalSourceSize)`.
     */
    fun prefill(len: Int) {
        val bufferLen = buffer.size
        if (len > bufferLen) {
            val delta = len - bufferLen
            var count = 0
            while (count < delta && getNext()) {
                count += 1
            }
        }
    }

    /** Indexed access into the buffered prefix. */
    operator fun get(index: Int): T = buffer[index]

    /** Index into the buffered prefix. */
    fun index(index: Int): T = buffer[index]

    /**
     * Returns a fresh list of the buffered elements at the given indices.
     */
    fun getAt(indices: IntArray): List<T> = indices.map { buffer[it] }

    /**
     * Returns a list of the buffered elements at the given indices array.
     */
    fun getArray(indices: IntArray): List<T> = indices.map { buffer[it] }

    companion object {
        /** Create a new [LazyBuffer] from an iterator. */
        fun <T> new(it: Iterator<T>, sourceHint: SizeHint = SizeHint(0, null)): LazyBuffer<T> =
            LazyBuffer(it, sourceHint)

        /** Create a new [LazyBuffer] from an iterable. */
        fun <T> new(iterable: Iterable<T>): LazyBuffer<T> = LazyBuffer(iterable)
    }
}
