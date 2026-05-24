// port-lint: source src/intersperse.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class IntersperseTest {
    @Test
    fun emptySourceYieldsNothing() {
        val it = intersperse(emptyList<Int>().iterator(), 0)
        assertFalse(it.hasNext())
        assertFailsWith<NoSuchElementException> { it.next() }
    }

    @Test
    fun singleElementHasNoSeparator() {
        val out = intersperse(listOf("only").iterator(), "_").asSequence().toList()
        assertEquals(listOf("only"), out)
    }

    @Test
    fun separatorBetweenEveryPair() {
        val out = intersperse(listOf(1, 2, 3, 4).iterator(), 0).asSequence().toList()
        assertEquals(listOf(1, 0, 2, 0, 3, 0, 4), out)
    }

    @Test
    fun intersperseWithCallsGeneratorBetweenElements() {
        var calls = 0
        val gen = IntersperseElement {
            calls += 1
            "[$calls]"
        }
        val out = intersperseWith(listOf("a", "b", "c").iterator(), gen).asSequence().toList()
        assertEquals(listOf("a", "[1]", "b", "[2]", "c"), out)
        assertEquals(2, calls)
    }

    @Test
    fun hasNextIsIdempotentBeforeNext() {
        val it = intersperse(listOf(1, 2).iterator(), 9)
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertEquals(1, it.next())
        assertTrue(it.hasNext())
        assertEquals(9, it.next())
        assertEquals(2, it.next())
        assertFalse(it.hasNext())
    }

    @Test
    fun nullableElementsRoundTrip() {
        val out = intersperse(listOf<Int?>(null, 1, null).iterator(), -1).asSequence().toList()
        assertEquals(listOf(null, -1, 1, -1, null), out)
    }

    @Test
    fun foldVisitsEveryEmittedElement() {
        val it = IntersperseWith(IntersperseElementSimple(0), listOf(1, 2, 3).iterator())
        val collected = it.fold(mutableListOf<Int>()) { acc, x ->
            acc.add(x); acc
        }
        assertEquals(listOf(1, 0, 2, 0, 3), collected)
    }

    @Test
    fun sizeHintDoublesAndCarriesPeekState() {
        val src = listOf(1, 2, 3)
        val it = IntersperseWith(IntersperseElementSimple(0), src.iterator(), src.size to src.size)
        // Before any next(), peek is null → subScalar(double, 1).
        assertEquals(5 to 5, it.sizeHint())
        assertEquals(1, it.next())
        // Now peek holds a buffered source item → addScalar(double, 1).
        assertEquals(4 to 4, it.sizeHint())
        assertEquals(0, it.next())
        // peek went back to Empty after consuming the buffered source item.
        assertEquals(3 to 3, it.sizeHint())
    }
}
