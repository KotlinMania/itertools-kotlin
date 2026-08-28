// port-lint: tests tests/merge_join.rs
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

    @Test
    fun empty() {
        val left = emptyList<Int>()
        val right = emptyList<Int>()
        val expected = emptyList<EitherOrBoth<Int, Int>>()
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun leftOnly() {
        val left = listOf(1, 2, 3)
        val right = emptyList<Int>()
        val expected = listOf(EitherOrBoth.Left(1), EitherOrBoth.Left(2), EitherOrBoth.Left(3))
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun rightOnly() {
        val left = emptyList<Int>()
        val right = listOf(1, 2, 3)
        val expected = listOf(EitherOrBoth.Right(1), EitherOrBoth.Right(2), EitherOrBoth.Right(3))
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun firstLeftThenRight() {
        val left = listOf(1, 2, 3)
        val right = listOf(4, 5, 6)
        val expected =
            listOf(
                EitherOrBoth.Left(1),
                EitherOrBoth.Left(2),
                EitherOrBoth.Left(3),
                EitherOrBoth.Right(4),
                EitherOrBoth.Right(5),
                EitherOrBoth.Right(6),
            )
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun firstRightThenLeft() {
        val left = listOf(4, 5, 6)
        val right = listOf(1, 2, 3)
        val expected =
            listOf(
                EitherOrBoth.Right(1),
                EitherOrBoth.Right(2),
                EitherOrBoth.Right(3),
                EitherOrBoth.Left(4),
                EitherOrBoth.Left(5),
                EitherOrBoth.Left(6),
            )
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun interspersedLeftAndRight() {
        val left = listOf(1, 3, 5)
        val right = listOf(2, 4, 6)
        val expected =
            listOf(
                EitherOrBoth.Left(1),
                EitherOrBoth.Right(2),
                EitherOrBoth.Left(3),
                EitherOrBoth.Right(4),
                EitherOrBoth.Left(5),
                EitherOrBoth.Right(6),
            )
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun overlappingLeftAndRight() {
        val left = listOf(1, 3, 4, 6)
        val right = listOf(2, 3, 4, 5)
        val expected =
            listOf(
                EitherOrBoth.Left(1),
                EitherOrBoth.Right(2),
                EitherOrBoth.Both(3, 3),
                EitherOrBoth.Both(4, 4),
                EitherOrBoth.Right(5),
                EitherOrBoth.Left(6),
            )
        val actual = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expected, actual)
    }
}
