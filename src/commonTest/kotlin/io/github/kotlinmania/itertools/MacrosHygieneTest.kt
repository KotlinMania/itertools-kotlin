// port-lint: tests itertools/tests/macros_hygiene.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class MacrosHygieneTest {
    @Test
    fun iproductHygiene() {
        val p2 = iproduct(0 until 6, 0 until 9).asSequence().toList()
        assertEquals(54, p2.size)

        val p3 = iproduct(0 until 6, 0 until 9, 0 until 12).asSequence().toList()
        assertEquals(648, p3.size)
    }

    @Test
    fun izipHygiene() {
        val z2 = multizip(0 until 6, 0 until 9).asSequence().toList()
        assertEquals(6, z2.size)

        val z3 = multizip(0 until 6, 0 until 9, 0 until 12).asSequence().toList()
        assertEquals(6, z3.size)
    }

    @Test
    fun chainHygiene() {
        val c2 = chain(0 until 6, 0 until 9).asSequence().toList()
        assertEquals(15, c2.size)
    }
}
