// port-lint: tests tests/tuples.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.Tuple1Combination
import io.github.kotlinmania.itertools.adaptors.Tuple2Combination
import io.github.kotlinmania.itertools.adaptors.Tuple3Combination
import io.github.kotlinmania.itertools.adaptors.tupleCombinations2
import io.github.kotlinmania.itertools.adaptors.tupleCombinations3
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TupleCombinationsTest {
    @Test
    fun testTuple1Combination() {
        val iter = Tuple1Combination(listOf(1, 2, 3).iterator())
        assertEquals(1, iter.next())
        assertEquals(2, iter.next())
        assertEquals(3, iter.next())
        assertFalse(iter.hasNext())
    }

    @Test
    fun testTuple2Combinations() {
        val v = listOf(1, 2, 3, 4)
        val comb = tupleCombinations2(v)
        assertEquals(6, comb.count())
        val list = comb.asSequence().toList()
        assertEquals(
            listOf(
                Pair(1, 2),
                Pair(1, 3),
                Pair(1, 4),
                Pair(2, 3),
                Pair(2, 4),
                Pair(3, 4),
            ),
            list,
        )
    }

    @Test
    fun testTuple3Combinations() {
        val v = listOf(1, 2, 3, 4)
        val comb = tupleCombinations3(v)
        assertEquals(4, comb.count())
        val list = comb.asSequence().toList()
        assertEquals(
            listOf(
                Triple(1, 2, 3),
                Triple(1, 2, 4),
                Triple(1, 3, 4),
                Triple(2, 3, 4),
            ),
            list,
        )
    }

    @Test
    fun testTupleCombinationsEmpty() {
        val empty = emptyList<Int>()
        val comb2 = tupleCombinations2(empty)
        assertFalse(comb2.hasNext())
        assertEquals(0, comb2.count())

        val comb3 = tupleCombinations3(empty)
        assertFalse(comb3.hasNext())
        assertEquals(0, comb3.count())
    }
}
