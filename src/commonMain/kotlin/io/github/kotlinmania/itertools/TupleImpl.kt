// port-lint: source tuple_impl.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that groups the items in tuples of size 1.
 */
class Tuples1<T>(
    private val iter: Iterator<T>,
) : Iterator<T> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("Tuples1 exhausted")
        }
        return iter.next()
    }

    /** Return elements that were not enough to form a complete tuple. */
    fun intoBuffer(): List<T> = emptyList()
}

/**
 * An iterator that groups the items in tuples (pairs) of size 2.
 */
class Tuples2<T>(
    private val iter: Iterator<T>,
) : Iterator<Pair<T, T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(2)

    private fun fillBuffer() {
        while (buffer.size < 2 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
    }

    override fun hasNext(): Boolean {
        fillBuffer()
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

    private fun fillBuffer() {
        while (buffer.size < 3 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
    }

    override fun hasNext(): Boolean {
        fillBuffer()
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
 * An iterator that groups the items in tuples of size 4.
 */
class Tuples4<T>(
    private val iter: Iterator<T>,
) : Iterator<List<T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(4)

    private fun fillBuffer() {
        while (buffer.size < 4 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
    }

    override fun hasNext(): Boolean {
        fillBuffer()
        return buffer.size == 4
    }

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("Tuples4 exhausted")
        }
        return listOf(buffer.removeFirst(), buffer.removeFirst(), buffer.removeFirst(), buffer.removeFirst())
    }

    /** Return elements that were not enough to form a complete tuple. */
    fun intoBuffer(): List<T> = buffer.toList()
}

/**
 * An iterator over all contiguous windows of size 1.
 */
class TupleWindows1<T>(
    private val iter: Iterator<T>,
) : Iterator<T> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("TupleWindows1 exhausted")
        }
        return iter.next()
    }
}

/**
 * An iterator over all contiguous windows of size 2.
 */
class TupleWindows2<T>(
    private val iter: Iterator<T>,
) : Iterator<Pair<T, T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(2)

    private fun fillBuffer() {
        while (buffer.size < 2 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
    }

    override fun hasNext(): Boolean {
        fillBuffer()
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
 * An iterator over all contiguous windows of size 3.
 */
class TupleWindows3<T>(
    private val iter: Iterator<T>,
) : Iterator<Triple<T, T, T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(3)

    private fun fillBuffer() {
        while (buffer.size < 3 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
    }

    override fun hasNext(): Boolean {
        fillBuffer()
        return buffer.size == 3
    }

    override fun next(): Triple<T, T, T> {
        if (!hasNext()) {
            throw NoSuchElementException("TupleWindows3 exhausted")
        }
        val first = buffer.removeFirst()
        val second = buffer[0]
        val third = buffer[1]
        return Triple(first, second, third)
    }
}

/**
 * An iterator over all contiguous windows of size 4.
 */
class TupleWindows4<T>(
    private val iter: Iterator<T>,
) : Iterator<List<T>> {
    private val buffer: ArrayDeque<T> = ArrayDeque(4)

    private fun fillBuffer() {
        while (buffer.size < 4 && iter.hasNext()) {
            buffer.addLast(iter.next())
        }
    }

    override fun hasNext(): Boolean {
        fillBuffer()
        return buffer.size == 4
    }

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("TupleWindows4 exhausted")
        }
        val first = buffer.removeFirst()
        val second = buffer[0]
        val third = buffer[1]
        val fourth = buffer[2]
        return listOf(first, second, third, fourth)
    }
}

/**
 * Create a new [Tuples1] iterator from an iterable.
 */
fun <T> tuples1(iterable: Iterable<T>): Tuples1<T> =
    Tuples1(iterable.iterator())

/**
 * Create a new [Tuples1] iterator.
 */
fun <T> tuples1(iterator: Iterator<T>): Tuples1<T> =
    Tuples1(iterator)

/**
 * Create a new [Tuples2] iterator from an iterable.
 */
fun <T> tuples2(iterable: Iterable<T>): Tuples2<T> =
    Tuples2(iterable.iterator())

/**
 * Create a new [Tuples2] iterator.
 */
fun <T> tuples2(iterator: Iterator<T>): Tuples2<T> =
    Tuples2(iterator)

/**
 * Create a new [Tuples3] iterator from an iterable.
 */
fun <T> tuples3(iterable: Iterable<T>): Tuples3<T> =
    Tuples3(iterable.iterator())

/**
 * Create a new [Tuples3] iterator.
 */
fun <T> tuples3(iterator: Iterator<T>): Tuples3<T> =
    Tuples3(iterator)

/**
 * Create a new [Tuples4] iterator from an iterable.
 */
fun <T> tuples4(iterable: Iterable<T>): Tuples4<T> =
    Tuples4(iterable.iterator())

/**
 * Create a new [Tuples4] iterator.
 */
fun <T> tuples4(iterator: Iterator<T>): Tuples4<T> =
    Tuples4(iterator)

/**
 * Create a new [TupleWindows1] iterator from an iterable.
 */
fun <T> tupleWindows1(iterable: Iterable<T>): TupleWindows1<T> =
    TupleWindows1(iterable.iterator())

/**
 * Create a new [TupleWindows1] iterator.
 */
fun <T> tupleWindows1(iterator: Iterator<T>): TupleWindows1<T> =
    TupleWindows1(iterator)

/**
 * Create a new [TupleWindows2] iterator from an iterable.
 */
fun <T> tupleWindows2(iterable: Iterable<T>): TupleWindows2<T> =
    TupleWindows2(iterable.iterator())

/**
 * Create a new [TupleWindows2] iterator.
 */
fun <T> tupleWindows2(iterator: Iterator<T>): TupleWindows2<T> =
    TupleWindows2(iterator)

/**
 * Create a new [TupleWindows3] iterator from an iterable.
 */
fun <T> tupleWindows3(iterable: Iterable<T>): TupleWindows3<T> =
    TupleWindows3(iterable.iterator())

/**
 * Create a new [TupleWindows3] iterator.
 */
fun <T> tupleWindows3(iterator: Iterator<T>): TupleWindows3<T> =
    TupleWindows3(iterator)

/**
 * Create a new [TupleWindows4] iterator from an iterable.
 */
fun <T> tupleWindows4(iterable: Iterable<T>): TupleWindows4<T> =
    TupleWindows4(iterable.iterator())

/**
 * Create a new [TupleWindows4] iterator.
 */
fun <T> tupleWindows4(iterator: Iterator<T>): TupleWindows4<T> =
    TupleWindows4(iterator)

/**
 * Collect the next pair of elements from an iterator, or null if fewer than 2 elements remain.
 */
fun <T> nextTuple2(iter: Iterator<T>): Pair<T, T>? {
    if (!iter.hasNext()) return null
    val a = iter.next()
    if (!iter.hasNext()) return null
    val b = iter.next()
    return Pair(a, b)
}

/**
 * Collect the next pair of elements from an iterable, or null if fewer than 2 elements remain.
 */
fun <T> nextTuple2(iterable: Iterable<T>): Pair<T, T>? =
    nextTuple2(iterable.iterator())

/**
 * Collect all elements into a pair if the iterator produces exactly 2 elements.
 */
fun <T> collectTuple2(iter: Iterator<T>): Pair<T, T>? {
    if (!iter.hasNext()) return null
    val a = iter.next()
    if (!iter.hasNext()) return null
    val b = iter.next()
    if (iter.hasNext()) return null
    return Pair(a, b)
}

/**
 * Collect all elements into a pair if the iterable contains exactly 2 elements.
 */
fun <T> collectTuple2(iterable: Iterable<T>): Pair<T, T>? =
    collectTuple2(iterable.iterator())
