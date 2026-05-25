// port-lint: source src/unique_impl.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.itertools

import kotlin.native.HiddenFromObjC

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.uniqueBy` for more information.
 *
 * Hidden from the Swift Export bridge: the plugin would otherwise erase `T`
 * and `V` to `Any?` and emit unchecked casts inside the generated bridge
 * file. The Kotlin surface stays strongly typed.
 */
@HiddenFromObjC
class UniqueBy<T, V> internal constructor(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint,
    private val f: (T) -> V,
) : Iterator<T> {
    internal val used: HashMap<V, Unit> = HashMap()
    private val buffered: ArrayDeque<T> = ArrayDeque()
    private var sourceDrained: Boolean = false

    /** Drives the source iterator until either a unique element is buffered or the source is drained. */
    private fun pump(): Boolean {
        if (buffered.isNotEmpty()) return true
        if (sourceDrained) return false
        while (iter.hasNext()) {
            val v = iter.next()
            val key = f(v)
            if (used.put(key, Unit) == null) {
                buffered.addLast(v)
                return true
            }
        }
        sourceDrained = true
        return false
    }

    override fun hasNext(): Boolean = pump()

    override fun next(): T {
        if (!pump()) throw NoSuchElementException()
        return buffered.removeFirst()
    }

    /**
     * Equivalent to upstream `Iterator::size_hint`. The lower bound is `1` only
     * when the source has at least one element to yield AND the dedup set is
     * empty — that first element is guaranteed unique. The upper bound is
     * inherited from the source: every source element could in principle be
     * unique.
     */
    fun sizeHint(): SizeHint {
        val (low, hi) = sourceHint
        val newLow = if (low > 0 && used.isEmpty()) 1 else 0
        return newLow to hi
    }
}

/** Create a new `UniqueBy` iterator. */
fun <T, V> uniqueBy(iter: Iterator<T>, sourceHint: SizeHint = 0 to null, f: (T) -> V): UniqueBy<T, V> =
    UniqueBy(iter, sourceHint, f)

/** Convenience overload that derives a size hint from [iterable]. */
fun <T, V> uniqueBy(iterable: Iterable<T>, f: (T) -> V): UniqueBy<T, V> =
    uniqueBy(iterable.iterator(), hintOfIterable(iterable), f)

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.unique` for more information.
 *
 * In the upstream Rust crate this is `Unique<I>` wrapping
 * `UniqueBy<I, I::Item, ()>` so the same hash bookkeeping is reused with the
 * key function fixed to the identity. The Kotlin port collapses the two by
 * exposing [unique] as a [UniqueBy] specialization with `f = { it }`.
 */
typealias Unique<T> = UniqueBy<T, T>

/** Create a new `Unique` iterator. */
fun <T> unique(iter: Iterator<T>, sourceHint: SizeHint = 0 to null): Unique<T> =
    UniqueBy(iter, sourceHint) { it }

/** Convenience overload that derives a size hint from [iterable]. */
fun <T> unique(iterable: Iterable<T>): Unique<T> =
    unique(iterable.iterator(), hintOfIterable(iterable))

private fun hintOfIterable(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> it.size to it.size
    else -> 0 to null
}
