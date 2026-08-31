// port-lint: tests itertools/src/adaptors/mod.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.batching
import io.github.kotlinmania.itertools.adaptors.cartesianProduct
import io.github.kotlinmania.itertools.adaptors.checkedBinomial
import io.github.kotlinmania.itertools.adaptors.filterMapOk
import io.github.kotlinmania.itertools.adaptors.filterOk
import io.github.kotlinmania.itertools.adaptors.interleave
import io.github.kotlinmania.itertools.adaptors.interleaveShortest
import io.github.kotlinmania.itertools.adaptors.positions
import io.github.kotlinmania.itertools.adaptors.putBack
import io.github.kotlinmania.itertools.adaptors.takeWhileRef
import io.github.kotlinmania.itertools.adaptors.update
import io.github.kotlinmania.itertools.adaptors.whileSome
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class AdaptorsTest {
    @Test
    fun testCheckedBinomial() {
        val limit = 50
        var row = MutableList<Int?>(limit + 1) { 0 }
        row[0] = 1
        for (n in 0..limit) {
            for (k in 0..limit) {
                assertEquals(row[k], checkedBinomial(n, k))
            }
            val newRow = mutableListOf<Int?>(1)
            for (k in 1..limit) {
                val prev = row[k - 1]
                val curr = row[k]
                val sum =
                    if (prev != null && curr != null) {
                        val s = prev.toLong() + curr.toLong()
                        if (s > Int.MAX_VALUE) null else s.toInt()
                    } else {
                        null
                    }
                newRow.add(sum)
            }
            row = newRow
        }
    }

    @Test
    fun testInterleaveAndShortest() {
        val a = listOf(1, 3, 5, 7)
        val b = listOf(2, 4)
        val intlv = interleave(a, b).asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5, 7), intlv)

        val intlvShort = interleaveShortest(a, b).asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5), intlvShort)
    }

    @Test
    fun testPutBack() {
        val pb = putBack(listOf(1, 2, 3))
        assertEquals(1, pb.next())
        pb.putBack(10)
        assertEquals(10, pb.next())
        assertEquals(2, pb.next())
        assertEquals(3, pb.next())
        assertFalse(pb.hasNext())
    }

    @Test
    fun testProduct() {
        val prod = cartesianProduct(listOf(1, 2), listOf("a", "b")).asSequence().toList()
        assertEquals(
            listOf(
                Pair(1, "a"),
                Pair(1, "b"),
                Pair(2, "a"),
                Pair(2, "b"),
            ),
            prod,
        )
    }

    @Test
    fun testBatching() {
        val b =
            batching(listOf(1, 2, 3, 4, 5)) { iter ->
                if (iter.hasNext()) {
                    var sum = iter.next()
                    if (iter.hasNext()) sum += iter.next()
                    sum
                } else {
                    null
                }
            }
        assertEquals(listOf(3, 7, 5), b.asSequence().toList())
    }

    @Test
    fun testTakeWhileRef() {
        val it = listOf(1, 2, 3, 4, 1, 2).iterator()
        val tw = takeWhileRef(it) { it < 4 }
        assertEquals(listOf(1, 2, 3), tw.asSequence().toList())
    }

    @Test
    fun testWhileSome() {
        val it = listOf(1, 2, null, 4, 5)
        val ws = whileSome(it)
        assertEquals(listOf(1, 2), ws.asSequence().toList())
    }

    @Test
    fun testFilterOkAndFilterMapOk() {
        val items: List<ItemResult<Int, String>> =
            listOf(
                ItemResult.Ok(1),
                ItemResult.Err("error"),
                ItemResult.Ok(2),
                ItemResult.Ok(3),
            )
        val filtered = filterOk(items) { it % 2 == 1 }
        assertEquals(
            listOf(ItemResult.Ok(1), ItemResult.Err("error"), ItemResult.Ok(3)),
            filtered.collect(),
        )

        val mapped = filterMapOk(items) { if (it % 2 == 1) it * 10 else null }
        assertEquals(
            listOf(ItemResult.Ok(10), ItemResult.Err("error"), ItemResult.Ok(30)),
            mapped.collect(),
        )
    }

    @Test
    fun testPositions() {
        val pos = positions(listOf(1, 2, 3, 4, 5)) { it % 2 == 0 }
        assertEquals(listOf(1, 3), pos.asSequence().toList())
    }

    @Test
    fun testUpdate() {
        val list = mutableListOf(1, 2, 3)
        val res = update(list) { /* no-op in test */ }.collect()
        assertEquals(listOf(1, 2, 3), res)
    }
}
