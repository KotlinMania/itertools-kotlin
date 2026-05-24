// port-lint: source src/minmax.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MinMaxTest {
    @Test
    fun toOptionNoElements() {
        val r: MinMaxResult<Int> = MinMaxResult.NoElements
        assertNull(r.toOption())
    }

    @Test
    fun toOptionOneElement() {
        val r: MinMaxResult<Int> = MinMaxResult.OneElement(1)
        assertEquals(1 to 1, r.toOption())
    }

    @Test
    fun toOptionMinMax() {
        val r: MinMaxResult<Int> = MinMaxResult.MinMax(1, 2)
        assertEquals(1 to 2, r.toOption())
    }

    private fun minmaxInts(values: List<Int>): MinMaxResult<Int> =
        minmaxImpl(
            values.iterator(),
            keyFor = { it },
            lt = { a, b, _, _ -> a < b },
        )

    @Test
    fun emptyIteratorYieldsNoElements() {
        assertEquals(MinMaxResult.NoElements, minmaxInts(emptyList()))
    }

    @Test
    fun singleElementIteratorYieldsOneElement() {
        assertEquals(MinMaxResult.OneElement(7), minmaxInts(listOf(7)))
    }

    @Test
    fun twoElementsAscending() {
        assertEquals(MinMaxResult.MinMax(1, 2), minmaxInts(listOf(1, 2)))
    }

    @Test
    fun twoElementsDescending() {
        assertEquals(MinMaxResult.MinMax(1, 2), minmaxInts(listOf(2, 1)))
    }

    @Test
    fun longerSequence() {
        assertEquals(MinMaxResult.MinMax(-3, 9), minmaxInts(listOf(4, -3, 7, 0, 9, 2)))
    }

    @Test
    fun oddLengthSequence() {
        assertEquals(MinMaxResult.MinMax(0, 9), minmaxInts(listOf(5, 9, 0, 3, 6)))
    }

    @Test
    fun stablyKeepsFirstMaxOnTies() {
        // Strict less-than: ties keep the first occurrence as the maximum.
        // 5 appears twice; min remains 1, max remains the first 5.
        val r = minmaxInts(listOf(5, 1, 5))
        assertEquals(MinMaxResult.MinMax(1, 5), r)
    }
}
