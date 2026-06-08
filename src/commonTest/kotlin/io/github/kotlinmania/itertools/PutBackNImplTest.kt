// port-lint: source src/put_back_n_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PutBackNImplTest {
    // The public factory putBackN(...) returns Iterator<T>; tests that exercise
    // putBack / sizeHint / fold construct the internal PutBackN class directly
    // (visible from commonTest because it's in the same module).

    @Test
    fun putBackNDocExample() {
        val it = PutBackN(listOf(1, 2, 3, 4).iterator(), SizeHint(4, 4))
        it.next()
        it.putBack(1)
        it.putBack(0)
        assertEquals(listOf(0, 1, 2, 3, 4), it.asSequence().toList())
    }

    @Test
    fun putBackNDrainsWithoutPutBack() {
        val out = putBackN(listOf("a", "b", "c")).asSequence().toList()
        assertEquals(listOf("a", "b", "c"), out)
    }

    @Test
    fun putBackNMostRecentComesFirst() {
        val it = PutBackN(emptyList<Int>().iterator(), SizeHint(0, 0))
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
        val it = PutBackN(listOf(1, 2, 3, 4).iterator(), SizeHint(4, 4))
        assertEquals(SizeHint(4, 4), it.sizeHint())
        it.next()
        assertEquals(SizeHint(3, 3), it.sizeHint())
        it.putBack(99)
        assertEquals(SizeHint(4, 4), it.sizeHint())
        it.putBack(98)
        assertEquals(SizeHint(5, 5), it.sizeHint())
    }

    @Test
    fun putBackNFoldYieldsStackThenSource() {
        val it = PutBackN(listOf(2, 3, 4).iterator(), SizeHint(3, 3))
        it.next()
        it.putBack(1)
        it.putBack(0)
        val out =
            it.fold(mutableListOf<Int>()) { acc, x ->
                acc.add(x)
                acc
            }
        assertEquals(listOf(0, 1, 3, 4), out)
    }

    @Test
    fun putBackNHasNextHandlesEmpty() {
        val it = PutBackN(emptyList<Int>().iterator(), SizeHint(0, 0))
        assertFalse(it.hasNext())
        it.putBack(7)
        assertTrue(it.hasNext())
        assertEquals(7, it.next())
        assertFalse(it.hasNext())
    }
}
