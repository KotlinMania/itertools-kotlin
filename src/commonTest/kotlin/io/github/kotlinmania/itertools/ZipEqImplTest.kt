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
        assertEquals(listOf(Zipped(1, "a"), Zipped(2, "b"), Zipped(3, "c")), out)
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
        assertEquals(Zipped(1, "a"), it.next())
        assertEquals(Zipped(2, "b"), it.next())
        assertFailsWith<IllegalStateException> { it.hasNext() }
    }

    @Test
    fun zipEqRightLongerThrows() {
        val a = listOf(1, 2)
        val b = listOf("a", "b", "c")
        val it = zipEq(a, b)
        assertEquals(Zipped(1, "a"), it.next())
        assertEquals(Zipped(2, "b"), it.next())
        assertFailsWith<IllegalStateException> { it.hasNext() }
    }

    @Test
    fun zipEqSizeHintMatchesRemaining() {
        // sizeHint is on the internal ZipEq class, not on the public Iterator<Zipped<A, B>>
        // returned by zipEq(...). Use the internal constructor directly for the size-hint
        // round-trip assertions; the user-facing iteration shape is covered by the other
        // tests above against the public factory.
        val a = listOf(1, 2, 3, 4)
        val b = listOf("a", "b", "c", "d")
        val it = ZipEq(a.iterator(), b.iterator(), SizeHint(a.size, a.size), SizeHint(b.size, b.size))
        assertEquals(SizeHint(4, 4), it.sizeHint())
        it.next()
        assertEquals(SizeHint(3, 3), it.sizeHint())
        it.next()
        it.next()
        it.next()
        assertEquals(SizeHint(0, 0), it.sizeHint())
        assertFalse(it.hasNext())
    }

    @Test
    fun zipEqHasNextIdempotent() {
        val it = zipEq(listOf(1, 2), listOf("a", "b"))
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertEquals(Zipped(1, "a"), it.next())
        assertTrue(it.hasNext())
        assertEquals(Zipped(2, "b"), it.next())
        assertFalse(it.hasNext())
    }
}
