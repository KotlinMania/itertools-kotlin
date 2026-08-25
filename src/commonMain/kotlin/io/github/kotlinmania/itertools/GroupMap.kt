// port-lint: source group_map.rs
package io.github.kotlinmania.itertools

import kotlin.jvm.JvmName

/**
 * Return a `Map` of keys mapped to a list of their corresponding values.
 *
 * See [`intoGroupMap`][Itertools.intoGroupMap] for more information.
 */
@JvmName("internalIntoGroupMap")
internal fun <K, V> intoGroupMap(iter: Iterator<Pair<K, V>>): MutableMap<K, MutableList<V>> {
    val lookup = mutableMapOf<K, MutableList<V>>()

    iter.forEach { (key, value) ->
        lookup.getOrPut(key) { mutableListOf() }.add(value)
    }

    return lookup
}

@JvmName("internalIntoGroupMapBy")
internal fun <K, V> intoGroupMapBy(iter: Iterator<V>, f: (V) -> K): MutableMap<K, MutableList<V>> =
    intoGroupMap(iter.asSequence().map { v -> f(v) to v }.iterator())

/**
 * Return a `Map` of keys mapped to a list of their corresponding values.
 */
public fun <K, V> Iterator<Pair<K, V>>.intoGroupMap(): Map<K, List<V>> =
    intoGroupMap(this)

/**
 * Return a `Map` of keys mapped to a list of their corresponding values.
 */
public fun <K, V> Iterable<Pair<K, V>>.intoGroupMap(): Map<K, List<V>> =
    intoGroupMap(iterator())

/**
 * Return a `Map` of keys mapped to a list of their corresponding values, grouping by the key produced by [f].
 */
public fun <K, V> Iterator<V>.intoGroupMapBy(f: (V) -> K): Map<K, List<V>> =
    intoGroupMapBy(this, f)

/**
 * Return a `Map` of keys mapped to a list of their corresponding values, grouping by the key produced by [f].
 */
public fun <K, V> Iterable<V>.intoGroupMapBy(f: (V) -> K): Map<K, List<V>> =
    intoGroupMapBy(iterator(), f)
