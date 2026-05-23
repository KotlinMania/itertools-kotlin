// port-lint: source src/sources.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SourcesTest {

    @Test
    fun iterateProducesExpectedPrefix() {
        val iter = iterate(1) { x -> x % 3 + 1 }
        val first5 = mutableListOf<Int>()
        repeat(5) { first5.add(iter.next()) }
        assertEquals(listOf(1, 2, 3, 1, 2), first5)
    }

    @Test
    fun iterateHasNextIsAlwaysTrue() {
        val iter = iterate(0) { x -> x + 1 }
        assertTrue(iter.hasNext())
        iter.next()
        iter.next()
        iter.next()
        assertTrue(iter.hasNext())
    }

    @Test
    fun iterateAdvancesStateOneAhead() {
        val iter = iterate(25) { x -> x - 10 }
        assertEquals(25, iter.next())
        assertEquals(15, iter.state)
        assertEquals(15, iter.next())
        assertEquals(5, iter.state)
        assertEquals(5, iter.next())
        assertEquals(-5, iter.state)
    }

    @Test
    fun iterateSizeHintIsMaxAndUnbounded() {
        val iter = iterate(0) { x -> x + 1 }
        val sh = iter.sizeHint()
        assertEquals(Int.MAX_VALUE, sh.first)
        assertEquals(null, sh.second)
    }
}
