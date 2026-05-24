// port-lint: source src/k_smallest.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class KSmallestTest {
    private val naturalInt: Comparator<Int> = Comparator { a, b -> a.compareTo(b) }

    @Test
    fun kSmallestGeneralEmptyIterator() {
        val out = kSmallestGeneral(emptyList<Int>().iterator(), 3, naturalInt)
        assertEquals(emptyList(), out)
    }

    @Test
    fun kSmallestGeneralZeroDrainsIterator() {
        val source = listOf(1, 2, 3, 4)
        val it = source.iterator()
        val out = kSmallestGeneral(it, 0, naturalInt)
        assertEquals(emptyList(), out)
        assertEquals(false, it.hasNext())
    }

    @Test
    fun kSmallestGeneralOneTakesMinimum() {
        val out = kSmallestGeneral(listOf(5, 2, 9, 1, 7).iterator(), 1, naturalInt)
        assertEquals(listOf(1), out)
    }

    @Test
    fun kSmallestGeneralReturnsAscendingOrder() {
        val out = kSmallestGeneral(listOf(5, 2, 9, 1, 7, 3, 8).iterator(), 3, naturalInt)
        assertEquals(listOf(1, 2, 3), out)
    }

    @Test
    fun kSmallestGeneralKExceedingLengthReturnsAll() {
        val out = kSmallestGeneral(listOf(3, 1, 2).iterator(), 10, naturalInt)
        assertEquals(listOf(1, 2, 3), out)
    }

    @Test
    fun kSmallestGeneralAgreesWithSortAndTake() {
        val values = listOf(8, 3, 9, 1, 6, 4, 7, 2, 5, 0)
        for (k in 0..values.size + 2) {
            val out = kSmallestGeneral(values.iterator(), k, naturalInt)
            val expected = values.sorted().take(minOf(k, values.size))
            assertEquals(expected, out, "k=$k")
        }
    }

    @Test
    fun kSmallestGeneralWithReverseComparatorReturnsLargest() {
        val reverse: Comparator<Int> = Comparator { a, b -> b.compareTo(a) }
        val out = kSmallestGeneral(listOf(5, 2, 9, 1, 7).iterator(), 3, reverse)
        assertEquals(listOf(9, 7, 5), out)
    }

    @Test
    fun kSmallestRelaxedGeneralReturnsAscendingOrder() {
        val out = kSmallestRelaxedGeneral(listOf(5, 2, 9, 1, 7, 3, 8).iterator(), 3, naturalInt)
        assertEquals(listOf(1, 2, 3), out)
    }

    @Test
    fun kSmallestRelaxedGeneralZeroDrainsIterator() {
        val it = listOf(1, 2, 3).iterator()
        val out = kSmallestRelaxedGeneral(it, 0, naturalInt)
        assertEquals(emptyList(), out)
        assertEquals(false, it.hasNext())
    }

    @Test
    fun kSmallestRelaxedGeneralKLargerThanInputReturnsSortedAll() {
        val out = kSmallestRelaxedGeneral(listOf(3, 1, 2).iterator(), 10, naturalInt)
        assertEquals(listOf(1, 2, 3), out)
    }

    @Test
    fun kSmallestRelaxedGeneralAgreesWithSortAndTake() {
        val values = listOf(8, 3, 9, 1, 6, 4, 7, 2, 5, 0, 11, 13, 12, 10)
        for (k in 0..values.size + 2) {
            val out = kSmallestRelaxedGeneral(values.iterator(), k, naturalInt)
            val expected = values.sorted().take(minOf(k, values.size))
            assertEquals(expected, out, "k=$k")
        }
    }

    @Test
    fun keyToCmpOrdersByKey() {
        val cmp = keyToCmp<String, Int> { it.length }
        val items = listOf("abcd", "a", "abc", "ab")
        val sorted = items.sortedWith(cmp)
        assertEquals(listOf("a", "ab", "abc", "abcd"), sorted)
    }

    @Test
    fun keyToCmpRoutesThroughKSmallestGeneral() {
        val cmp = keyToCmp<Pair<Int, String>, Int> { it.first }
        val items = listOf(3 to "c", 1 to "a", 4 to "d", 2 to "b")
        val out = kSmallestGeneral(items.iterator(), 2, cmp)
        assertEquals(listOf(1 to "a", 2 to "b"), out)
    }

    @Test
    fun kSmallestGeneralOnDuplicatesRetainsK() {
        val out = kSmallestGeneral(listOf(2, 2, 2, 1, 1, 1).iterator(), 3, naturalInt)
        assertEquals(3, out.size)
        assertEquals(listOf(1, 1, 1), out)
    }

    @Test
    fun kSmallestRelaxedGeneralOnLongInputStabilizesEveryDouble() {
        val values = (0..49).reversed().toList()
        val out = kSmallestRelaxedGeneral(values.iterator(), 5, naturalInt)
        assertEquals(listOf(0, 1, 2, 3, 4), out)
        assertTrue(out.size == 5)
    }
}
