// port-lint: source src/next_array.rs (#[cfg(test)] mod test)
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

    // The upstream `tracked_drop` test exercises Rust's per-element `Drop`
    // semantics in `MaybeUninit<T>`, including the safety promise that
    // partially-written elements are still destroyed when the panic-on-push
    // case unwinds. Kotlin has no analog: GC reclaims `MutableList<T>`
    // contents wholesale, and there is no per-element drop hook. The
    // upstream invariant being tested simply does not exist here, so no
    // faithful port of `tracked_drop` is possible.
}
