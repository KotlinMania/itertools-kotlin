// port-lint: tests tests/test_core.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class TestCore {
    @Test
    fun get1Max() {
        val it = (0 until 5).iterator().get(1)
        assertEquals(1, it)
    }

    @Test
    fun getOutOfBounds() {
        val it = (0 until 5).iterator().get(10)
        assertNull(it)
    }

    @Test
    fun product0() {
        val prod = listOf(Unit).iterator()
        assertEquals(1, prod.asSequence().count())
    }

    @Test
    fun iproduct1() {
        val s = listOf('α', 'β')
        val prod = s.iterator()
        assertEquals('α', prod.next())
        assertEquals('β', prod.next())
        assertFalse(prod.hasNext())
    }

    @Test
    fun product2() {
        val s = listOf('α', 'β')
        val prod = s.cartesianProduct(listOf(0, 1))
        assertEquals(Pair('α', 0), prod.next())
        assertEquals(Pair('α', 1), prod.next())
        assertEquals(Pair('β', 0), prod.next())
        assertEquals(Pair('β', 1), prod.next())
        assertFalse(prod.hasNext())
    }

    @Test
    fun productTemporary() {
        val prod = listOf(0, 1, 2).cartesianProduct(listOf(0, 1, 2))
        var count = 0
        while (prod.hasNext()) {
            prod.next()
            count++
        }
        assertEquals(9, count)
    }

    @Test
    fun izipMacro() {
        val zip = izip(2 until 3, 2 until 3)
        assertEquals(Pair(2, 2), zip.next())
        assertFalse(zip.hasNext())

        val zip3 = izip(0 until 3, 0 until 2, 0 until 2)
        for (i in 0 until 2) {
            assertEquals(Triple(i, i, i), zip3.next())
        }
        assertFalse(zip3.hasNext())
    }

    @Test
    fun multizip3() {
        val zip = multizip(0 until 3, 0 until 2, 0 until 2)
        for (i in 0 until 2) {
            assertEquals(Triple(i, i, i), zip.next())
        }
        assertFalse(zip.hasNext())
    }

    @Test
    fun chainMacro() {
        val chain1 = chain(2 until 3, emptyList())
        assertEquals(2, chain1.next())
        assertFalse(chain1.hasNext())

        val chain2 = chain(0 until 2, 2 until 3, 3 until 5)
        for (i in 0 until 5) {
            assertEquals(i, chain2.next())
        }
        assertFalse(chain2.hasNext())
    }

    @Test
    fun testInterleave() {
        val xs = emptyList<UByte>()
        val ys = listOf(7u.toUByte(), 9u.toUByte(), 8u.toUByte(), 10u.toUByte())
        val zs = listOf(2u.toUByte(), 77u.toUByte())
        val it1 = interleave(xs, ys)
        assertEqual(it1, ys)

        val rs = listOf(7u.toUByte(), 2u.toUByte(), 9u.toUByte(), 77u.toUByte(), 8u.toUByte(), 10u.toUByte())
        val it2 = interleave(ys, zs)
        assertEqual(it2, rs)
    }

    @Test
    fun testIntersperse() {
        val xs = listOf(1u.toUByte(), 2u.toUByte(), 3u.toUByte())
        val ys = listOf(1u.toUByte(), 0u.toUByte(), 2u.toUByte(), 0u.toUByte(), 3u.toUByte())
        val it = intersperse(xs, 0u.toUByte())
        assertEqual(it, ys)
    }

    @Test
    fun testIntersperseWith() {
        val xs = listOf(1u.toUByte(), 2u.toUByte(), 3u.toUByte())
        val ys = listOf(1u.toUByte(), 10u.toUByte(), 2u.toUByte(), 10u.toUByte(), 3u.toUByte())
        val it = intersperseWith(xs) { 10u.toUByte() }
        assertEqual(it, ys)
    }

    @Test
    fun dropping() {
        val xs = listOf(1, 2, 3)
        val it1 = xs.iterator().dropping(2)
        assertEquals(3, it1.next())
        assertFalse(it1.hasNext())

        val it2 = xs.iterator().dropping(5)
        assertFalse(it2.hasNext())
    }

    @Test
    fun batching() {
        val xs = listOf(0, 1, 2, 1, 3)
        val ys = listOf(Pair(0, 1), Pair(2, 1))

        val pit =
            xs.iterator().batching { it ->
                if (it.hasNext()) {
                    val x = it.next()
                    if (it.hasNext()) {
                        Pair(x, it.next())
                    } else {
                        null
                    }
                } else {
                    null
                }
            }
        assertEqual(pit, ys)
    }

    @Test
    fun testPutBack() {
        val xs = listOf(0, 1, 1, 1, 2, 1, 3, 3)
        val pb = putBack(xs.iterator())
        pb.next()
        pb.putBack(1)
        pb.putBack(0)
        assertEqual(pb, xs)
    }

    @Test
    fun merge() {
        val evens = (0 until 10 step 2).toList()
        val odds = (1 until 10 step 2).toList()
        assertEqual(evens.merge(odds), (0 until 10).toList())
    }

    @Test
    fun repeatn() {
        val s = "α"
        val it = repeatN(s, 3)
        assertEquals(s, it.next())
        assertEquals(s, it.next())
        assertEquals(s, it.next())
        assertFalse(it.hasNext())
    }

    @Test
    fun part() {
        val data1 = intArrayOf(7, 1, 1, 9, 1, 1, 3)
        val i1 = partition(data1) { it >= 3 }
        assertEquals(3, i1)
        assertEquals(listOf(7, 3, 9, 1, 1, 1, 1), data1.toList())

        val data2 = intArrayOf(7, 1, 1, 9, 1, 1, 3)
        val i2 = partition(data2) { it == 1 }
        assertEquals(4, i2)
        assertEquals(listOf(1, 1, 1, 1, 9, 7, 3), data2.toList())

        val data3 = intArrayOf(1, 2, 3, 4, 5, 6, 7, 8, 9)
        val i3 = partition(data3) { it % 3 == 0 }
        assertEquals(3, i3)
        assertEquals(listOf(9, 6, 3, 4, 5, 2, 7, 8, 1), data3.toList())
    }

    @Test
    fun treeReduceTest() {
        for (i in 1..20) {
            val list = (0 until i).toList()
            val expected = list.reduce { x, y -> x + y }
            val actual = treeReduce(list) { x, y -> x + y }
            assertEquals(expected, actual)
        }
    }

    @Test
    fun exactlyOne() {
        assertEquals(2, (0 until 10).filter { it == 2 }.exactlyOne().getOrNull())
        assertTrue((0 until 10).filter { it in 2..3 }.exactlyOne().isErr())
        assertTrue((0 until 10).filter { it in 2..4 }.exactlyOne().isErr())
        assertTrue((0 until 10).filter { false }.exactlyOne().isErr())
    }

    @Test
    fun atMostOne() {
        assertEquals(2, (0 until 10).filter { it == 2 }.atMostOne().getOrNull())
        assertTrue((0 until 10).filter { it in 2..3 }.atMostOne().isErr())
        assertTrue((0 until 10).filter { it in 2..4 }.atMostOne().isErr())
        assertEquals(null, (0 until 10).filter { false }.atMostOne().getOrNull())
    }

    @Test
    fun sum1() {
        val v = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        assertNull(sum1Int(v.subList(0, 0)))
        assertEquals(1, sum1Int(v.subList(1, 2)))
        assertEquals(3, sum1Int(v.subList(1, 3)))
        assertEquals(55, sum1Int(v))
    }

    @Test
    fun product1() {
        val v = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
        assertNull(product1Int(v.subList(0, 0)))
        assertEquals(0, product1Int(v.subList(0, 1)))
        assertEquals(2, product1Int(v.subList(1, 3)))
        assertEquals(24, product1Int(v.subList(1, 5)))
    }
}
