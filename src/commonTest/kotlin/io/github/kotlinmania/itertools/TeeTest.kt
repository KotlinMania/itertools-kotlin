// port-lint: source src/tee.rs (tests/test_std.rs::tee)
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class TeeTest {

    /**
     * Mirror of `tests/test_std.rs::tee` — the canonical upstream interleaving
     * test for `tee`. The Kotlin port keeps the same call order; because
     * Kotlin's `Iterator` exposes `hasNext` / `next` rather than a fused
     * `Option<T>` `next`, we assert on `hasNext()` as well, then on `next()`.
     */
    @Test
    fun teeInterleavedExhaustsBothHalves() {
        val xs = listOf(0, 1, 2, 3)
        val (t1, t2) = tee(xs)
        assertEquals(0, t1.next())
        assertEquals(0, t2.next())
        assertEquals(1, t1.next())
        assertEquals(2, t1.next())
        assertEquals(3, t1.next())
        assertFalse(t1.hasNext())
        assertEquals(1, t2.next())
        assertEquals(2, t2.next())
        assertFalse(t1.hasNext())
        assertEquals(3, t2.next())
        assertFalse(t2.hasNext())
        assertFalse(t1.hasNext())
        assertFalse(t2.hasNext())
    }

    /** Each half yields the full source sequence in order. */
    @Test
    fun teeBothHalvesYieldSource() {
        val xs = listOf(0, 1, 2, 3)
        val (t1, t2) = tee(xs)
        assertEquals(xs, t1.asSequence().toList())
        assertEquals(xs, t2.asSequence().toList())
    }

    /** Zipping the two halves produces a paired self-zip of the source. */
    @Test
    fun teeZippedHalvesProduceSelfPairs() {
        val xs = listOf(0, 1, 2, 3)
        val (t1, t2) = tee(xs)
        val expected = xs.zip(xs)
        val actual = t1.asSequence().zip(t2.asSequence()).toList()
        assertEquals(expected, actual)
    }

    /**
     * Mirror of `tests/quick.rs::size_tee` — after pulling one half by `n`,
     * the size hints relate to the remaining sequence as expected.
     */
    @Test
    fun teeSizeHintTracksConsumption() {
        val xs = listOf(0, 1, 2, 3, 4)
        val (t1, t2) = tee(xs)
        // Initial: nothing consumed.
        assertEquals(SizeHint(5, 5), t1.sizeHint())
        assertEquals(SizeHint(5, 5), t2.sizeHint())

        // Pull two from t1; t2 owns the backlog now (owner flipped to !id of
        // puller). t2's hint should include backlog; t1's should not.
        repeat(2) { t1.next() }
        val t1Hint = t1.sizeHint()
        val t2Hint = t2.sizeHint()
        assertEquals(3, t1Hint.lower)
        assertEquals(3, t1Hint.upper)
        assertEquals(5, t2Hint.lower)
        assertEquals(5, t2Hint.upper)
    }

    /** A fresh tee over an empty source yields no elements on either half. */
    @Test
    fun teeEmptySource() {
        val (t1, t2) = tee(emptyList<Int>())
        assertFalse(t1.hasNext())
        assertFalse(t2.hasNext())
    }

    /** Sequence input has no Collection size; size hint is the (0, null) default. */
    @Test
    fun teeNonCollectionSourceHasUnknownHint() {
        val (t1, t2) = teeNew(sequenceOf(10, 20, 30).iterator())
        assertEquals(SizeHint(0, null), t1.sizeHint())
        assertEquals(SizeHint(0, null), t2.sizeHint())
        assertEquals(listOf(10, 20, 30), t1.asSequence().toList())
        assertEquals(listOf(10, 20, 30), t2.asSequence().toList())
    }

    /** Pulling exclusively from one half drains the source; the other half then drains the backlog. */
    @Test
    fun teeOneSidedExhaustionLeavesBacklogForSibling() {
        val xs = listOf("a", "b", "c", "d")
        val (t1, t2) = tee(xs)
        assertEquals(xs, t1.asSequence().toList())
        // After t1 fully drained, t2 has the full backlog to itself.
        assertTrue(t2.hasNext())
        assertEquals(xs, t2.asSequence().toList())
    }
}
