// port-lint: source src/ziptuple.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class ZiptupleTest {
    @Test
    fun testMultizipPair() {
        val a = listOf(1, 2, 3)
        val b = listOf("a", "b", "c")
        val res = multizip(a, b).asSequence().toList()
        assertEquals(listOf(Pair(1, "a"), Pair(2, "b"), Pair(3, "c")), res)
    }

    @Test
    fun testMultizipTriple() {
        val a = listOf(1, 2)
        val b = listOf("a", "b")
        val c = listOf(true, false)
        val res = multizip(a, b, c).asSequence().toList()
        assertEquals(listOf(Triple(1, "a", true), Triple(2, "b", false)), res)
    }

    @Test
    fun testMultizipList() {
        val lists = listOf(
            listOf(1, 2, 3),
            listOf(4, 5, 6),
            listOf(7, 8, 9),
        )
        val res = multizip(lists).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, 4, 7),
                listOf(2, 5, 8),
                listOf(3, 6, 9),
            ),
            res,
        )
    }
}
