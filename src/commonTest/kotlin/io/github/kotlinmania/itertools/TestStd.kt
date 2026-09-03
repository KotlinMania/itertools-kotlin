// port-lint: tests tests/test_std.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestStd {
    @Test
    fun product3() {
        val prod =
            (0 until 3)
                .toList()
                .cartesianProduct((0 until 2).toList())
                .asSequence()
                .toList()
                .cartesianProduct((0 until 2).toList())
        var count = 0
        while (prod.hasNext()) {
            prod.next()
            count++
        }
        assertEquals(12, count)
    }

    @Test
    fun interleaveShortest() {
        val v0 = listOf(0, 2, 4)
        val v1 = listOf(1, 3, 5, 7)
        val it1 = interleaveShortest(v0, v1).asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4, 5), it1)

        val v2 = listOf(0, 2, 4, 6, 8)
        val v3 = listOf(1, 3, 5)
        val it2 = interleaveShortest(v2, v3).asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6), it2)
    }

    @Test
    fun duplicatesBy() {
        val xs = listOf("aaa", "bbbbb", "aa", "ccc", "bbbb", "aaaaa", "cccc")
        val ys = listOf("aa", "bbbb", "cccc")
        assertEqual(ys, xs.duplicatesBy { it.substring(0, 2) })
    }

    @Test
    fun duplicates() {
        val xs = listOf(0, 1, 2, 3, 2, 1, 3)
        val ys = listOf(2, 1, 3)
        assertEqual(ys, xs.duplicates())

        val xs2 = listOf(0, 1, 0, 1)
        val ys2 = listOf(0, 1)
        assertEqual(ys2, xs2.duplicates())
    }

    @Test
    fun uniqueBy() {
        val xs = listOf("aaa", "bbbbb", "aa", "ccc", "bbbb", "aaaaa", "cccc")
        val ys = listOf("aaa", "bbbbb", "ccc")
        assertEqual(ys, xs.uniqueBy { it.substring(0, 2) })
    }

    @Test
    fun unique() {
        val xs = listOf(0, 1, 2, 3, 2, 1, 3)
        val ys = listOf(0, 1, 2, 3)
        assertEqual(ys, xs.unique())

        val xs2 = listOf(0, 1)
        val ys2 = listOf(0, 1)
        assertEqual(ys2, xs2.unique())
    }

    @Test
    fun intersperse() {
        val xs = listOf("a", "", "b", "c")
        val v = xs.intersperse(", ").asSequence().toList()
        val text = v.joinToString("")
        assertEquals("a, , b, c", text)
    }

    @Test
    fun dedup() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 3)
        val ys = listOf(0, 1, 2, 1, 3)
        assertEqual(ys, xs.dedup())

        val xs2 = listOf(0, 0, 0, 0, 0)
        val ys2 = listOf(0)
        assertEqual(ys2, xs2.dedup())
    }

    @Test
    fun coalesce() {
        val data = listOf(-1.0, -2.0, -3.0, 3.0, 1.0, 0.0, -1.0)
        val it =
            data.coalesce { x, y ->
                if ((x >= 0.0) == (y >= 0.0)) {
                    io.github.kotlinmania.itertools.adaptors.CoalesceResult
                        .Merged(x + y)
                } else {
                    io.github.kotlinmania.itertools.adaptors.CoalesceResult
                        .Separate(x, y)
                }
            }
        assertEqual(listOf(-6.0, 4.0, -1.0), it)
    }

    @Test
    fun dedupBy() {
        val xs =
            listOf(
                Pair(0, 0),
                Pair(0, 1),
                Pair(1, 1),
                Pair(2, 1),
                Pair(0, 2),
                Pair(3, 1),
                Pair(0, 3),
                Pair(1, 3),
            )
        val ys =
            listOf(
                Pair(0, 0),
                Pair(0, 1),
                Pair(0, 2),
                Pair(3, 1),
                Pair(0, 3),
            )
        assertEqual(ys, xs.dedupBy { x, y -> x.second == y.second })
    }

    @Test
    fun dedupWithCount() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 3)
        val ys =
            listOf(
                Pair(1, 0),
                Pair(3, 1),
                Pair(1, 2),
                Pair(1, 1),
                Pair(2, 3),
            )
        assertEqual(ys, xs.dedupWithCount())
    }

    @Test
    fun allEqual() {
        assertTrue("".toList().allEqual())
        assertTrue("A".toList().allEqual())
        assertFalse("AABBCCC".toList().allEqual())
        assertTrue("AAAAAAA".toList().allEqual())
    }

    @Test
    fun allUnique() {
        assertTrue("ABCDEFGH".toList().allUnique())
        assertFalse("ABCDEFGA".toList().allUnique())
        assertTrue(emptyList<Int>().allUnique())
    }

    @Test
    fun testPutBackN() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 3)
        val pb = putBackN(xs.iterator())
        pb.next()
        pb.next()
        pb.putBack(1)
        pb.putBack(0)
        assertEqual(pb, xs)
    }

    @Test
    fun tee() {
        val xs = listOf(0, 1, 2, 3)
        val (t1, t2) = xs.tee()
        assertEquals(0, t1.next())
        assertEquals(0, t2.next())
        assertEquals(1, t1.next())
        assertEquals(2, t1.next())
        assertEquals(3, t1.next())
        assertFalse(t1.hasNext())
        assertEquals(1, t2.next())
        assertEquals(2, t2.next())
        assertEquals(3, t2.next())
        assertFalse(t2.hasNext())
    }

    @Test
    fun testRciter() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 5, 6)
        val r1 = rciter(xs.iterator())
        val r2 = r1.clone()
        assertEquals(0, r1.next())
        assertEquals(1, r2.next())
        val z = multizip(r1.iterator(), r2.iterator())
        assertEquals(Pair(1, 1), z.next())
        assertEquals(Pair(2, 1), z.next())
        assertEquals(Pair(3, 5), z.next())
        assertFalse(z.hasNext())
    }

    @Test
    fun mergeBy() {
        val odd = listOf(Pair(1, "hello"), Pair(3, "world"), Pair(5, "!"))
        val even = listOf(Pair(2, "foo"), Pair(4, "bar"), Pair(6, "baz"))
        val expected =
            listOf(
                Pair(1, "hello"),
                Pair(2, "foo"),
                Pair(3, "world"),
                Pair(4, "bar"),
                Pair(5, "!"),
                Pair(6, "baz"),
            )
        val results = odd.mergeBy(even) { a, b -> a.first <= b.first }
        assertEqual(results, expected)
    }

    @Test
    fun kmerge() {
        val it1 = listOf(0, 4, 8)
        val it2 = listOf(1, 5, 9)
        val it3 = listOf(2, 6)
        val it4 = listOf(3, 7)
        val res = listOf(it1, it2, it3, it4).kmerge()
        assertEqual(res, (0 until 10).toList())
    }

    @Test
    fun join() {
        val many = listOf(1, 2, 3)
        val one = listOf(1)
        val none = emptyList<Int>()

        assertEquals("1, 2, 3", join(many, ", "))
        assertEquals("1", join(one, ", "))
        assertEquals("", join(none, ", "))
    }

    @Test
    fun sortedBy() {
        val sc = listOf(3, 4, 1, 2).sortedWith { a: Int, b: Int -> a.compareTo(b) }
        assertEquals(listOf(1, 2, 3, 4), sc)

        val v = (0 until 5).sortedWith { a: Int, b: Int -> b.compareTo(a) }
        assertEquals(listOf(4, 3, 2, 1, 0), v)
    }

    @Test
    fun sortedByKey() {
        val sc = listOf(3, 4, 1, 2).sortedBy { it }
        assertEquals(listOf(1, 2, 3, 4), sc)

        val v = (0 until 5).sortedBy { -it }
        assertEquals(listOf(4, 3, 2, 1, 0), v)
    }

    @Test
    fun testMultipeek() {
        val nums = listOf(1, 2, 3, 4, 5)
        val mp = multipeek(nums.iterator())
        assertEquals(1, mp.peek())
        assertEquals(1, mp.next())
        assertEquals(2, mp.peek())
        assertEquals(3, mp.peek())
        assertEquals(2, mp.next())
        assertEquals(3, mp.peek())
        assertEquals(4, mp.peek())
        assertEquals(5, mp.peek())
        assertNull(mp.peek())
        assertEquals(3, mp.next())
        assertEquals(4, mp.next())
        assertEquals(5, mp.peek())
        assertNull(mp.peek())
        assertEquals(5, mp.next())
        assertFalse(mp.hasNext())
    }

    @Test
    fun testPeekNth() {
        val nums = listOf(1, 2, 3, 4, 5)
        val iter = peekNth(nums.iterator())
        assertEquals(1, iter.peekNth(0))
        assertEquals(1, iter.peekNth(0))
        assertEquals(1, iter.next())

        assertEquals(2, iter.peekNth(0))
        assertEquals(3, iter.peekNth(1))
        assertEquals(2, iter.next())

        assertEquals(3, iter.peekNth(0))
        assertEquals(4, iter.peekNth(1))
        assertEquals(5, iter.peekNth(2))
        assertNull(iter.peekNth(3))

        assertEquals(3, iter.next())
        assertEquals(4, iter.next())

        assertEquals(5, iter.peekNth(0))
        assertNull(iter.peekNth(1))
        assertEquals(5, iter.next())
        assertFalse(iter.hasNext())
    }

    @Test
    fun padUsing() {
        val v = listOf(0, 1, 2)
        val r = v.padUsing(5) { it }.asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4), r)
    }

    @Test
    fun concatNonEmpty() {
        val data = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9), data.concat().asSequence().toList())
    }

    @Test
    fun combinations() {
        val it1 = (1..2).combinations(2)
        assertEquals(listOf(listOf(1, 2)), it1.asSequence().toList())

        val it2 = (1..4).combinations(2).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(1, 3),
                listOf(1, 4),
                listOf(2, 3),
                listOf(2, 4),
                listOf(3, 4),
            ),
            it2,
        )
    }

    @Test
    fun combinationsZero() {
        val it = (1..2).combinations(0).asSequence().toList()
        assertEquals(listOf(emptyList<Int>()), it)
    }

    @Test
    fun permutationsZero() {
        val it = (1..2).permutations(0).asSequence().toList()
        assertEquals(listOf(emptyList<Int>()), it)
    }

    @Test
    fun combinationsWithReplacement() {
        val it = (0..2).combinationsWithReplacement(2).asSequence().toList()
        assertEquals(
            listOf(
                listOf(0, 0),
                listOf(0, 1),
                listOf(0, 2),
                listOf(1, 1),
                listOf(1, 2),
                listOf(2, 2),
            ),
            it,
        )
    }

    @Test
    fun powerset() {
        val it0 = (0 until 0).powerset().asSequence().toList()
        assertEquals(listOf(emptyList<Int>()), it0)

        val it1 = (0 until 1).powerset().asSequence().toList()
        assertEquals(listOf(emptyList<Int>(), listOf(0)), it1)

        val it2 = (0 until 2).powerset().asSequence().toList()
        assertEquals(
            listOf(emptyList<Int>(), listOf(0), listOf(1), listOf(0, 1)),
            it2,
        )
    }

    @Test
    fun whileSome() {
        val ns =
            (1 until 10)
                .map { if (it % 5 != 0) it else null }
                .whileSome()
                .asSequence()
                .toList()
        assertEquals(listOf(1, 2, 3, 4), ns)
    }

    @Test
    fun foldWhile() {
        var iterations = 0
        val vec = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val sum =
            when (
                val res =
                    vec.foldWhile(0) { acc, item ->
                        iterations++
                        val newSum = acc + item
                        if (newSum <= 20) {
                            FoldWhile.Continue(newSum)
                        } else {
                            FoldWhile.Done(acc)
                        }
                    }
            ) {
                is FoldWhile.Continue -> res.value
                is FoldWhile.Done -> res.value
            }
        assertEquals(6, iterations)
        assertEquals(15, sum)
    }
}
