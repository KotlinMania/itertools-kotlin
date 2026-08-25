// port-lint: source rciter_impl.rs
package io.github.kotlinmania.itertools

/**
 * A wrapper for a shared iterator that can be passed to multiple consumers,
 * sharing the underlying iterator state.
 *
 * The returned [RcIter] can be cloned, and each clone will refer back to the
 * same original iterator.
 *
 * [RcIter] allows doing interesting things like using `.zip()` on an iterator with
 * itself.
 *
 * See [rciter] for more information.
 */
class RcIter<T>(
    private val shared: Iterator<T>,
) : Iterator<T>, Iterable<T> {
    override fun hasNext(): Boolean = shared.hasNext()

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException("RcIter exhausted")
        return shared.next()
    }

    /**
     * Size hint for the shared iterator.
     */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /**
     * Create a new handle that shares the same underlying iterator.
     */
    fun share(): RcIter<T> = RcIter(shared)

    /**
     * Create a new handle that shares the same underlying iterator.
     */
    fun clone(): RcIter<T> = share()

    /**
     * Return an iterator from this `RcIter` (by simply cloning it).
     */
    fun intoIter(): RcIter<T> = clone()

    /**
     * Returns an iterator over the elements.
     */
    override fun iterator(): Iterator<T> = clone()

    /**
     * Yields the next element from the back of the iterator, if supported.
     */
    fun nextBack(): T? {
        return if (shared is ListIterator<T> && shared.hasPrevious()) {
            shared.previous()
        } else {
            null
        }
    }
}

/**
 * Return an iterator inside a shared [RcIter] wrapper.
 *
 * The returned [RcIter] can be cloned, and each clone will refer back to the
 * same original iterator.
 *
 * [RcIter] allows doing interesting things like using `zip` on an iterator with
 * itself.
 */
fun <T> rciter(iterable: Iterable<T>): RcIter<T> =
    RcIter(iterable.iterator())

/**
 * Return an iterator inside a shared [RcIter] wrapper.
 *
 * The returned [RcIter] can be cloned, and each clone will refer back to the
 * same original iterator.
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

