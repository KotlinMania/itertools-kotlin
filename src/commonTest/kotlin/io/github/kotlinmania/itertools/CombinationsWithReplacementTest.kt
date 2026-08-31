// port-lint: tests itertools/src/combinations_with_replacement.rs
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

    @Test
    fun testCombinationsWithReplacementRangeCount() {
        for (n in 0..4) {
            for (k in 0..4) {
                val positions = if (n == 0) (k - 1).coerceAtLeast(0) else n + k - 1
                val len = checkedBinomial(positions, k) ?: 0
                val it = combinationsWithReplacement((0 until n).toList(), k)
                assertEquals(len, it.sizeHint().lower)
                assertEquals(len, it.sizeHint().upper)
                assertEquals(len, combinationsWithReplacement((0 until n).toList(), k).count())

                for (count in (len - 1) downTo 0) {
                    val elem = it.next()
                    assertEquals(k, elem.size)
                    assertEquals(count, it.sizeHint().lower)
                    assertEquals(count, it.sizeHint().upper)
                }
                assertFalse(it.hasNext())
            }
        }
    }

    @Test
    fun testCombinationsWithReplacementNth() {
        val all = combinationsWithReplacement(listOf(0, 1, 2), 2).asSequence().toList()
        for (i in all.indices) {
            val it = combinationsWithReplacement(listOf(0, 1, 2), 2)
            assertEquals(all[i], it.nth(i))
        }
    }
}
