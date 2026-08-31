// port-lint: source itertools/src/peeking_take_while.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that allows peeking at an element before deciding to accept it.
 *
 * See [peekingTakeWhile] for more information.
 */
interface PeekingNext<out T> : Iterator<T> {
    /**
     * Pass a reference to the next iterator element to the closure [accept];
     * if [accept] returns `true`, return it as the next element,
     * else `null`.
     */
    fun peekingNext(accept: (T) -> Boolean): T?
}

/**
 * An iterator adaptor that wraps an [Iterator] with peekable capabilities.
 */
class PeekableIterator<T>(
    private val iter: Iterator<T>,
) : PeekingNext<T> {
    private var peeked: T? = null
    private var hasPeeked: Boolean = false

    /** Work like `peek` in `Peekable`. */
    fun peek(): T? {
        if (!hasPeeked && iter.hasNext()) {
            peeked = iter.next()
            hasPeeked = true
        }
        return if (hasPeeked) peeked else null
    }

    override fun peekingNext(accept: (T) -> Boolean): T? {
        val item = peek() ?: return null
        if (accept(item)) {
            return next()
        }
        return null
    }

    override fun hasNext(): Boolean = hasPeeked || iter.hasNext()

    override fun next(): T {
        if (hasPeeked) {
            hasPeeked = false
            val item = peeked
            peeked = null
            @Suppress("UNCHECKED_CAST")
            return item as T
        }
        return iter.next()
    }

    /** Size hint for the peekable iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)
}

/**
 * Returns a [PeekableIterator] wrapping this iterator.
 */
fun <T> Iterator<T>.peekable(): PeekableIterator<T> = PeekableIterator(this)

/**
 * Returns a [PeekableIterator] wrapping this iterable.
 */
fun <T> Iterable<T>.peekable(): PeekableIterator<T> = PeekableIterator(this.iterator())

/**
 * An iterator adaptor that takes items while a closure returns `true`.
 *
 * See [peekingTakeWhile] for more information.
 */
class PeekingTakeWhile<T>(
    private val iter: PeekingNext<T>,
    private val predicate: (T) -> Boolean,
) : PeekingNext<T> {
    private var peeked: T? = null
    private var hasPeeked: Boolean = false

    override fun peekingNext(accept: (T) -> Boolean): T? =
        iter.peekingNext { predicate(it) && accept(it) }

    override fun hasNext(): Boolean {
        if (!hasPeeked) {
            val next = iter.peekingNext(predicate)
            if (next != null) {
                peeked = next
                hasPeeked = true
            }
        }
        return hasPeeked
    }

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException("PeekingTakeWhile exhausted")
        hasPeeked = false
        val item = peeked
        peeked = null
        @Suppress("UNCHECKED_CAST")
        return item as T
    }

    /** Size hint for the iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)
}

/**
 * Create a [PeekingTakeWhile] adaptor over a [PeekingNext] iterator.
 */
fun <T> peekingTakeWhile(iter: PeekingNext<T>, predicate: (T) -> Boolean): PeekingTakeWhile<T> =
    PeekingTakeWhile(iter, predicate)
