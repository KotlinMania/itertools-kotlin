// port-lint: source take_while_inclusive.rs
package io.github.kotlinmania.itertools

import kotlin.jvm.JvmName

/**
 * An iterator adaptor that consumes elements while the given predicate is
 * `true`, including the element for which the predicate first returned
 * `false`.
 *
 * See [takeWhileInclusive] for more information.
 */
class TakeWhileInclusive<T>(
    private val iter: Iterator<T>,
    private val predicate: (T) -> Boolean,
    private val sourceHint: SizeHint = SizeHint(0, null),
) : Iterator<T> {
    private var done: Boolean = false
    private var slot: Slot<T>? = null
    private var consumed: Int = 0

    private class Slot<T>(
        val value: T,
    )

    private fun advance() {
        if (slot != null || done) return
        if (!iter.hasNext()) {
            done = true
            return
        }
        val item = iter.next()
        consumed += 1
        slot = Slot(item)
        if (!predicate(item)) {
            done = true
        }
    }

    override fun hasNext(): Boolean {
        if (slot != null) return true
        if (done) return false
        advance()
        return slot != null
    }

    override fun next(): T {
        if (!hasNext()) throw NoSuchElementException("TakeWhileInclusive exhausted")
        val current = slot!!
        slot = null
        return current.value
    }

    /**
     * Equivalent to upstream size hint. Lower bound is always zero
     * because the predicate may stop on the first element; upper bound mirrors
     * the source iterator's remaining upper bound, shrunk to zero once the
     * adaptor is exhausted.
     */
    fun sizeHint(): SizeHint {
        if (done && slot == null) return SizeHint(0, 0)
        val remaining = subScalar(sourceHint, consumed)
        return SizeHint(0, remaining.upper)
    }

    /**
     * Consumes the adaptor with a left fold, honoring the predicate-inclusive
     * stop condition.
     */
    fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var acc = initial
        while (hasNext()) {
            acc = operation(acc, next())
        }
        return acc
    }

    companion object {
        fun <T> new(iter: Iterator<T>, predicate: (T) -> Boolean): TakeWhileInclusive<T> =
            TakeWhileInclusive(iter, predicate)
    }
}

/**
 * `Iterable`-enabled constructor for [TakeWhileInclusive].

 *
 * Yields elements while [predicate] returns `true`, including the element
 * for which it first returns `false`.
 *
 * ```
 * val out = takeWhileInclusive(listOf(1, 2, 3, 4)) { it < 3 }.asSequence().toList()
 * // out == [1, 2, 3]
 * ```
 */
@JvmName("takeWhileInclusiveIterable")
fun <T> takeWhileInclusive(iterable: Iterable<T>, predicate: (T) -> Boolean): Iterator<T> =
    TakeWhileInclusive(iterable.iterator(), predicate, sourceSizeHint(iterable))

/**
 * `Iterator`-enabled extension for [TakeWhileInclusive].
 */
fun <T> Iterator<T>.takeWhileInclusive(predicate: (T) -> Boolean): Iterator<T> =
    TakeWhileInclusive(this, predicate)

/**
 * `Iterable`-enabled extension for [TakeWhileInclusive].
 */
fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): Iterator<T> =
    takeWhileInclusive(this, predicate)

private fun sourceSizeHint(it: Iterable<*>): SizeHint =
    when (it) {
        is Collection<*> -> SizeHint(it.size, it.size)
        else -> SizeHint(0, null)
    }


