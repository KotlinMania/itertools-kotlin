// port-lint: source src/iter_index.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class IterIndexTest {
    private val sample = listOf(0, 1, 2, 3, 4, 5)

    @Test
    fun rangeReturnsHalfOpenSlice() {
        assertEquals(listOf(1, 2, 3), get(sample, Range(1, 4)).asSequence().toList())
    }

    @Test
    fun rangeEmptyWhenStartGreaterThanOrEqualToEnd() {
        assertEquals(emptyList(), get(sample, Range(4, 4)).asSequence().toList())
        assertEquals(emptyList(), get(sample, Range(5, 2)).asSequence().toList())
    }

    @Test
    fun rangeInclusiveReturnsClosedSlice() {
        assertEquals(listOf(1, 2, 3), get(sample, RangeInclusive(1, 3)).asSequence().toList())
        assertEquals(listOf(5), get(sample, RangeInclusive(5, 5)).asSequence().toList())
    }

    @Test
    fun rangeInclusiveEmptyWhenStartGreaterThanEnd() {
        assertEquals(emptyList(), get(sample, RangeInclusive(4, 3)).asSequence().toList())
    }

    @Test
    fun rangeInclusiveRejectsStartZeroAtMaxEnd() {
        assertFailsWith<IllegalStateException> {
            get(sample, RangeInclusive(0, Int.MAX_VALUE))
        }
    }

    @Test
    fun rangeToTakesPrefix() {
        assertEquals(listOf(0, 1, 2), get(sample, RangeTo(3)).asSequence().toList())
        assertEquals(emptyList(), get(sample, RangeTo(0)).asSequence().toList())
    }

    @Test
    fun rangeToInclusiveTakesPrefixIncludingEnd() {
        assertEquals(listOf(0, 1, 2, 3), get(sample, RangeToInclusive(3)).asSequence().toList())
    }

    @Test
    fun rangeToInclusiveRejectsMaxEnd() {
        assertFailsWith<IllegalStateException> {
            get(sample, RangeToInclusive(Int.MAX_VALUE))
        }
    }

    @Test
    fun rangeFromDropsPrefix() {
        assertEquals(listOf(3, 4, 5), get(sample, RangeFrom(3)).asSequence().toList())
        assertEquals(emptyList(), get(sample, RangeFrom(10)).asSequence().toList())
    }

    @Test
    fun rangeFullReturnsAll() {
        assertEquals(sample, get(sample, RangeFull).asSequence().toList())
    }

    @Test
    fun indexIsCallableOnAnyIteratorElementType() {
        val words = listOf("a", "b", "c", "d")
        assertEquals(listOf("b", "c"), get(words, Range(1, 3)).asSequence().toList())
    }
}
