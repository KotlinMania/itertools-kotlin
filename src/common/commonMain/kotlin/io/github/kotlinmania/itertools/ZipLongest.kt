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
    private val aList: List<A>? = null,
    private val bList: List<B>? = null,
) : Iterator<EitherOrBoth<A, B>> {
    private var aStart: Int = 0
    private var aEnd: Int = aList?.size ?: 0
    private var bStart: Int = 0
    private var bEnd: Int = bList?.size ?: 0
    private var aExhausted: Boolean = false
    private var bExhausted: Boolean = false
    private var peeked: EitherOrBoth<A, B>? = null
    private var consumed: Int = 0

    constructor(aList: List<A>, bList: List<B>) : this(
        a = aList.iterator(),
        b = bList.iterator(),
        aHint = SizeHint(aList.size, aList.size),
        bHint = SizeHint(bList.size, bList.size),
        aList = aList,
        bList = bList,
    )

    private fun advance() {
        if (peeked != null) return
        if (aList != null && bList != null) {
            val aHas = aStart < aEnd
            val bHas = bStart < bEnd
            if (!aHas && !bHas) return
            peeked =
                when {
                    aHas && bHas -> EitherOrBoth.Both(aList[aStart++], bList[bStart++])
                    aHas -> EitherOrBoth.Left(aList[aStart++])
                    bHas -> EitherOrBoth.Right(bList[bStart++])
                    else -> null
                }
            return
        }
        if (aExhausted && bExhausted) return
        if (!aExhausted && a.hasNext()) {
            val aNext = a.next()
            if (!bExhausted && b.hasNext()) {
                peeked = EitherOrBoth.Both(aNext, b.next())
            } else {
                bExhausted = true
                peeked = EitherOrBoth.Left(aNext)
            }
        } else {
            aExhausted = true
            if (!bExhausted && b.hasNext()) {
                peeked = EitherOrBoth.Right(b.next())
            } else {
                bExhausted = true
                peeked = null
            }
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
        if (aList == null || bList == null) {
            return null
        }
        val aRemaining = aEnd - aStart
        val bRemaining = bEnd - bStart
        return when {
            aRemaining <= 0 && bRemaining <= 0 -> {
                if (peeked != null) next() else null
            }

            aRemaining > bRemaining -> {
                EitherOrBoth.Left(aList[--aEnd])
            }

            bRemaining > aRemaining -> {
                EitherOrBoth.Right(bList[--bEnd])
            }

            else -> {
                val aVal = aList[--aEnd]
                val bVal = bList[--bEnd]
                EitherOrBoth.Both(aVal, bVal)
            }
        }
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        if (aList != null && bList != null) {
            val aRemaining = (aEnd - aStart).coerceAtLeast(0)
            val bRemaining = (bEnd - bStart).coerceAtLeast(0)
            val maxRemaining = maxOf(aRemaining, bRemaining)
            val buffered = if (peeked != null) 1 else 0
            return SizeHint(maxRemaining + buffered, maxRemaining + buffered)
        }
        return max(subScalar(aHint, consumed), subScalar(bHint, consumed))
    }

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
        if (aList != null && bList != null) {
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
