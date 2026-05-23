// port-lint: source src/put_back_n_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PutBackNImplTest {
    @Test
    fun putBackNDocExample() {
        val it = putBackN(listOf(1, 2, 3, 4))
        it.next()
        it.putBack(1)
        it.putBack(0)
        assertEquals(listOf(0, 1, 2, 3, 4), it.asSequence().toList())
    }

    @Test
    fun putBackNDrainsWithoutPutBack() {
        val it = putBackN(listOf("a", "b", "c"))
        assertEquals(listOf("a", "b", "c"), it.asSequence().toList())
    }

    @Test
    fun putBackNMostRecentComesFirst() {
        val it = putBackN(emptyList<Int>())
        it.putBack(10)
        it.putBack(20)
        it.putBack(30)
        assertEquals(30, it.next())
        assertEquals(20, it.next())
        assertEquals(10, it.next())
        assertFalse(it.hasNext())
    }

    @Test
    fun putBackNSizeHintReflectsStackAndSource() {
        val it = putBackN(listOf(1, 2, 3, 4))
        assertEquals(4 to 4, it.sizeHint())
        it.next()
        assertEquals(3 to 3, it.sizeHint())
        it.putBack(99)
        assertEquals(4 to 4, it.sizeHint())
        it.putBack(98)
        assertEquals(5 to 5, it.sizeHint())
    }

    @Test
    fun putBackNFoldYieldsStackThenSource() {
        val it = putBackN(listOf(2, 3, 4))
        it.next()
        it.putBack(1)
        it.putBack(0)
        val out = it.fold(mutableListOf<Int>()) { acc, x ->
            acc.add(x); acc
        }
        assertEquals(listOf(0, 1, 3, 4), out)
    }

    @Test
    fun putBackNHasNextHandlesEmpty() {
        val it = putBackN(emptyList<Int>())
        assertFalse(it.hasNext())
        it.putBack(7)
        assertTrue(it.hasNext())
        assertEquals(7, it.next())
        assertFalse(it.hasNext())
    }
}
