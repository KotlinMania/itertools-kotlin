// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

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
