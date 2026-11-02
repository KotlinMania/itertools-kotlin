package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertSame

class PeekNthPredicateParityTest {
    @Test
    fun nextIfAcceptsPresentNullAndCallsThePredicate() {
        val iterator = peekNth(listOf<Int?>(null, 1))
        var calls = 0
        assertNull(
            iterator.nextIf {
                calls += 1
                it == null
            },
        )
        assertEquals(1, calls)
        assertEquals(1, iterator.next())
        assertFalse(iterator.hasNext())
    }

    @Test
    fun nextIfRejectsNullAndRestoresTheItem() {
        val iterator = peekNth(listOf<Int?>(null, 1))
        val seen = mutableListOf<Int?>()
        assertNull(
            iterator.nextIf {
                seen.add(it)
                false
            },
        )
        assertEquals(listOf<Int?>(null), seen)
        assertNull(iterator.next())
        assertEquals(1, iterator.next())
    }

    @Test
    fun nextIfEqConsumesAnAcceptedNull() {
        val iterator = peekNth(listOf<Int?>(null, 1))
        assertNull(iterator.nextIfEq(null))
        assertEquals(1, iterator.next())
    }

    @Test
    fun nextIfExceptionLeavesTheItemConsumed() {
        val iterator = peekNth(listOf(1, 2, 3))
        assertEquals(2, iterator.peekNth(1))
        val sentinel = IllegalStateException("predicate failure")
        assertSame(sentinel, assertFailsWith<IllegalStateException> { iterator.nextIf { throw sentinel } })
        assertEquals(2, iterator.next())
        assertEquals(3, iterator.next())
    }

    @Test
    fun peekingNextCallsThePredicateForNullButRetainsARejectedItem() {
        val iterator = peekNth(listOf<Int?>(null, 7))
        val seen = mutableListOf<Int?>()
        assertNull(
            iterator.peekingNext {
                seen.add(it)
                false
            },
        )
        assertEquals(listOf<Int?>(null), seen)
        assertNull(iterator.next())
        assertEquals(7, iterator.next())
    }

    @Test
    fun peekingNextExceptionRetainsTheItem() {
        val iterator = peekNth(listOf(1, 2))
        val sentinel = IllegalStateException("predicate failure")
        assertSame(sentinel, assertFailsWith<IllegalStateException> { iterator.peekingNext { throw sentinel } })
        assertEquals(1, iterator.next())
        assertEquals(2, iterator.next())
    }

    @Test
    fun exhaustedInputDoesNotCallEitherPredicate() {
        val iterator = peekNth(emptyList<Int?>())
        assertNull(iterator.nextIf { error("no item") })
        assertNull(iterator.peekingNext { error("no item") })
        assertFalse(iterator.hasNext())
    }
}
