// port-lint: source cons_tuples_impl.rs
package io.github.kotlinmania.itertools

/**
 * An iterator that maps an iterator of nested pairs like
 * `Pair(Pair(A, B), C)` to an iterator of `Triple(A, B, C)`.
 *
 * See [consTuples] for more information.
 */
class ConsTuples<A, B, C> internal constructor(
    private val iter: Iterator<Pair<Pair<A, B>, C>>,
) : Iterator<Triple<A, B, C>> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): Triple<A, B, C> {
        val (pairAB, c) = iter.next()
        val (a, b) = pairAB
        return Triple(a, b, c)
    }
}

/**
 * Create an iterator that maps iterators of `Pair(Pair(A, B), C)` to `Triple(A, B, C)`.
 */
fun <A, B, C> consTuples(iterable: Iterable<Pair<Pair<A, B>, C>>): ConsTuples<A, B, C> =
    ConsTuples(iterable.iterator())

/**
 * Create an iterator that maps iterators of `Pair(Pair(A, B), C)` to `Triple(A, B, C)`.
 */
fun <A, B, C> consTuples(iterator: Iterator<Pair<Pair<A, B>, C>>): ConsTuples<A, B, C> =
    ConsTuples(iterator)
