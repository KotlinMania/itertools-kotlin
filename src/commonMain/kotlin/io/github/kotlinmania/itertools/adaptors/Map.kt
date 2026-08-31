// port-lint: source adaptors/map.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult

/**
 * Special case mapping interface.
 */
interface MapSpecialCaseFn<T, Out> {
    fun call(t: T): Out
}

/**
 * Function wrapper for [MapOk].
 */
class MapSpecialCaseFnOk<T, U, E>(
    val f: (T) -> U,
) : MapSpecialCaseFn<ItemResult<T, E>, ItemResult<U, E>> {
    override fun call(t: ItemResult<T, E>): ItemResult<U, E> =
        when (t) {
            is ItemResult.Ok -> ItemResult.Ok(f(t.value))
            is ItemResult.Err -> ItemResult.Err(t.error)
        }
}

/**
 * Function wrapper for [MapInto].
 */
class MapSpecialCaseFnInto<T, U>(
    val f: (T) -> U,
) : MapSpecialCaseFn<T, U> {
    override fun call(t: T): U = f(t)
}

/**
 * An iterator adaptor applying a special case function.
 */
open class MapSpecialCase<T, Out>(
    val iter: Iterator<T>,
    val f: MapSpecialCaseFn<T, Out>,
) : Iterator<Out> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): Out {
        if (!hasNext()) {
            throw NoSuchElementException("MapSpecialCase exhausted")
        }
        return f.call(iter.next())
    }

    fun sizeHint(): io.github.kotlinmania.itertools.SizeHint =
        io.github.kotlinmania.itertools
            .SizeHint(0, null)

    fun <Acc> fold(init: Acc, foldF: (Acc, Out) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = foldF(acc, next())
        }
        return acc
    }

    fun collect(): List<Out> = asSequence().toList()

    fun nextBack(): Out? {
        if (!hasNext()) return null
        return next()
    }
}

/**
 * An iterator adapter to apply a transformation within a nested [ItemResult.Ok].
 *
 * See [mapOk] for more information.
 */
class MapOk<T, U, E> internal constructor(
    iter: Iterator<ItemResult<T, E>>,
    f: (T) -> U,
) : MapSpecialCase<ItemResult<T, E>, ItemResult<U, E>>(iter, MapSpecialCaseFnOk(f))

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
    iter: Iterator<T>,
    transform: (T) -> U,
) : MapSpecialCase<T, U>(iter, MapSpecialCaseFnInto(transform))

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
