// port-lint: tests itertools/src/next_array.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFails
import kotlin.test.assertNull

class NextArrayTest {
    @Test
    fun zeroLenTake() {
        val builder = ArrayBuilder<Unit>(0)
        val taken = builder.take()
        assertEquals(emptyList(), taken)
    }

    @Test
    fun zeroLenPush() {
        val builder = ArrayBuilder<Unit>(0)
        assertFails {
            builder.push(Unit)
        }
    }

    @Test
    fun push4() {
        val builder = ArrayBuilder<Unit>(4)
        assertNull(builder.take())

        builder.push(Unit)
        assertNull(builder.take())

        builder.push(Unit)
        assertNull(builder.take())

        builder.push(Unit)
        assertNull(builder.take())

        builder.push(Unit)
        assertEquals(listOf(Unit, Unit, Unit, Unit), builder.take())
    }

    // The upstream tracked drop test exercises per-element destruction
    // semantics in uninitialized buffers, including the safety promise that
    // partially-written elements are still destroyed when the error case unwinds.
    // Kotlin has no analog: memory management reclaims buffer contents wholesale,
    // and there is no per-element drop hook. The upstream invariant being tested
    // does not exist in Kotlin, so no faithful port is possible.
}
