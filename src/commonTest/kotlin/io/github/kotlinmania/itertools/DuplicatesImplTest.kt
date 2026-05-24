// port-lint: source src/duplicates_impl.rs (tests/test_std.rs::{duplicates, duplicates_by})
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DuplicatesImplTest {

    /**
     * Mirror of `tests/test_std.rs::duplicates_by` forward pass.
     *
     * The reverse cases from upstream are not ported because Kotlin's
     * `Iterator` does not implement `DoubleEndedIterator`; the upstream
     * adaptor only adds a `next_back` impl when the source supports it.
     */
    @Test
    fun duplicatesByYieldsEachKeyOnceOnItsSecondOccurrence() {
        val xs = listOf("aaa", "bbbbb", "aa", "ccc", "bbbb", "aaaaa", "cccc")
        val ys = listOf("aa", "bbbb", "cccc")
        val actual = duplicatesBy(xs) { it.substring(0, 2) }.asSequence().toList()
        assertEquals(ys, actual)
    }

    /** Mirror of `tests/test_std.rs::duplicates` forward pass. */
    @Test
    fun duplicatesYieldsEachValueOnceOnItsSecondOccurrence() {
        run {
            val xs = listOf(0, 1, 2, 3, 2, 1, 3)
            val ys = listOf(2, 1, 3)
            assertEquals(ys, duplicates(xs).asSequence().toList())
        }
        run {
            val xs = listOf(0, 1, 0, 1)
            val ys = listOf(0, 1)
            assertEquals(ys, duplicates(xs).asSequence().toList())
        }
        run {
            val xs = listOf(0, 1, 2, 1, 2)
            val ys = listOf(1, 2)
            assertEquals(ys, duplicates(xs).asSequence().toList())
        }
    }

    /** A unique-only source yields nothing. */
    @Test
    fun duplicatesEmptyOutputForUniqueInput() {
        val out = duplicates(listOf(1, 2, 3, 4, 5)).asSequence().toList()
        assertEquals(emptyList(), out)
    }

    /** Empty input yields nothing and throws on next(). */
    @Test
    fun duplicatesEmptyInput() {
        val it = duplicates(emptyList<Int>())
        assertFalse(it.hasNext())
        assertFailsWith<NoSuchElementException> { it.next() }
    }

    /** Third and later occurrences of a value are suppressed; only the second emits. */
    @Test
    fun duplicatesSuppressesThirdAndLaterOccurrences() {
        val out = duplicates(listOf(1, 1, 1, 1, 2, 2, 2)).asSequence().toList()
        assertEquals(listOf(1, 2), out)
    }

    /**
     * The lower size-hint bound is always 0 (we may only see unique items from
     * now on); the upper bound is the upstream `meta.pending + (remaining -
     * meta.pending) / 2` formula when there are more iter-remaining elements
     * than pending first-sightings.
     */
    @Test
    fun duplicatesSizeHintLowerIsZero() {
        val it = duplicates(listOf(1, 2, 3, 2, 1, 3))
        val (lo, hi) = it.sizeHint()
        assertEquals(0, lo)
        // 6 remaining, 0 pending → 0 + (6 - 0) / 2 = 3
        assertEquals(3, hi)
    }

    /** `duplicatesBy` with a constant key collapses all duplicates after the first. */
    @Test
    fun duplicatesByConstantKey() {
        // Every element has key 0, so they all map to the same bucket. The
        // first sighting is the seed; every later sighting is emitted as a
        // duplicate ONCE — except actually no, upstream only emits the
        // SECOND occurrence then suppresses the rest. So 4 elements with
        // the same key should yield exactly one element (the second).
        val out = duplicatesBy(listOf("a", "b", "c", "d")) { 0 }.asSequence().toList()
        assertEquals(listOf("b"), out)
    }

    /** hasNext() pumping is idempotent. */
    @Test
    fun duplicatesHasNextIdempotent() {
        val it = duplicates(listOf(1, 2, 1))
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertTrue(it.hasNext())
        assertEquals(1, it.next())
        assertFalse(it.hasNext())
        assertFalse(it.hasNext())
    }

    /** Non-Collection source: size hint stays `(0, null)` upper. */
    @Test
    fun duplicatesNonCollectionSourceHasNullUpper() {
        val it = duplicates(sequenceOf(1, 2, 1, 3).iterator())
        assertEquals(0 to null, it.sizeHint())
    }
}
