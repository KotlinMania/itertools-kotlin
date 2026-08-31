// port-lint: tests itertools/src/rciter_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class RcIterImplTest {
    @Test
    fun testSharedIteration() {
        val rc = rciter(listOf(1, 2, 3, 4, 5))
        val h1 = rc.share()
        val h2 = rc.share()

        assertEquals(1, h1.next())
        assertEquals(2, h2.next())
        assertEquals(3, h1.next())
        assertEquals(4, h2.next())
        assertEquals(5, h1.next())
        assertFalse(h2.hasNext())
    }
}
