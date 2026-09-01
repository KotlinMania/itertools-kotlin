// port-lint: source next_array.rs
package io.github.kotlinmania.itertools

/**
 * A list of at most [n] elements.
 *
 * Invariants:
 *
 * - `arr.size <= n` at all times.
 * - All elements currently present in `arr` are valid `T`s.
 */
internal class ArrayBuilder<T>(
    private val n: Int,
) {
    private val arr: MutableList<T> = ArrayList(n)

    /**
     * Initializes a new, empty `ArrayBuilder`.
     */
    companion object {
        fun <T> new(n: Int): ArrayBuilder<T> = ArrayBuilder(n)
    }

    /**
     * Pushes [value] onto the end of the list.
     *
     * Panics if `len >= n`.
     */
    fun push(value: T) {
        // PANICS: This will throw if `arr.size >= n`, matching the upstream
        // out-of-bounds indexing panic.
        if (arr.size >= n) {
            throw IndexOutOfBoundsException("ArrayBuilder push at capacity $n")
        }
        arr.add(value)
    }

    /**
     * Consumes the elements in the `ArrayBuilder` and returns them as a list
     * of exactly [n] elements.
     *
     * If `len < n`, this returns `null`.
     */
    fun take(): List<T>? {
        if (arr.size != n) return null
        val out = ArrayList(arr)
        arr.clear()
        return out
    }

    /** Returns a mutable view of the underlying list. */
    fun asMut(): MutableList<T> = arr

    /** Assuming all elements are initialized, get a mutable slice to them. */
    fun sliceAssumeInitMut(): MutableList<T> = arr

    /** Explicit destructor for resources. */
    fun drop() {
        arr.clear()
    }
}

/** Returns the next [n] elements from the iterator as a list, or null if fewer remain. */
internal fun <T> nextArray(source: Iterator<T>, n: Int): List<T>? {
    val builder = ArrayBuilder<T>(n)
    repeat(n) {
        if (!source.hasNext()) return null
        builder.push(source.next())
    }
    return builder.take()
}

/** Marker type for tracking drops. */
internal class TrackedDrop

/** Track drop helper function. */
internal fun trackedDrop() {}

