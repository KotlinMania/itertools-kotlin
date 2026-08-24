// port-lint: tests zip_longest.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ZipLongestTest {
    @Test
    fun testZipLongestEqualLength() {
        val a = listOf(1, 2)
        val b = listOf("a", "b")
        val out = zipLongest(a, b).asSequence().toList()
        assertEquals(
            listOf(
                EitherOrBoth.Both(1, "a"),
                EitherOrBoth.Both(2, "b"),
            ),
            out,
        )
    }

    @Test
    fun testZipLongestLeftLonger() {
        val a = listOf(1, 2, 3)
        val b = listOf("a")
        val out = zipLongest(a, b).asSequence().toList()
        assertEquals(
            listOf(
                EitherOrBoth.Both(1, "a"),
                EitherOrBoth.Left(2),
                EitherOrBoth.Left(3),
            ),
            out,
        )
    }

    @Test
    fun testZipLongestRightLonger() {
        val a = listOf(1)
        val b = listOf("a", "b", "c")
        val out = zipLongest(a, b).asSequence().toList()
        assertEquals(
            listOf(
                EitherOrBoth.Both(1, "a"),
                EitherOrBoth.Right("b"),
                EitherOrBoth.Right("c"),
            ),
            out,
        )
    }

    @Test
    fun testZipLongestEmpty() {
        val a = emptyList<Int>()
        val b = emptyList<String>()
        val it = zipLongest(a, b)
        assertFalse(it.hasNext())
        assertNull(it.nextBack())
    }

    @Test
    fun testZipLongestSizeHint() {
        val a = listOf(1, 2, 3, 4, 5)
        val b = listOf("a", "b")
        val it = zipLongest(a, b)
        assertEquals(SizeHint(5, 5), it.sizeHint())
        it.next()
        assertEquals(SizeHint(4, 4), it.sizeHint())
    }

    @Test
    fun testZipLongestNextBack() {
        val a = listOf(1, 2, 3)
        val b = listOf("a")
        val it = zipLongest(a, b)
        assertEquals(EitherOrBoth.Left(3), it.nextBack())
        assertEquals(EitherOrBoth.Left(2), it.nextBack())
        assertEquals(EitherOrBoth.Both(1, "a"), it.nextBack())
        assertNull(it.nextBack())
    }

    @Test
    fun testZipLongestRfold() {
        val a = listOf(1, 2)
        val b = listOf("x", "y", "z")
        val it = zipLongest(a, b)
        val list = it.rfold(mutableListOf<EitherOrBoth<Int, String>>()) { acc, item ->
            acc.add(item)
            acc
        }
        assertEquals(
            listOf(
                EitherOrBoth.Right("z"),
                EitherOrBoth.Both(2, "y"),
                EitherOrBoth.Both(1, "x"),
            ),
            list,
        )
    }
}
