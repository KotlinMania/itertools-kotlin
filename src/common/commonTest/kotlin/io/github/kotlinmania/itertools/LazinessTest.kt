// port-lint: tests tests/laziness.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.batching
import io.github.kotlinmania.itertools.adaptors.positions
import io.github.kotlinmania.itertools.adaptors.update
import kotlin.test.Test

class LazinessTest {
    class Panicking : Iterator<Int> {
        override fun hasNext(): Boolean = true

        override fun next(): Int = throw NoSuchElementException("iterator adaptor is not lazy")
    }

    @Test
    fun testLaziness() {
        // Construct adaptors without evaluating next(), verifying lazy instantiation
        val panicking: Iterator<Int> = Panicking()

        intersperse(panicking, 0)
        intersperseWith(panicking) { 0 }
        zipLongest(panicking, panicking)
        zipEq(panicking, panicking)
        batching(panicking) { if (it.hasNext()) it.next() else null }
        tupleWindows2(panicking)
        tupleWindows3(panicking)
        tupleWindows4(panicking)
        tuples2(panicking)
        tuples3(panicking)
        tuples4(panicking)
        positions(panicking) { it > 0 }
        update(panicking) {}
    }
}
