// port-lint: source combinations.rs
package io.github.kotlinmania.itertools

/**
 * A type holding indices of elements in a pool or buffer of items from an inner iterator
 * and used to pick out different combinations in a generic way.
 */
interface PoolIndex<T, R> {
    /** Extracts an item from the pool corresponding to current indices. */
    fun extractItem(pool: List<T>): R

    /** Returns the length of the indices. */
    fun len(): Int

    /** Number of elements in the indices. */
    val size: Int get() = len()

    /** Gets the index at position [i]. */
    operator fun get(i: Int): Int

    /** Sets the index at position [i] to [value]. */
    operator fun set(i: Int, value: Int)

    /** Returns an IntArray copy of the indices. */
    fun toIntArray(): IntArray
}

/**
 * PoolIndex implementation for List<T> results.
 */
class ListPoolIndex<T>(
    var indices: IntArray,
) : PoolIndex<T, List<T>> {
    override fun extractItem(pool: List<T>): List<T> = pool

    override fun len(): Int = indices.size

    override operator fun get(i: Int): Int = indices[i]

    override operator fun set(i: Int, value: Int) {
        indices[i] = value
    }

    override fun toIntArray(): IntArray = indices

    fun reset(k: Int) {
        indices = IntArray(k) { it }
    }
}

/**
 * PoolIndex implementation for array-like results.
 */
class ArrayPoolIndex<T>(
    private val indices: IntArray,
) : PoolIndex<T, List<T>> {
    override fun extractItem(pool: List<T>): List<T> = pool

    override fun len(): Int = indices.size

    override operator fun get(i: Int): Int = indices[i]

    override operator fun set(i: Int, value: Int) {
        indices[i] = value
    }

    override fun toIntArray(): IntArray = indices
}

/**
 * An iterator to iterate through all combinations in an iterator in a generic way.
 *
 * See [combinations] and [arrayCombinations] for more information.
 */
open class CombinationsGeneric<T, Idx : PoolIndex<T, R>, R>(
    iter: Iterator<T>,
    internal val indices: Idx,
    sourceHint: SizeHint = SizeHint(0, null),
) : Iterator<R> {
    private val pool: LazyBuffer<T> = LazyBuffer(iter, sourceHint)
    protected var first: Boolean = true

    /** Returns the length of a combination produced by this iterator. */
    fun k(): Int = indices.len()

    /** Returns the length of a combination produced by this iterator. */
    fun len(): Int = indices.len()

    /**
     * Returns the current length of the pool from which combination elements are selected.
     */
    fun n(): Int = pool.length

    internal fun src(): LazyBuffer<T> = pool

    internal fun nAndCount(): Pair<Int, Int> {
        val n = pool.count()
        return Pair(n, remainingFor(n, first, indices.toIntArray()) ?: 0)
    }

    protected fun init(): Boolean {
        pool.prefill(k())
        val done = k() > n()
        if (!done) {
            first = false
        }
        return done
    }

    protected fun incrementIndices(): Boolean {
        if (indices.len() == 0) {
            return true
        }
        var i = indices.len() - 1
        if (indices[i] == pool.length - 1) {
            pool.getNext()
        }

        while (indices[i] == i + pool.length - indices.len()) {
            if (i > 0) {
                i -= 1
            } else {
                return true
            }
        }

        indices[i] += 1
        for (j in (i + 1) until indices.len()) {
            indices[j] = indices[j - 1] + 1
        }
        return false
    }

    internal fun tryNthResult(n: Int): ItemResult<R, Int> {
        val done =
            if (first) {
                init()
            } else {
                incrementIndices()
            }
        if (done) return ItemResult.Err(0)
        for (i in 0 until n) {
            if (incrementIndices()) {
                return ItemResult.Err(i + 1)
            }
        }
        return ItemResult.Ok(indices.extractItem(pool.getAt(indices.toIntArray())))
    }

    internal fun tryNth(n: Int): R? =
        when (val res = tryNthResult(n)) {
            is ItemResult.Ok -> res.value
            is ItemResult.Err -> null
        }

    /** Returns the n-th combination without iterating through the preceding ones manually. */
    fun nth(n: Int): R? = tryNth(n)

    /** Returns the total count of remaining combinations. */
    fun count(): Int = nAndCount().second

    override fun hasNext(): Boolean {
        if (first) {
            pool.prefill(k())
            return k() <= n()
        }
        if (indices.len() == 0) return false
        var i = indices.len() - 1
        if (indices[i] == pool.length - 1) {
            pool.getNext()
        }
        while (i >= 0 && indices[i] == i + pool.length - indices.len()) {
            if (i > 0) i -= 1 else return false
        }
        return true
    }

    override fun next(): R {
        val done =
            if (first) {
                init()
            } else {
                incrementIndices()
            }
        if (done) {
            throw NoSuchElementException("Combinations exhausted")
        }
        return indices.extractItem(pool.getAt(indices.toIntArray()))
    }

    /** Size hint for remaining combinations. */
    fun sizeHint(): SizeHint {
        val (low, upp) = pool.sizeHint()
        val rLow = remainingFor(low, first, indices.toIntArray()) ?: Int.MAX_VALUE
        val rUpp = upp?.let { remainingFor(it, first, indices.toIntArray()) }
        return SizeHint(rLow, rUpp)
    }

    internal fun resetPool(newK: Int) {
        first = true
        pool.prefill(newK)
    }
}

/**
 * An iterator to iterate through all the `k`-length combinations in an iterator.
 *
 * See [combinations] for more information.
 */
class Combinations<T>(
    iter: Iterator<T>,
    kVal: Int,
    sourceHint: SizeHint = SizeHint(0, null),
) : CombinationsGeneric<T, ListPoolIndex<T>, List<T>>(
        iter,
        ListPoolIndex(IntArray(kVal) { it }),
        sourceHint,
    ) {
    companion object {
        fun <T> new(iter: Iterator<T>, k: Int, hint: SizeHint = SizeHint(0, null)): Combinations<T> =
            Combinations(iter, k, hint)
    }

    internal fun reset(newK: Int) {
        first = true
        indices.reset(newK)
        resetPool(newK)
    }
}

/**
 * An iterator for fixed-size combinations returned by [arrayCombinations].
 */
typealias ArrayCombinations<T> = Combinations<T>

/**
 * Calculates binomial coefficient (n choose k), or null if overflow occurs.
 */
internal fun checkedBinomial(n: Int, k: Int): Int? {
    if (n < k || k < 0 || n < 0) return 0
    var nVar = n
    val kVar = (n - k).coerceAtMost(k)
    var c: Long = 1
    for (i in 1..kVar) {
        val term1 = (c / i) * nVar
        val term2 = ((c % i) * nVar) / i
        val sum = term1 + term2
        if (sum > Int.MAX_VALUE.toLong()) return null
        c = sum
        nVar -= 1
    }
    return c.toInt()
}

/**
 * For a given size `n`, return the count of remaining combinations or null if it would overflow.
 */
private fun remainingFor(n: Int, first: Boolean, indices: IntArray): Int? {
    val k = indices.size
    if (n < k) return 0
    if (first) return checkedBinomial(n, k)

    var sum = 0
    for (i in indices.indices) {
        val n0 = indices[i]
        val bin = checkedBinomial(n - 1 - n0, k - i) ?: return null
        val newSum = sum.toLong() + bin.toLong()
        if (newSum > Int.MAX_VALUE.toLong()) return null
        sum = newSum.toInt()
    }
    return sum
}

/**
 * Create a new [Combinations] iterator adaptor.
 */
fun <T> combinations(iterable: Iterable<T>, k: Int): Combinations<T> {
    val hint =
        when (iterable) {
            is Collection<*> -> SizeHint(iterable.size, iterable.size)
            is IntProgression -> {
                val count =
                    if (iterable.step > 0) {
                        if (iterable.first <= iterable.last) (iterable.last - iterable.first) / iterable.step + 1 else 0
                    } else {
                        if (iterable.first >= iterable.last) (iterable.first - iterable.last) / (-iterable.step) + 1 else 0
                    }
                SizeHint(count, count)
            }
            else -> SizeHint(0, null)
        }
    return Combinations(iterable.iterator(), k, hint)
}

/**
 * Create a new [Combinations] iterator adaptor from an iterator.
 */
fun <T> combinations(iter: Iterator<T>, k: Int, hint: SizeHint = SizeHint(0, null)): Combinations<T> =
    Combinations(iter, k, hint)

/**
 * Create a new [ArrayCombinations] iterator adaptor.
 */
fun <T> arrayCombinations(iterable: Iterable<T>, k: Int): Combinations<T> =
    combinations(iterable, k)

/**
 * Create a new [ArrayCombinations] iterator adaptor from an iterator.
 */
fun <T> arrayCombinations(iter: Iterator<T>, k: Int, hint: SizeHint = SizeHint(0, null)): Combinations<T> =
    Combinations(iter, k, hint)
