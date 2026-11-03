package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult
import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adapter to filter values within a nested [ItemResult.Ok].
 */
class FilterOk<T, E> internal constructor(
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

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val (_, upper) = sizeHintOf(iter)
        return SizeHint(0, upper)
    }

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, ItemResult<T, E>) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }

    /** Collect remaining elements into a list. */
    fun collect(): List<ItemResult<T, E>> {
        val result = mutableListOf<ItemResult<T, E>>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    /** Yield the next element from the back if double-ended. */
    fun nextBack(): ItemResult<T, E>? {
        val list = collect()
        return list.lastOrNull()
    }

    /** Fold from the back. */
    fun <Acc> rfold(init: Acc, f: (Acc, ItemResult<T, E>) -> Acc): Acc {
        val list = collect()
        var acc = init
        for (i in list.indices.reversed()) {
            acc = f(acc, list[i])
        }
        return acc
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

/**
 * An iterator adapter to filter and apply a transformation on values within a nested [ItemResult.Ok].
 */
class FilterMapOk<T, U, E> internal constructor(
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

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val (_, upper) = sizeHintOf(iter)
        return SizeHint(0, upper)
    }

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, ItemResult<U, E>) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }

    /** Collect remaining elements into a list. */
    fun collect(): List<ItemResult<U, E>> {
        val result = mutableListOf<ItemResult<U, E>>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    /** Yield the next element from the back if double-ended. */
    fun nextBack(): ItemResult<U, E>? {
        val list = collect()
        return list.lastOrNull()
    }

    /** Fold from the back. */
    fun <Acc> rfold(init: Acc, f: (Acc, ItemResult<U, E>) -> Acc): Acc {
        val list = collect()
        var acc = init
        for (i in list.indices.reversed()) {
            acc = f(acc, list[i])
        }
        return acc
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

/**
 * Transposes an ItemResult of an Iterator into an Iterator of ItemResult.
 */
fun <T, E> transposeResult(result: ItemResult<Iterator<T>, E>): Iterator<ItemResult<T, E>> =
    when (result) {
        is ItemResult.Ok ->
            object : Iterator<ItemResult<T, E>> {
                private val it = result.value

                override fun hasNext(): Boolean = it.hasNext()

                override fun next(): ItemResult<T, E> = ItemResult.Ok(it.next())
            }
        is ItemResult.Err ->
            object : Iterator<ItemResult<T, E>> {
                private var yielded = false

                override fun hasNext(): Boolean = !yielded

                override fun next(): ItemResult<T, E> {
                    if (yielded) throw NoSuchElementException()
                    yielded = true
                    return ItemResult.Err(result.error)
                }
            }
    }
