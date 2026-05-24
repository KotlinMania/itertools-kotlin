// port-lint: ignore
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class UnzipTupleTest {

    @Test
    fun unzipPair() {
        val inputs = listOf(1 to 'a', 2 to 'b', 3 to 'c')
        val (a, b) = multiUnzip(inputs)
        assertEquals(listOf(1, 2, 3), a)
        assertEquals(listOf('a', 'b', 'c'), b)
    }

    @Test
    fun unzipPairEmpty() {
        val (a, b) = multiUnzip(emptyList<Pair<Int, String>>())
        assertEquals(emptyList(), a)
        assertEquals(emptyList(), b)
    }

    @Test
    fun unzipTriple() {
        val inputs = listOf(Triple(1, 2, 3), Triple(4, 5, 6), Triple(7, 8, 9))
        val (a, b, c) = multiUnzip(inputs)
        assertEquals(listOf(1, 4, 7), a)
        assertEquals(listOf(2, 5, 8), b)
        assertEquals(listOf(3, 6, 9), c)
    }

    @Test
    fun unzipTripleEmpty() {
        val (a, b, c) = multiUnzip(emptyList<Triple<Int, Int, Int>>())
        assertEquals(emptyList(), a)
        assertEquals(emptyList(), b)
        assertEquals(emptyList(), c)
    }
}
