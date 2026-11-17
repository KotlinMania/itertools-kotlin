// port-lint: tests cons_tuples_impl.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals

class ConsTuplesImplTest {
    @Test
    fun testConsTuples() {
        val input =
            listOf(
                Pair(Pair(1, "a"), true),
                Pair(Pair(2, "b"), false),
            )
        val result = consTuples(input).asSequence().toList()
        assertEquals(listOf(Triple(1, "a", true), Triple(2, "b", false)), result)
    }
}
