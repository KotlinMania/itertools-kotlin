// port-lint: source src/adaptors/map.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult
import kotlin.test.Test
import kotlin.test.assertEquals

class MapTest {
    @Test
    fun testMapOk() {
        val input = listOf(
            ItemResult.Ok(1),
            ItemResult.Err("fail"),
            ItemResult.Ok(3),
        )
        val mapped = mapOk(input) { it * 2 }.asSequence().toList()
        assertEquals(
            listOf(
                ItemResult.Ok(2),
                ItemResult.Err("fail"),
                ItemResult.Ok(6),
            ),
            mapped,
        )
    }

    @Test
    fun testMapInto() {
        val input = listOf(1, 2, 3)
        val mapped = mapInto(input) { it.toString() }.asSequence().toList()
        assertEquals(listOf("1", "2", "3"), mapped)
    }
}
