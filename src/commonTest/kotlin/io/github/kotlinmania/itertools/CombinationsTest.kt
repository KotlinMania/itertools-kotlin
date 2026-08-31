// port-lint: tests itertools/src/combinations.rs
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

    @Test
    fun testCombinationsRangeCount() {
        for (n in 0..4) {
            for (k in 0..4) {
                val len = checkedBinomial(n, k) ?: 0
                val it = combinations((0 until n).toList(), k)
                assertEquals(len, it.sizeHint().lower)
                assertEquals(len, it.sizeHint().upper)
                assertEquals(len, combinations((0 until n).toList(), k).count())

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
    fun testCheckedBinomial() {
        val limit = 500
        val row = ArrayList<Int?>()
        row.add(1)
        for (i in 1..limit) {
            row.add(0)
        }
        var currentRow: List<Int?> = row

        for (n in 0..limit) {
            for (k in 0..limit) {
                assertEquals(currentRow[k], checkedBinomial(n, k))
            }
            val nextRow = ArrayList<Int?>()
            nextRow.add(1)
            for (k in 1..limit) {
                val a = currentRow[k - 1]
                val b = currentRow[k]
                if (a == null || b == null) {
                    nextRow.add(null)
                } else {
                    val sum = a.toLong() + b.toLong()
                    if (sum > Int.MAX_VALUE.toLong()) {
                        nextRow.add(null)
                    } else {
                        nextRow.add(sum.toInt())
                    }
                }
            }
            currentRow = nextRow
        }
    }
}
