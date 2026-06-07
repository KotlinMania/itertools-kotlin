// port-lint: source src/with_position.rs
package io.github.kotlinmania.itertools

/**
 * A positioned element yielded by [withPosition].
 */
internal data class Positioned<T>(val position: Position, val value: T)

/**
 * An iterator adaptor that wraps each element in a [Position].
 *
 * Iterator element type is [Positioned].
 *
 * See [withPosition] for more information.
 */
internal class WithPosition<T>(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint,
) : Iterator<Positioned<T>> {

    private var handledFirst: Boolean = false
    private var consumed: Int = 0

    private var headSlot: Slot<T>? = null
    private var tailSlot: Slot<T>? = null
    private var sourceExhausted: Boolean = false

    private class Slot<T>(val value: T)

    private fun primeHead() {
        if (headSlot != null || sourceExhausted) return
        if (!iter.hasNext()) {
            sourceExhausted = true
            return
        }
        headSlot = Slot(iter.next())
        consumed += 1
    }

    private fun primeTail() {
        if (tailSlot != null || sourceExhausted) return
        if (!iter.hasNext()) {
            sourceExhausted = true
            return
        }
        tailSlot = Slot(iter.next())
        consumed += 1
    }

    override fun hasNext(): Boolean {
        primeHead()
        return headSlot != null
    }

    override fun next(): Positioned<T> {
        primeHead()
        val head = headSlot ?: throw NoSuchElementException("WithPosition exhausted")
        primeTail()
        val hasMore = tailSlot != null
        val position = if (!handledFirst) {
            handledFirst = true
            if (hasMore) Position.First else Position.Only
        } else {
            if (hasMore) Position.Middle else Position.Last
        }
        // Advance: tail becomes new head.
        headSlot = tailSlot
        tailSlot = null
        return Positioned(position, head.value)
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    fun sizeHint(): SizeHint {
        val carried = (if (headSlot != null) 1 else 0) + (if (tailSlot != null) 1 else 0)
        return addScalar(subScalar(sourceHint, consumed), carried)
    }

    /**
     * Consumes the adaptor with a left fold, tagging the first, middle, and
     * last elements per upstream's `fold` impl.
     */
    fun <B> fold(initial: B, operation: (B, Positioned<T>) -> B): B {
        var acc = initial
        while (hasNext()) {
            acc = operation(acc, next())
        }
        return acc
    }
}

/**
 * The first component of the value yielded by [withPosition].
 * Indicates the position of this element in the iterator results.
 *
 * See [withPosition] for more information.
 */
enum class Position {
    /** This is the first element. */
    First,

    /** This is neither the first nor the last element. */
    Middle,

    /** This is the last element. */
    Last,

    /** This is the only element. */
    Only,
}

/**
 * Create a new [WithPosition] iterator.
 *
 * ```
 * val tagged = withPosition(listOf("a", "b", "c")).asSequence().toList()
 * // tagged == [Positioned(Position.First, "a"), Positioned(Position.Middle, "b"), Positioned(Position.Last, "c")]
 * ```
 */
internal fun <T> withPosition(iterable: Iterable<T>): Iterator<Positioned<T>> =
    WithPosition(iterable.iterator(), withPositionSizeHint(iterable))

private fun withPositionSizeHint(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> SizeHint(it.size, it.size)
    else -> SizeHint(0, null)
}