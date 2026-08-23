// port-lint: tests combinations.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class CombinationsTest {
    @Test
    fun testCombinationsBasic() {
        val out = combinations(listOf(1, 2, 3, 4), 2).asSequence().toList()
        assertEquals(
            listOf(
                listOf(1, 2),
                listOf(1, 3),
                listOf(1, 4),
                listOf(2, 3),
                listOf(2, 4),
                listOf(3, 4),
            ),
            out,
        )
    }

    @Test
    fun testCombinationsZero() {
        assertEquals(listOf(emptyList()), combinations(listOf(1, 2), 0).asSequence().toList())
        assertEquals(listOf(emptyList()), combinations(emptyList<Int>(), 0).asSequence().toList())
    }

    @Test
    fun testCombinationsTooShort() {
        assertFalse(combinations(listOf(1, 2), 5).hasNext())
    }

    @Test
    fun testCombinationsNthAndCount() {
        val comb = combinations(listOf(1, 2, 3, 4), 2)
        assertEquals(6, comb.count())

        val combNth = combinations(listOf(1, 2, 3, 4), 2)
        assertEquals(listOf(1, 4), combNth.nth(2))
    }
}
