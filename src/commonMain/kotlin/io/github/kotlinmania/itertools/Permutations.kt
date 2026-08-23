// port-lint: source permutations.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor that iterates through all the `k`-permutations of the
 * elements from an iterator.
 *
 * See [permutations] for more information.
 */
class Permutations<T>(
    iter: Iterator<T>,
    k: Int,
    sourceHint: SizeHint = SizeHint(0, null),
) : Iterator<List<T>> {
    private val vals: LazyBuffer<T> = LazyBuffer(iter, sourceHint)
    private var state: PermutationState = PermutationState.Start(k)
    private var nextItem: List<T>? = null
    private var isExhausted: Boolean = false

    private sealed class PermutationState {
        data class Start(
            val k: Int,
        ) : PermutationState()

        data class Buffered(
            val k: Int,
            var minN: Int,
        ) : PermutationState()

        data class Loaded(
            val indices: IntArray,
            val cycles: IntArray,
        ) : PermutationState()

        object End : PermutationState()
    }

    private fun produceNext(): List<T>? {
        return when (val s = state) {
            is PermutationState.Start -> {
                if (s.k == 0) {
                    state = PermutationState.End
                    emptyList()
                } else {
                    vals.prefill(s.k)
                    if (vals.length < s.k) {
                        state = PermutationState.End
                        null
                    } else {
                        state = PermutationState.Buffered(s.k, s.k)
                        IntArray(s.k) { it }.map { vals[it] }
                    }
                }
            }
            is PermutationState.Buffered -> {
                if (vals.getNext()) {
                    val k = s.k
                    val minN = s.minN
                    val indices = IntArray(k) { if (it == k - 1) minN else it }
                    s.minN += 1
                    indices.map { vals[it] }
                } else {
                    val n = s.minN
                    val k = s.k
                    val prevIterationCount = n - k + 1
                    val indices = IntArray(n) { it }
                    val cycles = IntArray(k) { n - 1 - it }
                    for (step in 0 until prevIterationCount) {
                        if (advance(indices, cycles)) {
                            state = PermutationState.End
                            return null
                        }
                    }
                    val item = IntArray(k) { indices[it] }.map { vals[it] }
                    state = PermutationState.Loaded(indices, cycles)
                    item
                }
            }
            is PermutationState.Loaded -> {
                if (advance(s.indices, s.cycles)) {
                    state = PermutationState.End
                    null
                } else {
                    val k = s.cycles.size
                    IntArray(k) { s.indices[it] }.map { vals[it] }
                }
            }
            is PermutationState.End -> null
        }
    }

    override fun hasNext(): Boolean {
        if (isExhausted) return false
        if (nextItem != null) return true
        nextItem = produceNext()
        if (nextItem == null) {
            isExhausted = true
            return false
        }
        return true
    }

    override fun next(): List<T> {
        if (!hasNext()) {
            throw NoSuchElementException("Permutations exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("Permutations exhausted")
        nextItem = null
        return item
    }
}

private fun IntArray.rotateLeftInPlace(fromIndex: Int) {
    if (fromIndex >= size - 1) return
    val first = this[fromIndex]
    for (i in fromIndex until size - 1) {
        this[i] = this[i + 1]
    }
    this[size - 1] = first
}

private fun IntArray.swapInPlace(i: Int, j: Int) {
    val temp = this[i]
    this[i] = this[j]
    this[j] = temp
}

private fun advance(indices: IntArray, cycles: IntArray): Boolean {
    val n = indices.size
    val k = cycles.size
    for (i in (k - 1) downTo 0) {
        if (cycles[i] == 0) {
            cycles[i] = n - i - 1
            indices.rotateLeftInPlace(i)
        } else {
            val swapIndex = n - cycles[i]
            indices.swapInPlace(i, swapIndex)
            cycles[i] -= 1
            return false
        }
    }
    return true
}

/**
 * Create a new [Permutations] iterator adaptor from an [Iterable].
 */
fun <T> permutations(iterable: Iterable<T>, k: Int): Permutations<T> {
    val hint =
        when (iterable) {
            is Collection<*> -> SizeHint(iterable.size, iterable.size)
            else -> SizeHint(0, null)
        }
    return Permutations(iterable.iterator(), k, hint)
}

/**
 * Create a new [Permutations] iterator adaptor from an [Iterator].
 */
fun <T> permutations(iter: Iterator<T>, k: Int, hint: SizeHint = SizeHint(0, null)): Permutations<T> =
    Permutations(iter, k, hint)
