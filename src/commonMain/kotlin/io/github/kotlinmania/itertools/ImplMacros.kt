// port-lint: source impl_macros.rs
package io.github.kotlinmania.itertools

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
