// port-lint: tests free.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class FreeTest {
    @Test
    fun testEnumerate() {
        val list = listOf("a", "b", "c")
        val result = enumerate(list).asSequence().toList()
        assertEquals(3, result.size)
        assertEquals(0, result[0].index)
        assertEquals("a", result[0].value)
        assertEquals(1, result[1].index)
        assertEquals("b", result[1].value)
        assertEquals(2, result[2].index)
        assertEquals("c", result[2].value)
    }

    @Test
    fun testRev() {
        val list = listOf(1, 2, 3)
        assertEquals(listOf(3, 2, 1), rev(list).asSequence().toList())
    }

    @Test
    fun testZip() {
        val a = listOf(1, 2, 3, 4, 5)
        val b = listOf("a", "b", "c")
        val result = zip(a, b).asSequence().toList()
        assertEquals(listOf(Pair(1, "a"), Pair(2, "b"), Pair(3, "c")), result)
    }

    @Test
    fun testChain() {
        val a = listOf(1, 2, 3)
        val b = listOf(4, 5)
        assertEquals(listOf(1, 2, 3, 4, 5), chain(a, b).asSequence().toList())
    }

    @Test
    fun testCloned() {
        val a = listOf(1, 2, 3)
        assertEquals(listOf(1, 2, 3), cloned(a).asSequence().toList())
    }

    @Test
    fun testFold() {
        val a = listOf(1, 2, 3)
        val sum = fold(a, 0) { acc, x -> acc + x }
        assertEquals(6, sum)
    }

    @Test
    fun testAllAndAny() {
        val a = listOf(1, 2, 3)
        assertTrue(all(a) { it > 0 })
        assertFalse(all(a) { it > 2 })
        assertTrue(any(a) { it == 2 })
        assertFalse(any(a) { it < 0 })
    }

    @Test
    fun testMinAndMax() {
        val a = listOf(3, 1, 4, 1, 5, 9)
        assertEquals(1, min(a))
        assertEquals(9, max(a))
        assertNull(min(emptyList<Int>()))
        assertNull(max(emptyList<Int>()))
    }

    @Test
    fun testJoin() {
        val a = listOf(1, 2, 3)
        assertEquals("1, 2, 3", join(a, ", "))
    }

    @Test
    fun testSorted() {
        val a = listOf(3, 1, 4, 1, 5)
        assertEquals(listOf(1, 1, 3, 4, 5), sorted(a).asSequence().toList())
        assertEquals(listOf(1, 1, 3, 4, 5), sortedUnstable(a).asSequence().toList())
    }
}
