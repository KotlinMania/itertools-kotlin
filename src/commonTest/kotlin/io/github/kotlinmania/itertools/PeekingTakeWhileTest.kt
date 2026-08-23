// port-lint: source src/peeking_take_while.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PeekingTakeWhileTest {
    @Test
    fun testPeekingTakeWhilePeekable() {
        val r = (0 until 10).peekable()
        val taken = peekingTakeWhile(r) { it <= 3 }.asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3), taken)
        assertEquals(4, r.next())
    }

    @Test
    fun testPeekingTakeWhilePutBack() {
        val r = putBack(0 until 10)
        val taken = peekingTakeWhile(r) { it <= 3 }.asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3), taken)
        assertEquals(4, r.next())
        val remaining = peekingTakeWhile(r) { true }.asSequence().toList()
        assertEquals(listOf(5, 6, 7, 8, 9), remaining)
        assertFalse(r.hasNext())
    }

    @Test
    fun testPeekingTakeWhilePutBackN() {
        val r = PutBackN((6 until 10).iterator(), SizeHint(4, 4))
        for (elt in 5 downTo 0) {
            r.putBack(elt)
        }
        val taken = peekingTakeWhile(r) { it <= 3 }.asSequence().toList()
        assertEquals(listOf(0, 1, 2, 3), taken)
        assertEquals(4, r.next())
        val remaining = peekingTakeWhile(r) { true }.asSequence().toList()
        assertEquals(listOf(5, 6, 7, 8, 9), remaining)
        assertFalse(r.hasNext())
    }

    @Test
    fun testPeekingTakeWhileNested() {
        val xs = (0 until 10).peekable()
        val ys =
            peekingTakeWhile(
                peekingTakeWhile(xs) { it < 6 },
            ) { it != 3 }
                .asSequence()
                .toList()
        assertEquals(listOf(0, 1, 2), ys)
        assertEquals(3, xs.next())
    }
}
