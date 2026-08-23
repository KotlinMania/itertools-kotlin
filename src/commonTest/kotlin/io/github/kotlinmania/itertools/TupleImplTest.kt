// port-lint: source src/tuple_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class TupleImplTest {
    @Test
    fun testTuples2() {
        val list = listOf(1, 2, 3, 4, 5)
        val t2 = tuples2(list)
        val pairs = mutableListOf<Pair<Int, Int>>()
        while (t2.hasNext()) {
            pairs.add(t2.next())
        }
        assertEquals(listOf(Pair(1, 2), Pair(3, 4)), pairs)
        assertEquals(listOf(5), t2.intoBuffer())
    }

    @Test
    fun testTuples3() {
        val list = listOf(1, 2, 3, 4, 5, 6, 7)
        val t3 = tuples3(list)
        val triples = mutableListOf<Triple<Int, Int, Int>>()
        while (t3.hasNext()) {
            triples.add(t3.next())
        }
        assertEquals(listOf(Triple(1, 2, 3), Triple(4, 5, 6)), triples)
        assertEquals(listOf(7), t3.intoBuffer())
    }

    @Test
    fun testTupleWindows2() {
        val list = listOf(1, 2, 3, 4)
        val result = tupleWindows2(list).asSequence().toList()
        assertEquals(listOf(Pair(1, 2), Pair(2, 3), Pair(3, 4)), result)
    }
}
