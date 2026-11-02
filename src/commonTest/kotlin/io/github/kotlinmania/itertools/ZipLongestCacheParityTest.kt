package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ZipLongestCacheParityTest {
    @Test
    fun prefetchedSingletonIsStillAvailableFromTheBack() {
        val iterator = ZipLongest(listOf<Int?>(null), listOf<String?>(null))
        assertTrue(iterator.hasNext())
        assertEquals(EitherOrBoth.Both<Int?, String?>(null, null), iterator.nextBack())
        assertFalse(iterator.hasNext())
        assertNull(iterator.nextBack())
    }

    @Test
    fun exactListHintsIncludeThePendingFrontItem() {
        val iterator = ZipLongest(listOf(1, 2, 3), listOf("x"))
        assertEquals(SizeHint(3, 3), iterator.sizeHint())
        assertTrue(iterator.hasNext())
        assertTrue(iterator.hasNext())
        assertEquals(SizeHint(3, 3), iterator.sizeHint())
        assertEquals(EitherOrBoth.Left(3), iterator.nextBack())
        assertEquals(SizeHint(2, 2), iterator.sizeHint())
        assertEquals(EitherOrBoth.Both(1, "x"), iterator.next())
        assertEquals(SizeHint(1, 1), iterator.sizeHint())
        assertTrue(iterator.hasNext())
        assertEquals(SizeHint(1, 1), iterator.sizeHint())
        assertEquals(EitherOrBoth.Left(2), iterator.nextBack())
        assertEquals(SizeHint(0, 0), iterator.sizeHint())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun reverseFoldIncludesThePrefetchedFrontItem() {
        val iterator = ZipLongest(listOf(1, 2), listOf("x", "y", "z"))
        assertTrue(iterator.hasNext())
        val reversed =
            iterator.rfold(mutableListOf<EitherOrBoth<Int, String>>()) { acc, item ->
                acc.apply { add(item) }
            }
        assertEquals(
            listOf(EitherOrBoth.Right("z"), EitherOrBoth.Both(2, "y"), EitherOrBoth.Both(1, "x")),
            reversed,
        )
        assertFalse(iterator.hasNext())
    }

    @Test
    fun mixedEndsKeepNullableTagsUntilTheLastItem() {
        val iterator = ZipLongest(listOf<Int?>(null, 2, null), listOf<String?>(null, "x"))
        assertTrue(iterator.hasNext())
        assertEquals(EitherOrBoth.Left<Int?>(null), iterator.nextBack())
        assertEquals(EitherOrBoth.Both<Int?, String?>(null, null), iterator.next())
        assertTrue(iterator.hasNext())
        assertEquals(EitherOrBoth.Both(2, "x"), iterator.nextBack())
        assertFalse(iterator.hasNext())
        assertNull(iterator.nextBack())
    }

    @Test
    fun unprefetchedNullableTailAndEmptyInputKeepTheirContracts() {
        val iterator = ZipLongest(listOf<Int?>(null), emptyList<String?>())
        assertEquals(EitherOrBoth.Left<Int?>(null), iterator.nextBack())
        assertNull(iterator.nextBack())
        val empty = ZipLongest(emptyList<Int?>(), emptyList<String?>())
        assertFalse(empty.hasNext())
        assertEquals(SizeHint(0, 0), empty.sizeHint())
        assertNull(empty.nextBack())
    }
}
