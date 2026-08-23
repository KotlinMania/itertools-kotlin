// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

/**
 * A "meta iterator adaptor". Its closure receives a reference to the iterator
 * and may pick off as many elements as it likes, to produce the next iterator element.
 */
class Batching<T, R>(
    private val iter: Iterator<T>,
    private val f: (Iterator<T>) -> R?,
) : Iterator<R> {
    private var nextItem: R? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            nextItem = f(iter)
            hasNextCalculated = true
        }
        return nextItem != null
    }

    override fun next(): R {
        if (!hasNext()) {
            throw NoSuchElementException("Batching iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("Batching iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }
}

/**
 * Create a new [Batching] iterator.
 */
fun <T, R> batching(iter: Iterator<T>, f: (Iterator<T>) -> R?): Batching<T, R> =
    Batching(iter, f)

/**
 * Create a new [Batching] iterator from an [Iterable].
 */
fun <T, R> batching(iterable: Iterable<T>, f: (Iterator<T>) -> R?): Batching<T, R> =
    Batching(iterable.iterator(), f)
