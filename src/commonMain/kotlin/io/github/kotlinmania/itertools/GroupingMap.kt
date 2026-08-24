// port-lint: source grouping_map.rs
package io.github.kotlinmania.itertools

/**
 * `GroupingMap` is an intermediate struct for efficient group-and-fold operations.
 * It groups elements by their key and at the same time folds each group
 * using some aggregating operation.
 */
class GroupingMap<K, V> internal constructor(
    private val iter: Iterator<Pair<K, V>>,
) {
    /**
     * Groups elements from the `GroupingMap` source by key and applies `operation` to the elements
     * of each group sequentially, passing the previously accumulated value, the key,
     * and the current element as arguments, and stores the results in a [Map].
     */
    fun <R> aggregate(operation: (acc: R?, key: K, value: V) -> R?): Map<K, R> {
        val destinationMap = LinkedHashMap<K, R>()
        while (iter.hasNext()) {
            val (key, value) = iter.next()
            val acc = destinationMap.remove(key)
            val opRes = operation(acc, key, value)
            if (opRes != null) {
                destinationMap[key] = opRes
            }
        }
        return destinationMap
    }

    /**
     * Groups elements from the `GroupingMap` source by key and applies `operation` to the elements
     * of each group sequentially, passing the previously accumulated value, the key,
     * and the current element as arguments, and stores the results in a new map.
     *
     * [init] is called to obtain the initial value of each accumulator.
     */
    fun <R> foldWith(init: (key: K, value: V) -> R, operation: (acc: R, key: K, value: V) -> R): Map<K, R> =
        aggregate { acc, key, value ->
            val actualAcc = acc ?: init(key, value)
            operation(actualAcc, key, value)
        }

    /**
     * Groups elements from the `GroupingMap` source by key and applies `operation` to the elements
     * of each group sequentially, passing the previously accumulated value, the key,
     * and the current element as arguments, and stores the results in a new map.
     *
     * [init] is the initial value for each accumulator.
     */
    fun <R> fold(init: R, operation: (acc: R, key: K, value: V) -> R): Map<K, R> =
        foldWith({ _, _ -> init }, operation)

    /**
     * Groups elements from the `GroupingMap` source by key and applies `operation` to the elements
     * of each group sequentially. The initial value of the accumulator is the first element of the group.
     */
    fun reduce(operation: (acc: V, key: K, value: V) -> V): Map<K, V> =
        aggregate { acc, key, value ->
            if (acc != null) operation(acc, key, value) else value
        }

    /**
     * Deprecated alias for [reduce].
     */
    @Deprecated("Use reduce instead", ReplaceWith("reduce(operation)"))
    fun foldFirst(operation: (acc: V, key: K, value: V) -> V): Map<K, V> =
        reduce(operation)

    /**
     * Groups elements from the `GroupingMap` source by key and collects the elements of each group in
     * a [List].
     */
    fun collect(): Map<K, List<V>> {
        val destinationMap = LinkedHashMap<K, MutableList<V>>()
        while (iter.hasNext()) {
            val (key, value) = iter.next()
            destinationMap.getOrPut(key) { mutableListOf() }.add(value)
        }
        return destinationMap
    }

    /**
     * Groups elements from the `GroupingMap` source by key and finds the maximum of each group.
     *
     * If several elements are equally maximum, the last element is picked.
     */
    fun max(comparator: Comparator<in V>): Map<K, V> =
        maxBy { _, a, b -> comparator.compare(a, b) }

    /**
     * Groups elements from the `GroupingMap` source by key and finds the maximum of each group
     * with respect to the specified comparison function.
     *
     * If several elements are equally maximum, the last element is picked.
     */
    fun maxBy(compare: (key: K, a: V, b: V) -> Int): Map<K, V> =
        reduce { acc, key, value ->
            if (compare(key, acc, value) <= 0) value else acc
        }

    /**
     * Groups elements from the `GroupingMap` source by key and finds the element of each group
     * that gives the maximum from the specified function.
     */
    fun <R : Comparable<*>> maxByKey(f: (key: K, value: V) -> R): Map<K, V> =
        maxBy { key, a, b -> compareValues(f(key, a), f(key, b)) }

    /**
     * Groups elements from the `GroupingMap` source by key and finds the minimum of each group.
     *
     * If several elements are equally minimum, the first element is picked.
     */
    fun min(comparator: Comparator<in V>): Map<K, V> =
        minBy { _, a, b -> comparator.compare(a, b) }

    /**
     * Groups elements from the `GroupingMap` source by key and finds the minimum of each group
     * with respect to the specified comparison function.
     *
     * If several elements are equally minimum, the first element is picked.
     */
    fun minBy(compare: (key: K, a: V, b: V) -> Int): Map<K, V> =
        reduce { acc, key, value ->
            if (compare(key, acc, value) > 0) value else acc
        }

    /**
     * Groups elements from the `GroupingMap` source by key and finds the element of each group
     * that gives the minimum from the specified function.
     */
    fun <R : Comparable<*>> minByKey(f: (key: K, value: V) -> R): Map<K, V> =
        minBy { key, a, b -> compareValues(f(key, a), f(key, b)) }

    /**
     * Groups elements from the `GroupingMap` source by key and find the maximum and minimum of
     * each group with respect to the specified comparison function.
     */
    fun minmaxBy(compare: (key: K, a: V, b: V) -> Int): Map<K, MinMaxResult<V>> =
        aggregate { acc, key, value ->
            when (acc) {
                null -> MinMaxResult.OneElement(value)
                is MinMaxResult.OneElement -> {
                    val e = acc.value
                    if (compare(key, value, e) < 0) {
                        MinMaxResult.MinMax(value, e)
                    } else {
                        MinMaxResult.MinMax(e, value)
                    }
                }
                is MinMaxResult.MinMax -> {
                    val min = acc.min
                    val max = acc.max
                    if (compare(key, value, min) < 0) {
                        MinMaxResult.MinMax(value, max)
                    } else if (compare(key, value, max) >= 0) {
                        MinMaxResult.MinMax(min, value)
                    } else {
                        MinMaxResult.MinMax(min, max)
                    }
                }
                is MinMaxResult.NoElements -> MinMaxResult.OneElement(value)
            }
        }

    /**
     * Groups elements from the `GroupingMap` source by key and find the elements of each group
     * that gives the minimum and maximum from the specified function.
     */
    fun <R : Comparable<*>> minmaxByKey(f: (key: K, value: V) -> R): Map<K, MinMaxResult<V>> =
        minmaxBy { key, a, b -> compareValues(f(key, a), f(key, b)) }

    /**
     * Groups elements from the `GroupingMap` source by key and sums them using [plus].
     */
    fun sum(plus: (V, V) -> V): Map<K, V> =
        reduce { acc, _, value -> plus(acc, value) }

    /**
     * Groups elements from the `GroupingMap` source by key and multiplies them using [times].
     */
    fun product(times: (V, V) -> V): Map<K, V> =
        reduce { acc, _, value -> times(acc, value) }
}

/**
 * Convenience method for [Comparable] elements.
 */
fun <K, V : Comparable<V>> GroupingMap<K, V>.max(): Map<K, V> =
    maxBy { _, a, b -> a.compareTo(b) }

/**
 * Convenience method for [Comparable] elements.
 */
fun <K, V : Comparable<V>> GroupingMap<K, V>.min(): Map<K, V> =
    minBy { _, a, b -> a.compareTo(b) }

/**
 * Convenience method for [Comparable] elements.
 */
fun <K, V : Comparable<V>> GroupingMap<K, V>.minmax(): Map<K, MinMaxResult<V>> =
    minmaxBy { _, a, b -> a.compareTo(b) }

/**
 * Creates a new [GroupingMap] from an iterator of pairs.
 */
fun <K, V> groupingMap(iter: Iterator<Pair<K, V>>): GroupingMap<K, V> =
    GroupingMap(iter)

/**
 * Creates a new [GroupingMap] from [iter].
 */
fun <K, V> new(iter: Iterator<Pair<K, V>>): GroupingMap<K, V> =
    GroupingMap(iter)

/**
 * Creates a new [GroupingMap] from an iterable of pairs.
 */
fun <K, V> groupingMap(iterable: Iterable<Pair<K, V>>): GroupingMap<K, V> =
    GroupingMap(iterable.iterator())

/**
 * A wrapper function for [GroupingMap].
 */
class GroupingMapFn<K, V>(val keyMapper: (V) -> K) {
    fun call(v: V): Pair<K, V> = keyMapper(v) to v
}

typealias MapForGrouping<K, V> = Iterator<Pair<K, V>>
typealias GroupingMapBy<K, V> = GroupingMap<K, V>

internal fun <K, V> newMapForGrouping(iter: Iterator<V>, keyMapper: (V) -> K): Iterator<Pair<K, V>> {
    val fn = GroupingMapFn(keyMapper)
    return object : Iterator<Pair<K, V>> {
        override fun hasNext(): Boolean = iter.hasNext()
        override fun next(): Pair<K, V> = fn.call(iter.next())
    }
}

/**
 * Groups elements of this iterable by [keyMapper] into a [GroupingMap].
 */
fun <T, K> Iterable<T>.intoGroupingMapBy(keyMapper: (T) -> K): GroupingMap<K, T> =
    GroupingMap(newMapForGrouping(this.iterator(), keyMapper))

/**
 * Groups elements of this iterator by [keyMapper] into a [GroupingMap].
 */
fun <T, K> Iterator<T>.intoGroupingMapBy(keyMapper: (T) -> K): GroupingMap<K, T> =
    GroupingMap(newMapForGrouping(this, keyMapper))

/**
 * Creates a [GroupingMap] from an iterator of pairs.
 */
fun <K, V> Iterator<Pair<K, V>>.intoGroupingMap(): GroupingMap<K, V> =
    GroupingMap(this)

/**
 * Creates a [GroupingMap] from an iterable of pairs.
 */
fun <K, V> Iterable<Pair<K, V>>.intoGroupingMap(): GroupingMap<K, V> =
    GroupingMap(iterator())

