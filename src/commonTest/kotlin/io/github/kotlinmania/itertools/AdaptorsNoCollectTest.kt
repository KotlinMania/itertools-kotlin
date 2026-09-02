// port-lint: tests tests/adaptors_no_collect.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertTrue

class AdaptorsNoCollectTest {
    class PanickingCounter(var curr: Int = 0, val max: Int = 10_000) : Iterator<Unit> {
        override fun hasNext(): Boolean = true

        override fun next() {
            curr += 1
            if (curr == max) {
                throw IllegalStateException("Input iterator reached maximum of $max suggesting collection by adaptor")
            }
        }
    }

    private fun <T> noCollectTest(toAdaptor: (PanickingCounter) -> Iterator<T>) {
        val counter = PanickingCounter()
        val adaptor = toAdaptor(counter)
        var count = 0
        while (count < 5 && adaptor.hasNext()) {
            adaptor.next()
            count++
        }
        assertTrue(count == 5)
    }

    @Test
    fun combinationsWithReplacementNoCollect() {
        noCollectTest { iter -> combinationsWithReplacement(iter, 5) }
    }
}
