// port-lint: tests tests/peeking_take_while.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PeekingTakeWhileTest {
    @Test
    fun peekingTakeWhilePeekable() {
        val r = (0 until 10).iterator().peekable()
        r.peekingTakeWhile { it <= 3 }.asSequence().count()
        assertEquals(4, r.next())
    }

    @Test
    fun peekingTakeWhilePutBack() {
        val r = putBack(0 until 10)
        r.peekingTakeWhile { it <= 3 }.asSequence().count()
        assertEquals(4, r.next())
        r.peekingTakeWhile { true }.asSequence().count()
        assertNull(r.peekingNext { true })
    }

    @Test
    fun peekingTakeWhilePutBackN() {
        val r = putBackN(6 until 10)
        for (elt in 5 downTo 0) {
            r.putBack(elt)
        }
        r.peekingTakeWhile { it <= 3 }.asSequence().count()
        assertEquals(4, r.next())
        r.peekingTakeWhile { true }.asSequence().count()
        assertNull(r.peekingNext { true })
    }

    @Test
    fun peekingTakeWhileSliceIter() {
        val v = listOf(1, 2, 3, 4, 5, 6)
        val r = v.iterator().peekable()
        r.peekingTakeWhile { it <= 3 }.asSequence().count()
        assertEquals(4, r.next())
        r.peekingTakeWhile { true }.asSequence().count()
        assertEquals(false, r.hasNext())
    }

    @Test
    fun peekingTakeWhileSliceIterRev() {
        val v = listOf(1, 2, 3, 4, 5, 6)
        val r = v.asReversed().iterator().peekable()
        r.peekingTakeWhile { it >= 3 }.asSequence().count()
        assertEquals(2, r.next())
        r.peekingTakeWhile { true }.asSequence().count()
        assertEquals(false, r.hasNext())
    }

    @Test
    fun peekingTakeWhileNested() {
        val xs = (0 until 10).iterator().peekable()
        val ys = mutableListOf<Int>()
        val it1 = xs.peekingTakeWhile { it < 6 }.peekingTakeWhile { it != 3 }
        while (it1.hasNext()) {
            ys.add(it1.next())
        }
        assertEquals(listOf(0, 1, 2), ys)
        assertEquals(3, xs.next())

        val xs2 = (4 until 10).iterator().peekable()
        val ys2 = mutableListOf<Int>()
        val it2 = xs2.peekingTakeWhile { it != 3 }.peekingTakeWhile { it < 6 }
        while (it2.hasNext()) {
            ys2.add(it2.next())
        }
        assertEquals(listOf(4, 5), ys2)
        assertEquals(6, xs2.next())
    }
}
