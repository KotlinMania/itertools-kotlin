// port-lint: source src/repeatn.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that produces *n* repetitions of an element.
 *
 * See [repeatN] for more information.
 */
internal class RepeatN<A>(
    internal var elt: A?,
    private var n: Int,
) : Iterator<A> {
    override fun hasNext(): Boolean = n > 0

    override fun next(): A {
        val current =
            elt
                ?: throw NoSuchElementException("RepeatN exhausted")
        if (n > 1) {
            n -= 1
            return current
        }
        n = 0
        elt = null
        return current
    }

    /** Exact remaining length. */
    val size: Int
        get() = n

    /** `(n, n)` size hint. */
    fun sizeHint(): SizeHint = SizeHint(n, n)

    /** Fold over the remaining elements, consuming the iterator. */
    fun <B> fold(initial: B, operation: (B, A) -> B): B {
        val current = elt
        if (current == null || n == 0) {
            n = 0
            elt = null
            return initial
        }
        var acc = initial
        val remaining = n
        n = 0
        elt = null
        for (i in 1 until remaining) {
            acc = operation(acc, current)
        }
        return operation(acc, current)
    }

    /** Equivalent to upstream `DoubleEndedIterator::next_back`; identical to [next]. */
    fun nextBack(): A? = if (hasNext()) next() else null
}

/** Create an iterator that produces `n` repetitions of `element`. */
fun <A> repeatN(element: A, n: Int): Iterator<A> =
    if (n == 0) RepeatN(elt = null, n = 0) else RepeatN(elt = element, n = n)
