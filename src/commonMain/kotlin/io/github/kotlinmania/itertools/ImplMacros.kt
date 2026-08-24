// port-lint: source impl_macros.rs
package io.github.kotlinmania.itertools

/**
 * Implementation's internal macros ledger.
 */
public object ImplMacros

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
