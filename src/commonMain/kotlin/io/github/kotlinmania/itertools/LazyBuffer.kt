// port-lint: source src/lazy_buffer.rs
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
class LazyBuffer<T> internal constructor(
    private val it: Iterator<T>,
    private val sourceHint: SizeHint,
) {
    private val buffer: MutableList<T> = mutableListOf()
    private var consumed: Int = 0
    private var sourceExhausted: Boolean = false

    constructor(iterable: Iterable<T>) : this(
        iterable.iterator(),
        when (iterable) {
            is Collection<*> -> iterable.size to iterable.size
            else -> 0 to null
        },
    )

    /** Number of buffered (already-pulled) elements. */
    val length: Int
        get() = buffer.size

    /** Equivalent to upstream `LazyBuffer::size_hint`. */
    fun sizeHint(): SizeHint = addScalar(subScalar(sourceHint, consumed), length)

    /**
     * Drain the remaining source and return total length. Equivalent to
     * upstream `LazyBuffer::count` (which consumes self).
     */
    fun count(): Int {
        while (pullOne()) { /* drain */ }
        return length
    }

    private fun pullOne(): Boolean {
        if (sourceExhausted) return false
        if (!it.hasNext()) {
            sourceExhausted = true
            return false
        }
        buffer.add(it.next())
        consumed += 1
        return true
    }

    /**
     * Pull one more element from the source into the buffer. Returns `true`
     * if an element was buffered, `false` if the source is exhausted.
     */
    fun getNext(): Boolean = pullOne()

    /**
     * Buffer up to [len] elements, pulling from the source as needed. After
     * this returns, [length] is at least `min(len, totalSourceSize)`.
     */
    fun prefill(len: Int) {
        while (buffer.size < len && pullOne()) { /* fill */ }
    }

    /** Indexed access into the buffered prefix. */
    operator fun get(index: Int): T = buffer[index]

    /**
     * Returns a fresh list of the buffered elements at the given indices.
     *
     * Upstream Rust takes `&[usize]` and clones each element; in Kotlin
     * reference semantics make the clone implicit unless the element type
     * itself needs deep copying, which is the caller's responsibility.
     */
    fun getAt(indices: IntArray): List<T> = indices.map { buffer[it] }
}
