// port-lint: source src/diff.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor that allows putting back a single item to the front.
 */
class PutBack<T>(
    private val iter: Iterator<T>,
) : PeekingNext<T> {
    private var top: T? = null
    private var hasTop: Boolean = false

    /** Put back value (builder method). */
    fun withValue(value: T): PutBack<T> {
        putBack(value)
        return this
    }

    /**
     * Put back a single value to the front of the iterator.
     * If a value is already in the slot, it is returned.
     */
    fun putBack(x: T): T? {
        val old = if (hasTop) top else null
        top = x
        hasTop = true
        return old
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

    override fun hasNext(): Boolean = hasTop || iter.hasNext()

    override fun next(): T {
        if (hasTop) {
            hasTop = false
            val t = top
            top = null
            @Suppress("UNCHECKED_CAST")
            return t as T
        }
        return iter.next()
    }
}

/** Create an iterator where you can put back a single item. */
fun <T> putBack(iterable: Iterable<T>): PutBack<T> = PutBack(iterable.iterator())

/** Create an iterator where you can put back a single item. */
fun <T> putBack(iter: Iterator<T>): PutBack<T> = PutBack(iter)

/**
 * A type returned by the [diffWith] function.
 *
 * [Diff] represents the way in which the elements yielded by iterator `i` differ from iterator `j`.
 */
sealed class Diff<T, U> {
    /**
     * The index of the first non-matching element along with both iterators' remaining elements
     * starting with the first mismatch.
     */
    data class FirstMismatch<T, U>(
        val index: Int,
        val firstRemaining: PutBack<T>,
        val secondRemaining: PutBack<U>,
    ) : Diff<T, U>()

    /** The total number of elements that were in `j` along with the remaining elements of `i`. */
    data class Shorter<T, U>(
        val length: Int,
        val remaining: PutBack<T>,
    ) : Diff<T, U>()

    /** The total number of elements that were in `i` along with the remaining elements of `j`. */
    data class Longer<T, U>(
        val length: Int,
        val remaining: PutBack<U>,
    ) : Diff<T, U>()
}

/**
 * Compares every element yielded by both `i` and `j` with the given function in lock-step and
 * returns a [Diff] which describes how `j` differs from `i`.
 *
 * If the number of elements yielded by `j` is less than the number of elements yielded by `i`,
 * the number of `j` elements yielded will be returned along with `i`'s remaining elements as
 * [Diff.Shorter].
 *
 * If the two elements of a step differ, the index of those elements along with the remaining
 * elements of both `i` and `j` are returned as [Diff.FirstMismatch].
 *
 * If `i` becomes exhausted before `j` becomes exhausted, the number of elements in `i` along with
 * the remaining `j` elements will be returned as [Diff.Longer].
 */
fun <T, U> diffWith(
    i: Iterable<T>,
    j: Iterable<U>,
    isEqual: (T, U) -> Boolean,
): Diff<T, U>? = diffWith(i.iterator(), j.iterator(), isEqual)

/**
 * Compares every element yielded by both iterators with the given function.
 */
fun <T, U> diffWith(
    i: Iterator<T>,
    j: Iterator<U>,
    isEqual: (T, U) -> Boolean,
): Diff<T, U>? {
    var idx = 0
    while (i.hasNext()) {
        val iElem = i.next()
        if (!j.hasNext()) {
            return Diff.Shorter(idx, PutBack(i).withValue(iElem))
        }
        val jElem = j.next()
        if (!isEqual(iElem, jElem)) {
            val remainingI = PutBack(i).withValue(iElem)
            val remainingJ = PutBack(j).withValue(jElem)
            return Diff.FirstMismatch(idx, remainingI, remainingJ)
        }
        idx += 1
    }
    return if (j.hasNext()) {
        val jElem = j.next()
        Diff.Longer(idx, PutBack(j).withValue(jElem))
    } else {
        null
    }
}
