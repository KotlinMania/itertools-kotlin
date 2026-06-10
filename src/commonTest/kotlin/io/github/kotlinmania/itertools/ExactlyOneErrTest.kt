// port-lint: source src/exactly_one_err.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class ExactlyOneErrTest {
    @Test
    fun zeroPrefixDrainsInner() {
        val it = ExactlyOneError(null, listOf(1, 2, 3).iterator(), SizeHint(3, 3))
        assertEquals(listOf(1, 2, 3), it.asSequence().toList())
    }

    @Test
    fun bothPrefixYieldsFirstThenSecondThenInner() {
        val it =
            ExactlyOneError(
                ExactlyOneError.FirstTwo.Both(10, 20),
                listOf(30, 40).iterator(),
                SizeHint(2, 2),
            )
        assertEquals(listOf(10, 20, 30, 40), it.asSequence().toList())
    }

    @Test
    fun justSecondPrefixYieldsSecondThenInner() {
        val it =
            ExactlyOneError(
                ExactlyOneError.FirstTwo.JustSecond("only-second"),
                listOf("a", "b").iterator(),
                SizeHint(2, 2),
            )
        assertEquals(listOf("only-second", "a", "b"), it.asSequence().toList())
    }

    @Test
    fun emptyAndNoPrefixSignalsZeroExpected() {
        val it = ExactlyOneError(null, emptyList<Int>().iterator(), SizeHint(0, 0))
        assertFalse(it.hasNext())
        assertEquals("got zero elements when exactly one was expected", it.toString())
    }

    @Test
    fun bothPrefixSignalsAtLeastTwoExpected() {
        val it =
            ExactlyOneError(
                ExactlyOneError.FirstTwo.Both(1, 2),
                emptyList<Int>().iterator(),
                SizeHint(0, 0),
            )
        assertEquals("got at least 2 elements when exactly one was expected", it.toString())
    }

    @Test
    fun sizeHintReflectsPrefixAndInnerRemaining() {
        val it =
            ExactlyOneError(
                ExactlyOneError.FirstTwo.Both(1, 2),
                listOf(3, 4, 5).iterator(),
                SizeHint(3, 3),
            )
        assertEquals(SizeHint(5, 5), it.sizeHint())
        it.next()
        assertEquals(SizeHint(4, 4), it.sizeHint())
        it.next()
        assertEquals(SizeHint(3, 3), it.sizeHint())
        it.next()
        assertEquals(SizeHint(2, 2), it.sizeHint())
    }

    @Test
    fun foldVisitsPrefixThenInner() {
        val it =
            ExactlyOneError(
                ExactlyOneError.FirstTwo.Both(1, 2),
                listOf(3, 4).iterator(),
                SizeHint(2, 2),
            )
        val total = it.fold(0) { acc, x -> acc + x }
        assertEquals(10, total)
    }
}
