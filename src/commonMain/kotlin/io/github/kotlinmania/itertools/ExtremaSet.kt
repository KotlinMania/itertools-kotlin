// port-lint: source src/extrema_set.rs
package io.github.kotlinmania.itertools

/** Implementation guts for `minSet`, `minSetBy`, and `minSetByKey`. */
internal fun <T, K> minSetImpl(
    it: Iterator<T>,
    keyFor: (T) -> K,
    compare: (T, T, K, K) -> Int,
): MutableList<T> {
    if (!it.hasNext()) return mutableListOf()
    val first = it.next()
    var currentKey = keyFor(first)
    val result = mutableListOf(first)
    it.forEach { element ->
        val key = keyFor(element)
        val ordering = compare(element, result[0], key, currentKey)
        when {
            ordering < 0 -> {
                result.clear()
                result.add(element)
                currentKey = key
            }
            ordering == 0 -> {
                result.add(element)
            }
            else -> {}
        }
    }
    return result
}

/** Implementation guts for `maxSet`, `maxSetBy`, and `maxSetByKey`. */
internal fun <T, K> maxSetImpl(
    it: Iterator<T>,
    keyFor: (T) -> K,
    compare: (T, T, K, K) -> Int,
): MutableList<T> =
    minSetImpl(it, keyFor) { it1, it2, key1, key2 ->
        compare(it2, it1, key2, key1)
    }
