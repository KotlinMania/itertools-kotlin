// port-lint: source unziptuple.rs
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
fun <A, B> multiUnzip(i: Iterable<Pair<A, B>>): Pair<List<A>, List<B>> {
    val resA = mutableListOf<A>()
    val resB = mutableListOf<B>()
    for ((a, b) in i) {
        resA.add(a)
        resB.add(b)
    }
    return resA to resB
}

/**
 * Converts an iterator of tuples into a tuple of containers.
 */
fun <A, B> multiUnzip(i: Iterator<Pair<A, B>>): Pair<List<A>, List<B>> {
    val resA = mutableListOf<A>()
    val resB = mutableListOf<B>()
    while (i.hasNext()) {
        val (a, b) = i.next()
        resA.add(a)
        resB.add(b)
    }
    return resA to resB
}

/** [Triple] overload of [multiUnzip]. */
fun <A, B, C> multiUnzip(i: Iterable<Triple<A, B, C>>): Triple<List<A>, List<B>, List<C>> {
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

/** [Triple] overload of [multiUnzip] for [Iterator]. */
fun <A, B, C> multiUnzip(i: Iterator<Triple<A, B, C>>): Triple<List<A>, List<B>, List<C>> {
    val resA = mutableListOf<A>()
    val resB = mutableListOf<B>()
    val resC = mutableListOf<C>()
    while (i.hasNext()) {
        val (a, b, c) = i.next()
        resA.add(a)
        resB.add(b)
        resC.add(c)
    }
    return Triple(resA, resB, resC)
}

/**
 * An iterator that can be unzipped into multiple collections.
 */
interface MultiUnzip<FromI> {
    /**
     * Unzip this iterator into multiple collections.
     */
    fun multiunzip(): FromI
}

