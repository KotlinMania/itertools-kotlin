// port-lint: source ziptuple.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that generalizes `.zip()` and allows running multiple iterators in lockstep.
 */
class Zip2<A, B>(
    private val a: Iterator<A>,
    private val b: Iterator<B>,
) : Iterator<Pair<A, B>> {
    override fun hasNext(): Boolean = a.hasNext() && b.hasNext()

    override fun next(): Pair<A, B> {
        if (!hasNext()) {
            throw NoSuchElementException("Zip2 exhausted")
        }
        return Pair(a.next(), b.next())
    }
}

/**
 * An iterator that generalizes `.zip()` and allows running 3 iterators in lockstep.
 */
class Zip3<A, B, C>(
    private val a: Iterator<A>,
    private val b: Iterator<B>,
    private val c: Iterator<C>,
) : Iterator<Triple<A, B, C>> {
    override fun hasNext(): Boolean = a.hasNext() && b.hasNext() && c.hasNext()

    override fun next(): Triple<A, B, C> {
        if (!hasNext()) {
            throw NoSuchElementException("Zip3 exhausted")
        }
        return Triple(a.next(), b.next(), c.next())
    }
}

/**
 * An iterator that generalizes `.zip()` over multiple iterators represented as a list.
 */
class MultiZip<T>(
    private val iters: List<Iterator<T>>,
) : Iterator<List<T>> {
    override fun hasNext(): Boolean = iters.isNotEmpty() && iters.all { it.hasNext() }

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("MultiZip exhausted")
        }
        return iters.map { it.next() }
    }
}

/**
 * Multizip 2 iterables.
 */
fun <A, B> multizip(a: Iterable<A>, b: Iterable<B>): Zip2<A, B> =
    Zip2(a.iterator(), b.iterator())

/**
 * Multizip 3 iterables.
 */
fun <A, B, C> multizip(a: Iterable<A>, b: Iterable<B>, c: Iterable<C>): Zip3<A, B, C> =
    Zip3(a.iterator(), b.iterator(), c.iterator())

/**
 * Multizip a list of iterables of homogeneous type.
 */
fun <T> multizip(iters: Iterable<Iterable<T>>): MultiZip<T> =
    MultiZip(iters.map { it.iterator() })
