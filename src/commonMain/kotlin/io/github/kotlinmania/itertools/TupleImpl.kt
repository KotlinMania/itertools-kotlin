// port-lint: source src/tuple_impl.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that groups the items in tuples (pairs) of size 2.
 */
class Tuples2<T>(
    private val iter: Iterator<T>,
) : Iterator<Pair<T, T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(2)

    override fun hasNext(): Boolean {
        while (buffer.size < 2 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
        return buffer.size == 2
    }

    override fun next(): Pair<T, T> {
        if (!hasNext()) {
            throw NoSuchElementException("Tuples2 exhausted")
        }
        return Pair(buffer.removeFirst(), buffer.removeFirst())
    }

    /** Return elements that were not enough to form a complete tuple. */
    fun intoBuffer(): List<T> = buffer.toList()
}

/**
 * An iterator that groups the items in tuples (triples) of size 3.
 */
class Tuples3<T>(
    private val iter: Iterator<T>,
) : Iterator<Triple<T, T, T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(3)

    override fun hasNext(): Boolean {
        while (buffer.size < 3 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
        return buffer.size == 3
    }

    override fun next(): Triple<T, T, T> {
        if (!hasNext()) {
            throw NoSuchElementException("Tuples3 exhausted")
        }
        return Triple(buffer.removeFirst(), buffer.removeFirst(), buffer.removeFirst())
    }

    /** Return elements that were not enough to form a complete tuple. */
    fun intoBuffer(): List<T> = buffer.toList()
}

/**
 * An iterator over all contiguous windows of size 2.
 */
class TupleWindows2<T>(
    private val iter: Iterator<T>,
) : Iterator<Pair<T, T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(2)

    override fun hasNext(): Boolean {
        while (buffer.size < 2 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
        return buffer.size == 2
    }

    override fun next(): Pair<T, T> {
        if (!hasNext()) {
            throw NoSuchElementException("TupleWindows2 exhausted")
        }
        val first = buffer.removeFirst()
        val second = buffer.first()
        return Pair(first, second)
    }
}

/**
 * Create a new [Tuples2] iterator.
 */
fun <T> tuples2(iterable: Iterable<T>): Tuples2<T> =
    Tuples2(iterable.iterator())

/**
 * Create a new [Tuples3] iterator.
 */
fun <T> tuples3(iterable: Iterable<T>): Tuples3<T> =
    Tuples3(iterable.iterator())

/**
 * Create a new [TupleWindows2] iterator.
 */
fun <T> tupleWindows2(iterable: Iterable<T>): TupleWindows2<T> =
    TupleWindows2(iterable.iterator())
