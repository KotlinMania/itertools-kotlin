// port-lint: tests powerset.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class PowersetTest {
    @Test
    fun testPowersetBasic() {
        assertEquals(listOf(emptyList()), powerset(emptyList<Int>()).asSequence().toList())
        assertEquals(
            listOf(emptyList(), listOf(0)),
            powerset(listOf(0)).asSequence().toList(),
        )
        assertEquals(
            listOf(
                emptyList(),
                listOf(0),
                listOf(1),
                listOf(0, 1),
            ),
            powerset(listOf(0, 1)).asSequence().toList(),
        )
        assertEquals(
            listOf(
                emptyList(),
                listOf(0),
                listOf(1),
                listOf(2),
                listOf(0, 1),
                listOf(0, 2),
                listOf(1, 2),
                listOf(0, 1, 2),
            ),
            powerset(listOf(0, 1, 2)).asSequence().toList(),
        )
    }

    @Test
    fun testPowersetCounts() {
        assertEquals(16, powerset(0 until 4).count())
        assertEquals(256, powerset(0 until 8).count())
        assertEquals(65536, powerset(0 until 16).count())
    }

    @Test
    fun testPowersetNthAndFold() {
        val ps = powerset(listOf(0, 1, 2))
        assertEquals(listOf(1, 2), ps.nth(6))

        val psFold = powerset(listOf(0, 1))
        val count = psFold.fold(0) { acc, _ -> acc + 1 }
        assertEquals(4, count)
    }
}
