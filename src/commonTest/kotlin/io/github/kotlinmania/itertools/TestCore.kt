// port-lint: tests tests/test_core.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.batching
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestCore {
    @Test
    fun product0() {
        val prod = iproduct(listOf(Unit), listOf(Unit)).asSequence().toList()
        assertEquals(1, prod.size)
    }

    @Test
    fun iproduct1() {
        val s = "αβ".toList()
        val prod = iproduct(s, listOf(0, 1)).asSequence().toList()
        assertEquals(
            listOf(
                Pair('α', 0),
                Pair('α', 1),
                Pair('β', 0),
                Pair('β', 1),
            ),
            prod,
        )
    }

    @Test
    fun multizip3() {
        val zip = multizip(0 until 3, 0 until 2, listOf(0.toByte(), 1.toByte())).asSequence().toList()
        assertEquals(2, zip.size)
        assertEquals(Triple(0, 0, 0.toByte()), zip[0])
        assertEquals(Triple(1, 1, 1.toByte()), zip[1])
    }

    @Test
    fun testInterleave() {
        val xs = emptyList<Int>()
        val ys = listOf(7, 9, 8, 10)
        val zs = listOf(2, 77)

        val it1 = interleave(xs, ys).asSequence().toList()
        assertEquals(ys, it1)

        val it2 = interleave(ys, zs).asSequence().toList()
        assertEquals(listOf(7, 2, 9, 77, 8, 10), it2)
    }

    @Test
    fun testIntersperse() {
        val xs = listOf(1, 2, 3)
        val ys = listOf(1, 0, 2, 0, 3)
        val it = intersperse(xs, 0).asSequence().toList()
        assertEquals(ys, it)
    }

    @Test
    fun testIntersperseWith() {
        val xs = listOf(1, 2, 3)
        val ys = listOf(1, 10, 2, 10, 3)
        val it = intersperseWith(xs) { 10 }.asSequence().toList()
        assertEquals(ys, it)
    }

    @Test
    fun batching() {
        val xs = listOf(0, 1, 2, 1, 3)
        val ys = listOf(Pair(0, 1), Pair(2, 1))

        val pit =
            batching(xs.iterator()) {
                if (it.hasNext()) {
                    val x = it.next()
                    if (it.hasNext()) {
                        val y = it.next()
                        Pair(x, y)
                    } else {
                        null
                    }
                } else {
                    null
                }
            }.asSequence().toList()
        assertEquals(ys, pit)
    }

    @Test
    fun testRepeatN() {
        val s = "α"
        val it = repeatN(s, 3).asSequence().toList()
        assertEquals(listOf("α", "α", "α"), it)
    }

    @Test
    fun part() {
        val data = intArrayOf(7, 1, 1, 9, 1, 1, 3)
        val i = partitionInPlace(data) { it >= 3 }
        assertEquals(3, i)
        assertTrue(data.sliceArray(0 until i).all { it >= 3 })
        assertTrue(data.sliceArray(i until data.size).all { it < 3 })
    }

    @Test
    fun testTreeReduce() {
        for (i in 1..20) {
            val range = 0 until i
            assertEquals(range.reduce { x, y -> x + y }, treeReduce(range) { x, y -> x + y })
        }
        assertNull(treeReduce(emptyList<Int>()) { x, y -> x + y })
    }

    @Test
    fun testExactlyOne() {
        val okRes = exactlyOne((0 until 10).filter { it == 2 })
        assertTrue(okRes is ItemResult.Ok)
        assertEquals(2, okRes.value)

        val errRes1 = exactlyOne((0 until 10).filter { it in 2..3 })
        assertTrue(errRes1 is ItemResult.Err)
        assertEquals(listOf(2, 3), errRes1.error.asSequence().toList())

        val errResEmpty = exactlyOne((0 until 10).filter { false })
        assertTrue(errResEmpty is ItemResult.Err)
        assertEquals(emptyList(), errResEmpty.error.asSequence().toList())
    }

    @Test
    fun testAtMostOne() {
        val okRes = atMostOne((0 until 10).filter { it == 2 })
        assertTrue(okRes is ItemResult.Ok)
        assertEquals(2, okRes.value)

        val okResNone = atMostOne((0 until 10).filter { false })
        assertTrue(okResNone is ItemResult.Ok)
        assertNull(okResNone.value)

        val errRes = atMostOne((0 until 10).filter { it in 2..3 })
        assertTrue(errRes is ItemResult.Err)
        assertEquals(listOf(2, 3), errRes.error.asSequence().toList())
    }

    @Test
    fun testSum1() {
        val v = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        assertNull(sum1Int(emptyList()))
        assertEquals(1, sum1Int(v.subList(1, 2)))
        assertEquals(3, sum1Int(v.subList(1, 3)))
        assertEquals(55, sum1Int(v))
    }

    @Test
    fun testProduct1() {
        val v = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        assertNull(product1Int(emptyList()))
        assertEquals(0, product1Int(v.subList(0, 1)))
        assertEquals(2, product1Int(v.subList(1, 3)))
        assertEquals(24, product1Int(v.subList(1, 5)))
    }
}
