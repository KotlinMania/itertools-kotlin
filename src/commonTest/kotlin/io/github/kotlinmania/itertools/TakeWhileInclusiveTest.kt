// port-lint: source src/take_while_inclusive.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TakeWhileInclusiveTest {
    @Test
    fun includesStoppingElement() {
        val out = takeWhileInclusive(listOf(1, 2, 3, 4, 5)) { it < 3 }
            .asSequence().toList()
        assertEquals(listOf(1, 2, 3), out)
    }

    @Test
    fun stopsAfterFirstFalse() {
        val out = takeWhileInclusive(listOf(10, 20, 30, 40)) { it < 1 }
            .asSequence().toList()
        assertEquals(listOf(10), out)
    }

    @Test
    fun fullySatisfiedRunsToEnd() {
        val out = takeWhileInclusive(listOf(1, 2, 3)) { true }
            .asSequence().toList()
        assertEquals(listOf(1, 2, 3), out)
    }

    @Test
    fun emptySource() {
        val it = takeWhileInclusive(emptyList<Int>()) { true }
        assertFalse(it.hasNext())
    }

    // sizeHint / fold are on the internal TakeWhileInclusive class, not on the
    // public Iterator<T> returned by takeWhileInclusive(...). Construct directly.

    @Test
    fun sizeHintShrinksToZeroOnceDone() {
        val src = listOf(1, 2, 3, 4)
        val it = TakeWhileInclusive(src.iterator(), { it < 2 }, SizeHint(src.size, src.size))
        assertEquals(0, it.sizeHint().lower)
        assertEquals(4, it.sizeHint().upper)
        assertEquals(1, it.next())
        assertEquals(2, it.next())
        assertFalse(it.hasNext())
        assertEquals(SizeHint(0, 0), it.sizeHint())
    }

    @Test
    fun foldRespectsInclusiveStop() {
        val src = listOf(1, 2, 3, 4, 5)
        val it = TakeWhileInclusive(src.iterator(), { it < 3 }, SizeHint(src.size, src.size))
        val total = it.fold(0) { acc, x -> acc + x }
        assertEquals(6, total)
    }

    @Test
    fun nullableElementsRoundTrip() {
        val out = takeWhileInclusive(listOf<Int?>(1, null, 3, 4)) { it != null }
            .asSequence().toList()
        assertEquals(listOf<Int?>(1, null), out)
    }
}
