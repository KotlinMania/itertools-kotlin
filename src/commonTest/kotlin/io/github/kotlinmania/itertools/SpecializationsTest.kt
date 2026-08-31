// port-lint: tests tests/specializations.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.CoalesceResult
import io.github.kotlinmania.itertools.adaptors.batching
import io.github.kotlinmania.itertools.adaptors.coalesce
import io.github.kotlinmania.itertools.adaptors.dedup
import io.github.kotlinmania.itertools.adaptors.dedupWithCount
import io.github.kotlinmania.itertools.adaptors.filterMapOk
import io.github.kotlinmania.itertools.adaptors.filterOk
import io.github.kotlinmania.itertools.adaptors.interleave
import io.github.kotlinmania.itertools.adaptors.interleaveShortest
import io.github.kotlinmania.itertools.adaptors.mapOk
import io.github.kotlinmania.itertools.adaptors.multiCartesianProduct
import io.github.kotlinmania.itertools.adaptors.positions
import io.github.kotlinmania.itertools.adaptors.whileSome
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class SpecializationsTest {
    @Test
    fun testInterleaveSpecializations() {
        val a = listOf(1, 3, 5)
        val b = listOf(2, 4, 6, 8)
        val it = interleave(a, b)
        val collected = it.asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5, 6, 8), collected)
    }

    @Test
    fun testInterleaveShortestSpecializations() {
        val a = listOf(1, 3, 5)
        val b = listOf(2, 4, 6, 8)
        val it = interleaveShortest(a, b)
        val collected = it.asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5, 6), collected)
    }

    @Test
    fun testBatchingSpecializations() {
        val a = listOf(1, 2, 3, 4, 5)
        val it =
            batching(a.iterator()) { iter ->
                if (iter.hasNext()) {
                    val first = iter.next()
                    if (iter.hasNext()) Pair(first, iter.next()) else Pair(first, 0)
                } else {
                    null
                }
            }
        val collected = it.asSequence().toList()
        assertEquals(listOf(Pair(1, 2), Pair(3, 4), Pair(5, 0)), collected)
    }

    @Test
    fun testTuplesSpecializations() {
        val a = listOf(1, 2, 3, 4, 5)
        val t2 = tuples2(a).asSequence().toList()
        assertEquals(listOf(Pair(1, 2), Pair(3, 4)), t2)

        val t3 = tuples3(a).asSequence().toList()
        assertEquals(listOf(Triple(1, 2, 3)), t3)
    }

    @Test
    fun testTupleWindowsSpecializations() {
        val a = listOf(1, 2, 3, 4)
        val tw2 = tupleWindows2(a).asSequence().toList()
        assertEquals(listOf(Pair(1, 2), Pair(2, 3), Pair(3, 4)), tw2)

        val tw3 = tupleWindows3(a).asSequence().toList()
        assertEquals(listOf(Triple(1, 2, 3), Triple(2, 3, 4)), tw3)
    }

    @Test
    fun testMultiCartesianProductSpecializations() {
        val a = listOf(1, 2)
        val b = listOf(3, 4)
        val c = listOf(5)
        val mcp = multiCartesianProduct(listOf(a, b, c)).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, 3, 5),
                listOf(1, 4, 5),
                listOf(2, 3, 5),
                listOf(2, 4, 5),
            ),
            mcp,
        )
    }

    @Test
    fun testCoalesceSpecializations() {
        val a = listOf(1, 1, 2, 3, 3, 3)
        val it =
            coalesce(a) { x, y ->
                if (x == y) CoalesceResult.Merged(x + y) else CoalesceResult.Separate(x, y)
            }
        val collected = it.asSequence().toList()
        assertEquals(listOf(4, 6, 3), collected)
    }

    @Test
    fun testDedupSpecializations() {
        val a = listOf(1, 1, 2, 2, 2, 3, 1, 1)
        assertEquals(listOf(1, 2, 3, 1), dedup(a).asSequence().toList())
        assertEquals(
            listOf(Pair(2, 1), Pair(3, 2), Pair(1, 3), Pair(2, 1)),
            dedupWithCount(a).asSequence().toList(),
        )
    }

    @Test
    fun testDuplicatesSpecializations() {
        val a = listOf(1, 2, 3, 1, 2, 1)
        assertEquals(listOf(1, 2), duplicates(a).asSequence().toList())
        assertEquals(listOf(1, 2), duplicatesBy(a) { it }.asSequence().toList())
    }

    @Test
    fun testUniqueSpecializations() {
        val a = listOf(1, 2, 3, 1, 2, 1)
        assertEquals(listOf(1, 2, 3), unique(a).asSequence().toList())
        assertEquals(listOf(1, 2, 3), uniqueBy(a) { it }.asSequence().toList())
    }

    @Test
    fun testTakeWhileInclusiveSpecializations() {
        val a = listOf(1, 2, 3, 4, 5, 6)
        val it = a.iterator().takeWhileInclusive { it < 4 }
        assertEquals(listOf(1, 2, 3, 4), it.asSequence().toList())
    }

    @Test
    fun testWhileSomeSpecializations() {
        val a = listOf<Int?>(1, 2, 3, null, 5)
        val it = whileSome(a)
        assertEquals(listOf(1, 2, 3), it.asSequence().toList())
    }

    @Test
    fun testPadUsingSpecializations() {
        val a = listOf(1, 2)
        val it = padUsing(a, 5) { it * 10 }
        assertEquals(listOf(1, 2, 20, 30, 40), it.asSequence().toList())
    }

    @Test
    fun testWithPositionSpecializations() {
        val a = listOf(1, 2, 3)
        val it = withPosition(a).asSequence().toList()
        assertEquals(Position.First, it[0].position)
        assertEquals(Position.Middle, it[1].position)
        assertEquals(Position.Last, it[2].position)
    }

    @Test
    fun testPositionsSpecializations() {
        val a = listOf(1, 2, 3, 4, 5, 6)
        val it = positions(a) { it % 2 == 0 }
        assertEquals(listOf(1, 3, 5), it.asSequence().toList())
    }

    @Test
    fun testIntersperseSpecializations() {
        val a = listOf(1, 2, 3)
        assertEquals(listOf(1, 0, 2, 0, 3), intersperse(a, 0).asSequence().toList())
        assertEquals(listOf(1, 0, 2, 0, 3), intersperseWith(a) { 0 }.asSequence().toList())
    }

    @Test
    fun testCombinationsAndPermutationsSpecializations() {
        val a = listOf(1, 2, 3)
        assertEquals(
            listOf(listOf(1, 2), listOf(1, 3), listOf(2, 3)),
            combinations(a, 2).asSequence().toList(),
        )
        assertEquals(
            listOf(listOf(1, 1), listOf(1, 2), listOf(1, 3), listOf(2, 2), listOf(2, 3), listOf(3, 3)),
            combinationsWithReplacement(a, 2).asSequence().toList(),
        )
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(1, 3),
                listOf(2, 1),
                listOf(2, 3),
                listOf(3, 1),
                listOf(3, 2),
            ),
            permutations(a, 2).asSequence().toList(),
        )
    }

    @Test
    fun testPowersetSpecializations() {
        val a = listOf(1, 2)
        assertEquals(
            listOf(emptyList(), listOf(1), listOf(2), listOf(1, 2)),
            powerset(a).asSequence().toList(),
        )
    }

    @Test
    fun testZipLongestSpecializations() {
        val a = listOf(1, 2)
        val b = listOf("a", "b", "c")
        val it = zipLongest(a, b).asSequence().toList()
        assertEquals(
            listOf(
                EitherOrBoth.Both(1, "a"),
                EitherOrBoth.Both(2, "b"),
                EitherOrBoth.Right("c"),
            ),
            it,
        )
    }

    @Test
    fun testZipEqSpecializations() {
        val a = listOf(1, 2, 3)
        val b = listOf(4, 5, 6)
        assertEquals(listOf(Zipped(1, 4), Zipped(2, 5), Zipped(3, 6)), zipEq(a, b).asSequence().toList())
    }

    @Test
    fun testRepeatNSpecializations() {
        val rn = repeatN(42, 3)
        assertEquals(SizeHint(3, 3), rn.sizeHint())
        assertEquals(listOf(42, 42, 42), rn.asSequence().toList())
    }

    @Test
    fun testPutBackSpecializations() {
        val pb = putBackN(listOf(1, 2))
        pb.putBack(0)
        assertEquals(listOf(0, 1, 2), pb.asSequence().toList())
    }

    @Test
    fun testMultiPeekSpecializations() {
        val mp = multipeek(listOf(1, 2, 3))
        assertEquals(1, mp.peek())
        assertEquals(2, mp.peek())
        assertEquals(1, mp.next())
        assertEquals(2, mp.next())
        assertEquals(3, mp.next())
        assertFalse(mp.hasNext())
    }

    @Test
    fun testPeekNthSpecializations() {
        val pn = peekNth(listOf(10, 20, 30))
        assertEquals(20, pn.peekNth(1))
        assertEquals(10, pn.peekNth(0))
        assertEquals(30, pn.peekNth(2))
        assertEquals(10, pn.next())
    }

    @Test
    fun testMergeSpecializations() {
        val a = listOf(1, 3, 5)
        val b = listOf(2, 4, 6)
        assertEquals(listOf(1, 2, 3, 4, 5, 6), merge(a, b).asSequence().toList())
        assertEquals(listOf(1, 2, 3, 4, 5, 6), mergeBy(a, b) { x, y -> x <= y }.asSequence().toList())
    }

    @Test
    fun testMergeJoinBySpecializations() {
        val a = listOf(1, 2, 4)
        val b = listOf(2, 3, 4)
        val mj = mergeJoinBy(a, b) { x, y -> x.compareTo(y) }.asSequence().toList()
        assertEquals(
            listOf(
                EitherOrBoth.Left(1),
                EitherOrBoth.Both(2, 2),
                EitherOrBoth.Right(3),
                EitherOrBoth.Both(4, 4),
            ),
            mj,
        )
    }

    @Test
    fun testKmergeSpecializations() {
        val a = listOf(1, 4)
        val b = listOf(2, 5)
        val c = listOf(3, 6)
        assertEquals(listOf(1, 2, 3, 4, 5, 6), kmerge(listOf(a, b, c)).asSequence().toList())
        assertEquals(listOf(1, 2, 3, 4, 5, 6), kmergeBy(listOf(a, b, c)) { x, y -> x < y }.asSequence().toList())
    }

    @Test
    fun testResultAdaptorsSpecializations() {
        val items: List<ItemResult<Int, String>> =
            listOf(
                ItemResult.Ok(1),
                ItemResult.Ok(2),
                ItemResult.Err("fail"),
                ItemResult.Ok(3),
            )
        val mapped =
            mapOk(items) { it * 10 }
                .asSequence()
                .toList()
        assertEquals(ItemResult.Ok(10), mapped[0])
        assertEquals(ItemResult.Ok(20), mapped[1])
        assertEquals(ItemResult.Err("fail"), mapped[2])

        val filtered =
            filterOk(items) { it > 1 }
                .asSequence()
                .toList()
        assertEquals(ItemResult.Ok(2), filtered[0])
        assertEquals(ItemResult.Err("fail"), filtered[1])

        val filterMapped =
            filterMapOk(items) { if (it % 2 == 0) it else null }
                .asSequence()
                .toList()
        assertEquals(ItemResult.Ok(2), filterMapped[0])
        assertEquals(ItemResult.Err("fail"), filterMapped[1])
    }

    @Test
    fun testProcessResultsSpecializations() {
        val okItems: List<ItemResult<Int, String>> =
            listOf(
                ItemResult.Ok(1),
                ItemResult.Ok(2),
                ItemResult.Ok(3),
            )
        val sumOk = processResults(okItems) { it.asSequence().sum() }
        assertEquals(ItemResult.Ok(6), sumOk)

        val errItems: List<ItemResult<Int, String>> =
            listOf(
                ItemResult.Ok(1),
                ItemResult.Err("err"),
                ItemResult.Ok(3),
            )
        val sumErr = processResults(errItems) { it.asSequence().sum() }
        assertEquals(ItemResult.Err("err"), sumErr)
    }
}
