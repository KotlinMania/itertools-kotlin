// port-lint: source src/exactly_one_err.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ExactlyOneErrTest {
    @Test
    fun zeroPrefixDrainsInner() {
        val it = ExactlyOneError(null, listOf(1, 2, 3).iterator(), 3 to 3)
        assertEquals(listOf(1, 2, 3), it.asSequence().toList())
    }

    @Test
    fun bothPrefixYieldsFirstThenSecondThenInner() {
        val it = ExactlyOneError(
            ExactlyOneError.FirstTwo.Both(10, 20),
            listOf(30, 40).iterator(),
            2 to 2,
        )
        assertEquals(listOf(10, 20, 30, 40), it.asSequence().toList())
    }

    @Test
    fun justSecondPrefixYieldsSecondThenInner() {
        val it = ExactlyOneError(
            ExactlyOneError.FirstTwo.JustSecond("only-second"),
            listOf("a", "b").iterator(),
            2 to 2,
        )
        assertEquals(listOf("only-second", "a", "b"), it.asSequence().toList())
    }

    @Test
    fun emptyAndNoPrefixSignalsZeroExpected() {
        val it = ExactlyOneError(null, emptyList<Int>().iterator(), 0 to 0)
        assertFalse(it.hasNext())
        assertEquals("got zero elements when exactly one was expected", it.toString())
    }

    @Test
    fun bothPrefixSignalsAtLeastTwoExpected() {
        val it = ExactlyOneError(
            ExactlyOneError.FirstTwo.Both(1, 2),
            emptyList<Int>().iterator(),
            0 to 0,
        )
        assertEquals("got at least 2 elements when exactly one was expected", it.toString())
    }

    @Test
    fun sizeHintReflectsPrefixAndInnerRemaining() {
        val it = ExactlyOneError(
            ExactlyOneError.FirstTwo.Both(1, 2),
            listOf(3, 4, 5).iterator(),
            3 to 3,
        )
        assertEquals(5 to 5, it.sizeHint())
        it.next()
        assertEquals(4 to 4, it.sizeHint())
        it.next()
        assertEquals(3 to 3, it.sizeHint())
        it.next()
        assertEquals(2 to 2, it.sizeHint())
    }

    @Test
    fun foldVisitsPrefixThenInner() {
        val it = ExactlyOneError(
            ExactlyOneError.FirstTwo.Both(1, 2),
            listOf(3, 4).iterator(),
            2 to 2,
        )
        val total = it.fold(0) { acc, x -> acc + x }
        assertEquals(10, total)
    }
}
