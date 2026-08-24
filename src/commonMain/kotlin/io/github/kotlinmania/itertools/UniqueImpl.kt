// port-lint: source unique_impl.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.uniqueBy` for more information.
 */
internal class UniqueBy<T, V> internal constructor(
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

    /**
     * Returns the size hint for the iterator. The lower bound is `1` only
     * when the source has at least one element to yield AND the dedup set is
     * empty — that first element is guaranteed unique. The upper bound is
     * inherited from the source: every source element could in principle be
     * unique.
     */
    fun sizeHint(): SizeHint {
        val newLow = if (sourceHint.lower > 0 && used.isEmpty()) 1 else 0
        return SizeHint(newLow, sourceHint.upper)
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
internal fun <T, V> uniqueBy(iter: Iterator<T>, sourceHint: SizeHint = SizeHint(0, null), f: (T) -> V): UniqueBy<T, V> =
    UniqueBy(iter, sourceHint, f)

/** Filter duplicate elements from [iterable], comparing by the key produced by [f]. */
fun <T, V> uniqueBy(iterable: Iterable<T>, f: (T) -> V): Iterator<T> =
    uniqueBy(iterable.iterator(), hintOfIterable(iterable), f)

/**
 * An iterator adapter to filter out duplicate elements.
 *
 * See `Itertools.unique` for more information.
 */
internal typealias Unique<T> = UniqueBy<T, T>

/** Create a new `Unique` iterator. */
internal fun <T> unique(iter: Iterator<T>, sourceHint: SizeHint = SizeHint(0, null)): Unique<T> =
    UniqueBy(iter, sourceHint) { it }

/** Filter duplicate elements from [iterable], comparing by identity. */
fun <T> unique(iterable: Iterable<T>): Iterator<T> =
    unique(iterable.iterator(), hintOfIterable(iterable))

private fun hintOfIterable(it: Iterable<*>): SizeHint =
    when (it) {
        is Collection<*> -> SizeHint(it.size, it.size)
        else -> SizeHint(0, null)
    }
