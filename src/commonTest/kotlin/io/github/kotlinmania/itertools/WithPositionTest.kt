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
        assertEquals(listOf(Positioned(Position.Only, 42)), out)
    }

    @Test
    fun twoElementsFirstThenLast() {
        val out = withPosition(listOf("a", "b")).asSequence().toList()
        assertEquals(listOf(Positioned(Position.First, "a"), Positioned(Position.Last, "b")), out)
    }

    @Test
    fun threeOrMoreElementsTagFirstMiddleLast() {
        val out = withPosition(listOf(1, 2, 3, 4)).asSequence().toList()
        assertEquals(
            listOf(
                Positioned(Position.First, 1),
                Positioned(Position.Middle, 2),
                Positioned(Position.Middle, 3),
                Positioned(Position.Last, 4),
            ),
            out,
        )
    }

    // sizeHint / fold live on the internal WithPosition class; construct directly.

    @Test
    fun sizeHintMirrorsRemaining() {
        val src = listOf(1, 2, 3)
        val it = WithPosition(src.iterator(), SizeHint(src.size, src.size))
        assertEquals(SizeHint(3, 3), it.sizeHint())
        it.next()
        assertEquals(SizeHint(2, 2), it.sizeHint())
        it.next()
        assertEquals(SizeHint(1, 1), it.sizeHint())
        it.next()
        assertEquals(SizeHint(0, 0), it.sizeHint())
        assertFalse(it.hasNext())
    }

    @Test
    fun foldVisitsEveryPositionOnce() {
        val src = listOf("x", "y", "z")
        val it = WithPosition(src.iterator(), SizeHint(src.size, src.size))
        val collected = it.fold(mutableListOf<Positioned<String>>()) { acc, p ->
            acc.add(p); acc
        }
        assertEquals(
            listOf(
                Positioned(Position.First, "x"),
                Positioned(Position.Middle, "y"),
                Positioned(Position.Last, "z"),
            ),
            collected,
        )
    }

    @Test
    fun nullableElementsRoundTrip() {
        val out = withPosition(listOf<Int?>(null, 1, null)).asSequence().toList()
        assertEquals(
            listOf(
                Positioned(Position.First, null),
                Positioned(Position.Middle, 1),
                Positioned(Position.Last, null),
            ),
            out,
        )
    }
}
