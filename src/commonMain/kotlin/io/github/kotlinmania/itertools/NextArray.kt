// port-lint: source src/next_array.rs
package io.github.kotlinmania.itertools

/**
 * A list of at most [n] elements.
 *
 * Invariants:
 *
 * - `arr.size <= n` at all times.
 * - All elements currently present in `arr` are valid `T`s.
 *
 * Kotlin note: the upstream Rust uses `[MaybeUninit<T>; N]` so it can hold an
 * exactly-sized backing store while only the leading `len` elements are
 * initialized. Kotlin has no analog for `MaybeUninit` and no const generics,
 * so this port uses a growable [MutableList] whose `size` doubles as the
 * upstream `len`. Memory is reclaimed when [take] returns the contents to
 * the caller, which matches the upstream behavior of resetting `len = 0`
 * and replacing the backing array.
 */
internal class ArrayBuilder<T>(private val n: Int) {
    private val arr: MutableList<T> = ArrayList(n)

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

    /** Equivalent to upstream `AsMut<[T]>::as_mut`. */
    fun asMut(): MutableList<T> = arr
}

/** Equivalent to `it.next_array()`. */
internal fun <T> nextArray(source: Iterator<T>, n: Int): List<T>? {
    val builder = ArrayBuilder<T>(n)
    repeat(n) {
        if (!source.hasNext()) return null
        builder.push(source.next())
    }
    return builder.take()
}
