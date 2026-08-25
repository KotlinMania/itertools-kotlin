// port-lint: source unique_impl.rs
package io.github.kotlinmania.itertools

import kotlin.jvm.JvmName

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.uniqueBy` for more information.
 */
public class UniqueBy<T, V> internal constructor(
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint,
    internal val f: (T) -> V,
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

    /** Equivalent to upstream size hint. */
    fun sizeHint(): SizeHint {
        val (lower, upper) = sourceHint
        val newLow = if (lower > 0 && used.isEmpty()) 1 else 0
        return SizeHint(newLow, upper)
    }

    /**
     * Counts remaining unique elements.
     */
    fun count(): Int {
        val currentUsed = used.size
        while (iter.hasNext()) {
            used[f(iter.next())] = Unit
        }
        return used.size - currentUsed + buffered.size
    }

    /**
     * Returns the previous unique element from the back if supported.
     */
    fun nextBack(): T? {
        return null
    }

    companion object {
        fun <T, V> new(iter: Iterator<T>, sourceHint: SizeHint = SizeHint(0, null), f: (T) -> V): UniqueBy<T, V> =
            UniqueBy(iter, sourceHint, f)
    }
}

/** Count the number of new unique keys in iterable (`used` is the set already seen). */
internal fun <K> countNewKeys(used: HashMap<K, Unit>, iterable: Iterable<K>): Int {
    val currentUsed = used.size
    for (key in iterable) {
        used[key] = Unit
    }
    return used.size - currentUsed
}

/** Create a new `UniqueBy` iterator. */
@JvmName("uniqueByIter")
public fun <T, V> uniqueBy(iter: Iterator<T>, sourceHint: SizeHint = SizeHint(0, null), f: (T) -> V): UniqueBy<T, V> =
    UniqueBy(iter, sourceHint, f)

/** Filter duplicate elements from [iterable], comparing by the key produced by [f]. */
@JvmName("uniqueByIterable")
public fun <T, V> uniqueBy(iterable: Iterable<T>, f: (T) -> V): UniqueBy<T, V> =
    uniqueBy(iterable.iterator(), hintOfIterable(iterable), f)

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.unique` for more information.
 */
public typealias Unique<T> = UniqueBy<T, T>

/** Create a new `Unique` iterator. */
@JvmName("uniqueIter")
public fun <T> unique(iter: Iterator<T>, sourceHint: SizeHint = SizeHint(0, null)): Unique<T> =
    UniqueBy(iter, sourceHint) { it }

/** Filter duplicate elements from [iterable], comparing by identity. */
@JvmName("uniqueIterable")
public fun <T> unique(iterable: Iterable<T>): Unique<T> =
    unique(iterable.iterator(), hintOfIterable(iterable))

/**
 * Filter duplicate elements from this iterator, comparing by the key produced by [f].
 */
public fun <T, V> Iterator<T>.uniqueBy(f: (T) -> V): UniqueBy<T, V> =
    uniqueBy(this, SizeHint(0, null), f)

/**
 * Filter duplicate elements from this iterable, comparing by the key produced by [f].
 */
public fun <T, V> Iterable<T>.uniqueBy(f: (T) -> V): UniqueBy<T, V> =
    uniqueBy(this, f)

/**
 * Filter duplicate elements from this iterator, comparing by identity.
 */
public fun <T> Iterator<T>.unique(): Unique<T> =
    unique(this)

/**
 * Filter duplicate elements from this iterable, comparing by identity.
 */
public fun <T> Iterable<T>.unique(): Unique<T> =
    unique(this)

private fun hintOfIterable(it: Iterable<*>): SizeHint =
    when (it) {
        is Collection<*> -> SizeHint(it.size, it.size)
        else -> SizeHint(0, null)
    }
