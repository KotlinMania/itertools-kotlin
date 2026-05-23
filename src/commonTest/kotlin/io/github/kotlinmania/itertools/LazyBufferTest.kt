// port-lint: source src/lazy_buffer.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class LazyBufferTest {
    @Test
    fun startsEmptyAndPullsOnDemand() {
        val lb = LazyBuffer(listOf(10, 20, 30))
        assertEquals(0, lb.length)
        assertTrue(lb.getNext())
        assertEquals(1, lb.length)
        assertEquals(10, lb[0])
        assertTrue(lb.getNext())
        assertEquals(20, lb[1])
    }

    @Test
    fun getNextReturnsFalseWhenSourceExhausted() {
        val lb = LazyBuffer(listOf(1, 2))
        assertTrue(lb.getNext())
        assertTrue(lb.getNext())
        assertFalse(lb.getNext())
        assertFalse(lb.getNext())
    }

    @Test
    fun prefillBuffersUpToLen() {
        val lb = LazyBuffer(listOf(1, 2, 3, 4, 5))
        lb.prefill(3)
        assertEquals(3, lb.length)
        assertEquals(listOf(1, 2, 3), (0 until lb.length).map { lb[it] })
        lb.prefill(2)
        assertEquals(3, lb.length)
        lb.prefill(99)
        assertEquals(5, lb.length)
    }

    @Test
    fun getAtClonesAtIndices() {
        val lb = LazyBuffer(listOf("a", "b", "c", "d"))
        lb.prefill(4)
        assertEquals(listOf("c", "a", "b"), lb.getAt(intArrayOf(2, 0, 1)))
    }

    @Test
    fun sizeHintCombinesBufferAndSourceRemaining() {
        val lb = LazyBuffer(listOf(1, 2, 3, 4, 5))
        assertEquals(5 to 5, lb.sizeHint())
        lb.getNext()
        assertEquals(5 to 5, lb.sizeHint())
        lb.getNext()
        assertEquals(5 to 5, lb.sizeHint())
        lb.prefill(5)
        assertEquals(5 to 5, lb.sizeHint())
    }

    @Test
    fun countDrainsAndReturnsTotal() {
        val lb = LazyBuffer(listOf(1, 2, 3, 4))
        lb.getNext()
        assertEquals(4, lb.count())
        assertEquals(4, lb.length)
    }

    @Test
    fun emptySourceIsImmediatelyDone() {
        val lb = LazyBuffer(emptyList<Int>())
        assertEquals(0, lb.length)
        assertFalse(lb.getNext())
        assertEquals(0, lb.count())
    }
}
