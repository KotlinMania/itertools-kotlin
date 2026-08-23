// port-lint: tests adaptors/multi_product.rs
package io.github.kotlinmania.itertools.adaptors

import kotlin.test.Test
import kotlin.test.assertEquals

class MultiProductTest {
    @Test
    fun testMultiProduct() {
        val lists =
            listOf(
                listOf(1, 2),
                listOf("a", "b"),
                listOf(true),
            )
        val result = multiCartesianProduct(lists).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, "a", true),
                listOf(1, "b", true),
                listOf(2, "a", true),
                listOf(2, "b", true),
            ),
            result,
        )
    }

    @Test
    fun testMultiProductWithEmptyList() {
        val lists =
            listOf(
                listOf(1, 2),
                emptyList<String>(),
                listOf(true),
            )
        val result = multiCartesianProduct(lists).asSequence().toList()
        assertEquals(emptyList(), result)
    }
}
