// port-lint: tests tests/quick.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.interleave
import io.github.kotlinmania.itertools.adaptors.interleaveShortest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class QuickTest {
    @Test
    fun testInterleaveProperties() {
        val cases =
            listOf(
                emptyList<Int>() to emptyList(),
                listOf(1, 2, 3) to emptyList(),
                emptyList<Int>() to listOf(4, 5),
                listOf(1, 3, 5) to listOf(2, 4, 6),
                listOf(1, 2) to listOf(3, 4, 5, 6),
                listOf(1, 2, 3, 4) to listOf(5, 6),
            )
        for ((a, b) in cases) {
            val res = interleave(a.iterator(), b.iterator()).asSequence().toList()
            assertEquals(a.size + b.size, res.size)
            val expected = mutableListOf<Int>()
            val maxLen = maxOf(a.size, b.size)
            for (i in 0 until maxLen) {
                if (i < a.size) expected.add(a[i])
                if (i < b.size) expected.add(b[i])
            }
            assertEquals(expected, res)
        }
    }

    @Test
    fun testInterleaveShortestProperties() {
        val cases =
            listOf(
                emptyList<Int>() to emptyList(),
                listOf(1, 2, 3) to emptyList(),
                emptyList<Int>() to listOf(4, 5),
                listOf(1, 3, 5) to listOf(2, 4, 6),
                listOf(1, 2) to listOf(3, 4, 5, 6),
                listOf(1, 2, 3, 4) to listOf(5, 6),
            )
        for ((a, b) in cases) {
            val res = interleaveShortest(a.iterator(), b.iterator()).asSequence().toList()
            val expected = mutableListOf<Int>()
            val ai = a.iterator()
            val bi = b.iterator()
            var fromB = false
            while (true) {
                if (fromB) {
                    if (bi.hasNext()) {
                        expected.add(bi.next())
                        fromB = false
                    } else {
                        break
                    }
                } else {
                    if (ai.hasNext()) {
                        expected.add(ai.next())
                        fromB = true
                    } else {
                        break
                    }
                }
            }
            assertEquals(expected, res)
        }
    }

    @Test
    fun testIntersperseProperties() {
        val empty =
            emptyList<Int>()
                .iterator()
                .intersperse(0)
                .asSequence()
                .toList()
        assertEquals(emptyList(), empty)

        val single =
            listOf(42)
                .iterator()
                .intersperse(0)
                .asSequence()
                .toList()
        assertEquals(listOf(42), single)

        val multi =
            listOf(1, 2, 3)
                .iterator()
                .intersperse(0)
                .asSequence()
                .toList()
        assertEquals(listOf(1, 0, 2, 0, 3), multi)
    }

    @Test
    fun testZipLongestProperties() {
        val cases =
            listOf(
                emptyList<Int>() to emptyList<String>(),
                listOf(1, 2) to emptyList(),
                emptyList<Int>() to listOf("a", "b"),
                listOf(1, 2) to listOf("a", "b", "c"),
                listOf(1, 2, 3) to listOf("a"),
            )
        for ((a, b) in cases) {
            val res =
                a
                    .iterator()
                    .zipLongest(b.iterator())
                    .asSequence()
                    .toList()
            val expectedLen = maxOf(a.size, b.size)
            assertEquals(expectedLen, res.size)
            for (i in 0 until expectedLen) {
                val expectedLeft = if (i < a.size) a[i] else null
                val expectedRight = if (i < b.size) b[i] else null
                val pair = res[i]
                when {
                    expectedLeft != null && expectedRight != null -> {
                        assertTrue(pair.isBoth())
                        assertEquals(expectedLeft, pair.left())
                        assertEquals(expectedRight, pair.right())
                    }
                    expectedLeft != null -> {
                        assertTrue(pair.isLeft())
                        assertEquals(expectedLeft, pair.left())
                        assertNull(pair.right())
                    }
                    else -> {
                        assertTrue(pair.isRight())
                        assertNull(pair.left())
                        assertEquals(expectedRight, pair.right())
                    }
                }
            }
        }
    }

    @Test
    fun testPadTailProperties() {
        val list = listOf(1, 2)
        val padded = padUsing(list, 4) { 0 }.asSequence().toList()
        assertEquals(listOf(1, 2, 0, 0), padded)

        val unpadded = padUsing(list, 2) { 0 }.asSequence().toList()
        assertEquals(listOf(1, 2), unpadded)

        val less = padUsing(list, 1) { 0 }.asSequence().toList()
        assertEquals(listOf(1, 2), less)
    }

    @Test
    fun testPeekingTakeWhileProperties() {
        val list = listOf(1, 2, 3, 4, 5, 2, 1)
        val iter = list.iterator().peekable()
        val taken = iter.peekingTakeWhile { it < 4 }.asSequence().toList()
        assertEquals(listOf(1, 2, 3), taken)
        val remainder = iter.asSequence().toList()
        assertEquals(listOf(4, 5, 2, 1), remainder)
    }

    @Test
    fun testTakeWhileInclusiveProperties() {
        val list = listOf(1, 2, 3, 4, 5)
        val res =
            list
                .iterator()
                .takeWhileInclusive { it < 3 }
                .asSequence()
                .toList()
        assertEquals(listOf(1, 2, 3), res)

        val empty =
            emptyList<Int>()
                .iterator()
                .takeWhileInclusive { it < 3 }
                .asSequence()
                .toList()
        assertEquals(emptyList(), empty)
    }

    @Test
    fun testPutBackProperties() {
        val pb = putBack(listOf(1, 2).iterator())
        assertEquals(1, pb.next())
        pb.putBack(10)
        assertEquals(10, pb.next())
        assertEquals(2, pb.next())
        assertFalse(pb.hasNext())
    }

    @Test
    fun testPutBackNProperties() {
        val pbn = PutBackN(listOf(1, 2).iterator())
        assertEquals(1, pbn.next())
        pbn.putBack(10)
        pbn.putBack(20)
        assertEquals(20, pbn.next())
        assertEquals(10, pbn.next())
        assertEquals(2, pbn.next())
        assertFalse(pbn.hasNext())
    }

    @Test
    fun testMultiPeekProperties() {
        val mp = multipeek(listOf(1, 2, 3, 4))
        assertEquals(1, mp.peek())
        assertEquals(2, mp.peek())
        assertEquals(3, mp.peek())
        mp.resetPeek()
        assertEquals(1, mp.peek())
        assertEquals(1, mp.next())
        assertEquals(2, mp.peek())
        assertEquals(2, mp.next())
        assertEquals(3, mp.next())
        assertEquals(4, mp.next())
        assertFalse(mp.hasNext())
    }

    @Test
    fun testCombinationsProperties() {
        val list = listOf(1, 2, 3, 4)
        val combs2 =
            list
                .iterator()
                .combinations(2)
                .asSequence()
                .toList()
        assertEquals(6, combs2.size)
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(1, 3),
                listOf(1, 4),
                listOf(2, 3),
                listOf(2, 4),
                listOf(3, 4),
            ),
            combs2,
        )

        val combs0 =
            list
                .iterator()
                .combinations(0)
                .asSequence()
                .toList()
        assertEquals(listOf(emptyList()), combs0)

        val combsTooLarge =
            list
                .iterator()
                .combinations(5)
                .asSequence()
                .toList()
        assertEquals(emptyList(), combsTooLarge)
    }

    @Test
    fun testCombinationsWithReplacementProperties() {
        val list = listOf(1, 2)
        val combs =
            list
                .iterator()
                .combinationsWithReplacement(2)
                .asSequence()
                .toList()
        assertEquals(
            listOf(
                listOf(1, 1),
                listOf(1, 2),
                listOf(2, 2),
            ),
            combs,
        )
    }

    @Test
    fun testPermutationsProperties() {
        val list = listOf(1, 2, 3)
        val perms =
            list
                .iterator()
                .permutations(2)
                .asSequence()
                .toList()
        assertEquals(6, perms.size)
        val allPerms =
            list
                .iterator()
                .permutations(3)
                .asSequence()
                .toList()
        assertEquals(6, allPerms.size)
    }

    @Test
    fun testPowersetProperties() {
        val list = listOf(1, 2, 3)
        val ps =
            list
                .iterator()
                .powerset()
                .asSequence()
                .toList()
        assertEquals(8, ps.size)
        assertEquals(emptyList(), ps[0])
    }

    @Test
    fun testUniqueProperties() {
        val list = listOf(1, 2, 2, 3, 1, 4, 3, 5)
        val u =
            list
                .iterator()
                .unique()
                .asSequence()
                .toList()
        assertEquals(listOf(1, 2, 3, 4, 5), u)
    }

    @Test
    fun testUniqueByProperties() {
        val list = listOf("a", "bb", "ccc", "d", "ee")
        val u =
            list
                .iterator()
                .uniqueBy { it.length }
                .asSequence()
                .toList()
        assertEquals(listOf("a", "bb", "ccc"), u)
    }

    @Test
    fun testCoalesceProperties() {
        val list = listOf(1, 2, 2, 3, 3, 3)
        val c =
            list
                .iterator()
                .dedup()
                .asSequence()
                .toList()
        assertEquals(listOf(1, 2, 3), c)
    }

    @Test
    fun testKMergeProperties() {
        val lists =
            listOf(
                listOf(1, 4, 7),
                listOf(2, 5, 8),
                listOf(3, 6, 9),
            )
        val merged = kmerge(lists).asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9), merged)
    }

    @Test
    fun testTeeProperties() {
        val list = listOf(1, 2, 3)
        val (i1, i2) = list.iterator().tee()
        assertEquals(listOf(1, 2, 3), i1.asSequence().toList())
        assertEquals(listOf(1, 2, 3), i2.asSequence().toList())
    }
}
