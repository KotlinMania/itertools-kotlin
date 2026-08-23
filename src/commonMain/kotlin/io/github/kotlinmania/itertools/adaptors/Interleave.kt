// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

/**
 * An iterator adaptor that alternates elements from two iterators until both run out.
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
}

/**
 * Create an iterator that interleaves elements in [i] and [j].
 */
fun <T> interleave(i: Iterator<T>, j: Iterator<T>): Interleave<T> =
    Interleave(i, j)

/**
 * Create an iterator that interleaves elements in [i] and [j].
 */
fun <T> interleave(i: Iterable<T>, j: Iterable<T>): Interleave<T> =
    Interleave(i.iterator(), j.iterator())

/**
 * An iterator adaptor that alternates elements from two iterators until one of them runs out.
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
}

/**
 * Create a new [InterleaveShortest] iterator.
 */
fun <T> interleaveShortest(i: Iterator<T>, j: Iterator<T>): InterleaveShortest<T> =
    InterleaveShortest(i, j)

/**
 * Create a new [InterleaveShortest] iterator from iterables.
 */
fun <T> interleaveShortest(i: Iterable<T>, j: Iterable<T>): InterleaveShortest<T> =
    InterleaveShortest(i.iterator(), j.iterator())
