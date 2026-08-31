// port-lint: tests itertools/tests/test_std.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.CoalesceResult
import io.github.kotlinmania.itertools.adaptors.coalesce
import io.github.kotlinmania.itertools.adaptors.dedup
import io.github.kotlinmania.itertools.adaptors.dedupBy
import io.github.kotlinmania.itertools.adaptors.dedupByWithCount
import io.github.kotlinmania.itertools.adaptors.dedupWithCount
import io.github.kotlinmania.itertools.adaptors.multiCartesianProduct
import io.github.kotlinmania.itertools.adaptors.whileSome
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestStd {
    @Test
    fun product3() {
        val prod = iproduct(0 until 3, 0 until 2, 0 until 2).asSequence().toList()
        assertEquals(12, prod.size)
        for (i in 0 until 3) {
            for (j in 0 until 2) {
                for (k in 0 until 2) {
                    assertEquals(Triple(i, j, k), prod[i * 2 * 2 + j * 2 + k])
                }
            }
        }
        var count = 0
        for (item in iproduct(0 until 3, 0 until 2, 0 until 2, 0 until 3).asSequence()) {
            count++
            assertEquals(4, item.size)
        }
        assertEquals(36, count)
    }

    @Test
    fun interleaveShortest() {
        val v0 = listOf(0, 2, 4)
        val v1 = listOf(1, 3, 5, 7)
        val it1 = interleaveShortest(v0, v1).asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4, 5), it1)

        val v0Longer = listOf(0, 2, 4, 6, 8)
        val v1Shorter = listOf(1, 3, 5)
        val it2 = interleaveShortest(v0Longer, v1Shorter).asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6), it2)
    }

    @Test
    fun duplicatesBy() {
        val xs = listOf("aaa", "bbbbb", "aa", "ccc", "bbbb", "aaaaa", "cccc")
        val ys = listOf("aa", "bbbb", "cccc")
        assertEquals(ys, duplicatesBy(xs) { it.take(2) }.asSequence().toList())
    }

    @Test
    fun duplicates() {
        val xs = listOf(0, 1, 2, 3, 2, 1, 3)
        val ys = listOf(2, 1, 3)
        assertEquals(ys, duplicates(xs).asSequence().toList())

        val xs2 = listOf(0, 1, 0, 1)
        val ys2 = listOf(0, 1)
        assertEquals(ys2, duplicates(xs2).asSequence().toList())

        val xs3 = listOf(0, 1, 2, 1, 2)
        val ys3 = listOf(1, 2)
        assertEquals(ys3, duplicates(xs3).asSequence().toList())
    }

    @Test
    fun uniqueBy() {
        val xs = listOf("aaa", "bbbbb", "aa", "ccc", "bbbb", "aaaaa", "cccc")
        val ys = listOf("aaa", "bbbbb", "ccc")
        assertEquals(ys, uniqueBy(xs) { it.take(2) }.asSequence().toList())
    }

    @Test
    fun unique() {
        val xs = listOf(0, 1, 2, 3, 2, 1, 3)
        val ys = listOf(0, 1, 2, 3)
        assertEquals(ys, unique(xs).asSequence().toList())

        val xs2 = listOf(0, 1)
        val ys2 = listOf(0, 1)
        assertEquals(ys2, unique(xs2).asSequence().toList())
    }

    @Test
    fun intersperse() {
        val xs = listOf("a", "", "b", "c")
        val v = intersperse(xs, ", ").asSequence().toList()
        val text = v.joinToString("")
        assertEquals("a, , b, c", text)

        val ys = listOf(0, 1, 2, 3)
        val it = intersperse(ys.take(0), 1)
        assertFalse(it.hasNext())
    }

    @Test
    fun dedup() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 3)
        val ys = listOf(0, 1, 2, 1, 3)
        assertEquals(ys, dedup(xs).asSequence().toList())

        val xs2 = listOf(0, 0, 0, 0, 0)
        val ys2 = listOf(0)
        assertEquals(ys2, dedup(xs2).asSequence().toList())
    }

    @Test
    fun coalesce() {
        val data = listOf(-1.0, -2.0, -3.0, 3.0, 1.0, 0.0, -1.0)
        val it =
            coalesce(data) { x: Double, y: Double ->
                if ((x >= 0.0) == (y >= 0.0)) {
                    CoalesceResult.Merged(x + y)
                } else {
                    CoalesceResult.Separate(x, y)
                }
            }
        assertEquals(listOf(-6.0, 4.0, -1.0), it.asSequence().toList())
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
        assertEquals(
            ys,
            io.github.kotlinmania.itertools.adaptors
                .dedupBy(xs) { x, y -> x.second == y.second }
                .asSequence()
                .toList(),
        )

        val xs2 = listOf(Pair(0, 1), Pair(0, 2), Pair(0, 3), Pair(0, 4), Pair(0, 5))
        val ys2 = listOf(Pair(0, 1))
        assertEquals(
            ys2,
            io.github.kotlinmania.itertools.adaptors
                .dedupBy(xs2) { x, y -> x.first == y.first }
                .asSequence()
                .toList(),
        )
    }

    @Test
    fun dedupWithCount() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 3)
        val ys = listOf(Pair(1, 0), Pair(3, 1), Pair(1, 2), Pair(1, 1), Pair(2, 3))
        assertEquals(
            ys,
            io.github.kotlinmania.itertools.adaptors
                .dedupWithCount(xs)
                .asSequence()
                .toList(),
        )

        val xs2 = listOf(0, 0, 0, 0, 0)
        val ys2 = listOf(Pair(5, 0))
        assertEquals(
            ys2,
            io.github.kotlinmania.itertools.adaptors
                .dedupWithCount(xs2)
                .asSequence()
                .toList(),
        )
    }

    @Test
    fun dedupByWithCount() {
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
                Pair(1, Pair(0, 0)),
                Pair(3, Pair(0, 1)),
                Pair(1, Pair(0, 2)),
                Pair(1, Pair(3, 1)),
                Pair(2, Pair(0, 3)),
            )
        assertEquals(
            ys,
            io.github.kotlinmania.itertools.adaptors
                .dedupByWithCount(xs) { x, y -> x.second == y.second }
                .asSequence()
                .toList(),
        )
    }

    @Test
    fun allEqual() {
        assertTrue("".toList().allEqual())
        assertTrue("A".toList().allEqual())
        assertFalse("AABBCCC".toList().allEqual())
        assertTrue("AAAAAAA".toList().allEqual())
    }

    @Test
    fun allEqualValue() {
        assertEquals<AllEqualValueResult<Char>>(AllEqualValueResult.Empty, "".toList().allEqualValue())
        assertEquals(AllEqualValueResult.AllEqual('A'), "A".toList().allEqualValue())
        assertEquals(AllEqualValueResult.NotEqual('A', 'B'), "AABBCCC".toList().allEqualValue())
        assertEquals(AllEqualValueResult.AllEqual('A'), "AAAAAAA".toList().allEqualValue())
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
        val pb = putBackN(xs)
        pb.next()
        pb.next()
        pb.putBack(1)
        pb.putBack(0)
        assertEquals(xs, pb.asSequence().toList())
    }

    @Test
    fun tee() {
        val xs = listOf(0, 1, 2, 3)
        val (t1, t2) = tee(xs)
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
        val r1 = rciter(xs)
        val r2 = r1.clone()
        assertEquals(0, r1.next())
        assertEquals(1, r2.next())
        val z = r1.zip(r2)
        assertEquals(Pair(1, 1), z.next())
        assertEquals(Pair(2, 1), z.next())
        assertEquals(Pair(3, 5), z.next())
        assertFalse(z.hasNext())
    }

    @Test
    fun traitPointers() {
        val it: Iterator<Int> = (0 until 10).iterator()
        assertEquals(0, it.next())
        assertEquals(1, it.next())
        assertEquals(2, it.next())
        assertEquals(Pair(1, 4), it.findPosition { it == 4 })
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
        val results = mergeBy(odd, even) { a, b -> a.first <= b.first }.asSequence().toList()
        assertEquals(expected, results)
    }

    @Test
    fun mergeByBtree() {
        val bt1 = mapOf("hello" to 1, "world" to 3).toList().sortedBy { it.first }
        val bt2 = mapOf("foo" to 2, "bar" to 4).toList().sortedBy { it.first }
        val results =
            mergeBy(bt1, bt2) { a, b ->
                a.first <= b.first
            }.asSequence().toList()
        val expected = listOf(Pair("bar", 4), Pair("foo", 2), Pair("hello", 1), Pair("world", 3))
        assertEquals(expected, results)
    }

    @Test
    fun kmerge() {
        val its = (0 until 4).map { s -> (s until 10 step 4).toList() }
        assertEquals((0 until 10).toList(), kmerge(its).asSequence().toList())
    }

    @Test
    fun kmerge2() {
        val its = listOf(3, 2, 1, 0).map { s -> (s until 10 step 4).toList() }
        assertEquals((0 until 10).toList(), kmerge(its).asSequence().toList())
    }

    @Test
    fun kmergeEmpty() {
        val its = (0 until 4).map { emptyList<Int>() }
        assertFalse(kmerge(its).hasNext())
    }

    @Test
    fun kmergeSizeHint() {
        val its = (0 until 5).map { (0 until 10).toList() }
        assertEquals(SizeHint(50, 50), kmerge(its).sizeHint())
    }

    @Test
    fun kmergeEmptySizeHint() {
        val its = (0 until 5).map { emptyList<Int>() }
        assertEquals(SizeHint(0, 0), kmerge(its).sizeHint())
    }

    @Test
    fun join() {
        val many = listOf(1, 2, 3)
        val one = listOf(1)
        val none = emptyList<Int>()

        assertEquals("1, 2, 3", many.joinToString(", "))
        assertEquals("1", one.joinToString(", "))
        assertEquals("", none.joinToString(", "))
    }

    @Test
    fun sortedBy() {
        val sc = listOf(3, 4, 1, 2).sortedWith(compareBy { it })
        assertEquals(listOf(1, 2, 3, 4), sc)

        val v = (0 until 5).sortedWith(compareByDescending { it })
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
        val nums = listOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte())
        val mp = multipeek(nums)
        assertEquals(1.toByte(), mp.peek())
        assertEquals(1.toByte(), mp.next())
        assertEquals(2.toByte(), mp.peek())
        assertEquals(3.toByte(), mp.peek())
        assertEquals(2.toByte(), mp.next())
        assertEquals(3.toByte(), mp.peek())
        assertEquals(4.toByte(), mp.peek())
        assertEquals(5.toByte(), mp.peek())
        assertNull(mp.peek())
        assertEquals(3.toByte(), mp.next())
        assertEquals(4.toByte(), mp.next())
        assertEquals(5.toByte(), mp.peek())
        assertNull(mp.peek())
        assertEquals(5.toByte(), mp.next())
        assertFalse(mp.hasNext())
        assertNull(mp.peek())
    }

    @Test
    fun testMultipeekReset() {
        val data = listOf(1, 2, 3, 4)
        val mp = multipeek(data)
        assertEquals(1, mp.peek())
        assertEquals(1, mp.next())
        assertEquals(2, mp.peek())
        assertEquals(3, mp.peek())
        mp.resetPeek()
        assertEquals(2, mp.peek())
        assertEquals(2, mp.next())
    }

    @Test
    fun testMultipeekPeekingNext() {
        val nums = listOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte())
        val mp = multipeek(nums)
        assertEquals(1.toByte(), mp.peekingNext { it != 0.toByte() })
        assertEquals(2.toByte(), mp.next())
        assertEquals(3.toByte(), mp.peek())
        assertEquals(4.toByte(), mp.peek())
        assertEquals(3.toByte(), mp.peekingNext { it == 3.toByte() })
        assertEquals(4.toByte(), mp.peek())
        assertNull(mp.peekingNext { it != 4.toByte() })
        assertEquals(4.toByte(), mp.peekingNext { it == 4.toByte() })
        assertEquals(5.toByte(), mp.peek())
        assertEquals(6.toByte(), mp.peek())
        assertNull(mp.peekingNext { it != 5.toByte() })
        assertEquals(7.toByte(), mp.peek())
        assertEquals(5.toByte(), mp.peekingNext { it == 5.toByte() })
        assertEquals(6.toByte(), mp.peekingNext { it == 6.toByte() })
        assertEquals(7.toByte(), mp.peek())
        assertNull(mp.peek())
        assertEquals(7.toByte(), mp.next())
        assertNull(mp.peek())
    }

    @Test
    fun testRepeatNPeekingNext() {
        val rn = repeatN(0, 5)
        assertNull(rn.peekingNext { it != 0 })
        assertEquals(0, rn.peekingNext { it <= 0 })
        assertEquals(0, rn.next())
        assertEquals(0, rn.peekingNext { it <= 0 })
        assertNull(rn.peekingNext { it != 0 })
        assertEquals(0, rn.peekingNext { it >= 0 })
        assertEquals(0, rn.next())
        assertNull(rn.peekingNext { it <= 0 })
        assertFalse(rn.hasNext())
    }

    @Test
    fun testPeekNth() {
        val nums = listOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte())
        val iter = peekNth(nums)

        assertEquals(1.toByte(), iter.peekNth(0))
        assertEquals(1.toByte(), iter.peekNth(0))
        assertEquals(1.toByte(), iter.next())

        assertEquals(2.toByte(), iter.peekNth(0))
        assertEquals(3.toByte(), iter.peekNth(1))
        assertEquals(2.toByte(), iter.next())

        assertEquals(3.toByte(), iter.peekNth(0))
        assertEquals(4.toByte(), iter.peekNth(1))
        assertEquals(5.toByte(), iter.peekNth(2))
        assertNull(iter.peekNth(3))

        assertEquals(3.toByte(), iter.next())
        assertEquals(4.toByte(), iter.next())

        assertEquals(5.toByte(), iter.peekNth(0))
        assertNull(iter.peekNth(1))
        assertEquals(5.toByte(), iter.next())
        assertFalse(iter.hasNext())

        assertNull(iter.peekNth(0))
        assertNull(iter.peekNth(1))
    }

    @Test
    fun testPeekNthPeekingNext() {
        val nums = listOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte())
        val iter = peekNth(nums)

        assertEquals(1.toByte(), iter.peekingNext { it != 0.toByte() })
        assertEquals(2.toByte(), iter.next())

        assertEquals(3.toByte(), iter.peekNth(0))
        assertEquals(4.toByte(), iter.peekNth(1))
        assertEquals(3.toByte(), iter.peekingNext { it == 3.toByte() })
        assertEquals(4.toByte(), iter.peek())

        assertNull(iter.peekingNext { it != 4.toByte() })
        assertEquals(4.toByte(), iter.peekingNext { it == 4.toByte() })
        assertEquals(5.toByte(), iter.peekNth(0))
        assertEquals(6.toByte(), iter.peekNth(1))

        assertNull(iter.peekingNext { it != 5.toByte() })
        assertEquals(5.toByte(), iter.peek())

        assertEquals(5.toByte(), iter.peekingNext { it == 5.toByte() })
        assertEquals(6.toByte(), iter.peekingNext { it == 6.toByte() })
        assertEquals(7.toByte(), iter.peekNth(0))
        assertNull(iter.peekNth(1))
        assertEquals(7.toByte(), iter.next())
        assertNull(iter.peek())
    }

    @Test
    fun testPeekNthNextIf() {
        val nums = listOf(1.toByte(), 2.toByte(), 3.toByte(), 4.toByte(), 5.toByte(), 6.toByte(), 7.toByte())
        val iter = peekNth(nums)

        assertEquals(1.toByte(), iter.nextIf { it != 0.toByte() })
        assertEquals(2.toByte(), iter.next())

        assertEquals(3.toByte(), iter.peekNth(0))
        assertEquals(4.toByte(), iter.peekNth(1))
        assertEquals(3.toByte(), iter.nextIfEq(3.toByte()))
        assertEquals(4.toByte(), iter.peek())

        assertNull(iter.nextIf { it != 4.toByte() })
        assertEquals(4.toByte(), iter.nextIfEq(4.toByte()))
        assertEquals(5.toByte(), iter.peekNth(0))
        assertEquals(6.toByte(), iter.peekNth(1))

        assertNull(iter.nextIf { it != 5.toByte() })
        assertEquals(5.toByte(), iter.peek())

        assertEquals(5.toByte(), iter.nextIf { it % 2 != 0 })
        assertEquals(6.toByte(), iter.nextIfEq(6.toByte()))
        assertEquals(7.toByte(), iter.peekNth(0))
        assertNull(iter.peekNth(1))
        assertEquals(7.toByte(), iter.next())
        assertNull(iter.peek())
    }

    @Test
    fun padUsing() {
        val v = listOf(0, 1, 2)
        val r = padUsing(v, 5) { it }.asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4), r)

        val r2 = padUsing(v, 1) { error("Unreachable") }.asSequence().toList()
        assertEquals(listOf(0, 1, 2), r2)
    }

    @Test
    fun chunkByLazy2() {
        val data = listOf(0, 1)
        val chunks = data.chunkBy { it }
        val gs = chunks.asSequence().toList()
        assertEquals(data, gs.flatMap { it.second })

        val data2 = listOf(0, 1, 1, 0, 0)
        val chunks2 = data2.chunkBy { it }
        val gs2 = chunks2.asSequence().toList().toMutableList()
        val tail = gs2.subList(1, gs2.size).reversed()
        val reordered: List<Pair<Int, List<Int>>> = listOf(gs2[0]) + tail
        assertEquals(listOf(0, 0, 0, 1, 1), reordered.flatMap { it.second })
    }

    @Test
    fun chunkByLazy3() {
        val data = listOf(0, 0, 0, 1, 1, 0, 0, 1, 1, 2, 2)
        val grouper = data.chunkBy { it }.asSequence().toList()
        var last: Pair<Int, List<Int>>? = null
        for (chunk in grouper) {
            val prev = last
            if (prev != null) {
                for (elt in prev.second) {
                    assertTrue(elt != chunk.first && kotlin.math.abs(elt - chunk.first) == 1)
                }
            }
            last = chunk
        }
    }

    @Test
    fun concatEmpty() {
        val data: List<List<Int>> = emptyList()
        assertEquals(emptyList<Int>(), data.concat().asSequence().toList())
    }

    @Test
    fun concatNonEmpty() {
        val data = listOf(listOf(1, 2, 3), listOf(4, 5, 6), listOf(7, 8, 9))
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 7, 8, 9), data.concat().asSequence().toList())
    }

    @Test
    fun combinations() {
        assertFalse(combinations(1 until 3, 5).hasNext())

        val it1 = combinations(1 until 3, 2).asSequence().toList()
        assertEquals(listOf(listOf(1, 2)), it1)

        val it2 = combinations(1 until 5, 2).asSequence().toList()
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

        assertEquals(emptyList<List<Int>>(), combinations(emptyList<Int>(), 2).asSequence().toList())
        assertEquals(listOf(listOf(0)), combinations(0 until 1, 1).asSequence().toList())
        assertEquals(listOf(listOf(0), listOf(1)), combinations(0 until 2, 1).asSequence().toList())
        assertEquals(listOf(listOf(0, 1)), combinations(0 until 2, 2).asSequence().toList())
    }

    @Test
    fun combinationsOfTooShort() {
        for (i in 1 until 10) {
            assertFalse(combinations(0 until 0, i).hasNext())
            assertFalse(combinations(0 until (i - 1), i).hasNext())
        }
    }

    @Test
    fun combinationsZero() {
        assertEquals(listOf(emptyList()), combinations(1 until 3, 0).asSequence().toList())
        assertEquals(listOf(emptyList()), combinations(emptyList<Int>(), 0).asSequence().toList())
    }

    private fun binomial(n: Int, k: Int): Int {
        if (k > n || k < 0 || n < 0) return 0
        var res = 1
        val kMin = minOf(k, n - k)
        for (i in 1..kMin) {
            res = res * (n - i + 1) / i
        }
        return res
    }

    @Test
    fun combinationsRangeCount() {
        for (n in 0..7) {
            for (k in 0..7) {
                val len = binomial(n, k)
                val it = combinations(0 until n, k)
                assertEquals(len, it.count())
                assertEquals(len, it.sizeHint().lower)
                assertEquals(len, it.sizeHint().upper)
                val it2 = combinations(0 until n, k)
                for (count in (len - 1) downTo 0) {
                    assertTrue(it2.hasNext())
                    it2.next()
                    assertEquals(count, it2.count())
                }
                assertFalse(it2.hasNext())
            }
        }
    }

    @Test
    fun combinationsInexactSizeHints() {
        for (k in 0..7) {
            val numbers = (0 until 18).filter { it % 2 == 0 } // 9 elements
            val it = combinations(numbers, k)
            val realN = numbers.size
            val len = binomial(realN, k)
            assertEquals(len, it.count())
        }
    }

    @Test
    fun permutationsZero() {
        assertEquals(listOf(emptyList()), permutations(1 until 3, 0).asSequence().toList())
        assertEquals(listOf(emptyList()), permutations(emptyList<Int>(), 0).asSequence().toList())
    }

    @Test
    fun permutationsRangeCount() {
        for (n in 0..4) {
            for (k in 0..4) {
                val len =
                    if (k <= n) {
                        var prod = 1
                        for (x in (n - k + 1)..n) prod *= x
                        prod
                    } else {
                        0
                    }
                val it = permutations(0 until n, k)
                assertEquals(len, it.count())
            }
        }
    }

    @Test
    fun permutationsOverflowedSizeHints() {
        val it = permutations(generateSequence { 1 }.iterator(), 2, hint = SizeHint(Int.MAX_VALUE, null))
        assertEquals(Int.MAX_VALUE, it.sizeHint().lower)
        assertNull(it.sizeHint().upper)
        for (nbGenerated in 1..1000) {
            it.next()
            assertTrue(it.sizeHint().lower >= Int.MAX_VALUE - nbGenerated)
            assertNull(it.sizeHint().upper)
        }
    }

    @Test
    fun combinationsWithReplacement() {
        assertEquals(listOf(listOf(0, 0)), combinationsWithReplacement(0 until 1, 2).asSequence().toList())
        assertEquals(
            listOf(
                listOf(0, 0),
                listOf(0, 1),
                listOf(0, 2),
                listOf(1, 1),
                listOf(1, 2),
                listOf(2, 2),
            ),
            combinationsWithReplacement(0 until 3, 2).asSequence().toList(),
        )
        assertEquals(listOf(emptyList()), combinationsWithReplacement(0 until 3, 0).asSequence().toList())
        assertEquals(listOf(emptyList()), combinationsWithReplacement(emptyList<Int>(), 0).asSequence().toList())
        assertEquals(emptyList<List<Int>>(), combinationsWithReplacement(emptyList<Int>(), 2).asSequence().toList())
    }

    @Test
    fun combinationsWithReplacementRangeCount() {
        for (n in 0..4) {
            for (k in 0..4) {
                val len = binomial((n + k - 1).coerceAtLeast(0), k)
                val it = combinationsWithReplacement(0 until n, k)
                assertEquals(len, it.count())
            }
        }
    }

    @Test
    fun powerset() {
        assertEquals(listOf(emptyList()), powerset(emptyList<Int>()).asSequence().toList())
        assertEquals(listOf(emptyList(), listOf(0)), powerset(0 until 1).asSequence().toList())
        assertEquals(
            listOf(emptyList(), listOf(0), listOf(1), listOf(0, 1)),
            powerset(0 until 2).asSequence().toList(),
        )
        assertEquals(
            listOf(
                emptyList(),
                listOf(0),
                listOf(1),
                listOf(2),
                listOf(0, 1),
                listOf(0, 2),
                listOf(1, 2),
                listOf(0, 1, 2),
            ),
            powerset(0 until 3).asSequence().toList(),
        )

        assertEquals(1 shl 4, powerset(0 until 4).asSequence().count())
        assertEquals(1 shl 8, powerset(0 until 8).asSequence().count())
    }

    @Test
    fun diffMismatch() {
        val a = listOf(1, 2, 3, 4)
        val b = listOf(1, 5, 3, 4)
        val diff = diffWith(a, b) { x, y -> x == y }
        assertNotNull(diff)
        assertTrue(diff is Diff.FirstMismatch)
        assertEquals(1, diff.index)
        assertEquals(listOf(5, 3, 4), diff.secondRemaining.asSequence().toList())
    }

    @Test
    fun diffLonger() {
        val a = listOf(1, 2, 3, 4)
        val bLonger = listOf(1, 2, 3, 4, 5, 6)
        val diffLonger = diffWith(a, bLonger) { x, y -> x == y }
        assertNotNull(diffLonger)
        assertTrue(diffLonger is Diff.Longer)
        assertEquals(listOf(5, 6), diffLonger.remaining.asSequence().toList())
    }

    @Test
    fun diffShorter() {
        val a = listOf(1, 2, 3, 4)
        val bShorter = listOf(1, 2)
        val diffShorter = diffWith(a, bShorter) { x, y -> x == y }
        assertNotNull(diffShorter)
        assertTrue(diffShorter is Diff.Shorter)
        assertEquals(2, diffShorter.length)
    }

    private data class Val(
        val a: Int,
        val b: Int,
    ) : Comparable<Val> {
        override fun compareTo(other: Val): Int = a.compareTo(other.a)
    }

    @Test
    fun extremaSet() {
        val data = listOf(Val(0, 1), Val(2, 0), Val(0, 2), Val(1, 0), Val(2, 1))

        val minSet = data.minSet()
        assertEquals(listOf(Val(0, 1), Val(0, 2)), minSet)

        val minSetByKey = data.minSetByKey { it.b }
        assertEquals(listOf(Val(2, 0), Val(1, 0)), minSetByKey)

        val minSetBy = data.minSetBy { x, y -> x.b.compareTo(y.b) }
        assertEquals(listOf(Val(2, 0), Val(1, 0)), minSetBy)

        val maxSet = data.maxSet()
        assertEquals(listOf(Val(2, 0), Val(2, 1)), maxSet)

        val maxSetByKey = data.maxSetByKey { it.b }
        assertEquals(listOf(Val(0, 2)), maxSetByKey)

        val maxSetBy = data.maxSetBy { x, y -> x.b.compareTo(y.b) }
        assertEquals(listOf(Val(0, 2)), maxSetBy)
    }

    @Test
    fun minmax() {
        assertEquals<MinMaxResult<Int>>(MinMaxResult.NoElements, emptyList<Int>().minmax())
        assertEquals<MinMaxResult<Int>>(MinMaxResult.OneElement(1), listOf(1).minmax())

        val data = listOf(Val(0, 1), Val(2, 0), Val(0, 2), Val(1, 0), Val(2, 1))
        val mm = data.minmax()
        assertEquals(MinMaxResult.MinMax(Val(0, 1), Val(2, 1)), mm)

        val mmByKey = data.minmaxByKey { it.b }
        assertEquals(MinMaxResult.MinMax(Val(2, 0), Val(0, 2)), mmByKey)
    }

    @Test
    fun format() {
        val data = listOf(0, 1, 2, 3)
        assertEquals("0, 1, 2, 3", newFormatDefault(data.iterator(), ", ").toString())
        assertEquals("0--1--2--3", newFormatDefault(data.iterator(), "--").toString())
    }

    @Test
    fun whileSome() {
        val ns =
            whileSome((1 until 10).map { x -> if (x % 5 != 0) x else null })
                .asSequence()
                .toList()
        assertEquals(listOf(1, 2, 3, 4), ns)
    }

    @Test
    fun foldWhile() {
        var iterations = 0
        val vec = listOf(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        val sum =
            vec
                .foldWhile(0) { acc, item ->
                    iterations++
                    val newSum = acc + item
                    if (newSum <= 20) {
                        FoldWhile.Continue(newSum)
                    } else {
                        FoldWhile.Done(acc)
                    }
                }.intoInner()
        assertEquals(6, iterations)
        assertEquals(15, sum)
    }

    @Test
    fun treeReduce() {
        val x =
            listOf(
                "",
                "0",
                "0 1 x",
                "0 1 x 2 x",
                "0 1 x 2 3 x x",
                "0 1 x 2 3 x x 4 x",
                "0 1 x 2 3 x x 4 5 x x",
                "0 1 x 2 3 x x 4 5 x 6 x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x 10 x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x 10 11 x x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x 10 11 x x 12 x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x 10 11 x x 12 13 x x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x 10 11 x x 12 13 x 14 x x x",
                "0 1 x 2 3 x x 4 5 x 6 7 x x x 8 9 x 10 11 x x 12 13 x 14 15 x x x x",
            )
        for (i in x.indices) {
            val s = x[i]
            val expected = if (s.isEmpty()) null else s
            val numStrings = (0 until i).map { it.toString() }
            val actual = treeReduce(numStrings) { a, b -> "$a $b x" }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun exactlyOneQuestionMarkSyntaxWorks() {
        val res = emptyList<Int>().exactlyOne()
        assertTrue(res is ItemResult.Err)
    }

    @Test
    fun multiunzip() {
        val (a, b, c) =
            multiUnzip(
                listOf(
                    Triple(0, 1, 2),
                    Triple(3, 4, 5),
                    Triple(6, 7, 8),
                ),
            )
        assertEquals(listOf(0, 3, 6), a)
        assertEquals(listOf(1, 4, 7), b)
        assertEquals(listOf(2, 5, 8), c)
    }

    @Test
    fun multiCartesianProduct() {
        val a = listOf(1, 2)
        val b = listOf(3, 4)
        val c = listOf(5, 6)
        val res = multiCartesianProduct(listOf(a, b, c)).asSequence().toList()
        assertEquals(8, res.size)
        assertEquals(listOf(1, 3, 5), res[0])
        assertEquals(listOf(2, 4, 6), res[7])
    }
}
