// port-lint: tests itertools/tests/test_std.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ItertoolsExtensionTest {
    @Test
    fun testInterleaveAndShortest() {
        val a = listOf(1, 3, 5)
        val b = listOf(2, 4)
        assertEquals(listOf(1, 2, 3, 4, 5), a.interleave(b).asSequence().toList())
        assertEquals(
            listOf(1, 2, 3, 4, 5),
            a
                .iterator()
                .interleave(b.iterator())
                .asSequence()
                .toList(),
        )
        assertEquals(listOf(1, 2, 3, 4, 5), a.interleaveShortest(b).asSequence().toList())
        assertEquals(
            listOf(1, 2, 3, 4, 5),
            a
                .iterator()
                .interleaveShortest(b.iterator())
                .asSequence()
                .toList(),
        )
    }

    @Test
    fun testIntersperse() {
        val list = listOf("a", "b", "c")
        assertEquals(listOf("a", ",", "b", ",", "c"), list.intersperse(",").asSequence().toList())
        assertEquals(
            listOf("a", ",", "b", ",", "c"),
            list
                .iterator()
                .intersperse(",")
                .asSequence()
                .toList(),
        )
        assertEquals(listOf("a", "-", "b", "-", "c"), list.intersperseWith { "-" }.asSequence().toList())
        assertEquals(
            listOf("a", "-", "b", "-", "c"),
            list
                .iterator()
                .intersperseWith { "-" }
                .asSequence()
                .toList(),
        )
    }

    @Test
    fun testBatching() {
        val list = listOf(1, 2, 3, 4, 5)
        val batches =
            list
                .batching { iter ->
                    if (!iter.hasNext()) {
                        null
                    } else {
                        val sum = iter.next() + (if (iter.hasNext()) iter.next() else 0)
                        sum
                    }
                }.asSequence()
                .toList()
        assertEquals(listOf(3, 7, 5), batches)
    }

    @Test
    fun testChunkByAndGroupBy() {
        val data = listOf(1, 1, 2, 3, 3, 3, 2, 2)
        val chunkKeys =
            data
                .chunkBy { it }
                .asSequence()
                .map { (k, v) -> Pair(k, v.toList()) }
                .toList()
        assertEquals(
            listOf(
                Pair(1, listOf(1, 1)),
                Pair(2, listOf(2)),
                Pair(3, listOf(3, 3, 3)),
                Pair(2, listOf(2, 2)),
            ),
            chunkKeys,
        )
        val groupKeys =
            data
                .groupBy { it }
                .asSequence()
                .map { (k, v) -> Pair(k, v.toList()) }
                .toList()
        assertEquals(chunkKeys, groupKeys)
    }

    @Test
    fun testChunks() {
        val list = listOf(1, 2, 3, 4, 5)
        val chunks =
            list
                .chunks(2)
                .asSequence()
                .map { it.toList() }
                .toList()
        assertEquals(listOf(listOf(1, 2), listOf(3, 4), listOf(5)), chunks)
    }

    @Test
    fun testItemResultAdaptors() {
        val items: List<ItemResult<Int, String>> =
            listOf(
                ItemResult.Ok(1),
                ItemResult.Err("err"),
                ItemResult.Ok(3),
            )
        val mapped = items.mapOk { it * 10 }.asSequence().toList()
        assertEquals(ItemResult.Ok(10), mapped[0])
        assertEquals(ItemResult.Err("err"), mapped[1])
        assertEquals(ItemResult.Ok(30), mapped[2])

        val filtered = items.filterOk { it > 1 }.asSequence().toList()
        assertEquals(listOf(ItemResult.Err("err"), ItemResult.Ok(3)), filtered)

        val filterMapped = items.filterMapOk { if (it > 1) it * 2 else null }.asSequence().toList()
        assertEquals(listOf(ItemResult.Err("err"), ItemResult.Ok(6)), filterMapped)

        val nested: List<ItemResult<List<Int>, String>> =
            listOf(
                ItemResult.Ok(listOf(1, 2)),
                ItemResult.Err("err"),
                ItemResult.Ok(listOf(3)),
            )
        val flattened = nested.flattenOk().asSequence().toList()
        assertEquals(
            listOf(
                ItemResult.Ok(1),
                ItemResult.Ok(2),
                ItemResult.Err("err"),
                ItemResult.Ok(3),
            ),
            flattened,
        )
    }

    @Test
    fun testMergeAndMergeBy() {
        val a = listOf(1, 3, 5)
        val b = listOf(2, 4, 6)
        assertEquals(listOf(1, 2, 3, 4, 5, 6), a.merge(b).asSequence().toList())
        assertEquals(
            listOf(1, 2, 3, 4, 5, 6),
            a
                .iterator()
                .merge(b.iterator())
                .asSequence()
                .toList(),
        )
        assertEquals(listOf(1, 2, 3, 4, 5, 6), a.mergeBy(b) { x, y -> x <= y }.asSequence().toList())
    }

    @Test
    fun testCartesianProduct() {
        val a = listOf(1, 2)
        val b = listOf("x", "y")
        assertEquals(
            listOf(Pair(1, "x"), Pair(1, "y"), Pair(2, "x"), Pair(2, "y")),
            a.cartesianProduct(b).asSequence().toList(),
        )
    }

    @Test
    fun testDedup() {
        val data = listOf(1, 1, 2, 3, 3, 1)
        assertEquals(listOf(1, 2, 3, 1), data.dedup().asSequence().toList())
        assertEquals(
            listOf(1, 2, 3, 1),
            data
                .iterator()
                .dedup()
                .asSequence()
                .toList(),
        )
        assertEquals(
            listOf(Pair(2, 1), Pair(1, 2), Pair(2, 3), Pair(1, 1)),
            data.dedupWithCount().asSequence().toList(),
        )
    }

    @Test
    fun testCombinationsAndPermutations() {
        val data = listOf(1, 2, 3)
        val combs = data.combinations(2).asSequence().toList()
        assertEquals(listOf(listOf(1, 2), listOf(1, 3), listOf(2, 3)), combs)

        val perms = data.permutations(2).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(1, 3),
                listOf(2, 1),
                listOf(2, 3),
                listOf(3, 1),
                listOf(3, 2),
            ),
            perms,
        )

        val power = data.powerset().asSequence().toList()
        assertEquals(8, power.size)
    }

    @Test
    fun testPositionsAndUpdate() {
        val data = listOf("a", "bb", "ccc", "dd")
        assertEquals(listOf(1, 3), data.positions { it.length == 2 }.asSequence().toList())

        var count = 0
        val updated = data.update { count += it.length }.asSequence().toList()
        assertEquals(data, updated)
        assertEquals(8, count)
    }

    @Test
    fun testAllEqualAndUnique() {
        assertTrue(listOf(1, 1, 1).allEqual())
        assertFalse(listOf(1, 2, 1).allEqual())
        assertTrue(emptyList<Int>().allEqual())

        assertEquals(AllEqualValueResult.Empty, emptyList<Int>().allEqualValue())
        assertEquals(AllEqualValueResult.AllEqual(5), listOf(5, 5, 5).allEqualValue())
        assertEquals(AllEqualValueResult.NotEqual(5, 6), listOf(5, 6, 5).allEqualValue())

        assertTrue(listOf(1, 2, 3).allUnique())
        assertFalse(listOf(1, 2, 1).allUnique())
    }

    @Test
    fun testFindMethods() {
        val data = listOf(10, 20, 30, 40)
        assertEquals(Pair(1, 20), data.findPosition { it == 20 })
        assertNull(data.findPosition { it == 99 })

        assertEquals(30, data.findOrLast { it == 30 })
        assertEquals(40, data.findOrLast { it == 99 })
        assertNull(emptyList<Int>().findOrLast { it == 1 })

        assertEquals(20, data.findOrFirst { it == 20 })
        assertEquals(10, data.findOrFirst { it == 99 })
        assertNull(emptyList<Int>().findOrFirst { it == 1 })

        assertTrue(data.iterator().contains(30))
        assertFalse(data.iterator().contains(99))
    }

    @Test
    fun testDroppingAndConcat() {
        val data = listOf(1, 2, 3, 4, 5)
        assertEquals(listOf(3, 4, 5), data.dropping(2).asSequence().toList())
        assertEquals(listOf(1, 2, 3), data.droppingBack(2))

        val nested = listOf(listOf(1, 2), listOf(3, 4))
        assertEquals(listOf(1, 2, 3, 4), nested.concat())
    }

    @Test
    fun testCollectVecAndTryCollect() {
        val iter = listOf(1, 2, 3).iterator()
        assertEquals(listOf(1, 2, 3), iter.collectVec())

        val okList: List<ItemResult<Int, String>> = listOf(ItemResult.Ok(1), ItemResult.Ok(2))
        assertEquals(ItemResult.Ok(listOf(1, 2)), okList.iterator().tryCollect())

        val errList: List<ItemResult<Int, String>> = listOf(ItemResult.Ok(1), ItemResult.Err("fail"))
        assertEquals(ItemResult.Err("fail"), errList.iterator().tryCollect())
    }

    @Test
    fun testSetFromAndJoin() {
        val list = mutableListOf(0, 0, 0, 0)
        val written = list.setFrom(listOf(1, 2))
        assertEquals(2, written)
        assertEquals(listOf(1, 2, 0, 0), list)

        assertEquals("a - b - c", listOf("a", "b", "c").join(" - "))
    }

    @Test
    fun testFoldMethods() {
        val data = listOf(1, 2, 3, 4)
        assertEquals(10, data.fold1 { a, b -> a + b })
        assertNull(emptyList<Int>().fold1 { a, b -> a + b })

        val sumWhile =
            data.foldWhile(0) { acc, x ->
                if (acc + x > 5) {
                    FoldWhile.Done(acc)
                } else {
                    FoldWhile.Continue(acc + x)
                }
            }
        assertEquals(3, sumWhile.intoInner())
        assertTrue(sumWhile.isDone())

        assertEquals(10, data.sum1())
        assertNull(emptyList<Int>().sum1())

        assertEquals(24, data.product1())
        assertNull(emptyList<Int>().product1())
    }

    @Test
    fun testSortingAndExtrema() {
        val data = listOf(5, 2, 8, 1, 9, 3)
        assertEquals(listOf(1, 2, 3, 5, 8, 9), data.sorted())
        assertEquals(listOf(1, 2, 3, 5, 8, 9), data.sortedUnstable())
        assertEquals(listOf(1, 2, 3), data.kSmallest(3))
        assertEquals(listOf(9, 8, 5), data.kLargest(3))
        assertEquals(listOf(1, 2, 3), data.kSmallestRelaxed(3))
        assertEquals(listOf(9, 8, 5), data.kLargestRelaxed(3))

        assertEquals(listOf(8, 1, 9, 3), data.tail(4))
    }

    @Test
    fun testPartitionsAndPositions() {
        val data = listOf(1, 2, 3, 4, 5, 6)
        val (evens, odds) = data.partitionMap { if (it % 2 == 0) Either.Left(it) else Either.Right(it) }
        assertEquals(listOf(2, 4, 6), evens)
        assertEquals(listOf(1, 3, 5), odds)

        val items: List<ItemResult<Int, String>> = listOf(ItemResult.Ok(1), ItemResult.Err("x"), ItemResult.Ok(2))
        val (oks, errs) = items.partitionResult()
        assertEquals(listOf(1, 2), oks)
        assertEquals(listOf("x"), errs)

        assertEquals(4, listOf(5, 2, 8, 1, 9, 3).positionMax())
        assertEquals(3, listOf(5, 2, 8, 1, 9, 3).positionMin())
        assertEquals(MinMaxResult.MinMax(3, 4), listOf(5, 2, 8, 1, 9, 3).positionMinmax())
    }

    @Test
    fun testCountsAndMultiunzip() {
        val data = listOf("a", "b", "a", "c", "b", "a")
        assertEquals(mapOf("a" to 3, "b" to 2, "c" to 1), data.counts())
        assertEquals(mapOf(1 to 6), data.countsBy { it.length })

        val pairs = listOf(Pair(1, "a"), Pair(2, "b"))
        val (nums, chars) = pairs.multiunzip()
        assertEquals(listOf(1, 2), nums)
        assertEquals(listOf("a", "b"), chars)
    }

    @Test
    fun testFreeFunctions() {
        assertTrue(equal(listOf(1, 2, 3), listOf(1, 2, 3)))
        assertFalse(equal(listOf(1, 2), listOf(1, 2, 3)))

        assertEqual(listOf("x", "y"), listOf("x", "y"))

        val mutList = mutableListOf(1, 2, 3, 4, 5, 6)
        val split = partition(mutList) { it % 2 == 0 }
        assertEquals(3, split)
        assertTrue(mutList.subList(0, split).all { it % 2 == 0 })
        assertTrue(mutList.subList(split, mutList.size).all { it % 2 != 0 })
    }

    @Test
    fun testGetNextArrayCollectTupleAndMapInto() {
        val list = listOf(10, 20, 30, 40)
        assertEquals(10, list.iterator().get(0))
        assertEquals(30, list.iterator().get(2))
        assertNull(list.iterator().get(5))
        assertNull(list.iterator().get(-1))

        val iter = list.iterator()
        val arr = iter.nextArray(2)
        assertEquals(listOf(10, 20), arr)
        assertEquals(listOf(30, 40), iter.collectArray(2))

        val pairIter = listOf("a", "b").iterator()
        assertEquals(Pair("a", "b"), pairIter.collectTuple())

        val mapped = listOf(1, 2, 3).mapInto { it * 10 }.asSequence().toList()
        assertEquals(listOf(10, 20, 30), mapped)
    }
}
