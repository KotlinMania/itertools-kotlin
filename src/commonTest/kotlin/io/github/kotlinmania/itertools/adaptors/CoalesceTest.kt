// port-lint: source src/adaptors/coalesce.rs
package io.github.kotlinmania.itertools.adaptors

import kotlin.test.Test
import kotlin.test.assertEquals

class CoalesceTest {
    @Test
    fun testCoalesce() {
        val list = listOf(-1, 2, -3, 4, 5, -6)
        val result = coalesce(list) { a, b ->
            if (a >= 0 && b >= 0) {
                CoalesceResult.Merged(a + b)
            } else {
                CoalesceResult.Separate(a, b)
            }
        }.asSequence().toList()

        assertEquals(listOf(-1, 2, -3, 9, -6), result)
    }

    @Test
    fun testDedup() {
        val list = listOf(1, 1, 2, 3, 3, 3, 2, 2, 4)
        val result = dedup(list).asSequence().toList()
        assertEquals(listOf(1, 2, 3, 2, 4), result)
    }

    @Test
    fun testDedupWithCount() {
        val list = listOf("a", "a", "b", "c", "c", "c")
        val result = dedupWithCount(list).asSequence().toList()
        assertEquals(
            listOf(
                Pair(2, "a"),
                Pair(1, "b"),
                Pair(3, "c"),
            ),
            result,
        )
    }
}
