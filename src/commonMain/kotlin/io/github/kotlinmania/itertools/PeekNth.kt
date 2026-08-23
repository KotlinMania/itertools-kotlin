// port-lint: source src/peek_nth.rs
package io.github.kotlinmania.itertools

/**
 * A drop-in replacement for peekable iterators which adds a [peekNth]
 * method allowing the user to peek at a value several iterations forward
 * without advancing the base iterator.
 *
 * See [peekNth] for more information.
 */
class PeekNth<T>(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint = SizeHint(0, null),
) : PeekingNext<T> {
    private val buf: ArrayDeque<T> = ArrayDeque()

    /** Works like the `peek` method in peekable iterators. */
    fun peek(): T? = peekNth(0)

    /** Returns the `nth` value without advancing the iterator. */
    fun peekNth(n: Int): T? {
        val unbufferedItems = (n + 1) - buf.size
        if (unbufferedItems > 0) {
            for (i in 0 until unbufferedItems) {
                if (iter.hasNext()) {
                    buf.addLast(iter.next())
                } else {
                    break
                }
            }
        }
        return if (n in 0 until buf.size) buf[n] else null
    }

    /** Returns the next item if [func] returns `true` for it. */
    fun nextIf(func: (T) -> Boolean): T? {
        val item = peek() ?: return null
        if (func(item)) {
            return next()
        }
        return null
    }

    /** Returns the next item if it equals [expected]. */
    fun nextIfEq(expected: T): T? = nextIf { it == expected }

    override fun peekingNext(accept: (T) -> Boolean): T? {
        val item = peek() ?: return null
        if (accept(item)) {
            return next()
        }
        return null
    }

    override fun hasNext(): Boolean = buf.isNotEmpty() || iter.hasNext()

    override fun next(): T {
        if (buf.isNotEmpty()) {
            return buf.removeFirst()
        }
        return iter.next()
    }

    /** Returns the size hint. */
    fun sizeHint(): SizeHint = addScalar(sourceHint, buf.size)
}

/**
 * Create a new [PeekNth] iterator.
 */
fun <T> peekNth(iterable: Iterable<T>): PeekNth<T> {
    val hint =
        when (iterable) {
            is Collection<*> -> SizeHint(iterable.size, iterable.size)
            else -> SizeHint(0, null)
        }
    return PeekNth(iterable.iterator(), hint)
}

/**
 * Create a new [PeekNth] iterator from an iterator.
 */
fun <T> peekNth(iter: Iterator<T>, hint: SizeHint = SizeHint(0, null)): PeekNth<T> =
    PeekNth(iter, hint)
