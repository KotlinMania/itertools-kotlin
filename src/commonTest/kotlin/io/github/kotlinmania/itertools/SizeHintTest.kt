// port-lint: tests size_hint.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class SizeHintTest {
    @Test
    fun mulSizeHints() {
        assertEquals(SizeHint(9, 16), mul(SizeHint(3, 4), SizeHint(3, 4)))
        assertEquals(SizeHint(Int.MAX_VALUE, null), mul(SizeHint(3, 4), SizeHint(Int.MAX_VALUE, null)))
        assertEquals(SizeHint(0, 0), mul(SizeHint(3, null), SizeHint(0, 0)))
    }

    @Test
    fun testAddAndScalars() {
        assertEquals(SizeHint(5, 7), add(SizeHint(2, 3), SizeHint(3, 4)))
        assertEquals(SizeHint(5, null), add(SizeHint(2, null), SizeHint(3, 4)))
        assertEquals(SizeHint(7, 9), addScalar(SizeHint(2, 4), 5))
        assertEquals(SizeHint(0, 2), subScalar(SizeHint(2, 4), 2))
        assertEquals(SizeHint(6, 12), mulScalar(SizeHint(2, 4), 3))
    }

    @Test
    fun testMinMax() {
        assertEquals(SizeHint(5, 10), max(SizeHint(2, 10), SizeHint(5, 8)))
        assertEquals(SizeHint(2, 8), min(SizeHint(2, 10), SizeHint(5, 8)))
        assertEquals(SizeHint(5, null), max(SizeHint(2, null), SizeHint(5, 8)))
        assertEquals(SizeHint(2, 8), min(SizeHint(2, null), SizeHint(5, 8)))
    }
}
