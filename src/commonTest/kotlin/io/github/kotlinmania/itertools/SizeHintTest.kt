// port-lint: source src/size_hint.rs
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
}
