// port-lint: source src/powerset.rs
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
        assertEquals(16, powerset(0 until 4).asSequence().count())
    }
}
