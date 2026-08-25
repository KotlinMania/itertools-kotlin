// port-lint: source combinations.rs
package io.github.kotlinmania.itertools

/**
 * An iterator to iterate through all the `k`-length combinations in an iterator.
 *
 * See [combinations] for more information.
 */
class Combinations<T>(
    iter: Iterator<T>,
    private var kVal: Int,
    sourceHint: SizeHint = SizeHint(0, null),
) : Iterator<List<T>> {
    private var indices: IntArray = IntArray(kVal) { it }
    private val pool: LazyBuffer<T> = LazyBuffer(iter, sourceHint)
    private var first: Boolean = true

    /** Returns the length of a combination produced by this iterator. */
    fun k(): Int = indices.size

    /** Returns the length of a combination produced by this iterator. */
    fun len(): Int = indices.size

    /**
     * Returns the current length of the pool from which combination elements are selected.
     */
    fun n(): Int = pool.length

    companion object {
        fun <T> new(iter: Iterator<T>, k: Int, hint: SizeHint = SizeHint(0, null)): Combinations<T> =
            Combinations(iter, k, hint)
    }

    internal fun src(): LazyBuffer<T> = pool

    internal fun nAndCount(): Pair<Int, Int> {
        val n = pool.count()
        return Pair(n, remainingFor(n, first, indices) ?: 0)
    }

    private fun init(): Boolean {
        pool.prefill(k())
        val done = k() > n()
        if (!done) {
            first = false
        }
        return done
    }

    private fun incrementIndices(): Boolean {
        if (indices.isEmpty()) {
            return true
        }
        var i = indices.size - 1
        if (indices[i] == pool.length - 1) {
            pool.getNext()
        }

        while (indices[i] == i + pool.length - indices.size) {
            if (i > 0) {
                i -= 1
            } else {
                return true
            }
        }

        indices[i] += 1
        for (j in (i + 1) until indices.size) {
            indices[j] = indices[j - 1] + 1
        }
        return false
    }

    internal fun tryNthResult(n: Int): ItemResult<List<T>, Int> {
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
        return ItemResult.Ok(pool.getAt(indices))
    }

    internal fun tryNth(n: Int): List<T>? =
        when (val res = tryNthResult(n)) {
            is ItemResult.Ok -> res.value
            is ItemResult.Err -> null
        }

    /** Returns the n-th combination without iterating through the preceding ones manually. */
    fun nth(n: Int): List<T>? = tryNth(n)

    /** Returns the total count of remaining combinations. */
    fun count(): Int = nAndCount().second

    override fun hasNext(): Boolean {
        if (first) {
            pool.prefill(k())
            return k() <= n()
        }
        // Check if next combination can be produced
        if (indices.isEmpty()) return false
        var i = indices.size - 1
        if (indices[i] == pool.length - 1) {
            pool.getNext()
        }
        while (i >= 0 && indices[i] == i + pool.length - indices.size) {
            if (i > 0) i -= 1 else return false
        }
        return true
    }

    override fun next(): List<T> {
        val done =
            if (first) {
                init()
            } else {
                incrementIndices()
            }
        if (done) {
            throw NoSuchElementException("Combinations exhausted")
        }
        return pool.getAt(indices)
    }

    internal fun reset(newK: Int) {
        first = true
        kVal = newK
        indices = IntArray(newK) { it }
        pool.prefill(newK)
    }

    /** Size hint for remaining combinations. */
    fun sizeHint(): SizeHint {
        val (low, upp) = pool.sizeHint()
        val rLow = remainingFor(low, first, indices) ?: Int.MAX_VALUE
        val rUpp = upp?.let { remainingFor(it, first, indices) }
        return SizeHint(rLow, rUpp)
    }
}

/**
 * Calculates binomial coefficient (n choose k), or null if overflow occurs.
 */
internal fun checkedBinomial(n: Int, k: Int): Int? {
    if (n < k || k < 0 || n < 0) return 0
    var nVar = n
    var kVar = (n - k).coerceAtMost(k)
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
            else -> SizeHint(0, null)
        }
    return Combinations(iterable.iterator(), k, hint)
}

/**
 * Create a new [Combinations] iterator adaptor from an iterator.
 */
fun <T> combinations(iter: Iterator<T>, k: Int, hint: SizeHint = SizeHint(0, null)): Combinations<T> =
    Combinations(iter, k, hint)
