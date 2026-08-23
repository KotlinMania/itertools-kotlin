// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

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
