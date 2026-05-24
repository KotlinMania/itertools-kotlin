// port-lint: source src/zip_eq_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZipEqImplTest {
    @Test
    fun zipEqEqualLength() {
        val a = listOf(1, 2, 3)
        val b = listOf("a", "b", "c")
        val out = zipEq(a, b).asSequence().toList()
        assertEquals(listOf(1 to "a", 2 to "b", 3 to "c"), out)
    }

    @Test
    fun zipEqBothEmpty() {
        val a = emptyList<Int>()
        val b = emptyList<Int>()
        val it = zipEq(a, b)
        assertFalse(it.hasNext())
    }

    @Test
    fun zipEqLeftLongerThrows() {
        val a = listOf(1, 2, 3)
        val b = listOf("a", "b")
        val it = zipEq(a, b)
        assertEquals(1 to "a", it.next())
        assertEquals(2 to "b", it.next())
        assertFailsWith<IllegalStateException> { it.hasNext() }
    }

    @Test
    fun zipEqRightLongerThrows() {
        val a = listOf(1, 2)
        val b = listOf("a", "b", "c")
        val it = zipEq(a, b)
        assertEquals(1 to "a", it.next())
        assertEquals(2 to "b", it.next())
        assertFailsWith<IllegalStateException> { it.hasNext() }
    }

    @Test
    fun zipEqSizeHintMatchesRemaining() {
        // sizeHint is on the internal ZipEq class, not on the public Iterator<Pair<A, B>>
        // returned by zipEq(...). Use the internal constructor directly for the size-hint
        // round-trip assertions; the user-facing iteration shape is covered by the other
        // tests above against the public factory.
        val a = listOf(1, 2, 3, 4)
        val b = listOf("a", "b", "c", "d")
        val it = ZipEq(a.iterator(), b.iterator(), a.size to a.size, b.size to b.size)
        assertEquals(4 to 4, it.sizeHint())
        it.next()
        assertEquals(3 to 3, it.sizeHint())
        it.next()
        it.next()
        it.next()
        assertEquals(0 to 0, it.sizeHint())
        assertFalse(it.hasNext())
    }

    @Test
    fun zipEqHasNextIdempotent() {
        val it = zipEq(listOf(1, 2), listOf("a", "b"))
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertEquals(1 to "a", it.next())
        assertTrue(it.hasNext())
        assertEquals(2 to "b", it.next())
        assertFalse(it.hasNext())
    }
}
