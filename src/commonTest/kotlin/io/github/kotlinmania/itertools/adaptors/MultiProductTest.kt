// port-lint: tests itertools/src/adaptors/multi_product.rs
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

    @Test
    fun testMultiProductCountAndSizeHint() {
        val lists =
            listOf(
                listOf(1, 2),
                listOf("a", "b", "c"),
                listOf(true, false),
            )
        val mp = multiCartesianProduct(lists)
        assertEquals(12, mp.sizeHint().lower)
        assertEquals(12, mp.sizeHint().upper)
        assertEquals(12, mp.count())
    }

    @Test
    fun testMultiProductLast() {
        val lists =
            listOf(
                listOf(1, 2),
                listOf("a", "b"),
                listOf(true, false),
            )
        val mp = multiCartesianProduct(lists)
        assertEquals(listOf(2, "b", false), mp.last())
    }

    @Test
    fun testMultiProductEmptyInputs() {
        val lists = emptyList<List<Int>>()
        val mp = multiCartesianProduct(lists)
        assertEquals(emptyList(), mp.last())
        val mp2 = multiCartesianProduct(lists)
        assertEquals(1, mp2.count())
    }
}
