// port-lint: tests multipeek_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class MultiPeekImplTest {
    @Test
    fun testMultiPeekBasic() {
        val mp = multipeek(listOf(1, 2, 3, 4))
        assertEquals(1, mp.peek())
        assertEquals(2, mp.peek())
        assertEquals(3, mp.peek())
        assertEquals(1, mp.next())
        // After next(), cursor resets to index 0 (which is now element 2)
        assertEquals(2, mp.peek())
        assertEquals(3, mp.peek())
        assertEquals(4, mp.peek())
        assertNull(mp.peek())
    }

    @Test
    fun testResetPeek() {
        val mp = multipeek(listOf(10, 20, 30))
        assertEquals(10, mp.peek())
        assertEquals(20, mp.peek())
        mp.resetPeek()
        assertEquals(10, mp.peek())
    }

    @Test
    fun testPeekingNext() {
        val mp = multipeek(listOf(1, 2, 3))
        val item = mp.peekingNext { it < 2 }
        assertEquals(1, item)
        val rejected = mp.peekingNext { it > 5 }
        assertNull(rejected)
        assertEquals(2, mp.next())
    }
}
