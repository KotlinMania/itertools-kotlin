// port-lint: tests permutations.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class PermutationsTest {
    @Test
    fun testPermutationsBasic() {
        assertEquals(
            listOf(listOf(1, 2), listOf(2, 1)),
            permutations(listOf(1, 2), 2).asSequence().toList(),
        )
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(1, 3),
                listOf(2, 1),
                listOf(2, 3),
                listOf(3, 1),
                listOf(3, 2),
            ),
            permutations(listOf(1, 2, 3), 2).asSequence().toList(),
        )
    }

    @Test
    fun testPermutationsZero() {
        assertEquals(listOf(emptyList()), permutations(listOf(1, 2), 0).asSequence().toList())
        assertEquals(listOf(emptyList()), permutations(emptyList<Int>(), 0).asSequence().toList())
    }
}
