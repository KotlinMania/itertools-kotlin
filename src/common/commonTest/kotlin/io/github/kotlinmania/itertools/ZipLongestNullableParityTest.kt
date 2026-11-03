package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ZipLongestNullableParityTest {
    private val left: List<Int?> = listOf(null, 7, null)
    private val right: List<String?> = listOf(null, "x")
    private val expected: List<EitherOrBoth<Int?, String?>> =
        listOf(EitherOrBoth.Both(null, null), EitherOrBoth.Both(7, "x"), EitherOrBoth.Left(null))

    @Test
    fun listPathPreservesNullPayloads() {
        assertEquals(expected, ZipLongest(left, right).asSequence().toList())
        assertEquals(listOf(EitherOrBoth.Right<String?>(null)), ZipLongest(emptyList<Int?>(), listOf<String?>(null)).asSequence().toList())
    }

    @Test
    fun iteratorPathPreservesNullPayloadsAndSuffixes() {
        assertEquals(expected, ZipLongest(left.iterator(), right.iterator()).asSequence().toList())
    }

    @Test
    fun repeatedAvailabilityChecksKeepThePendingNullItem() {
        val iterator = ZipLongest(listOf<Int?>(null, 7).iterator(), listOf<String?>(null).iterator())
        assertTrue(iterator.hasNext())
        assertTrue(iterator.hasNext())
        assertEquals(EitherOrBoth.Both(null, null), iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(EitherOrBoth.Left(7), iterator.next())
        assertFalse(iterator.hasNext())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun exhaustedSourcesStayFusedWhileTheOtherSourceContainsNull() {
        var probes = 0
        val reviving =
            object : Iterator<Int?> {
                override fun hasNext(): Boolean = ++probes > 1

                override fun next(): Int? {
                    if (probes <= 1) throw NoSuchElementException()
                    return 7
                }
            }
        val iterator = ZipLongest(reviving, listOf<String?>(null, "x").iterator())
        assertEquals(listOf(EitherOrBoth.Right(null), EitherOrBoth.Right("x")), iterator.asSequence().toList())
        assertEquals(1, probes)
    }

    @Test
    fun presenceQueriesInspectTheVariant() {
        assertTrue(EitherOrBoth.Left<Int?>(null).hasLeft())
        assertFalse(EitherOrBoth.Left<Int?>(null).hasRight())
        assertTrue(EitherOrBoth.Right<String?>(null).hasRight())
        assertFalse(EitherOrBoth.Right<String?>(null).hasLeft())
        assertTrue(EitherOrBoth.Both<Int?, String?>(null, null).hasLeft())
        assertTrue(EitherOrBoth.Both<Int?, String?>(null, null).hasRight())
    }

    @Test
    fun foldsKeepTheNullableTagsInBothDirections() {
        val folded =
            ZipLongest(left.iterator(), right.iterator()).fold(mutableListOf<EitherOrBoth<Int?, String?>>()) { acc, item ->
                acc.apply { add(item) }
            }
        assertEquals(expected, folded)
        val reversed =
            ZipLongest(left, right).rfold(mutableListOf<EitherOrBoth<Int?, String?>>()) { acc, item ->
                acc.apply { add(item) }
            }
        assertEquals(expected.reversed(), reversed)
    }
}
