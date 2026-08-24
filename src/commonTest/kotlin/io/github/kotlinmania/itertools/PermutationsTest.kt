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

    @Test
    fun testPermutationsRangeCount() {
        for (n in 0..4) {
            for (k in 0..4) {
                var len = 1
                if (k <= n) {
                    for (i in (n - k + 1)..n) {
                        len *= i
                    }
                } else {
                    len = 0
                }
                val it = permutations((0 until n).toList(), k)
                assertEquals(len, it.sizeHint().lower)
                assertEquals(len, it.sizeHint().upper)
                assertEquals(len, permutations((0 until n).toList(), k).count())

                for (count in (len - 1) downTo 0) {
                    val elem = it.next()
                    assertEquals(k, elem.size)
                    assertEquals(count, it.sizeHint().lower)
                    assertEquals(count, it.sizeHint().upper)
                }
                assertEquals(false, it.hasNext())
            }
        }
    }
}
