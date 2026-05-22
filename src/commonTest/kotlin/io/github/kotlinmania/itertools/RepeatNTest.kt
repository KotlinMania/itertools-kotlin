// port-lint: source tests/test_core.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RepeatNTest {
    @Test
    fun repeatn() {
        val s = "α"
        val it = repeatN(s, 3)
        assertEquals(3, it.size)
        assertTrue(it.hasNext())
        assertEquals(s, it.next())
        assertEquals(s, it.next())
        assertEquals(s, it.next())
        assertFalse(it.hasNext())
    }

    @Test
    fun repeatnZero() {
        val it = repeatN(42, 0)
        assertEquals(0, it.size)
        assertFalse(it.hasNext())
    }

    @Test
    fun foldRemaining() {
        val sum = repeatN(7, 4).fold(0) { acc, x -> acc + x }
        assertEquals(28, sum)
    }

    @Test
    fun foldEmpty() {
        val sum = repeatN(7, 0).fold(100) { acc, x -> acc + x }
        assertEquals(100, sum)
    }
}
