// port-lint: source itertools/src/adaptors/coalesce.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

/**
 * Result of attempting to coalesce two adjacent elements.
 */
sealed class CoalesceResult<out T> {
    /** The two items were successfully merged into [merged]. */
    data class Merged<out T>(
        val merged: T,
    ) : CoalesceResult<T>()

    /** The two items could not be merged and remain separate. */
    data class Separate<out T>(
        val first: T,
        val second: T,
    ) : CoalesceResult<T>()
}

/**
 * Predicate interface for coalescing adjacent items.
 */
fun interface CoalescePredicate<T> {
    /** Attempt to coalesce a pair of items. */
    fun coalescePair(t: T, item: T): CoalesceResult<T>
}

/** Marker type indicating no element count is maintained. */
object NoCount

/** Marker type indicating an element count is maintained. */
object WithCount

/**
 * An iterator adaptor that may join together adjacent elements.
 *
 * See [coalesce] for more information.
 */
class CoalesceBy<T>(
    private val iter: Iterator<T>,
    private val f: CoalescePredicate<T>,
) : Iterator<T> {
    private var last: T? = null
    private var hasLast: Boolean = false
    private var exhausted: Boolean = false

    override fun hasNext(): Boolean = hasLast || (!exhausted && iter.hasNext())

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("CoalesceBy exhausted")
        }
        var accum: T =
            if (hasLast) {
                hasLast = false
                val l = last!!
                last = null
                l
            } else {
                iter.next()
            }

        while (iter.hasNext()) {
            val nextItem = iter.next()
            when (val res = f.coalescePair(accum, nextItem)) {
                is CoalesceResult.Merged -> {
                    accum = res.merged
                }
                is CoalesceResult.Separate -> {
                    last = res.second
                    hasLast = true
                    return res.first
                }
            }
        }
        exhausted = true
        return accum
    }

    /** Size hint for the coalesce iterator. */
    fun sizeHint(): SizeHint = SizeHint(if (hasNext()) 1 else 0, null)

    /** Consumes the iterator and folds elements with [init] and [fnAcc]. */
    fun <Acc> fold(init: Acc, fnAcc: (Acc, T) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = fnAcc(acc, next())
        }
        return acc
    }

    companion object {
        fun <T> new(iter: Iterator<T>, f: CoalescePredicate<T>): CoalesceBy<T> =
            CoalesceBy(iter, f)

        fun <T> new(iter: Iterator<T>, f: (T, T) -> CoalesceResult<T>): CoalesceBy<T> =
            CoalesceBy(iter, CoalescePredicate(f))
    }
}

/**
 * An iterator adaptor that may join together adjacent elements.
 */
typealias Coalesce<T> = CoalesceBy<T>

/**
 * Create a new [CoalesceBy] iterator adaptor.
 */
fun <T> coalesce(iter: Iterator<T>, f: (T, T) -> CoalesceResult<T>): CoalesceBy<T> =
    CoalesceBy(iter, CoalescePredicate(f))

/**
 * Create a new [CoalesceBy] iterator adaptor from an [Iterable].
 */
fun <T> coalesce(iterable: Iterable<T>, f: (T, T) -> CoalesceResult<T>): CoalesceBy<T> =
    CoalesceBy(iterable.iterator(), CoalescePredicate(f))

/**
 * Predicate interface for deduplicating items.
 */
fun interface DedupPredicate<T> {
    /** Determine whether two items should be considered duplicates. */
    fun dedupPair(a: T, b: T): Boolean
}

/**
 * Equality predicate using standard equals comparison.
 */
class DedupEq<T> : DedupPredicate<T> {
    override fun dedupPair(a: T, b: T): Boolean = a == b
}

/**
 * Adapts a [DedupPredicate] into a [CoalescePredicate] without counting.
 */
class DedupPred2CoalescePred<T>(
    private val pred: DedupPredicate<T>,
) : CoalescePredicate<T> {
    override fun coalescePair(t: T, item: T): CoalesceResult<T> =
        if (pred.dedupPair(t, item)) {
            CoalesceResult.Merged(t)
        } else {
            CoalesceResult.Separate(t, item)
        }
}

/**
 * Adapts a [DedupPredicate] into a pair coalesce calculation with counting.
 */
class DedupPredWithCount2CoalescePred<T>(
    private val pred: DedupPredicate<T>,
) {
    fun coalescePair(t: Pair<Int, T>, item: T): CoalesceResult<Pair<Int, T>> =
        if (pred.dedupPair(t.second, item)) {
            CoalesceResult.Merged(Pair(t.first + 1, t.second))
        } else {
            CoalesceResult.Separate(t, Pair(1, item))
        }
}

/**
 * An iterator adaptor that removes repeated duplicates, determining equality using a comparison function.
 *
 * See [dedupBy] for more information.
 */
class DedupBy<T>(
    private val iter: Iterator<T>,
    private val same: DedupPredicate<T>,
) : Iterator<T> {
    private val coalesceIter = CoalesceBy(iter, DedupPred2CoalescePred(same))

    override fun hasNext(): Boolean = coalesceIter.hasNext()

    override fun next(): T = coalesceIter.next()

    /** Size hint for the dedup iterator. */
    fun sizeHint(): SizeHint = coalesceIter.sizeHint()

    /** Consumes the iterator and folds elements with [init] and [fnAcc]. */
    fun <Acc> fold(init: Acc, fnAcc: (Acc, T) -> Acc): Acc = coalesceIter.fold(init, fnAcc)

    companion object {
        fun <T> new(iter: Iterator<T>, same: DedupPredicate<T>): DedupBy<T> = DedupBy(iter, same)

        fun <T> new(iter: Iterator<T>, same: (T, T) -> Boolean): DedupBy<T> = DedupBy(iter, DedupPredicate(same))
    }
}

/**
 * An iterator adaptor that removes repeated duplicates.
 */
typealias Dedup<T> = DedupBy<T>

/**
 * Create a new [DedupBy] iterator.
 */
fun <T> dedupBy(iter: Iterator<T>, same: (T, T) -> Boolean): DedupBy<T> =
    DedupBy(iter, DedupPredicate(same))

/**
 * Create a new [DedupBy] iterator from an [Iterable].
 */
fun <T> dedupBy(iterable: Iterable<T>, same: (T, T) -> Boolean): DedupBy<T> =
    DedupBy(iterable.iterator(), DedupPredicate(same))

/**
 * Create a new `dedup` iterator removing repeated equal items.
 */
fun <T> dedup(iter: Iterator<T>): DedupBy<T> =
    DedupBy(iter, DedupEq())

/**
 * Create a new `dedup` iterator from an [Iterable] removing repeated equal items.
 */
fun <T> dedup(iterable: Iterable<T>): DedupBy<T> =
    DedupBy(iterable.iterator(), DedupEq())

/**
 * An iterator adaptor that removes repeated duplicates, while keeping a count of how many
 * repeated elements were present.
 */
class DedupByWithCount<T>(
    private val iter: Iterator<T>,
    private val same: DedupPredicate<T>,
) : Iterator<Pair<Int, T>> {
    private val pred = DedupPredWithCount2CoalescePred(same)
    private var last: Pair<Int, T>? = null
    private var hasLast: Boolean = false
    private var exhausted: Boolean = false

    override fun hasNext(): Boolean = hasLast || (!exhausted && iter.hasNext())

    override fun next(): Pair<Int, T> {
        if (!hasNext()) {
            throw NoSuchElementException("DedupByWithCount exhausted")
        }
        var accum: Pair<Int, T> =
            if (hasLast) {
                hasLast = false
                val l = last!!
                last = null
                l
            } else {
                Pair(1, iter.next())
            }

        while (iter.hasNext()) {
            val nextItem = iter.next()
            when (val res = pred.coalescePair(accum, nextItem)) {
                is CoalesceResult.Merged -> {
                    accum = res.merged
                }
                is CoalesceResult.Separate -> {
                    last = res.second
                    hasLast = true
                    return res.first
                }
            }
        }
        exhausted = true
        return accum
    }

    /** Size hint for the dedup with count iterator. */
    fun sizeHint(): SizeHint = SizeHint(if (hasNext()) 1 else 0, null)

    /** Consumes the iterator and folds elements with [init] and [fnAcc]. */
    fun <Acc> fold(init: Acc, fnAcc: (Acc, Pair<Int, T>) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = fnAcc(acc, next())
        }
        return acc
    }

    companion object {
        fun <T> new(iter: Iterator<T>, same: DedupPredicate<T>): DedupByWithCount<T> = DedupByWithCount(iter, same)

        fun <T> new(iter: Iterator<T>, same: (T, T) -> Boolean): DedupByWithCount<T> = DedupByWithCount(iter, DedupPredicate(same))
    }
}

/**
 * An iterator adaptor that removes repeated duplicates, while keeping a count of how many
 * repeated elements were present.
 */
typealias DedupWithCount<T> = DedupByWithCount<T>

/**
 * Create a new [DedupByWithCount] iterator.
 */
fun <T> dedupByWithCount(iter: Iterator<T>, same: (T, T) -> Boolean): DedupByWithCount<T> =
    DedupByWithCount(iter, DedupPredicate(same))

/**
 * Create a new [DedupByWithCount] iterator from an [Iterable].
 */
fun <T> dedupByWithCount(iterable: Iterable<T>, same: (T, T) -> Boolean): DedupByWithCount<T> =
    DedupByWithCount(iterable.iterator(), DedupPredicate(same))

/**
 * Create a new `dedupWithCount` iterator.
 */
fun <T> dedupWithCount(iter: Iterator<T>): DedupByWithCount<T> =
    DedupByWithCount(iter, DedupEq())

/**
 * Create a new `dedupWithCount` iterator from an [Iterable].
 */
fun <T> dedupWithCount(iterable: Iterable<T>): DedupByWithCount<T> =
    DedupByWithCount(iterable.iterator(), DedupEq())
