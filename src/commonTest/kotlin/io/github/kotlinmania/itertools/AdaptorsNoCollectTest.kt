// port-lint: tests tests/adaptors_no_collect.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertNotEquals

class AdaptorsNoCollectTest {
    class PanickingCounter(
        var curr: Int = 0,
        val max: Int = 10_000,
    ) : Iterator<Unit> {
        override fun hasNext(): Boolean = true

        override fun next() {
            if (!hasNext()) {
                throw NoSuchElementException("PanickingCounter exhausted")
            }
            curr += 1
            assertNotEquals(
                max,
                curr,
                "Input iterator reached maximum of $max suggesting collection by adaptor",
            )
        }
    }

    private fun <A> noCollectTest(toAdaptor: (Iterator<Unit>) -> Iterator<A>) {
        val counter = PanickingCounter(curr = 0, max = 10_000)
        val adaptor = toAdaptor(counter)
        for (i in 0 until 5) {
            if (adaptor.hasNext()) {
                adaptor.next()
            }
        }
    }

    @Test
    fun permutationsNoCollect() {
        noCollectTest { iter -> permutations(iter, 5) }
    }

    @Test
    fun combinationsNoCollect() {
        noCollectTest { iter -> combinations(iter, 5) }
    }

    @Test
    fun combinationsWithReplacementNoCollect() {
        noCollectTest { iter -> combinationsWithReplacement(iter, 5) }
    }
}
