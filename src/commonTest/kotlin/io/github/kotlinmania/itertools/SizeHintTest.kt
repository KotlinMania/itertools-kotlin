// port-lint: source src/size_hint.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class SizeHintTest {
    @Test
    fun mulSizeHints() {
        assertEquals(9 to 16, mul(3 to 4, 3 to 4))
        assertEquals(Int.MAX_VALUE to null, mul(3 to 4, Int.MAX_VALUE to null))
        assertEquals(0 to 0, mul(3 to null, 0 to 0))
    }
}
