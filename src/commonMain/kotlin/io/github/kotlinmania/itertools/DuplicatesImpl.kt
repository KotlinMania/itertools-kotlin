// port-lint: source src/duplicates_impl.rs
package io.github.kotlinmania.itertools

/**
 * Holds a key together with the value it was derived from, and yields one or
 * the other but never both. Models the `KeyXorValue<K, V>` trait from the
 * upstream `private` module.
 *
 * Internal to this file: matches the upstream `mod private { … }` visibility.
 */
internal interface KeyXorValue<K, V> {
    fun keyRef(): K
    fun key(): K
    fun value(): V
}

/** `KeyXorValue` impl for the `(K, V)` shape — `KeyValue<K, V>` upstream. */
internal class KeyValue<K, V>(private val k: K, private val v: V) : KeyXorValue<K, V> {
    override fun keyRef(): K = k
    override fun key(): K = k
    override fun value(): V = v
}

/**
 * `KeyXorValue` impl for the case where the key *is* the value — `JustValue<V>`
 * upstream. In Rust the implementor takes `self` by value and may produce the
 * same field as both key and value; in Kotlin we hand the same reference back
 * twice, which is equivalent for the immutable element types this adaptor is
 * meant for.
 */
internal class JustValue<V>(private val v: V) : KeyXorValue<V, V> {
    override fun keyRef(): V = v
    override fun key(): V = v
    override fun value(): V = v
}

/** A keying method for use with [DuplicatesBy]. */
internal interface KeyMethod<K, V> {
    fun make(value: V): KeyXorValue<K, V>
}

/** Apply the identity function to elements before checking them for equality. */
internal class ById<V> : KeyMethod<V, V> {
    override fun make(value: V): KeyXorValue<V, V> = JustValue(value)
}

/** Apply a user-supplied function to elements before checking them for equality. */
internal class ByFn<K, V>(private val f: (V) -> K) : KeyMethod<K, V> {
    override fun make(value: V): KeyXorValue<K, V> = KeyValue(f(value), value)
}

/**
 * Per-key bookkeeping used by [DuplicatesBy].
 *
 * `used` records every key seen so far. `false` means we have observed the key
 * exactly once and are waiting for the second occurrence to emit; `true` means
 * the second occurrence has been emitted already and any further occurrences
 * are suppressed. `pending` counts the still-unmatched first occurrences and
 * drives the upper bound of [DuplicatesBy.sizeHint].
 */
internal class Meta<K, V>(
    private val keyMethod: KeyMethod<K, V>,
) {
    internal val used: HashMap<K, Boolean> = HashMap()
    internal var pending: Int = 0

    /**
     * Takes an item and returns it back to the caller wrapped in a one-element
     * [ArrayDeque] if it is the second time we see it. Otherwise the item is
     * consumed and an empty deque is returned.
     *
     * Returning a deque (rather than `V?`) keeps the contract honest when `V`
     * itself is a nullable type: a present `null` element is not the same as
     * "no element."
     */
    fun filter(item: V): ArrayDeque<V> {
        val kv = keyMethod.make(item)
        val produced = used[kv.keyRef()]
        return when (produced) {
            null -> {
                used[kv.key()] = false
                pending += 1
                ArrayDeque()
            }
            true -> ArrayDeque()
            false -> {
                used[kv.keyRef()] = true
                pending -= 1
                ArrayDeque<V>().apply { addLast(kv.value()) }
            }
        }
    }
}

/**
 * An iterator adapter to filter for duplicate elements.
 *
 * See `Itertools.duplicatesBy` for more information.
 */
class DuplicatesBy<T, K> internal constructor(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint,
    keyMethod: KeyMethod<K, T>,
) : Iterator<T> {
    internal val meta: Meta<K, T> = Meta(keyMethod)
    private val buffered: ArrayDeque<T> = ArrayDeque()
    private var iterConsumed: Int = 0

    /** Drives the source iterator until either a duplicate is buffered or the source is drained. */
    private fun pump(): Boolean {
        if (buffered.isNotEmpty()) return true
        while (iter.hasNext()) {
            val v = iter.next()
            iterConsumed += 1
            val emitted = meta.filter(v)
            if (emitted.isNotEmpty()) {
                buffered.addLast(emitted.removeFirst())
                return true
            }
        }
        return false
    }

    override fun hasNext(): Boolean = pump()

    override fun next(): T {
        if (!pump()) throw NoSuchElementException()
        return buffered.removeFirst()
    }

    /**
     * Equivalent to upstream `Iterator::size_hint`. The lower bound is always 0
     * because we may only encounter unique items from now on; the upper bound
     * collapses based on how many first-sightings are still pending.
     */
    fun sizeHint(): SizeHint {
        val iterRemainingHi = sourceHint.second?.let { hi ->
            val remaining = hi - iterConsumed
            if (remaining < 0) 0 else remaining
        }
        val hi = iterRemainingHi?.let { remaining ->
            if (remaining <= meta.pending) {
                // fewer or equally many iter-remaining elements than pending
                // elements => at most, each iter-remaining element is matched
                remaining
            } else {
                // fewer pending elements than iter-remaining elements
                // => at most:
                //    * each pending element is matched
                //    * the other iter-remaining elements come in pairs
                meta.pending + (remaining - meta.pending) / 2
            }
        }
        return 0 to hi
    }
}

/** Create a new `DuplicatesBy` iterator. */
fun <T, K> duplicatesBy(iter: Iterator<T>, sourceHint: SizeHint = 0 to null, f: (T) -> K): DuplicatesBy<T, K> =
    DuplicatesBy(iter, sourceHint, ByFn(f))

/** Convenience overload that derives a size hint from [iterable]. */
fun <T, K> duplicatesBy(iterable: Iterable<T>, f: (T) -> K): DuplicatesBy<T, K> =
    duplicatesBy(iterable.iterator(), hintOfIterable(iterable), f)

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.duplicates` for more information.
 */
typealias Duplicates<T> = DuplicatesBy<T, T>

/** Create a new `Duplicates` iterator. */
fun <T> duplicates(iter: Iterator<T>, sourceHint: SizeHint = 0 to null): Duplicates<T> =
    DuplicatesBy(iter, sourceHint, ById())

/** Convenience overload that derives a size hint from [iterable]. */
fun <T> duplicates(iterable: Iterable<T>): Duplicates<T> =
    duplicates(iterable.iterator(), hintOfIterable(iterable))

private fun hintOfIterable(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> it.size to it.size
    else -> 0 to null
}
