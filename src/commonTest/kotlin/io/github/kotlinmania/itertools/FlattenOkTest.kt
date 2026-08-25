// port-lint: tests flatten_ok.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

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
        assertNull(it.nextBack())
    }

    @Test
    fun testFlattenOkMixedExpectedForward() {
        val mixData =
            listOf(
                ItemResult.Ok(listOf(0, 1)),
                ItemResult.Err(false),
                ItemResult.Ok(listOf(2, 3)),
                ItemResult.Err(true),
                ItemResult.Ok(listOf(4, 5)),
            )
        val expected =
            listOf(
                ItemResult.Ok(0),
                ItemResult.Ok(1),
                ItemResult.Err(false),
                ItemResult.Ok(2),
                ItemResult.Ok(3),
                ItemResult.Err(true),
                ItemResult.Ok(4),
                ItemResult.Ok(5),
            )
        assertEquals(expected, flattenOk(mixData).asSequence().toList())
    }

    @Test
    fun testFlattenOkMixedExpectedReverse() {
        val mixData =
            listOf(
                ItemResult.Ok(listOf(0, 1)),
                ItemResult.Err(false),
                ItemResult.Ok(listOf(2, 3)),
                ItemResult.Err(true),
                ItemResult.Ok(listOf(4, 5)),
            )
        val it = flattenOk(mixData)
        val reversed = mutableListOf<ItemResult<Int, Boolean>>()
        while (true) {
            val item = it.nextBack() ?: break
            reversed.add(item)
        }
        val expected =
            listOf(
                ItemResult.Ok(5),
                ItemResult.Ok(4),
                ItemResult.Err(true),
                ItemResult.Ok(3),
                ItemResult.Ok(2),
                ItemResult.Err(false),
                ItemResult.Ok(1),
                ItemResult.Ok(0),
            )
        assertEquals(expected, reversed)
    }

    @Test
    fun testFlattenOkCollectOkForward() {
        val okData =
            listOf(
                ItemResult.Ok(listOf(0, 1)),
                ItemResult.Ok(listOf(2, 3)),
                ItemResult.Ok(listOf(4, 5)),
            )
        val expected = (0 until 6).map { ItemResult.Ok(it) }
        assertEquals(expected, flattenOk(okData).asSequence().toList())
    }

    @Test
    fun testFlattenOkCollectOkReverse() {
        val okData: List<ItemResult<List<Int>, String>> =
            listOf(
                ItemResult.Ok(listOf(0, 1)),
                ItemResult.Ok(listOf(2, 3)),
                ItemResult.Ok(listOf(4, 5)),
            )
        val it = flattenOk(okData)
        val reversed = mutableListOf<ItemResult<Int, String>>()
        while (true) {
            val item = it.nextBack() ?: break
            reversed.add(item)
        }
        val expected: List<ItemResult<Int, String>> = (0 until 6).reversed().map { ItemResult.Ok(it) }
        assertEquals(expected, reversed)
    }

    @Test
    fun testFlattenOkFoldAndRfold() {
        val okData: List<ItemResult<List<Int>, String>> =
            listOf(
                ItemResult.Ok(listOf(1, 2)),
                ItemResult.Ok(listOf(3, 4)),
            )
        val folded =
            flattenOk(okData).fold(0) { acc, res ->
                if (res is ItemResult.Ok) acc + res.value else acc
            }
        assertEquals(10, folded)

        val rfolded =
            flattenOk(okData).rfold(mutableListOf<Int>()) { acc, res ->
                if (res is ItemResult.Ok) acc.add(res.value)
                acc
            }
        assertEquals(listOf(4, 3, 2, 1), rfolded)
    }
}
