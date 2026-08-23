// port-lint: source src/grouping_map.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class GroupingMapTest {
    @Test
    fun testCollect() {
        val words = listOf("apple", "banana", "avocado", "cherry", "blueberry")
        val groups = words.intoGroupingMapBy { it.first() }.collect()
        assertEquals(listOf("apple", "avocado"), groups['a'])
        assertEquals(listOf("banana", "blueberry"), groups['b'])
        assertEquals(listOf("cherry"), groups['c'])
    }

    @Test
    fun testFold() {
        val words = listOf("apple", "banana", "avocado", "cherry", "blueberry")
        val counts = words.intoGroupingMapBy { it.first() }.fold(0) { acc, _, _ -> acc + 1 }
        assertEquals(2, counts['a'])
        assertEquals(2, counts['b'])
        assertEquals(1, counts['c'])
    }

    @Test
    fun testReduce() {
        val numbers = listOf(1, 2, 3, 4, 5, 6)
        val sums = numbers.intoGroupingMapBy { it % 2 }.reduce { acc, _, v -> acc + v }
        assertEquals(1 + 3 + 5, sums[1])
        assertEquals(2 + 4 + 6, sums[0])
    }

    @Test
    fun testMinMax() {
        val numbers = listOf(1, 10, 2, 9, 3, 8)
        val minmax = numbers.intoGroupingMapBy { it % 2 }.minmax()
        assertEquals(MinMaxResult.MinMax(1, 9), minmax[1])
        assertEquals(MinMaxResult.MinMax(2, 10), minmax[0])
    }
}
