// port-lint: source src/diff.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class DiffTest {
    @Test
    fun testDiffEqual() {
        val a = listOf(1, 2, 3)
        val b = listOf(1, 2, 3)
        val result = diffWith(a, b) { x, y -> x == y }
        assertNull(result)
    }

    @Test
    fun testDiffMismatch() {
        val a = listOf(1, 2, 3, 4)
        val b = listOf(1, 5, 3, 4)
        val diff = diffWith(a, b) { x, y -> x == y }
        assertTrue(diff is Diff.FirstMismatch)
        assertEquals(1, diff.index)
        assertEquals(listOf(2, 3, 4), diff.firstRemaining.asSequence().toList())
        assertEquals(listOf(5, 3, 4), diff.secondRemaining.asSequence().toList())
    }

    @Test
    fun testDiffLonger() {
        val a = listOf(1, 2, 3, 4)
        val b = listOf(1, 2, 3, 4, 5, 6)
        val diff = diffWith(a, b) { x, y -> x == y }
        assertTrue(diff is Diff.Longer)
        assertEquals(4, diff.length)
        assertEquals(listOf(5, 6), diff.remaining.asSequence().toList())
    }

    @Test
    fun testDiffShorter() {
        val a = listOf(1, 2, 3, 4)
        val b = listOf(1, 2)
        val diff = diffWith(a, b) { x, y -> x == y }
        assertTrue(diff is Diff.Shorter)
        assertEquals(2, diff.length)
        assertEquals(listOf(3, 4), diff.remaining.asSequence().toList())
    }
}
