// port-lint: source src/kmerge_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class KMergeImplTest {
    @Test
    fun testKmerge() {
        val lists = listOf(
            listOf(0, 2, 4),
            listOf(1, 3, 5),
            listOf(6, 7),
        )
        val merged = kmerge(lists).asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3, 4, 5, 6, 7), merged)
    }

    @Test
    fun testKmergeEmpty() {
        val lists = emptyList<List<Int>>()
        val merged = kmerge(lists).asSequence().toList()
        assertEquals(emptyList(), merged)
    }

    @Test
    fun testKmergeWithEmptySublists() {
        val lists = listOf(
            emptyList<Int>(),
            listOf(1, 4),
            emptyList(),
            listOf(2, 3),
        )
        val merged = kmerge(lists).asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4), merged)
    }

    @Test
    fun testKmergeByCustomComparator() {
        val lists = listOf(
            listOf(5, 3, 1),
            listOf(6, 4, 2),
        )
        val merged = kmergeBy(lists) { a, b -> a > b }.asSequence().toList()
        assertEquals(listOf(6, 5, 4, 3, 2, 1), merged)
    }
}
