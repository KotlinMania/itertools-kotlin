// port-lint: source merge_join.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor that merges the two base iterators in ascending order.
 * If both base iterators are sorted (ascending), the result is sorted.
 *
 * See [merge] and [mergeBy] for more information.
 */
class MergeBy<T>(
    private val left: Iterator<T>,
    private val right: Iterator<T>,
    private val isLte: (T, T) -> Boolean,
    private val leftHint: SizeHint = SizeHint(0, null),
    private val rightHint: SizeHint = SizeHint(0, null),
) : Iterator<T> {
    private val leftBuf: ArrayDeque<T> = ArrayDeque(1)
    private val rightBuf: ArrayDeque<T> = ArrayDeque(1)
    private var consumed: Int = 0

    private fun fillPending() {
        if (leftBuf.isEmpty() && left.hasNext()) {
            leftBuf.addLast(left.next())
        }
        if (rightBuf.isEmpty() && right.hasNext()) {
            rightBuf.addLast(right.next())
        }
    }

    override fun hasNext(): Boolean {
        fillPending()
        return leftBuf.isNotEmpty() || rightBuf.isNotEmpty()
    }

    override fun next(): T {
        fillPending()
        if (leftBuf.isEmpty() && rightBuf.isEmpty()) {
            throw NoSuchElementException("MergeBy exhausted")
        }
        consumed += 1
        return if (leftBuf.isNotEmpty() && rightBuf.isNotEmpty()) {
            val l = leftBuf.first()
            val r = rightBuf.first()
            if (isLte(l, r)) {
                leftBuf.removeFirst()
            } else {
                rightBuf.removeFirst()
            }
        } else if (leftBuf.isNotEmpty()) {
            leftBuf.removeFirst()
        } else {
            rightBuf.removeFirst()
        }
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = subScalar(add(leftHint, rightHint), consumed)
}

/**
 * An iterator adaptor that merge-joins items from the two base iterators in ascending order.
 *
 * See [mergeJoinBy] for more information.
 */
class MergeJoinBy<L, R>(
    private val left: Iterator<L>,
    private val right: Iterator<R>,
    private val cmp: (L, R) -> Int,
    private val leftHint: SizeHint = SizeHint(0, null),
    private val rightHint: SizeHint = SizeHint(0, null),
) : Iterator<EitherOrBoth<L, R>> {
    private val leftBuf: ArrayDeque<L> = ArrayDeque(1)
    private val rightBuf: ArrayDeque<R> = ArrayDeque(1)

    private fun fillPending() {
        if (leftBuf.isEmpty() && left.hasNext()) {
            leftBuf.addLast(left.next())
        }
        if (rightBuf.isEmpty() && right.hasNext()) {
            rightBuf.addLast(right.next())
        }
    }

    override fun hasNext(): Boolean {
        fillPending()
        return leftBuf.isNotEmpty() || rightBuf.isNotEmpty()
    }

    override fun next(): EitherOrBoth<L, R> {
        fillPending()
        if (leftBuf.isEmpty() && rightBuf.isEmpty()) {
            throw NoSuchElementException("MergeJoinBy exhausted")
        }
        return if (leftBuf.isNotEmpty() && rightBuf.isNotEmpty()) {
            val l = leftBuf.first()
            val r = rightBuf.first()
            val c = cmp(l, r)
            when {
                c == 0 -> {
                    leftBuf.removeFirst()
                    rightBuf.removeFirst()
                    EitherOrBoth.Both(l, r)
                }
                c < 0 -> {
                    EitherOrBoth.Left(leftBuf.removeFirst())
                }
                else -> {
                    EitherOrBoth.Right(rightBuf.removeFirst())
                }
            }
        } else if (leftBuf.isNotEmpty()) {
            EitherOrBoth.Left(leftBuf.removeFirst())
        } else {
            EitherOrBoth.Right(rightBuf.removeFirst())
        }
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val lower = maxOf(leftHint.lower, rightHint.lower)
        val upper =
            if (leftHint.upper != null && rightHint.upper != null) {
                val sum = leftHint.upper.toLong() + rightHint.upper.toLong()
                if (sum > Int.MAX_VALUE.toLong()) null else sum.toInt()
            } else {
                null
            }
        return SizeHint(lower, upper)
    }
}

/**
 * Create an iterator that merges elements in `i` and `j`.
 */
fun <T : Comparable<T>> merge(i: Iterable<T>, j: Iterable<T>): MergeBy<T> {
    val lHint = if (i is Collection<*>) SizeHint(i.size, i.size) else SizeHint(0, null)
    val rHint = if (j is Collection<*>) SizeHint(j.size, j.size) else SizeHint(0, null)
    return MergeBy(i.iterator(), j.iterator(), { a, b -> a <= b }, lHint, rHint)
}

/**
 * Create an iterator that merges elements in `i` and `j`.
 */
fun <T : Comparable<T>> merge(i: Iterator<T>, j: Iterator<T>): MergeBy<T> =
    MergeBy(i, j, { a, b -> a <= b })

/**
 * Create an iterator that merges elements in `i` and `j` using a custom comparison predicate.
 */
fun <T> mergeBy(i: Iterable<T>, j: Iterable<T>, isLte: (T, T) -> Boolean): MergeBy<T> {
    val lHint = if (i is Collection<*>) SizeHint(i.size, i.size) else SizeHint(0, null)
    val rHint = if (j is Collection<*>) SizeHint(j.size, j.size) else SizeHint(0, null)
    return MergeBy(i.iterator(), j.iterator(), isLte, lHint, rHint)
}

/**
 * Create an iterator that merges elements in `i` and `j` using a custom comparison predicate.
 */
fun <T> mergeBy(i: Iterator<T>, j: Iterator<T>, isLte: (T, T) -> Boolean): MergeBy<T> =
    MergeBy(i, j, isLte)

/**
 * Return an iterator adaptor that merge-joins items from the two base iterators in ascending order.
 */
fun <L, R> mergeJoinBy(
    left: Iterable<L>,
    right: Iterable<R>,
    cmp: (L, R) -> Int,
): MergeJoinBy<L, R> {
    val lHint = if (left is Collection<*>) SizeHint(left.size, left.size) else SizeHint(0, null)
    val rHint = if (right is Collection<*>) SizeHint(right.size, right.size) else SizeHint(0, null)
    return MergeJoinBy(left.iterator(), right.iterator(), cmp, lHint, rHint)
}

/**
 * Return an iterator adaptor that merge-joins items from the two base iterators in ascending order.
 */
fun <L, R> mergeJoinBy(
    left: Iterator<L>,
    right: Iterator<R>,
    cmp: (L, R) -> Int,
): MergeJoinBy<L, R> =
    MergeJoinBy(left, right, cmp)
