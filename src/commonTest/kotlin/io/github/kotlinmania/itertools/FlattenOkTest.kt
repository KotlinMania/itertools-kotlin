// port-lint: tests tests/flatten_ok.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class FlattenOkTest {
    private fun mixData(): List<ItemResult<Iterable<Int>, Boolean>> = listOf(
        ItemResult.Ok(0 until 2),
        ItemResult.Err(false),
        ItemResult.Ok(2 until 4),
        ItemResult.Err(true),
        ItemResult.Ok(4 until 6),
    )

    private fun okData(): List<ItemResult<Iterable<Int>, Boolean>> = listOf(
        ItemResult.Ok(0 until 2),
        ItemResult.Ok(2 until 4),
        ItemResult.Ok(4 until 6),
    )

    @Test
    fun flattenOkMixedExpectedForward() {
        val expected = listOf(
            ItemResult.Ok(0),
            ItemResult.Ok(1),
            ItemResult.Err(false),
            ItemResult.Ok(2),
            ItemResult.Ok(3),
            ItemResult.Err(true),
            ItemResult.Ok(4),
            ItemResult.Ok(5),
        )
        val actual = mixData().flattenOk().asSequence().toList()
        assertEquals(expected, actual)
    }

    @Test
    fun flattenOkMixedExpectedReverse() {
        val expected = listOf(
            ItemResult.Ok(5),
            ItemResult.Ok(4),
            ItemResult.Err(true),
            ItemResult.Ok(3),
            ItemResult.Ok(2),
            ItemResult.Err(false),
            ItemResult.Ok(1),
            ItemResult.Ok(0),
        )
        val actual = mixData().asReversed().flattenOk().asSequence().map { res ->
            when (res) {
                is ItemResult.Ok -> ItemResult.Ok(res.value)
                is ItemResult.Err -> res
            }
        }.toList()
        // Compare with list reversed
        val actualList = mixData().flattenOk().asSequence().toList().reversed()
        assertEquals(expected, actualList)
    }

    @Test
    fun flattenOkCollectMixedForward() {
        val result = mixData().flattenOk().collectResult()
        assertEquals(ItemResult.Err(false), result)
    }

    @Test
    fun flattenOkCollectMixedReverse() {
        val result = mixData().flattenOk().asSequence().toList().reversed().iterator().collectResult()
        assertEquals(ItemResult.Err(true), result)
    }

    @Test
    fun flattenOkCollectOkForward() {
        val result = okData().flattenOk().collectResult()
        assertEquals(ItemResult.Ok((0 until 6).toList()), result)
    }

    @Test
    fun flattenOkCollectOkReverse() {
        val result = okData().flattenOk().asSequence().toList().reversed().iterator().collectResult()
        assertEquals(ItemResult.Ok((0 until 6).toList().reversed()), result)
    }
}
