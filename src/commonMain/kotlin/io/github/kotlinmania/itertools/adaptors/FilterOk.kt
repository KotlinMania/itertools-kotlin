// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult

/**
 * An iterator adapter to filter values within a nested [ItemResult.Ok].
 */
class FilterOk<T, E>(
    private val iter: Iterator<ItemResult<T, E>>,
    private val predicate: (T) -> Boolean,
) : Iterator<ItemResult<T, E>> {
    private var nextItem: ItemResult<T, E>? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            while (iter.hasNext()) {
                val item = iter.next()
                when (item) {
                    is ItemResult.Ok -> {
                        if (predicate(item.value)) {
                            nextItem = item
                            hasNextCalculated = true
                            return true
                        }
                    }
                    is ItemResult.Err -> {
                        nextItem = item
                        hasNextCalculated = true
                        return true
                    }
                }
            }
            return false
        }
        return true
    }

    override fun next(): ItemResult<T, E> {
        if (!hasNext()) {
            throw NoSuchElementException("FilterOk iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("FilterOk iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }
}

/**
 * Create a new [FilterOk] iterator.
 */
fun <T, E> filterOk(iter: Iterator<ItemResult<T, E>>, predicate: (T) -> Boolean): FilterOk<T, E> =
    FilterOk(iter, predicate)

/**
 * Create a new [FilterOk] iterator from an [Iterable].
 */
fun <T, E> filterOk(iterable: Iterable<ItemResult<T, E>>, predicate: (T) -> Boolean): FilterOk<T, E> =
    FilterOk(iterable.iterator(), predicate)
