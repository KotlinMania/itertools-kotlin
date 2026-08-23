// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult

/**
 * An iterator adapter to filter and apply a transformation on values within a nested [ItemResult.Ok].
 */
class FilterMapOk<T, U, E>(
    private val iter: Iterator<ItemResult<T, E>>,
    private val transform: (T) -> U?,
) : Iterator<ItemResult<U, E>> {
    private var nextItem: ItemResult<U, E>? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            while (iter.hasNext()) {
                val item = iter.next()
                when (item) {
                    is ItemResult.Ok -> {
                        val mapped = transform(item.value)
                        if (mapped != null) {
                            nextItem = ItemResult.Ok(mapped)
                            hasNextCalculated = true
                            return true
                        }
                    }
                    is ItemResult.Err -> {
                        nextItem = ItemResult.Err(item.error)
                        hasNextCalculated = true
                        return true
                    }
                }
            }
            return false
        }
        return true
    }

    override fun next(): ItemResult<U, E> {
        if (!hasNext()) {
            throw NoSuchElementException("FilterMapOk iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("FilterMapOk iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }
}

/**
 * Create a new [FilterMapOk] iterator.
 */
fun <T, U, E> filterMapOk(iter: Iterator<ItemResult<T, E>>, transform: (T) -> U?): FilterMapOk<T, U, E> =
    FilterMapOk(iter, transform)

/**
 * Create a new [FilterMapOk] iterator from an [Iterable].
 */
fun <T, U, E> filterMapOk(iterable: Iterable<ItemResult<T, E>>, transform: (T) -> U?): FilterMapOk<T, U, E> =
    FilterMapOk(iterable.iterator(), transform)
