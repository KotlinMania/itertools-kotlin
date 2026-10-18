package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adaptor that allows putting back a single item to the front of the iterator.
 */
class PutBack<T>(
    private val iter: Iterator<T>,
) : Iterator<T>,
    io.github.kotlinmania.itertools.PeekingNext<T> {
    private val top: ArrayDeque<T> = ArrayDeque(1)

    override fun peekingNext(accept: (T) -> Boolean): T? {
        if (hasNext()) {
            val r = next()
            if (!accept(r)) {
                putBack(r)
                return null
            }
            return r
        }
        return null
    }

    /**
     * Put back value [value] (builder method).
     */
    fun withValue(value: T): PutBack<T> {
        putBack(value)
        return this
    }

    /**
     * Split the [PutBack] into its parts.
     */
    fun intoParts(): Pair<T?, Iterator<T>> = Pair(top.firstOrNull(), iter)

    /**
     * Put back a single value to the front of the iterator.
     * If a value is already in the put back slot, it is returned.
     */
    fun putBack(x: T): T? {
        val old = if (top.isNotEmpty()) top.removeFirst() else null
        top.addLast(x)
        return old
    }

    override fun hasNext(): Boolean = top.isNotEmpty() || iter.hasNext()

    override fun next(): T {
        if (top.isNotEmpty()) {
            return top.removeFirst()
        }
        return iter.next()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val (lower, upper) = sizeHintOf(iter)
        val x = if (top.isNotEmpty()) 1 else 0
        return SizeHint(lower + x, upper?.plus(x))
    }

    /** Count remaining elements. */
    fun count(): Int {
        var c = if (top.isNotEmpty()) 1 else 0
        while (iter.hasNext()) {
            iter.next()
            c++
        }
        return c
    }

    /** Return the last element. */
    fun last(): T? {
        var lastVal: T? = if (top.isNotEmpty()) top.first() else null
        while (iter.hasNext()) {
            lastVal = iter.next()
        }
        return lastVal
    }

    /** Return the nth element. */
    fun nth(n: Int): T? {
        var rem = n
        if (top.isNotEmpty()) {
            if (rem == 0) {
                return next()
            }
            top.removeFirst()
            rem--
        }
        while (iter.hasNext()) {
            val v = iter.next()
            if (rem == 0) return v
            rem--
        }
        return null
    }

    /** Test if all elements satisfy predicate. */
    fun all(predicate: (T) -> Boolean): Boolean {
        while (hasNext()) {
            if (!predicate(next())) return false
        }
        return true
    }

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, T) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create an iterator where you can put back a single item.
 */
fun <T> putBack(iterable: Iterable<T>): PutBack<T> = PutBack(iterable.iterator())

/**
 * Create an iterator where you can put back a single item.
 */
fun <T> putBack(iter: Iterator<T>): PutBack<T> = PutBack(iter)
