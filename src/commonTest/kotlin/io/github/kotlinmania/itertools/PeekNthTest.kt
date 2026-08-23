// port-lint: tests peek_nth.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PeekNthTest {
    @Test
    fun testPeekNth() {
        val it = peekNth(listOf(1, 2, 3, 4, 5))
        assertEquals(1, it.peek())
        assertEquals(1, it.peekNth(0))
        assertEquals(2, it.peekNth(1))
        assertEquals(3, it.peekNth(2))
        assertEquals(4, it.peekNth(3))
        assertEquals(5, it.peekNth(4))
        assertNull(it.peekNth(5))

        assertEquals(1, it.next())
        assertEquals(2, it.peekNth(0))
        assertEquals(3, it.peekNth(1))

        assertEquals(2, it.nextIf { it == 2 })
        assertNull(it.nextIf { it == 99 })
        assertEquals(3, it.nextIfEq(3))

        assertEquals(4, it.next())
        assertEquals(5, it.next())
        assertNull(it.peek())
    }
}
