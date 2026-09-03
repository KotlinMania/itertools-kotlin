// port-lint: source put_back_n_impl.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor that allows putting multiple items in front of the iterator.
 *
 * Iterator element type is the source iterator's element type.
 */
class PutBackN<T>(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint = SizeHint(0, null),
) : PeekingNext<T> {
    private val top: ArrayDeque<T> = ArrayDeque()
    private var consumed: Int = 0

    /**
     * Puts [x] in front of the iterator.
     *
     * The values are yielded in order of the most recently put back values first.
     *
     * ```
     * val it = putBackN(1..4)
     * it.next()
     * it.putBack(1)
     * it.putBack(0)
     * // it now yields 0, 1, 1, 2, 3, 4
     * ```
     */
    fun putBack(x: T) {
        top.addLast(x)
    }

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

    override fun hasNext(): Boolean = top.isNotEmpty() || iter.hasNext()

    override fun next(): T {
        if (top.isNotEmpty()) {
            return top.removeLast()
        }
        val nextValue = iter.next()
        consumed += 1
        return nextValue
    }

    /** Returns the size hint. */
    fun sizeHint(): SizeHint = addScalar(subScalar(sourceHint, consumed), top.size)

    /**
     * Consumes the iterator with a left fold, starting from the most recently
     * put-back values (in reverse insertion order) and then the source.
     */
    fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var acc = initial
        while (top.isNotEmpty()) {
            acc = operation(acc, top.removeLast())
        }
        while (iter.hasNext()) {
            acc = operation(acc, iter.next())
        }
        return acc
    }
}

/**
 * Create an iterator where you can put back multiple values to the front of the iteration.
 *
 * Iterator element type is the source's element type.
 */
fun <T> putBackN(iterable: Iterable<T>): PutBackN<T> =
    PutBackN(iterable.iterator(), sizeHintOfIterable(iterable))

/**
 * Create an iterator where you can put back multiple values to the front of the iteration.
 */
@kotlin.jvm.JvmName("putBackNFromIterator")
fun <T> putBackN(iterator: Iterator<T>): PutBackN<T> =
    PutBackN(iterator)

/**
 * Create an iterator where you can put back multiple values to the front of the iteration.
 */
fun <T> Iterator<T>.putBackN(): PutBackN<T> =
    PutBackN(this)

private fun sizeHintOfIterable(it: Iterable<*>): SizeHint =
    when (it) {
        is Collection<*> -> SizeHint(it.size, it.size)
        else -> SizeHint(0, null)
    }
