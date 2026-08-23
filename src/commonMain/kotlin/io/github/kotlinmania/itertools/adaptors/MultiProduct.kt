// port-lint: source src/adaptors/multi_product.rs
package io.github.kotlinmania.itertools.adaptors

/**
 * An iterator adaptor that iterates over the cartesian product of
 * multiple iterators.
 *
 * An iterator element type is `List<T>`.
 *
 * See [multiCartesianProduct] for more information.
 */
class MultiProduct<T>(
    private val pool: List<List<T>>,
) : Iterator<List<T>> {
    private val indices: IntArray = IntArray(pool.size)
    private var hasMore: Boolean = pool.isNotEmpty() && pool.none { it.isEmpty() }

    override fun hasNext(): Boolean = hasMore

    override fun next(): List<T> {
        if (!hasMore) {
            throw NoSuchElementException("MultiProduct exhausted")
        }
        val result = List(pool.size) { i -> pool[i][indices[i]] }

        // Advance to next combination
        var i = pool.size - 1
        while (i >= 0) {
            indices[i] += 1
            if (indices[i] < pool[i].size) {
                break
            }
            indices[i] = 0
            i -= 1
        }
        if (i < 0) {
            hasMore = false
        }

        return result
    }
}

/**
 * Create a new cartesian product iterator over an arbitrary number of iterables.
 */
fun <T> multiCartesianProduct(iters: Iterable<Iterable<T>>): MultiProduct<T> {
    val pool = iters.map { it.toList() }
    return MultiProduct(pool)
}

/**
 * Create a new cartesian product iterator over an arbitrary number of iterators.
 */
fun <T> multiCartesianProduct(iters: List<Iterator<T>>): MultiProduct<T> {
    val pool = iters.map { it.asSequence().toList() }
    return MultiProduct(pool)
}
