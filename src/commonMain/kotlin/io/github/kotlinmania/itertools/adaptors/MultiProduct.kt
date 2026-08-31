// port-lint: source itertools/src/adaptors/multi_product.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint
import io.github.kotlinmania.itertools.add
import io.github.kotlinmania.itertools.mul

/**
 * Holds the state of a single iterator within a [MultiProduct].
 */
internal class MultiProductIter<T>(
    private val pool: List<T>,
) {
    private var index: Int = 0

    fun next(): T? =
        if (index < pool.size) {
            val item = pool[index]
            index += 1
            item
        } else {
            null
        }

    fun reset(): T? {
        index = 0
        return next()
    }

    fun count(): Int = (pool.size - index).coerceAtLeast(0)

    fun origCount(): Int = pool.size

    fun sizeHint(): SizeHint = SizeHint(count(), count())

    fun origSizeHint(): SizeHint = SizeHint(origCount(), origCount())

    fun last(): T? = pool.lastOrNull()

    companion object {
        fun <T> new(items: List<T>): MultiProductIter<T> = MultiProductIter(items)
    }
}

/**
 * Internals for [MultiProduct].
 */
internal class MultiProductInner<T>(
    val iters: List<MultiProductIter<T>>,
    var cur: MutableList<T>? = null,
)

/**
 * An iterator adaptor that iterates over the cartesian product of
 * multiple iterators.
 *
 * An iterator element type is `List<T>`.
 *
 * See [multiCartesianProduct] for more information.
 */
class MultiProduct<T> internal constructor(
    private var inner: MultiProductInner<T>?,
) : Iterator<List<T>> {
    constructor(pool: List<List<T>>) : this(
        MultiProductInner(
            iters = pool.map { MultiProductIter.new(it) },
            cur = null,
        ),
    )

    private var nextElement: List<T>? = null
    private var hasNextCalculated: Boolean = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            nextElement = computeNext()
            hasNextCalculated = true
        }
        return nextElement != null
    }

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("MultiProduct exhausted")
        }
        val result = nextElement!!
        hasNextCalculated = false
        nextElement = null
        return result
    }

    private fun computeNext(): List<T>? {
        val state = inner ?: return null
        val cur = state.cur
        if (cur != null) {
            if (state.iters.isEmpty()) {
                inner = null
                return null
            }
            // Find from the right a non-finished iterator and reset finished ones
            for (idx in state.iters.indices.reversed()) {
                val iter = state.iters[idx]
                val newItem = iter.next()
                if (newItem != null) {
                    cur[idx] = newItem
                    return cur.toList()
                } else {
                    val resetItem = iter.reset()
                    if (resetItem != null) {
                        cur[idx] = resetItem
                    }
                }
            }
            inner = null
            return null
        } else {
            // First time
            val firstList = ArrayList<T>(state.iters.size)
            for (iter in state.iters) {
                val item =
                    iter.next() ?: run {
                        inner = null
                        return null
                    }
                firstList.add(item)
            }
            if (state.iters.isEmpty()) {
                inner = null
                return null
            }
            state.cur = firstList
            return firstList.toList()
        }
    }

    /**
     * Consumes the iterator and returns the number of remaining elements.
     */
    fun count(): Int {
        val state = inner ?: return 0
        val cur = state.cur
        return if (cur == null) {
            var product = 1
            for (iter in state.iters) {
                val c = iter.origCount()
                if (c == 0) return 0
                product *= c
            }
            product
        } else {
            var acc = 0
            for (iter in state.iters) {
                if (acc != 0) {
                    acc *= iter.origCount()
                }
                acc += iter.count()
            }
            acc
        }
    }

    /**
     * Returns the bounds on the remaining length of the iterator.
     */
    fun sizeHint(): SizeHint {
        val state = inner ?: return SizeHint(0, 0)
        val cur = state.cur
        return if (cur == null) {
            state.iters.fold(SizeHint(1, 1)) { acc, iter ->
                mul(acc, iter.origSizeHint())
            }
        } else {
            if (state.iters.isEmpty()) {
                SizeHint(0, 0)
            } else {
                val first = state.iters.first()
                state.iters.drop(1).fold(first.sizeHint()) { sh, iter ->
                    add(mul(sh, iter.origSizeHint()), iter.sizeHint())
                }
            }
        }
    }

    /**
     * Consumes the iterator, returning the last element.
     */
    fun last(): List<T>? {
        val state = inner ?: return null
        val cur = state.cur
        return if (cur != null) {
            var count = state.iters.size
            val lastList =
                state.iters.mapIndexed { idx, iter ->
                    val l = iter.last()
                    if (l != null) {
                        l
                    } else {
                        count -= 1
                        cur[idx]
                    }
                }
            if (count == 0) {
                null
            } else {
                lastList
            }
        } else {
            val list = ArrayList<T>(state.iters.size)
            for (iter in state.iters) {
                val l = iter.last() ?: return null
                list.add(l)
            }
            list
        }
    }
}

/**
 * Create a new cartesian product iterator over an arbitrary number of iterables.
 */
fun <T> multiCartesianProduct(iters: Iterable<Iterable<T>>): MultiProduct<T> {
    val pool = iters.map { it.toList() }
    val inner =
        MultiProductInner(
            iters = pool.map { MultiProductIter.new(it) },
            cur = null,
        )
    return MultiProduct(inner)
}

/**
 * Create a new cartesian product iterator over an arbitrary number of iterators.
 */
fun <T> multiCartesianProduct(iters: List<Iterator<T>>): MultiProduct<T> {
    val pool = iters.map { it.asSequence().toList() }
    val inner =
        MultiProductInner(
            iters = pool.map { MultiProductIter.new(it) },
            cur = null,
        )
    return MultiProduct(inner)
}

/**
 * Create a new cartesian product iterator over an arbitrary iterable of iterators.
 */
fun <T> multiProduct(iters: Iterable<Iterator<T>>): MultiProduct<T> {
    val pool = iters.map { it.asSequence().toList() }
    val inner =
        MultiProductInner(
            iters = pool.map { MultiProductIter.new(it) },
            cur = null,
        )
    return MultiProduct(inner)
}
