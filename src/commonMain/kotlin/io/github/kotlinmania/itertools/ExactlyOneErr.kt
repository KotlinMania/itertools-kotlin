// port-lint: source src/exactly_one_err.rs
package io.github.kotlinmania.itertools

/**
 * Iterator returned for the error case of `Itertools.exactlyOne()`.
 * Yields exactly the same elements as the input iterator.
 *
 * During the execution of `exactlyOne` the iterator must be mutated. This
 * wrapper effectively "restores" the state of the input iterator when it's
 * handed back.
 *
 * Very similar to [PutBackN] except this iterator only supports 0-2
 * pre-buffered elements and does not use a backing list.
 */
class ExactlyOneError<T> internal constructor(
    firstTwo: FirstTwo<T>?,
    private val inner: Iterator<T>,
    private val innerHint: SizeHint,
) : Iterator<T> {

    private var firstTwo: FirstTwo<T>? = firstTwo
    private var consumed: Int = 0

    /**
     * The two-or-fewer prefix that `exactlyOne` pulled out of the source
     * before discovering the error. Modelled as a sealed type, mirroring the
     * upstream `Either<[T; 2], T>`.
     */
    sealed class FirstTwo<out T> {
        class Both<T>(val first: T, val second: T) : FirstTwo<T>()
        class JustSecond<T>(val second: T) : FirstTwo<T>()
    }

    private fun additionalLen(): Int = when (firstTwo) {
        is FirstTwo.Both<*> -> 2
        is FirstTwo.JustSecond<*> -> 1
        null -> 0
    }

    override fun hasNext(): Boolean = firstTwo != null || inner.hasNext()

    override fun next(): T {
        val current = firstTwo
        return when (current) {
            is FirstTwo.Both -> {
                firstTwo = FirstTwo.JustSecond(current.second)
                current.first
            }
            is FirstTwo.JustSecond -> {
                firstTwo = null
                current.second
            }
            null -> {
                consumed += 1
                inner.next()
            }
        }
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    fun sizeHint(): SizeHint = addScalar(subScalar(innerHint, consumed), additionalLen())

    /**
     * Consumes the wrapper with a left fold, yielding the pre-buffered
     * elements in original order before draining the inner iterator.
     */
    fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var acc = initial
        when (val current = firstTwo) {
            is FirstTwo.Both -> {
                acc = operation(acc, current.first)
                acc = operation(acc, current.second)
            }
            is FirstTwo.JustSecond -> {
                acc = operation(acc, current.second)
            }
            null -> {}
        }
        firstTwo = null
        while (inner.hasNext()) {
            acc = operation(acc, inner.next())
        }
        return acc
    }

    /**
     * Human-readable description. Mirrors upstream's `Display` impl:
     * "got at least 2 elements when exactly one was expected" or
     * "got zero elements when exactly one was expected".
     */
    override fun toString(): String =
        if (additionalLen() > 0) {
            "got at least 2 elements when exactly one was expected"
        } else {
            "got zero elements when exactly one was expected"
        }
}
