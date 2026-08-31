// port-lint: source itertools/src/combinations_with_replacement.rs
package io.github.kotlinmania.itertools

/**
 * An iterator to iterate through all the `k`-length combinations in an iterator, with replacement.
 *
 * See [combinationsWithReplacement] for more information.
 */
class CombinationsWithReplacement<T>(
    iter: Iterator<T>,
    k: Int,
    sourceHint: SizeHint = SizeHint(0, null),
) : Iterator<List<T>> {
    private val indices: IntArray = IntArray(k) { 0 }
    private val pool: LazyBuffer<T> = LazyBuffer(iter, sourceHint)
    private var first: Boolean = true
    private var nextItem: List<T>? = null
    private var isExhausted: Boolean = false

    private fun incrementIndices(): Boolean {
        pool.getNext()

        var incrementIndex = -1
        var incrementValue = 0
        for (i in indices.indices.reversed()) {
            if (indices[i] < pool.length - 1) {
                incrementIndex = i
                incrementValue = indices[i] + 1
                break
            }
        }
        return if (incrementIndex != -1) {
            for (j in incrementIndex until indices.size) {
                indices[j] = incrementValue
            }
            false
        } else {
            true
        }
    }

    private fun produceNext(): List<T>? {
        if (first) {
            if (indices.isEmpty()) {
                first = false
                return emptyList()
            }
            if (pool.length == 0 && !pool.getNext()) {
                isExhausted = true
                return null
            }
            first = false
            return pool.getAt(indices)
        }
        if (incrementIndices()) {
            isExhausted = true
            return null
        }
        return pool.getAt(indices)
    }

    override fun hasNext(): Boolean {
        if (isExhausted) return false
        if (nextItem != null) return true
        nextItem = produceNext()
        if (nextItem == null) {
            isExhausted = true
            return false
        }
        return true
    }

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("CombinationsWithReplacement exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("CombinationsWithReplacement exhausted")
        nextItem = null
        return item
    }

    /**
     * Returns the n-th combination with replacement without iterating through the preceding ones manually.
     */
    fun nth(n: Int): List<T>? {
        if (nextItem != null) {
            val item = nextItem
            nextItem = null
            if (n == 0) return item
            return nth(n - 1)
        }
        if (isExhausted) return null
        if (first) {
            if (indices.isEmpty()) {
                first = false
                if (n == 0) return emptyList()
                isExhausted = true
                return null
            }
            if (pool.length == 0 && !pool.getNext()) {
                isExhausted = true
                return null
            }
            first = false
        } else if (incrementIndices()) {
            isExhausted = true
            return null
        }
        for (step in 0 until n) {
            if (incrementIndices()) {
                isExhausted = true
                return null
            }
        }
        return pool.getAt(indices)
    }

    /**
     * Returns the total count of remaining combinations with replacement.
     */
    fun count(): Int {
        if (isExhausted) return 0
        val n = pool.count()
        val base = remainingFor(n, first, indices) ?: Int.MAX_VALUE
        val extra = if (nextItem != null) 1 else 0
        val total = base.toLong() + extra.toLong()
        if (total > Int.MAX_VALUE.toLong()) return Int.MAX_VALUE
        return total.toInt()
    }

    /**
     * Equivalent to upstream size hint.
     */
    fun sizeHint(): SizeHint {
        val (low, upp) = pool.sizeHint()
        val rLow = remainingFor(low, first, indices) ?: Int.MAX_VALUE
        val rUpp = upp?.let { remainingFor(it, first, indices) }
        return SizeHint(rLow, rUpp)
    }
}

private fun remainingFor(n: Int, first: Boolean, indices: IntArray): Int? {
    fun count(nVal: Int, kVal: Int): Int? {
        val positions =
            if (nVal == 0) {
                (kVal - 1).coerceAtLeast(0)
            } else {
                nVal - 1 + kVal
            }
        return checkedBinomial(positions, kVal)
    }

    val k = indices.size
    if (first) {
        return count(n, k)
    }

    var sum = 0
    for (i in indices.indices) {
        val n0 = indices[i]
        val c = count(n - 1 - n0, k - i) ?: return null
        val newSum = sum.toLong() + c.toLong()
        if (newSum > Int.MAX_VALUE.toLong()) return null
        sum = newSum.toInt()
    }
    return sum
}

/**
 * Create a new [CombinationsWithReplacement] from an [Iterable].
 */
fun <T> combinationsWithReplacement(iterable: Iterable<T>, k: Int): CombinationsWithReplacement<T> {
    val hint =
        when (iterable) {
            is Collection<*> -> SizeHint(iterable.size, iterable.size)
            else -> SizeHint(0, null)
        }
    return CombinationsWithReplacement(iterable.iterator(), k, hint)
}

/**
 * Create a new [CombinationsWithReplacement] from an [Iterator].
 */
fun <T> combinationsWithReplacement(
    iter: Iterator<T>,
    k: Int,
    hint: SizeHint = SizeHint(0, null),
): CombinationsWithReplacement<T> = CombinationsWithReplacement(iter, k, hint)
