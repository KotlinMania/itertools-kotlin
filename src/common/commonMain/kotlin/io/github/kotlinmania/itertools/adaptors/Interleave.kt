package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adaptor that alternates elements from two iterators until both run out.
 *
 * This iterator is *fused*.
 */
class Interleave<T>(
    private val i: Iterator<T>,
    private val j: Iterator<T>,
) : Iterator<T> {
    private var nextComingFromJ = false

    override fun hasNext(): Boolean = i.hasNext() || j.hasNext()

    override fun next(): T {
        nextComingFromJ = !nextComingFromJ
        return if (nextComingFromJ) {
            if (i.hasNext()) {
                i.next()
            } else if (j.hasNext()) {
                j.next()
            } else {
                throw NoSuchElementException("Interleave exhausted")
            }
        } else {
            if (j.hasNext()) {
                j.next()
            } else if (i.hasNext()) {
                i.next()
            } else {
                throw NoSuchElementException("Interleave exhausted")
            }
        }
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val iHint = sizeHintOf(i)
        val jHint = sizeHintOf(j)
        return io.github.kotlinmania.itertools
            .add(iHint, jHint)
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create an iterator that interleaves elements in [i] and [j].
 */
fun <T> interleave(i: Iterator<T>, j: Iterator<T>): Interleave<T> = Interleave(i, j)

/**
 * Create an iterator that interleaves elements in [i] and [j].
 */
fun <T> interleave(i: Iterable<T>, j: Iterable<T>): Interleave<T> = Interleave(i.iterator(), j.iterator())

/**
 * An iterator adaptor that alternates elements from the two iterators until one of them runs out.
 *
 * This iterator is *fused*.
 */
class InterleaveShortest<T>(
    private val i: Iterator<T>,
    private val j: Iterator<T>,
) : Iterator<T> {
    private var nextComingFromJ = false

    override fun hasNext(): Boolean =
        if (nextComingFromJ) {
            j.hasNext()
        } else {
            i.hasNext()
        }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("InterleaveShortest exhausted")
        }
        val e =
            if (nextComingFromJ) {
                j.next()
            } else {
                i.next()
            }
        nextComingFromJ = !nextComingFromJ
        return e
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val iHint = sizeHintOf(i)
        val jHint = sizeHintOf(j)
        val (currHint, nextHint) = if (nextComingFromJ) Pair(jHint, iHint) else Pair(iHint, jHint)
        val minHint =
            io.github.kotlinmania.itertools
                .min(currHint, nextHint)
        val combined =
            io.github.kotlinmania.itertools
                .mulScalar(minHint, 2)
        val lower = if (currHint.lower > nextHint.lower) combined.lower + 1 else combined.lower
        val upper =
            if (currHint.upper != null && nextHint.upper != null && currHint.upper > nextHint.upper) {
                combined.upper?.plus(1)
            } else {
                combined.upper
            }
        return SizeHint(lower, upper)
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create a new [InterleaveShortest] iterator.
 */
fun <T> interleaveShortest(i: Iterator<T>, j: Iterator<T>): InterleaveShortest<T> = InterleaveShortest(i, j)

/**
 * Create a new [InterleaveShortest] iterator from iterables.
 */
fun <T> interleaveShortest(i: Iterable<T>, j: Iterable<T>): InterleaveShortest<T> =
    InterleaveShortest(i.iterator(), j.iterator())
