// port-lint: source src/with_position.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class WithPositionTest {
    @Test
    fun emptyYieldsNothing() {
        val it = withPosition(emptyList<Int>())
        assertFalse(it.hasNext())
    }

    @Test
    fun singleElementIsOnly() {
        val out = withPosition(listOf(42)).asSequence().toList()
        assertEquals(listOf(Position.Only to 42), out)
    }

    @Test
    fun twoElementsFirstThenLast() {
        val out = withPosition(listOf("a", "b")).asSequence().toList()
        assertEquals(listOf(Position.First to "a", Position.Last to "b"), out)
    }

    @Test
    fun threeOrMoreElementsTagFirstMiddleLast() {
        val out = withPosition(listOf(1, 2, 3, 4)).asSequence().toList()
        assertEquals(
            listOf(
                Position.First to 1,
                Position.Middle to 2,
                Position.Middle to 3,
                Position.Last to 4,
            ),
            out,
        )
    }

    @Test
    fun sizeHintMirrorsRemaining() {
        val it = withPosition(listOf(1, 2, 3))
        assertEquals(3 to 3, it.sizeHint())
        it.next()
        assertEquals(2 to 2, it.sizeHint())
        it.next()
        assertEquals(1 to 1, it.sizeHint())
        it.next()
        assertEquals(0 to 0, it.sizeHint())
        assertFalse(it.hasNext())
    }

    @Test
    fun foldVisitsEveryPositionOnce() {
        val it = withPosition(listOf("x", "y", "z"))
        val collected = it.fold(mutableListOf<Pair<Position, String>>()) { acc, p ->
            acc.add(p); acc
        }
        assertEquals(
            listOf(
                Position.First to "x",
                Position.Middle to "y",
                Position.Last to "z",
            ),
            collected,
        )
    }

    @Test
    fun nullableElementsRoundTrip() {
        val out = withPosition(listOf<Int?>(null, 1, null)).asSequence().toList()
        assertEquals(
            listOf(
                Position.First to null,
                Position.Middle to 1,
                Position.Last to null,
            ),
            out,
        )
    }
}
