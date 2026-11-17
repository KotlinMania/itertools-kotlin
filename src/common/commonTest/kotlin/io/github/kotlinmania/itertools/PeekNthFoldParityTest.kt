package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

class PeekNthFoldParityTest {
    @Test
    fun foldConsumesBothPrefetchedAndUnderlyingItems() {
        val iterator = peekNth(listOf(1, 2, 3, 4))
        assertEquals(3, iterator.peekNth(2))
        val output =
            iterator.fold(mutableListOf<Int>()) { acc, item ->
                acc.apply { add(item) }
            }
        assertEquals(listOf(1, 2, 3, 4), output)
        assertFalse(iterator.hasNext())
        assertFailsWith<NoSuchElementException> { iterator.next() }
    }

    @Test
    fun prefetchedNullIsConsumedExactlyOnce() {
        val iterator = peekNth(listOf<Int?>(null, 2))
        assertNull(iterator.peek())
        val output =
            iterator.fold(mutableListOf<Int?>()) { acc, item ->
                acc.apply { add(item) }
            }
        assertEquals(listOf(null, 2), output)
        assertFalse(iterator.hasNext())
        assertFailsWith<NoSuchElementException> { iterator.next() }
    }

    @Test
    fun callbackFailureDoesNotReplayProcessedOrFailingItems() {
        val iterator = peekNth(listOf(1, 2, 3))
        assertEquals(2, iterator.peekNth(1))
        val visited = mutableListOf<Int>()
        val sentinel = IllegalStateException("fold callback")
        val error =
            assertFailsWith<IllegalStateException> {
                iterator.fold(0) { acc, item ->
                    visited.add(item)
                    if (item == 2) throw sentinel
                    acc + item
                }
            }
        assertSame(sentinel, error)
        assertEquals(listOf(1, 2), visited)
        assertEquals(3, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun emptyFoldKeepsItsInitialValueWithoutCallingTheCallback() {
        val iterator = peekNth(emptyList<Int?>())
        assertNull(iterator.peek())
        assertEquals(42, iterator.fold(42) { _, _ -> error("no item") })
        assertFalse(iterator.hasNext())
    }
}
