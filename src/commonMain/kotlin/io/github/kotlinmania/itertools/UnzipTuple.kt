// port-lint: source src/unziptuple.rs
package io.github.kotlinmania.itertools

/**
 * Converts an iterator of tuples into a tuple of containers.
 *
 * `multiUnzip()` consumes an entire iterator of n-ary tuples, producing `n` collections, one for
 * each column.
 *
 * This function is, in some sense, the opposite of `multizip`.
 *
 * ```
 * val inputs = listOf(Triple(1, 2, 3), Triple(4, 5, 6), Triple(7, 8, 9))
 * val (a, b, c) = multiUnzip(inputs)
 * check(a == listOf(1, 4, 7))
 * check(b == listOf(2, 5, 8))
 * check(c == listOf(3, 6, 9))
 * ```
 */
internal fun <A, B> multiUnzip(i: Iterable<Pair<A, B>>): Pair<List<A>, List<B>> {
    val resA = mutableListOf<A>()
    val resB = mutableListOf<B>()
    for ((a, b) in i) {
        resA.add(a)
        resB.add(b)
    }
    return resA to resB
}

/** [Triple] overload of [multiUnzip]. */
internal fun <A, B, C> multiUnzip(i: Iterable<Triple<A, B, C>>): Triple<List<A>, List<B>, List<C>> {
    val resA = mutableListOf<A>()
    val resB = mutableListOf<B>()
    val resC = mutableListOf<C>()
    for ((a, b, c) in i) {
        resA.add(a)
        resB.add(b)
        resC.add(c)
    }
    return Triple(resA, resB, resC)
}
