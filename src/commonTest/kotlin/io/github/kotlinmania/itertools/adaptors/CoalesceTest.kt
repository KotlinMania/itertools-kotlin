// port-lint: tests adaptors/coalesce.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint
import kotlin.test.Test
import kotlin.test.assertEquals

class CoalesceTest {
    @Test
    fun testCoalesce() {
        val list = listOf(-1, 2, -3, 4, 5, -6)
        val result =
            coalesce(list) { a, b ->
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

    @Test
    fun testCoalesceFoldAndSizeHint() {
        val list = listOf(1, 2, 3, 4)
        val iter =
            coalesce(list) { a, b ->
                CoalesceResult.Merged(a + b)
            }
        assertEquals(SizeHint(1, null), iter.sizeHint())
        val sum = iter.fold(0) { acc, x -> acc + x }
        assertEquals(10, sum)
    }

    @Test
    fun testDedupFoldAndExtensions() {
        val list = listOf(1, 1, 2, 2, 3)
        val deduped = dedup(list)
        val sum = deduped.fold(0) { acc, x -> acc + x }
        assertEquals(6, sum)

        val withCount = dedupWithCount(list)
        val countSum = withCount.fold(0) { acc, pair -> acc + pair.first }
        assertEquals(5, countSum)
    }
}
