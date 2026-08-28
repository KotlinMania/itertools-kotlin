// port-lint: tests zip.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.batching
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class ZipTest {
    @Test
    fun zipLongestFused() {
        val a: List<Int?> = listOf(1, null, 3, 4)
        val b = listOf(1, 2, 3)

        val batch =
            batching(a.iterator()) { iter ->
                if (iter.hasNext()) iter.next() else null
            }
        val unfused = zipLongest(batch, b.iterator()).asSequence().toList()
        assertEquals(
            listOf(
                EitherOrBoth.Both(1, 1),
                EitherOrBoth.Right(2),
                EitherOrBoth.Right(3),
            ),
            unfused,
        )
    }

    @Test
    fun testZipLongestSizeHint() {
        val v = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)
        val v2 = listOf(10, 11, 12)

        val it1 = zipLongest(generateSequence(1) { it % 9 + 1 }.iterator(), v.iterator())
        assertEquals(SizeHint(0, null), it1.sizeHint())

        val it2 = zipLongest(v, v2)
        assertEquals(SizeHint(10, 10), it2.sizeHint())
    }

    @Test
    fun testDoubleEndedZipLongest() {
        val xs = listOf(1, 2, 3, 4, 5, 6)
        val ys = listOf(1, 2, 3, 7)
        val it = xs.zipLongest(ys)
        assertEquals(EitherOrBoth.Both(1, 1), it.next())
        assertEquals(EitherOrBoth.Both(2, 2), it.next())
        assertEquals(EitherOrBoth.Left(6), it.nextBack())
        assertEquals(EitherOrBoth.Left(5), it.nextBack())
        assertEquals(EitherOrBoth.Both(4, 7), it.nextBack())
        assertEquals(EitherOrBoth.Both(3, 3), it.next())
        assertFalse(it.hasNext())
    }

    @Test
    fun testDoubleEndedZip() {
        val xs = listOf(1, 2, 3, 4, 5, 6)
        val ys = listOf(1, 2, 3, 7)
        val it = multizip(Pair(xs, ys))
        assertEquals(Pair(4, 7), it.nextBack())
        assertEquals(Pair(3, 3), it.nextBack())
        assertEquals(Pair(2, 2), it.nextBack())
        assertEquals(Pair(1, 1), it.nextBack())
        assertNull(it.nextBack())
    }
}
