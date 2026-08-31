// port-lint: source pad_tail.rs
package io.github.kotlinmania.itertools

import kotlin.jvm.JvmName

/**
 * An iterator adaptor that pads a sequence to a minimum length by filling
 * missing elements using a function.
 *
 * Iterator element type is the source iterator's element type.
 *
 * See [padUsing] for more information.
 */
public class PadUsing<T>(
    private val iter: Iterator<T>,
    private var min: Int,
    private val sourceHint: SizeHint,
    private val filler: (Int) -> T,
    private val listIterator: ListIterator<T>? = null,
) : Iterator<T> {
    private var pos: Int = 0
    private var sourceExhausted: Boolean = false

    constructor(list: List<T>, min: Int, filler: (Int) -> T) : this(
        list.iterator(),
        min,
        SizeHint(list.size, list.size),
        filler,
        list.listIterator(list.size),
    )

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

    /** Returns the next element from the back when double-ended iteration is available. */
    fun nextBack(): T? {
        val li = listIterator ?: return null
        val remaining = li.previousIndex() + 1
        return if (min == 0) {
            if (li.hasPrevious()) li.previous() else null
        } else if (remaining >= min) {
            min -= 1
            li.previous()
        } else {
            min -= 1
            filler(min)
        }
    }

    /** Returns the size hint. */
    fun sizeHint(): SizeHint {
        val tail = if (pos < min) min - pos else 0
        val remaining = subScalar(sourceHint, pos)
        return max(remaining, SizeHint(tail, tail))
    }

    /** Consumes the adaptor with a left fold. */
    fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var acc = initial
        while (hasNext()) {
            acc = operation(acc, next())
        }
        return acc
    }

    /** Consumes the adaptor with a right fold. */
    fun <B> rfold(initial: B, operation: (B, T) -> B): B {
        val li = listIterator
        if (li != null) {
            var acc = initial
            while (true) {
                val item = nextBack() ?: break
                acc = operation(acc, item)
            }
            return acc
        }
        val items = asSequence().toList()
        var acc = initial
        for (i in items.indices.reversed()) {
            acc = operation(acc, items[i])
        }
        return acc
    }
}

/**
 * Create a new [PadUsing] iterator.
 *
 * Drains [iter]; if fewer than [min] elements have been produced, calls
 * [filler] with each missing zero-based position until the minimum length is
 * reached. If the source produces at least [min] elements, [filler] is never
 * called.
 */
@JvmName("padUsingIter")
public fun <T> padUsing(iter: Iterator<T>, min: Int, filler: (Int) -> T): PadUsing<T> =
    PadUsing(iter, min, SizeHint(0, null), filler)

@JvmName("padUsingIterable")
public fun <T> padUsing(iterable: Iterable<T>, min: Int, filler: (Int) -> T): PadUsing<T> =
    if (iterable is List<T>) {
        PadUsing(iterable, min, filler)
    } else {
        PadUsing(iterable.iterator(), min, padTailIterableHint(iterable), filler)
    }

private fun padTailIterableHint(it: Iterable<*>): SizeHint =
    when (it) {
        is Collection<*> -> SizeHint(it.size, it.size)
        else -> SizeHint(0, null)
    }
