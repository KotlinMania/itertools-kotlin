// port-lint: tests tests/tuples.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

class TuplesTest {
    @Test
    fun tuples() {
        val v = listOf(1, 2, 3, 4, 5)

        val iter1 = tuples1(v)
        assertEquals(1, iter1.next())
        assertEquals(2, iter1.next())
        assertEquals(3, iter1.next())
        assertEquals(4, iter1.next())
        assertEquals(5, iter1.next())
        assertFalse(iter1.hasNext())
        assertEquals(emptyList(), iter1.intoBuffer())

        val iter2 = tuples2(v)
        assertEquals(Pair(1, 2), iter2.next())
        assertEquals(Pair(3, 4), iter2.next())
        assertFalse(iter2.hasNext())
        assertEquals(listOf(5), iter2.intoBuffer())

        val iter3 = tuples3(v)
        assertEquals(Triple(1, 2, 3), iter3.next())
        assertFalse(iter3.hasNext())
        assertEquals(listOf(4, 5), iter3.intoBuffer())

        val iter4 = tuples4(v)
        assertEquals(listOf(1, 2, 3, 4), iter4.next())
        assertFalse(iter4.hasNext())
        assertEquals(listOf(5), iter4.intoBuffer())
    }

    @Test
    fun tupleWindows() {
        val v = listOf(1, 2, 3, 4, 5)

        val iter1 = tupleWindows1(v)
        assertEquals(1, iter1.next())
        assertEquals(2, iter1.next())
        assertEquals(3, iter1.next())

        val iter2 = tupleWindows2(v)
        assertEquals(Pair(1, 2), iter2.next())
        assertEquals(Pair(2, 3), iter2.next())
        assertEquals(Pair(3, 4), iter2.next())
        assertEquals(Pair(4, 5), iter2.next())
        assertFalse(iter2.hasNext())

        val iter3 = tupleWindows3(v)
        assertEquals(Triple(1, 2, 3), iter3.next())
        assertEquals(Triple(2, 3, 4), iter3.next())
        assertEquals(Triple(3, 4, 5), iter3.next())
        assertFalse(iter3.hasNext())

        val iter4 = tupleWindows4(v)
        assertEquals(listOf(1, 2, 3, 4), iter4.next())
        assertEquals(listOf(2, 3, 4, 5), iter4.next())
        assertFalse(iter4.hasNext())

        val vShort = listOf(1, 2, 3)
        val iterShort4 = tupleWindows4(vShort)
        assertFalse(iterShort4.hasNext())
    }

    @Test
    fun nextTuple() {
        val v = listOf(1, 2, 3, 4, 5)
        val iter = v.iterator()
        assertEquals(Pair(1, 2), nextTuple2(iter))
        assertEquals(Pair(3, 4), nextTuple2(iter))
        assertEquals(null, nextTuple2(iter))
    }

    @Test
    fun collectTuple() {
        val v2 = listOf(1, 2)
        assertEquals(Pair(1, 2), collectTuple2(v2))

        val v1 = listOf(1)
        assertEquals(null, collectTuple2(v1))

        val v3 = listOf(1, 2, 3)
        assertEquals(null, collectTuple2(v3))
    }
}
