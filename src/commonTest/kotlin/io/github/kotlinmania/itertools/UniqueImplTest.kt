// port-lint: tests src/unique_impl.rs (tests/test_std.rs::{unique, unique_by})
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class UniqueImplTest {

    /**
     * Mirror of `tests/test_std.rs::unique_by` forward pass.
     *
     * The reverse cases from upstream are not ported because Kotlin's
     * `Iterator` does not implement `DoubleEndedIterator`; the upstream
     * adaptor only adds a `next_back` impl when the source supports it.
     */
    @Test
    fun uniqueByYieldsEachKeyOnceOnItsFirstOccurrence() {
        val xs = listOf("aaa", "bbbbb", "aa", "ccc", "bbbb", "aaaaa", "cccc")
        val ys = listOf("aaa", "bbbbb", "ccc")
        val actual = uniqueBy(xs) { it.substring(0, 2) }.asSequence().toList()
        assertEquals(ys, actual)
    }

    /** Mirror of `tests/test_std.rs::unique` forward pass. */
    @Test
    fun uniqueYieldsEachValueOnceOnItsFirstOccurrence() {
        run {
            val xs = listOf(0, 1, 2, 3, 2, 1, 3)
            val ys = listOf(0, 1, 2, 3)
            val actual = unique(xs).asSequence().toList()
            assertEquals(ys, actual)
        }
        run {
            val xs = listOf(0, 1)
            val ys = listOf(0, 1)
            val actual = unique(xs).asSequence().toList()
            assertEquals(ys, actual)
        }
    }

    /** Empty source yields no elements and reports an exhausted iterator. */
    @Test
    fun uniqueOverEmptyIterableIsExhausted() {
        val it = unique(emptyList<Int>())
        assertFalse(it.hasNext())
        assertFailsWith<NoSuchElementException> { it.next() }
    }

    /** All-duplicate source yields exactly one element per unique key. */
    @Test
    fun uniqueByOverAllDuplicatesYieldsOnePerKey() {
        val xs = listOf("a", "a", "a", "a")
        val actual = unique(xs).asSequence().toList()
        assertEquals(listOf("a"), actual)
    }

    /**
     * Size hint lower bound is 1 only while the dedup set is empty and the
     * source still has elements to yield; once any element has been emitted,
     * the lower bound collapses to 0 because we may never see a new key again.
     */
    @Test
    fun uniqueSizeHintLowerBoundCollapsesAfterFirstYield() {
        val xs = listOf(0, 0, 0)
        val it = uniqueBy(xs.iterator(), SizeHint(xs.size, xs.size)) { it: Int -> it }
        val before = it.sizeHint()
        assertEquals(1, before.lower)
        assertEquals(3, before.upper)

        assertTrue(it.hasNext())
        assertEquals(0, it.next())

        val after = it.sizeHint()
        assertEquals(0, after.lower)
        assertEquals(3, after.upper)
    }

    /** The upper bound from a finite source propagates unchanged. */
    @Test
    fun uniqueSizeHintUpperBoundMatchesSourceForFiniteCollections() {
        val xs = listOf(1, 2, 3, 4)
        val it = uniqueBy(xs.iterator(), SizeHint(xs.size, xs.size)) { it: Int -> it }
        val (low, hi) = it.sizeHint()
        assertEquals(1, low)
        assertEquals(4, hi)
    }
}
