// port-lint: tests combinations_with_replacement.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CombinationsWithReplacementTest {
    @Test
    fun testCombinationsWithReplacementBasic() {
        assertEquals(
            listOf(listOf(0, 0)),
            combinationsWithReplacement(listOf(0), 2).asSequence().toList(),
        )
        assertEquals(
            listOf(
                listOf(0, 0),
                listOf(0, 1),
                listOf(0, 2),
                listOf(1, 1),
                listOf(1, 2),
                listOf(2, 2),
            ),
            combinationsWithReplacement(listOf(0, 1, 2), 2).asSequence().toList(),
        )
        assertEquals(
            listOf(emptyList()),
            combinationsWithReplacement(listOf(0, 1, 2), 0).asSequence().toList(),
        )
        assertEquals(
            listOf(emptyList()),
            combinationsWithReplacement(emptyList<Int>(), 0).asSequence().toList(),
        )
        assertFalse(combinationsWithReplacement(emptyList<Int>(), 2).hasNext())
    }
}
