// port-lint: tests tests/merge_join.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class MergeJoinTest {
    @Test
    fun empty() {
        val left: List<UInt> = emptyList()
        val right: List<UInt> = emptyList()
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = emptyList()
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun leftOnly() {
        val left: List<UInt> = listOf(1u, 2u, 3u)
        val right: List<UInt> = emptyList()
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = listOf(
            EitherOrBoth.Left(1u),
            EitherOrBoth.Left(2u),
            EitherOrBoth.Left(3u),
        )
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun rightOnly() {
        val left: List<UInt> = emptyList()
        val right: List<UInt> = listOf(1u, 2u, 3u)
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = listOf(
            EitherOrBoth.Right(1u),
            EitherOrBoth.Right(2u),
            EitherOrBoth.Right(3u),
        )
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun firstLeftThenRight() {
        val left: List<UInt> = listOf(1u, 2u, 3u)
        val right: List<UInt> = listOf(4u, 5u, 6u)
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = listOf(
            EitherOrBoth.Left(1u),
            EitherOrBoth.Left(2u),
            EitherOrBoth.Left(3u),
            EitherOrBoth.Right(4u),
            EitherOrBoth.Right(5u),
            EitherOrBoth.Right(6u),
        )
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun firstRightThenLeft() {
        val left: List<UInt> = listOf(4u, 5u, 6u)
        val right: List<UInt> = listOf(1u, 2u, 3u)
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = listOf(
            EitherOrBoth.Right(1u),
            EitherOrBoth.Right(2u),
            EitherOrBoth.Right(3u),
            EitherOrBoth.Left(4u),
            EitherOrBoth.Left(5u),
            EitherOrBoth.Left(6u),
        )
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun interspersedLeftAndRight() {
        val left: List<UInt> = listOf(1u, 3u, 5u)
        val right: List<UInt> = listOf(2u, 4u, 6u)
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = listOf(
            EitherOrBoth.Left(1u),
            EitherOrBoth.Right(2u),
            EitherOrBoth.Left(3u),
            EitherOrBoth.Right(4u),
            EitherOrBoth.Left(5u),
            EitherOrBoth.Right(6u),
        )
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }

    @Test
    fun overlappingLeftAndRight() {
        val left: List<UInt> = listOf(1u, 3u, 4u, 6u)
        val right: List<UInt> = listOf(2u, 3u, 4u, 5u)
        val expectedResult: List<EitherOrBoth<UInt, UInt>> = listOf(
            EitherOrBoth.Left(1u),
            EitherOrBoth.Right(2u),
            EitherOrBoth.Both(3u, 3u),
            EitherOrBoth.Both(4u, 4u),
            EitherOrBoth.Right(5u),
            EitherOrBoth.Left(6u),
        )
        val actualResult = mergeJoinBy(left, right) { l, r -> l.compareTo(r) }.asSequence().toList()
        assertEquals(expectedResult, actualResult)
    }
}
