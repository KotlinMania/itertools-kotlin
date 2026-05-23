// port-lint: source src/pad_tail.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor that pads a sequence to a minimum length by filling
 * missing elements using a function.
 *
 * Iterator element type is the source iterator's element type.
 *
 * See [padUsing] for more information.
 */
class PadUsing<T> internal constructor(
    private val iter: Iterator<T>,
    private var min: Int,
    private val sourceHint: SizeHint,
    private val filler: (Int) -> T,
) : Iterator<T> {

    private var pos: Int = 0
    private var sourceExhausted: Boolean = false

    override fun hasNext(): Boolean {
        if (!sourceExhausted && iter.hasNext()) return true
        sourceExhausted = true
        return pos < min
    }

    override fun next(): T {
        if (!sourceExhausted) {
            if (iter.hasNext()) {
                pos += 1
                return iter.next()
            }
            sourceExhausted = true
        }
        if (pos < min) {
            val produced = filler(pos)
            pos += 1
            return produced
        }
        throw NoSuchElementException("PadUsing exhausted")
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    fun sizeHint(): SizeHint {
        val tail = if (pos < min) min - pos else 0
        val remaining = subScalar(sourceHint, pos)
        return max(remaining, tail to tail)
    }

    /** Consumes the adaptor with a left fold. */
    fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var acc = initial
        while (hasNext()) {
            acc = operation(acc, next())
        }
        return acc
    }
}

/**
 * Create a new [PadUsing] iterator.
 *
 * Drains [iterable]; if fewer than [min] elements have been produced, calls
 * [filler] with each missing zero-based position until the minimum length is
 * reached. If the source produces at least [min] elements, [filler] is never
 * called.
 *
 * ```
 * val padded = padUsing(listOf(1, 2), 5) { i -> i * 10 }.asSequence().toList()
 * // padded == [1, 2, 20, 30, 40]
 * ```
 */
fun <T> padUsing(iterable: Iterable<T>, min: Int, filler: (Int) -> T): PadUsing<T> =
    PadUsing(iterable.iterator(), min, padTailIterableHint(iterable), filler)

private fun padTailIterableHint(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> it.size to it.size
    else -> 0 to null
}
