// port-lint: source cons_tuples_impl.rs
package io.github.kotlinmania.itertools

/**
 * Function object for cons-tuples mapping.
 */
object ConsTuplesFn

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

/**
 * Cartesian product of two iterables.
 */
fun <A, B> iproduct(a: Iterable<A>, b: Iterable<B>): Iterator<Pair<A, B>> =
    sequence {
        for (x in a) {
            for (y in b) {
                yield(Pair(x, y))
            }
        }
    }.iterator()

/**
 * Cartesian product of three iterables.
 */
fun <A, B, C> iproduct(a: Iterable<A>, b: Iterable<B>, c: Iterable<C>): Iterator<Triple<A, B, C>> =
    sequence {
        for (x in a) {
            for (y in b) {
                for (z in c) {
                    yield(Triple(x, y, z))
                }
            }
        }
    }.iterator()

/**
 * Cartesian product of four iterables.
 */
fun <A, B, C, D> iproduct(
    a: Iterable<A>,
    b: Iterable<B>,
    c: Iterable<C>,
    d: Iterable<D>,
): Iterator<List<Any?>> =
    sequence {
        for (w in a) {
            for (x in b) {
                for (y in c) {
                    for (z in d) {
                        yield(listOf(w, x, y, z))
                    }
                }
            }
        }
    }.iterator()

