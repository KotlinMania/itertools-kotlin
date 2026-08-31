// port-lint: source itertools/src/multipeek_impl.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor that allows the user to peek at multiple `.next()`
 * values without advancing the base iterator.
 *
 * See [multipeek] for more information.
 */
class MultiPeek<T>(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint = SizeHint(0, null),
) : PeekingNext<T> {
    private val buf: ArrayDeque<T> = ArrayDeque()
    private var index: Int = 0

    /** Reset the peeking cursor. */
    fun resetPeek() {
        index = 0
    }

    /**
     * Works like `.next()` with the only difference that it doesn't
     * advance itself. `.peek()` can be called multiple times, to peek
     * further ahead. When `.next()` is called, the peeking cursor is reset.
     */
    fun peek(): T? {
        val ret =
            if (index < buf.size) {
                buf[index]
            } else {
                if (iter.hasNext()) {
                    val item = iter.next()
                    buf.addLast(item)
                    item
                } else {
                    return null
                }
            }
        index += 1
        return ret
    }

    override fun peekingNext(accept: (T) -> Boolean): T? {
        if (buf.isEmpty()) {
            val item = peek() ?: return null
            if (!accept(item)) {
                return null
            }
        } else {
            val item = buf.first()
            if (!accept(item)) {
                return null
            }
        }
        return next()
    }

    override fun hasNext(): Boolean = buf.isNotEmpty() || iter.hasNext()

    override fun next(): T {
        index = 0
        if (buf.isNotEmpty()) {
            return buf.removeFirst()
        }
        return iter.next()
    }

    /** Hints at the size of the iterator. */
    fun sizeHint(): SizeHint = addScalar(sourceHint, buf.size)

    /**
     * Consumes the adaptor with a left fold.
     */
    fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var acc = initial
        while (buf.isNotEmpty()) {
            acc = operation(acc, buf.removeFirst())
        }
        while (iter.hasNext()) {
            acc = operation(acc, iter.next())
        }
        return acc
    }
}

/**
 * Create a new [MultiPeek] iterator from an [Iterable].
 */
fun <T> multipeek(iterable: Iterable<T>): MultiPeek<T> {
    val hint =
        when (iterable) {
            is Collection<*> -> SizeHint(iterable.size, iterable.size)
            else -> SizeHint(0, null)
        }
    return MultiPeek(iterable.iterator(), hint)
}

/**
 * Create a new [MultiPeek] iterator from an [Iterator].
 */
fun <T> multipeek(iter: Iterator<T>, hint: SizeHint = SizeHint(0, null)): MultiPeek<T> =
    MultiPeek(iter, hint)
