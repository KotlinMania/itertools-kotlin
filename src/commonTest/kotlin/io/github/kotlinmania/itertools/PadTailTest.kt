// port-lint: source src/pad_tail.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class PadTailTest {
    @Test
    fun padsShortSourceUsingFillerWithPosition() {
        val padded = padUsing(listOf(1, 2), 5) { i -> i * 10 }
            .asSequence().toList()
        assertEquals(listOf(1, 2, 20, 30, 40), padded)
    }

    @Test
    fun sourceLongerThanMinPassesThrough() {
        val out = padUsing(listOf(1, 2, 3, 4, 5), 3) { _ -> 0 }
            .asSequence().toList()
        assertEquals(listOf(1, 2, 3, 4, 5), out)
    }

    @Test
    fun sourceExactlyMinNoFiller() {
        var calls = 0
        val out = padUsing(listOf(1, 2, 3), 3) { _ ->
            calls += 1; -1
        }.asSequence().toList()
        assertEquals(listOf(1, 2, 3), out)
        assertEquals(0, calls)
    }

    @Test
    fun emptySourceFullyPadded() {
        val out = padUsing(emptyList<String>(), 3) { i -> "f$i" }
            .asSequence().toList()
        assertEquals(listOf("f0", "f1", "f2"), out)
    }

    @Test
    fun minZeroIsIdentity() {
        val out = padUsing(listOf("a", "b"), 0) { _ -> "filler" }
            .asSequence().toList()
        assertEquals(listOf("a", "b"), out)
    }

    // sizeHint / fold live on the internal PadUsing class; construct directly.

    @Test
    fun sizeHintReflectsMinAndRemaining() {
        val src = listOf(1, 2)
        val it = PadUsing(src.iterator(), 5, src.size to src.size) { i -> i }
        assertEquals(5 to 5, it.sizeHint())
        it.next()
        assertEquals(4 to 4, it.sizeHint())
        it.next()
        assertEquals(3 to 3, it.sizeHint())
        it.next()
        it.next()
        it.next()
        assertFalse(it.hasNext())
        assertEquals(0 to 0, it.sizeHint())
    }

    @Test
    fun foldVisitsSourceThenFiller() {
        val src = listOf("a", "b")
        val it = PadUsing(src.iterator(), 4, src.size to src.size) { i -> "f$i" }
        val out = it.fold(mutableListOf<String>()) { acc, x ->
            acc.add(x); acc
        }
        assertEquals(listOf("a", "b", "f2", "f3"), out)
    }
}
