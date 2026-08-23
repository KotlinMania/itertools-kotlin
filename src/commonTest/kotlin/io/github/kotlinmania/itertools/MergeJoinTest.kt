// port-lint: source src/merge_join.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class MergeJoinTest {
    @Test
    fun testMerge() {
        val a = listOf(1, 3, 5)
        val b = listOf(2, 4, 6)
        val merged = merge(a, b).asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5, 6), merged)
    }

    @Test
    fun testMergeWithDuplicates() {
        val a = listOf(1, 2, 3)
        val b = listOf(2, 3, 4)
        val merged = merge(a, b).asSequence().toList()
        assertEquals(listOf(1, 2, 2, 3, 3, 4), merged)
    }

    @Test
    fun testMergeBy() {
        val a = listOf(5, 3, 1)
        val b = listOf(6, 4, 2)
        val merged = mergeBy(a, b) { x, y -> x >= y }.asSequence().toList()
        assertEquals(listOf(6, 5, 4, 3, 2, 1), merged)
    }

    @Test
    fun testMergeJoinBy() {
        val a = listOf(1, 2, 4, 5)
        val b = listOf(2, 3, 5, 6)
        val res = mergeJoinBy(a, b) { x, y -> x.compareTo(y) }.asSequence().toList()

        assertEquals(
            listOf(
                EitherOrBoth.Left(1),
                EitherOrBoth.Both(2, 2),
                EitherOrBoth.Right(3),
                EitherOrBoth.Left(4),
                EitherOrBoth.Both(5, 5),
                EitherOrBoth.Right(6),
            ),
            res,
        )
    }
}
