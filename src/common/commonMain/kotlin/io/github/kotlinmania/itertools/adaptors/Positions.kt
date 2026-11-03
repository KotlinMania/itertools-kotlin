package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adapter to get the positions of each element that matches a predicate.
 */
class Positions<T>(
    private val iter: Iterator<T>,
    private val predicate: (T) -> Boolean,
) : Iterator<Int> {
    private var currentIndex = 0
    private var nextIndex: Int? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            while (iter.hasNext()) {
                val item = iter.next()
                val idx = currentIndex
                currentIndex++
                if (predicate(item)) {
                    nextIndex = idx
                    hasNextCalculated = true
                    return true
                }
            }
            return false
        }
        return true
    }

    override fun next(): Int {
        if (!hasNext()) {
            throw NoSuchElementException("Positions iterator exhausted")
        }
        val idx = nextIndex ?: throw NoSuchElementException("Positions iterator exhausted")
        nextIndex = null
        hasNextCalculated = false
        return idx
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val (_, upper) = sizeHintOf(iter)
        return SizeHint(0, upper)
    }

    /** Yield the previous matching position from the back. */
    fun nextBack(): Int? {
        val list = mutableListOf<Int>()
        while (hasNext()) {
            list.add(next())
        }
        return list.lastOrNull()
    }

    /** Fold over matching positions from the back. */
    fun <B> rfold(init: B, f: (B, Int) -> B): B {
        val list = mutableListOf<Int>()
        while (hasNext()) {
            list.add(next())
        }
        var acc = init
        for (i in list.indices.reversed()) {
            acc = f(acc, list[i])
        }
        return acc
    }
}

/**
 * Create a new [Positions] iterator.
 */
fun <T> positions(iter: Iterator<T>, predicate: (T) -> Boolean): Positions<T> =
    Positions(iter, predicate)

/**
 * Create a new [Positions] iterator from an [Iterable].
 */
fun <T> positions(iterable: Iterable<T>, predicate: (T) -> Boolean): Positions<T> =
    Positions(iterable.iterator(), predicate)
