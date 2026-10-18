package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adapter that yields elements while [predicate] returns true.
 */
class TakeWhileRef<T>(
    private val iter: Iterator<T>,
    private val predicate: (T) -> Boolean,
) : Iterator<T> {
    private val peeked: ArrayDeque<T> = ArrayDeque(1)
    private var done: Boolean = false

    override fun hasNext(): Boolean {
        if (done) return false
        if (peeked.isNotEmpty()) return true
        if (!iter.hasNext()) {
            done = true
            return false
        }
        val nextVal = iter.next()
        if (predicate(nextVal)) {
            peeked.addLast(nextVal)
            return true
        }
        done = true
        return false
    }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("TakeWhileRef exhausted")
        }
        return peeked.removeFirst()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        if (done) return SizeHint(0, 0)
        val x = if (peeked.isNotEmpty()) 1 else 0
        val (_, upper) = sizeHintOf(iter)
        return SizeHint(0, upper?.plus(x))
    }
}

/**
 * Create a new [TakeWhileRef] iterator.
 */
fun <T> takeWhileRef(iterable: Iterable<T>, predicate: (T) -> Boolean): TakeWhileRef<T> =
    TakeWhileRef(iterable.iterator(), predicate)

/**
 * Create a new [TakeWhileRef] iterator from an [Iterator].
 */
fun <T> takeWhileRef(iter: Iterator<T>, predicate: (T) -> Boolean): TakeWhileRef<T> =
    TakeWhileRef(iter, predicate)
