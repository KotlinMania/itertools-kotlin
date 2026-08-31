// port-lint: tests itertools/src/groupbylazy.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class GroupbylazyTest {
    @Test
    fun testChunkBy() {
        val list = listOf(1, 1, 2, 2, 2, 3, 1, 1)
        val groups = chunkBy(list) { it }.asSequence().toList()
        assertEquals(
            listOf(
                Pair(1, listOf(1, 1)),
                Pair(2, listOf(2, 2, 2)),
                Pair(3, listOf(3)),
                Pair(1, listOf(1, 1)),
            ),
            groups,
        )
    }

    @Test
    fun testChunks() {
        val list = listOf(1, 2, 3, 4, 5)
        val chunks = chunks(list, 2).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(3, 4),
                listOf(5),
            ),
            chunks,
        )
    }
}
