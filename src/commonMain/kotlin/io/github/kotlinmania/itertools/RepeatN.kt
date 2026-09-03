// port-lint: source repeatn.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that produces *n* repetitions of an element.
 *
 * See [repeatN] for more information.
 */
class RepeatN<A>(
    internal var elt: A?,
    private var n: Int,
) : PeekingNext<A> {

    override fun hasNext(): Boolean = n > 0

    override fun next(): A {
        val elt = this.elt ?: throw NoSuchElementException("RepeatN exhausted")
        if (n > 1) {
            n -= 1
            return elt
        } else {
            n = 0
            this.elt = null
            return elt
        }
    }

    override fun peekingNext(accept: (A) -> Boolean): A? {
        val current = elt ?: return null
        if (n > 0 && accept(current)) {
            return next()
        }
        return null
    }

    /** Exact remaining length. */
    val size: Int
        get() = n

    /** `(n, n)` size hint. */
    fun sizeHint(): SizeHint {
        val n = this.n
        return SizeHint(n, n)
    }

    /** Fold over the remaining elements, consuming the iterator. */
    fun <B> fold(init: B, f: (B, A) -> B): B {
        val elt = this.elt
        if (elt != null && n > 0) {
            var acc = init
            for (i in 1 until n) {
                acc = f(acc, elt)
            }
            n = 0
            this.elt = null
            return f(acc, elt)
        }
        return init
    }

    /** Fold over the remaining elements in reverse order, consuming the iterator. */
    fun <B> rfold(init: B, f: (B, A) -> B): B {
        return fold(init, f)
    }

    /** Returns the next element from the back; identical to [next]. */
    fun nextBack(): A? {
        val next = if (hasNext()) next() else null
        return next
    }
}

/** Create an iterator that produces `n` repetitions of `element`. */
fun <A> repeatN(element: A, n: Int): RepeatN<A> {
    return if (n == 0) {
        RepeatN(elt = null, n = 0)
    } else {
        RepeatN(
            elt = element,
            n = n,
        )
    }
}
