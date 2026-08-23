// port-lint: source exactly_one_err.rs
package io.github.kotlinmania.itertools

/**
 * Iterator returned for the error case of `exactlyOne()`.
 * Yields exactly the same elements as the input iterator.
 *
 * During the execution of `exactlyOne` the iterator must be mutated. This
 * wrapper effectively "restores" the state of the input iterator when it's
 * handed back.
 */
class ExactlyOneError<T>(
    firstTwo: FirstTwo<T>?,
    private val inner: Iterator<T>,
    private val innerHint: SizeHint = SizeHint(0, null),
) : Iterator<T> {
    private var firstTwo: FirstTwo<T>? = firstTwo
    private var consumed: Int = 0

    /**
     * The two-or-fewer prefix that `exactlyOne` pulled out of the source
     * before discovering the error.
     */
    sealed class FirstTwo<out T> {
        class Both<T>(
            val first: T,
            val second: T,
        ) : FirstTwo<T>()

        class JustSecond<T>(
            val second: T,
        ) : FirstTwo<T>()
    }

    private fun additionalLen(): Int =
        when (firstTwo) {
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

    override fun toString(): String =
        if (additionalLen() > 0) {
            "got at least 2 elements when exactly one was expected"
        } else {
            "got zero elements when exactly one was expected"
        }
}

/**
 * If the iterator yields exactly one element, that element will be returned.
 * Otherwise an error will be returned containing an iterator that has the same output as the input iterator.
 */
fun <T> exactlyOne(iter: Iterator<T>): ItemResult<T, ExactlyOneError<T>> {
    if (!iter.hasNext()) {
        return ItemResult.Err(ExactlyOneError(null, iter))
    }
    val first = iter.next()
    if (!iter.hasNext()) {
        return ItemResult.Ok(first)
    }
    val second = iter.next()
    return ItemResult.Err(
        ExactlyOneError(ExactlyOneError.FirstTwo.Both(first, second), iter),
    )
}

/**
 * If the iterable yields exactly one element, that element will be returned.
 * Otherwise an error will be returned containing an iterator that has the same output as the input iterator.
 */
fun <T> exactlyOne(iterable: Iterable<T>): ItemResult<T, ExactlyOneError<T>> =
    exactlyOne(iterable.iterator())

/**
 * If the iterator yields no elements, `Ok(null)` will be returned. If the iterator yields
 * exactly one element, `Ok(element)` will be returned, otherwise an error will be returned
 * containing an iterator that has the same output as the input iterator.
 */
fun <T> atMostOne(iter: Iterator<T>): ItemResult<T?, ExactlyOneError<T>> {
    if (!iter.hasNext()) {
        return ItemResult.Ok(null)
    }
    val first = iter.next()
    if (!iter.hasNext()) {
        return ItemResult.Ok(first)
    }
    val second = iter.next()
    return ItemResult.Err(
        ExactlyOneError(ExactlyOneError.FirstTwo.Both(first, second), iter),
    )
}

/**
 * If the iterable yields no elements, `Ok(null)` will be returned. If the iterable yields
 * exactly one element, `Ok(element)` will be returned, otherwise an error will be returned
 * containing an iterator that has the same output as the input iterator.
 */
fun <T> atMostOne(iterable: Iterable<T>): ItemResult<T?, ExactlyOneError<T>> =
    atMostOne(iterable.iterator())
