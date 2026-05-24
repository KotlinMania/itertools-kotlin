// port-lint: source src/concat_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class ConcatImplTest {
    @Test
    fun concatFlattensNestedLists() {
        val input = listOf(listOf(1), listOf(2, 3), listOf(4, 5, 6))
        assertEquals(listOf(1, 2, 3, 4, 5, 6), concat(input))
    }

    @Test
    fun concatEmptyOuterReturnsEmptyList() {
        assertEquals(emptyList(), concat(emptyList<List<Int>>()))
    }

    @Test
    fun concatHonoursIterableOrder() {
        val input = sequenceOf(listOf("a", "b"), listOf("c"), listOf("d", "e")).asIterable()
        assertEquals(listOf("a", "b", "c", "d", "e"), concat(input))
    }

    @Test
    fun concatPreservesDuplicates() {
        val input = listOf(listOf(1, 1), listOf(1), listOf(2, 1))
        assertEquals(listOf(1, 1, 1, 2, 1), concat(input))
    }
}
