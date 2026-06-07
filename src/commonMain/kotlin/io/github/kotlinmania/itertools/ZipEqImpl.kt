// port-lint: source src/zip_eq_impl.rs
package io.github.kotlinmania.itertools

/**
 * A pair of elements produced by [zipEq].
 */
internal data class Zipped<A, B>(val first: A, val second: B)

/**
 * An iterator which iterates two other iterators simultaneously
 * and throws if they have different lengths.
 *
 * See [zipEq] for more information.
 */
internal class ZipEq<A, B>(
    private val a: Iterator<A>,
    private val b: Iterator<B>,
    private val aHint: SizeHint,
    private val bHint: SizeHint,
) : Iterator<Zipped<A, B>> {

    private var peeked: Zipped<A, B>? = null
    private var exhausted: Boolean = false
    private var consumed: Int = 0

    private fun advance() {
        if (peeked != null || exhausted) return
        val aHas = a.hasNext()
        val bHas = b.hasNext()
        when {
            !aHas && !bHas -> {
                exhausted = true
            }
            aHas && bHas -> {
                peeked = Zipped(a.next(), b.next())
            }
            else -> {
                throw IllegalStateException(
                    "itertools: .zipEq() reached end of one iterator before the other",
                )
            }
        }
    }

    override fun hasNext(): Boolean {
        advance()
        return peeked != null
    }

    override fun next(): Zipped<A, B> {
        advance()
        val current = peeked
            ?: throw NoSuchElementException("ZipEq exhausted")
        peeked = null
        consumed += 1
        return current
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    fun sizeHint(): SizeHint = min(subScalar(aHint, consumed), subScalar(bHint, consumed))
}

/**
 * Zips two iterables but **throws** if they are not of the same length.
 *
 * `Iterable`-enabled version of `Itertools.zipEq` (when present).
 *
 * ```
 * val data = listOf(1, 2, 3, 4, 5)
 * for (z in zipEq(data.subList(0, data.size - 1), data.subList(1, data.size))) {
 *     // z.first and z.second are adjacent elements
 * }
 * ```
 */
internal fun <A, B> zipEq(i: Iterable<A>, j: Iterable<B>): Iterator<Zipped<A, B>> =
    ZipEq(i.iterator(), j.iterator(), sizeHintOf(i), sizeHintOf(j))

internal fun <A, B> zipEq(i: Iterator<A>, j: Iterator<B>, aHint: SizeHint, bHint: SizeHint): ZipEq<A, B> =
    ZipEq(i, j, aHint, bHint)

private fun sizeHintOf(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> SizeHint(it.size, it.size)
    else -> SizeHint(0, null)
}
