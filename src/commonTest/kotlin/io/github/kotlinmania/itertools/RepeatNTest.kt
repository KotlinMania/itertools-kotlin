// port-lint: source tests/test_core.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RepeatNTest {
    // size / fold live on the internal RepeatN class; the public repeatN(...)
    // factory returns Iterator<A>. Construct the internal class directly to
    // exercise the type-specific shape.

    @Test
    fun repeatn() {
        val s = "α"
        val it = repeatN(s, 3)
        assertTrue(it.hasNext())
        assertEquals(s, it.next())
        assertEquals(s, it.next())
        assertEquals(s, it.next())
        assertFalse(it.hasNext())
    }

    @Test
    fun repeatnZero() {
        val it = repeatN(42, 0)
        assertFalse(it.hasNext())
    }

    @Test
    fun repeatnSizeProperty() {
        val it = RepeatN(elt = "α", n = 3)
        assertEquals(3, it.size)
    }

    @Test
    fun repeatnZeroSizeProperty() {
        val it = RepeatN(elt = null, n = 0)
        assertEquals(0, it.size)
    }

    @Test
    fun foldRemaining() {
        val it = RepeatN(elt = 7, n = 4)
        val sum = it.fold(0) { acc, x -> acc + x }
        assertEquals(28, sum)
    }

    @Test
    fun foldEmpty() {
        val it = RepeatN<Int>(elt = null, n = 0)
        val sum = it.fold(100) { acc, x -> acc + x }
        assertEquals(100, sum)
    }
}
