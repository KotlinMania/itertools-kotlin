// port-lint: source src/zip_longest.rs
package io.github.kotlinmania.itertools

/**
 * An iterator which iterates two other iterators simultaneously
 * and wraps the elements in [EitherOrBoth].
 *
 * See [zipLongest] for more information.
 */
class ZipLongest<A, B>(
    private val a: Iterator<A>,
    private val b: Iterator<B>,
    private val aHint: SizeHint = SizeHint(0, null),
    private val bHint: SizeHint = SizeHint(0, null),
) : Iterator<EitherOrBoth<A, B>> {
    private var aExhausted: Boolean = false
    private var bExhausted: Boolean = false
    private var peeked: EitherOrBoth<A, B>? = null
    private var consumed: Int = 0

    private fun advance() {
        if (peeked != null || (aExhausted && bExhausted)) return
        val aNext = if (!aExhausted && a.hasNext()) a.next() else null.also { aExhausted = true }
        val bNext = if (!bExhausted && b.hasNext()) b.next() else null.also { bExhausted = true }

        peeked =
            when {
                aNext != null && bNext != null -> EitherOrBoth.Both(aNext, bNext)
                aNext != null -> EitherOrBoth.Left(aNext)
                bNext != null -> EitherOrBoth.Right(bNext)
                else -> null
            }
    }

    override fun hasNext(): Boolean {
        advance()
        return peeked != null
    }

    override fun next(): EitherOrBoth<A, B> {
        advance()
        val current =
            peeked
                ?: throw NoSuchElementException("ZipLongest exhausted")
        peeked = null
        consumed += 1
        return current
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = max(subScalar(aHint, consumed), subScalar(bHint, consumed))
}

/**
 * Create a new [ZipLongest] iterator.
 */
fun <A, B> zipLongest(a: Iterable<A>, b: Iterable<B>): ZipLongest<A, B> {
    val aHint =
        when (a) {
            is Collection<*> -> SizeHint(a.size, a.size)
            else -> SizeHint(0, null)
        }
    val bHint =
        when (b) {
            is Collection<*> -> SizeHint(b.size, b.size)
            else -> SizeHint(0, null)
        }
    return ZipLongest(a.iterator(), b.iterator(), aHint, bHint)
}

/**
 * Create a new [ZipLongest] iterator from iterators.
 */
fun <A, B> zipLongest(a: Iterator<A>, b: Iterator<B>, aHint: SizeHint = SizeHint(0, null), bHint: SizeHint = SizeHint(0, null)): ZipLongest<A, B> =
    ZipLongest(a, b, aHint, bHint)
