// port-lint: source src/intersperse.rs (tests/test_core.rs::test_intersperse, test_intersperse_with)
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntersperseTest {

    /** Mirror of `tests/test_core.rs::test_intersperse`. */
    @Test
    fun intersperseBasic() {
        val xs = listOf<UByte>(1u, 2u, 3u)
        val ys = listOf<UByte>(1u, 0u, 2u, 0u, 3u)
        assertEquals(ys, intersperse(xs, 0u).asSequence().toList())
    }

    /** Mirror of `tests/test_core.rs::test_intersperse_with`. */
    @Test
    fun intersperseWithLambda() {
        val xs = listOf<UByte>(1u, 2u, 3u)
        val ys = listOf<UByte>(1u, 10u, 2u, 10u, 3u)
        val i: UByte = 10u
        assertEquals(ys, intersperseWith(xs) { i }.asSequence().toList())
    }

    @Test
    fun intersperseEmpty() {
        val it = intersperse(emptyList<Int>(), 99)
        assertFalse(it.hasNext())
        assertFailsWith<NoSuchElementException> { it.next() }
    }

    @Test
    fun intersperseSingleElementYieldsNoSeparator() {
        val out = intersperse(listOf(42), 0).asSequence().toList()
        assertEquals(listOf(42), out)
    }

    /** Source of two elements yields `[a, sep, b]` — no trailing separator. */
    @Test
    fun intersperseTwoElements() {
        val out = intersperse(listOf("a", "b"), "-").asSequence().toList()
        assertEquals(listOf("a", "-", "b"), out)
    }

    /**
     * Size hint tracks the upstream `iter.size_hint() * 2` baseline,
     * adjusted for buffered state. For a 3-element collection input,
     * the initial hint is `(5, 5)` and decreases by one on every next().
     */
    @Test
    fun intersperseSizeHintTracksOutput() {
        val it = intersperse(listOf(1, 2, 3), 0)
        assertEquals(5 to 5, it.sizeHint())
        it.next() // yield 1
        assertEquals(4 to 4, it.sizeHint())
        it.next() // yield 0
        assertEquals(3 to 3, it.sizeHint())
        it.next() // yield 2
        assertEquals(2 to 2, it.sizeHint())
        it.next() // yield 0
        assertEquals(1 to 1, it.sizeHint())
        it.next() // yield 3
        assertEquals(0 to 0, it.sizeHint())
        assertFalse(it.hasNext())
    }

    /** Repeatedly calling hasNext() without next() must not advance the iterator. */
    @Test
    fun intersperseHasNextIdempotent() {
        val it = intersperse(listOf(1, 2), 0)
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertEquals(1, it.next())
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertEquals(0, it.next())
        assertEquals(2, it.next())
        assertFalse(it.hasNext())
        assertFalse(it.hasNext())
    }

    /** Non-Collection source: size hint stays `(0, null)`. */
    @Test
    fun intersperseNonCollectionSourceHasUnknownHint() {
        val it = intersperseWith(sequenceOf(1, 2, 3).iterator(), IntersperseElementSimple(0))
        assertEquals(0 to null, it.sizeHint())
        assertEquals(listOf(1, 0, 2, 0, 3), it.asSequence().toList())
    }
}
