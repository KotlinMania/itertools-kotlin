// port-lint: source src/group_map.rs
package io.github.kotlinmania.itertools

/**
 * Return a `Map` of keys mapped to a list of their corresponding values.
 *
 * See [`intoGroupMap`][Itertools.intoGroupMap] for more information.
 */
internal fun <K, V> intoGroupMap(iter: Iterator<Pair<K, V>>): MutableMap<K, MutableList<V>> {
    val lookup = mutableMapOf<K, MutableList<V>>()

    iter.forEach { (key, value) ->
        lookup.getOrPut(key) { mutableListOf() }.add(value)
    }

    return lookup
}

internal fun <K, V> intoGroupMapBy(iter: Iterator<V>, f: (V) -> K): MutableMap<K, MutableList<V>> =
    intoGroupMap(iter.asSequence().map { v -> f(v) to v }.iterator())
