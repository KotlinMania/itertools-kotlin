// port-lint: source src/combinations_with_replacement.rs
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

    /** Returns the size hint for remaining combinations with replacement. */
    fun sizeHint(): SizeHint {
        val (low, upp) = pool.sizeHint()
        val rLow = remainingForWithReplacement(low, first, indices) ?: Int.MAX_VALUE
        val rUpp = upp?.let { remainingForWithReplacement(it, first, indices) }
        return SizeHint(rLow, rUpp)
    }
}

private fun remainingForWithReplacement(n: Int, first: Boolean, indices: IntArray): Int? {
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
