// port-lint: source zip_longest.rs
package io.github.kotlinmania.itertools

import kotlin.jvm.JvmName

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
    private val aDoubleEnded: ListIterator<A>? = null,
    private val bDoubleEnded: ListIterator<B>? = null,
) : Iterator<EitherOrBoth<A, B>> {
    private var aExhausted: Boolean = false
    private var bExhausted: Boolean = false
    private var peeked: EitherOrBoth<A, B>? = null
    private var consumed: Int = 0

    constructor(aList: List<A>, bList: List<B>) : this(
        aList.iterator(),
        bList.iterator(),
        SizeHint(aList.size, aList.size),
        SizeHint(bList.size, bList.size),
        aList.listIterator(aList.size),
        bList.listIterator(bList.size),
    )

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

    /**
     * Returns the next element from the back when double-ended iteration is available.
     */
    fun nextBack(): EitherOrBoth<A, B>? {
        val aDe = aDoubleEnded
        val bDe = bDoubleEnded
        if (aDe == null || bDe == null) {
            return null
        }
        val aRemaining = aDe.previousIndex() + 1
        val bRemaining = bDe.previousIndex() + 1
        return when {
            aRemaining == 0 && bRemaining == 0 -> null
            aRemaining > bRemaining -> EitherOrBoth.Left(aDe.previous())
            bRemaining > aRemaining -> EitherOrBoth.Right(bDe.previous())
            else -> EitherOrBoth.Both(aDe.previous(), bDe.previous())
        }
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = max(subScalar(aHint, consumed), subScalar(bHint, consumed))

    /** Consumes the iterator and folds elements with [init] and [f]. */
    fun <R> fold(init: R, f: (R, EitherOrBoth<A, B>) -> R): R {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }

    /** Folds elements in reverse order. */
    fun <R> rfold(init: R, f: (R, EitherOrBoth<A, B>) -> R): R {
        val aDe = aDoubleEnded
        val bDe = bDoubleEnded
        if (aDe != null && bDe != null) {
            var acc = init
            while (true) {
                val item = nextBack() ?: break
                acc = f(acc, item)
            }
            return acc
        }
        val items = asSequence().toList()
        var acc = init
        for (i in items.indices.reversed()) {
            acc = f(acc, items[i])
        }
        return acc
    }
}

/**
 * Create a new [ZipLongest] iterator.
 */
@JvmName("zipLongestIterable")
fun <A, B> zipLongest(a: Iterable<A>, b: Iterable<B>): ZipLongest<A, B> {
    if (a is List<A> && b is List<B>) {
        return ZipLongest(a, b)
    }
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
@JvmName("zipLongestIter")
fun <A, B> zipLongest(a: Iterator<A>, b: Iterator<B>, aHint: SizeHint = SizeHint(0, null), bHint: SizeHint = SizeHint(0, null)): ZipLongest<A, B> =
    ZipLongest(a, b, aHint, bHint)

/**
 * Create an iterator which iterates over both this and the specified iterator simultaneously, yielding [EitherOrBoth] pairs.
 */
fun <A, B> Iterator<A>.zipLongest(other: Iterator<B>): ZipLongest<A, B> =
    zipLongest(this, other)

/**
 * Create an iterator which iterates over both this and the specified iterable simultaneously, yielding [EitherOrBoth] pairs.
 */
fun <A, B> Iterable<A>.zipLongest(other: Iterable<B>): ZipLongest<A, B> =
    zipLongest(this, other)
