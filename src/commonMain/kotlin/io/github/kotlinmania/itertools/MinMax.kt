// port-lint: source src/minmax.rs
package io.github.kotlinmania.itertools

/**
 * `MinMaxResult` is a sealed type returned by `minmax`.
 *
 * See [Itertools.minmax] for more detail.
 */
internal sealed class MinMaxResult<out T> {
    /** Empty iterator. */
    internal object NoElements : MinMaxResult<Nothing>()

    /** Iterator with one element, so the minimum and maximum are the same. */
    internal data class OneElement<T>(
        val value: T,
    ) : MinMaxResult<T>()

    /**
     * More than one element in the iterator, the first element is not larger
     * than the second.
     */
    internal data class MinMax<T>(
        val min: T,
        val max: T,
    ) : MinMaxResult<T>()

    /**
     * `toOption` creates a nullable `Pair<T, T>`. The returned value is `null`
     * if and only if the [MinMaxResult] is [NoElements]. Otherwise `(x, y)` is
     * returned where `x <= y`. If the [MinMaxResult] is [OneElement] containing
     * `x`, the returned pair references `x` for both positions.
     *
     * # Examples
     *
     * ```
     * val r: MinMaxResult<Int> = MinMaxResult.NoElements
     * check(r.toOption() == null)
     *
     * val r1 = MinMaxResult.OneElement(1)
     * check(r1.toOption() == 1 to 1)
     *
     * val r2 = MinMaxResult.MinMax(1, 2)
     * check(r2.toOption() == 1 to 2)
     * ```
     */
    fun toOption(): Pair<T, T>? =
        when (this) {
            is NoElements -> null
            is OneElement -> value to value
            is MinMax -> min to max
        }
}

/** Implementation guts for `minmax` and `minmaxBy`. */
internal fun <I, K> minmaxImpl(
    it: Iterator<I>,
    keyFor: (I) -> K,
    lt: (I, I, K, K) -> Boolean,
): MinMaxResult<I> {
    if (!it.hasNext()) return MinMaxResult.NoElements
    val first0 = it.next()
    if (!it.hasNext()) return MinMaxResult.OneElement(first0)
    val second0 = it.next()
    val xk0 = keyFor(first0)
    val yk0 = keyFor(second0)
    var min: I
    var max: I
    var minKey: K
    var maxKey: K
    if (!lt(second0, first0, yk0, xk0)) {
        min = first0
        max = second0
        minKey = xk0
        maxKey = yk0
    } else {
        min = second0
        max = first0
        minKey = yk0
        maxKey = xk0
    }

    while (true) {
        // `first` and `second` are the two next elements we want to look
        // at.  We first compare `first` and `second` (#1). The smaller one
        // is then compared to current minimum (#2). The larger one is
        // compared to current maximum (#3). This way we do 3 comparisons
        // for 2 elements.
        if (!it.hasNext()) break
        val first = it.next()
        if (!it.hasNext()) {
            val firstKey = keyFor(first)
            if (lt(first, min, firstKey, minKey)) {
                min = first
            } else if (!lt(first, max, firstKey, maxKey)) {
                max = first
            }
            break
        }
        val second = it.next()
        val firstKey = keyFor(first)
        val secondKey = keyFor(second)
        if (!lt(second, first, secondKey, firstKey)) {
            if (lt(first, min, firstKey, minKey)) {
                min = first
                minKey = firstKey
            }
            if (!lt(second, max, secondKey, maxKey)) {
                max = second
                maxKey = secondKey
            }
        } else {
            if (lt(second, min, secondKey, minKey)) {
                min = second
                minKey = secondKey
            }
            if (!lt(first, max, firstKey, maxKey)) {
                max = first
                maxKey = firstKey
            }
        }
    }

    return MinMaxResult.MinMax(min, max)
}
