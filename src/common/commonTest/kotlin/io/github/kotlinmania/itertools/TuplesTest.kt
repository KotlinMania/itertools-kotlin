// port-lint: tests tests/tuples.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class TuplesTest {
    @Test
    fun tuples() {
        val v = listOf(1, 2, 3, 4, 5)

        val iter1 = Tuples(v.iterator(), 1)
        assertEquals(listOf(1), iter1.next())
        assertEquals(listOf(2), iter1.next())
        assertEquals(listOf(3), iter1.next())
        assertEquals(listOf(4), iter1.next())
        assertEquals(listOf(5), iter1.next())
        assertFalse(iter1.hasNext())
        assertFalse(iter1.intoBuffer().hasNext())

        val iter2 = Tuples2(v.iterator())
        assertEquals(Pair(1, 2), iter2.next())
        assertEquals(Pair(3, 4), iter2.next())
        assertFalse(iter2.hasNext())
        assertEquals(listOf(5), iter2.intoBuffer())

        val iter3 = Tuples3(v.iterator())
        assertEquals(Triple(1, 2, 3), iter3.next())
        assertFalse(iter3.hasNext())
        assertEquals(listOf(4, 5), iter3.intoBuffer())

        val iter4 = Tuples4(v.iterator())
        assertEquals(listOf(1, 2, 3, 4), iter4.next())
        assertFalse(iter4.hasNext())
        assertEquals(listOf(5), iter4.intoBuffer())
    }

    @Test
    fun tupleWindows() {
        val v = listOf(1, 2, 3, 4, 5)

        val iter1 = TupleWindows1(v.iterator())
        assertEquals(1, iter1.next())
        assertEquals(2, iter1.next())
        assertEquals(3, iter1.next())

        val iter2 = TupleWindows2(v.iterator())
        assertEquals(Pair(1, 2), iter2.next())
        assertEquals(Pair(2, 3), iter2.next())
        assertEquals(Pair(3, 4), iter2.next())
        assertEquals(Pair(4, 5), iter2.next())
        assertFalse(iter2.hasNext())

        val iter3 = TupleWindows3(v.iterator())
        assertEquals(Triple(1, 2, 3), iter3.next())
        assertEquals(Triple(2, 3, 4), iter3.next())
        assertEquals(Triple(3, 4, 5), iter3.next())
        assertFalse(iter3.hasNext())

        val iter4 = TupleWindows4(v.iterator())
        assertEquals(listOf(1, 2, 3, 4), iter4.next())
        assertEquals(listOf(2, 3, 4, 5), iter4.next())
        assertFalse(iter4.hasNext())

        val vSmall = listOf(1, 2, 3)
        val iterSmall = TupleWindows4(vSmall.iterator())
        assertFalse(iterSmall.hasNext())
    }

    @Test
    fun nextTuple() {
        val v = listOf(1, 2, 3, 4, 5)
        val iter = v.iterator()
        assertEquals(Pair(1, 2), nextTuple2(iter))
        assertEquals(Pair(3, 4), nextTuple2(iter))
        assertNull(nextTuple2(iter))
    }

    @Test
    fun collectTuple() {
        val v = listOf(1, 2)
        assertEquals(Pair(1, 2), collectTuple2(v))

        val v1 = listOf(1)
        assertNull(collectTuple2(v1))

        val v3 = listOf(1, 2, 3)
        assertNull(collectTuple2(v3))
    }
}
