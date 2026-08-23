// port-lint: source src/flatten_ok.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class FlattenOkTest {
    @Test
    fun testFlattenOkMixed() {
        val input: List<ItemResult<List<Int>, String>> =
            listOf(
                ItemResult.Ok(listOf(0, 1)),
                ItemResult.Err("err1"),
                ItemResult.Ok(listOf(2, 3)),
                ItemResult.Err("err2"),
                ItemResult.Ok(listOf(4, 5)),
            )

        val out = flattenOk(input).asSequence().toList()
        val expected =
            listOf(
                ItemResult.Ok(0),
                ItemResult.Ok(1),
                ItemResult.Err("err1"),
                ItemResult.Ok(2),
                ItemResult.Ok(3),
                ItemResult.Err("err2"),
                ItemResult.Ok(4),
                ItemResult.Ok(5),
            )
        assertEquals(expected, out)
    }

    @Test
    fun testFlattenOkEmpty() {
        val input = emptyList<ItemResult<List<Int>, String>>()
        val it = flattenOk(input)
        assertFalse(it.hasNext())
    }
}
