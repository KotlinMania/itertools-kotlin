// port-lint: source rciter_impl.rs
package io.github.kotlinmania.itertools

/**
 * A shared iterator that can be passed to multiple consumers,
 * sharing the underlying iterator state.
 *
 * See [rciter] for more information.
 */
class RcIter<T>(
    private val shared: Iterator<T>,
) : Iterator<T> {
    override fun hasNext(): Boolean = shared.hasNext()

    override fun next(): T = shared.next()

    /**
     * Create a new handle that shares the same underlying iterator.
     */
    fun share(): RcIter<T> = RcIter(shared)
}

/**
 * Return an iterator adaptor that allows sharing the iterator across consumers.
 */
fun <T> rciter(iterable: Iterable<T>): RcIter<T> =
    RcIter(iterable.iterator())

/**
 * Return an iterator adaptor that allows sharing the iterator across consumers.
 */
fun <T> rciter(iterator: Iterator<T>): RcIter<T> =
    RcIter(iterator)

/**
 * Return an iterator adaptor that allows sharing the iterator across consumers.
 */
fun <T> Iterable<T>.intoRciter(): RcIter<T> =
    rciter(this)

/**
 * Return an iterator adaptor that allows sharing the iterator across consumers.
 */
fun <T> Iterator<T>.intoRciter(): RcIter<T> =
    rciter(this)
