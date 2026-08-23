// port-lint: source adaptors/map.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult

/**
 * An iterator adapter to apply a transformation within a nested [ItemResult.Ok].
 *
 * See [mapOk] for more information.
 */
class MapOk<T, U, E> internal constructor(
    private val iter: Iterator<ItemResult<T, E>>,
    private val f: (T) -> U,
) : Iterator<ItemResult<U, E>> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): ItemResult<U, E> =
        when (val item = iter.next()) {
            is ItemResult.Ok -> ItemResult.Ok(f(item.value))
            is ItemResult.Err -> ItemResult.Err(item.error)
        }
}

/**
 * Create a new [MapOk] iterator.
 */
fun <T, U, E> mapOk(iter: Iterator<ItemResult<T, E>>, f: (T) -> U): MapOk<T, U, E> =
    MapOk(iter, f)

/**
 * Create a new [MapOk] iterator from an [Iterable].
 */
fun <T, U, E> mapOk(iterable: Iterable<ItemResult<T, E>>, f: (T) -> U): MapOk<T, U, E> =
    MapOk(iterable.iterator(), f)

/**
 * An iterator adapter to apply conversion to each element.
 */
class MapInto<T, U>(
    private val iter: Iterator<T>,
    private val transform: (T) -> U,
) : Iterator<U> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): U = transform(iter.next())
}

/**
 * Create a new [MapInto] iterator.
 */
fun <T, U> mapInto(iter: Iterator<T>, transform: (T) -> U): MapInto<T, U> =
    MapInto(iter, transform)

/**
 * Create a new [MapInto] iterator from an [Iterable].
 */
fun <T, U> mapInto(iterable: Iterable<T>, transform: (T) -> U): MapInto<T, U> =
    MapInto(iterable.iterator(), transform)
