package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adapter to apply a mutating function to each element before yielding it.
 */
class Update<T>(
    private val iter: Iterator<T>,
    private val action: (T) -> Unit,
) : Iterator<T> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): T {
        val item = iter.next()
        action(item)
        return item
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = sizeHintOf(iter)

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, g: (Acc, T) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            val item = iter.next()
            action(item)
            acc = g(acc, item)
        }
        return acc
    }

    /** Collect remaining elements. */
    fun collect(): List<T> {
        val result = mutableListOf<T>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    /** Yield the previous element from the back. */
    fun nextBack(): T? {
        val list = collect()
        return list.lastOrNull()
    }
}

/**
 * Create a new [Update] iterator.
 */
fun <T> update(iter: Iterator<T>, action: (T) -> Unit): Update<T> =
    Update(iter, action)

/**
 * Create a new [Update] iterator from an [Iterable].
 */
fun <T> update(iterable: Iterable<T>, action: (T) -> Unit): Update<T> =
    Update(iterable.iterator(), action)
