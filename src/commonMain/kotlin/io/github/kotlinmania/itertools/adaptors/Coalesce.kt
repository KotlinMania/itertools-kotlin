// port-lint: source adaptors/coalesce.rs
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

/**
 * An iterator adaptor that may join together adjacent elements.
 *
 * See [coalesce] for more information.
 */
class CoalesceBy<T>(
    private val iter: Iterator<T>,
    private val f: CoalescePredicate<T>,
) : Iterator<T> {
    private val lastBuf: ArrayDeque<T> = ArrayDeque(1)

    override fun hasNext(): Boolean = lastBuf.isNotEmpty() || iter.hasNext()

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("CoalesceBy exhausted")
        }
        var current: T =
            if (lastBuf.isNotEmpty()) {
                lastBuf.removeFirst()
            } else {
                iter.next()
            }

        while (iter.hasNext()) {
            val nextItem = iter.next()
            when (val res = f.coalescePair(current, nextItem)) {
                is CoalesceResult.Merged -> {
                    current = res.merged
                }
                is CoalesceResult.Separate -> {
                    lastBuf.addLast(res.second)
                    return res.first
                }
            }
        }
        return current
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
        fun <T> new(iter: Iterator<T>, f: CoalescePredicate<T>): CoalesceBy<T> = CoalesceBy(iter, f)
        fun <T> new(iter: Iterator<T>, f: (T, T) -> CoalesceResult<T>): CoalesceBy<T> = CoalesceBy(iter, CoalescePredicate(f))
    }
}

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
 * An iterator adaptor that removes repeated duplicates, determining equality using a comparison function.
 *
 * See [dedupBy] for more information.
 */
class DedupBy<T>(
    private val iter: Iterator<T>,
    private val same: DedupPredicate<T>,
) : Iterator<T> {
    private val coalesceIter =
        CoalesceBy(iter) { a, b ->
            if (same.dedupPair(a, b)) {
                CoalesceResult.Merged(a)
            } else {
                CoalesceResult.Separate(a, b)
            }
        }

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
    DedupBy(iter) { a, b -> a == b }

/**
 * Create a new `dedup` iterator from an [Iterable] removing repeated equal items.
 */
fun <T> dedup(iterable: Iterable<T>): DedupBy<T> =
    DedupBy(iterable.iterator()) { a, b -> a == b }

/**
 * An iterator adaptor that removes repeated duplicates, while keeping a count of how many
 * repeated elements were present.
 */
class DedupByWithCount<T>(
    private val iter: Iterator<T>,
    private val same: DedupPredicate<T>,
) : Iterator<Pair<Int, T>> {
    private val lastBuf: ArrayDeque<Pair<Int, T>> = ArrayDeque(1)

    override fun hasNext(): Boolean = lastBuf.isNotEmpty() || iter.hasNext()

    override fun next(): Pair<Int, T> {
        if (!hasNext()) {
            throw NoSuchElementException("DedupByWithCount exhausted")
        }
        var count = 1
        var item: T =
            if (lastBuf.isNotEmpty()) {
                val l = lastBuf.removeFirst()
                count = l.first
                l.second
            } else {
                iter.next()
            }

        while (iter.hasNext()) {
            val nextItem = iter.next()
            if (same.dedupPair(item, nextItem)) {
                count += 1
            } else {
                lastBuf.addLast(Pair(1, nextItem))
                return Pair(count, item)
            }
        }
        return Pair(count, item)
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
    DedupByWithCount(iter) { a, b -> a == b }

/**
 * Create a new `dedupWithCount` iterator from an [Iterable].
 */
fun <T> dedupWithCount(iterable: Iterable<T>): DedupByWithCount<T> =
    DedupByWithCount(iterable.iterator()) { a, b -> a == b }
