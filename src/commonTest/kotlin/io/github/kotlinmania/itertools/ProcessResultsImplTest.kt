// port-lint: source src/process_results_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class ProcessResultsImplTest {
    @Test
    fun testProcessResultsSuccess() {
        val input: List<ItemResult<Int, String>> = listOf(
            ItemResult.Ok(1),
            ItemResult.Ok(2),
            ItemResult.Ok(3),
        )
        val res = processResults(input) { iter ->
            iter.asSequence().sum()
        }
        assertEquals(ItemResult.Ok(6), res)
    }

    @Test
    fun testProcessResultsWithError() {
        val input: List<ItemResult<Int, String>> = listOf(
            ItemResult.Ok(1),
            ItemResult.Ok(2),
            ItemResult.Err("error occurred"),
            ItemResult.Ok(4),
        )
        val res = processResults(input) { iter ->
            iter.asSequence().sum()
        }
        assertEquals(ItemResult.Err("error occurred"), res)
    }
}
