// port-lint: source extrema_set.rs
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

/**
 * Return all minimum elements of an iterator.
 */
fun <T : Comparable<T>> Iterator<T>.minSet(): List<T> =
    minSetImpl(this, { }) { x, y, _, _ -> x.compareTo(y) }

/**
 * Return all minimum elements of an iterable.
 */
fun <T : Comparable<T>> Iterable<T>.minSet(): List<T> =
    iterator().minSet()

/**
 * Return all minimum elements of an iterator, as determined by the comparison function.
 */
fun <T> Iterator<T>.minSetBy(compare: (T, T) -> Int): List<T> =
    minSetImpl(this, { }) { x, y, _, _ -> compare(x, y) }

/**
 * Return all minimum elements of an iterable, as determined by the comparison function.
 */
fun <T> Iterable<T>.minSetBy(compare: (T, T) -> Int): List<T> =
    iterator().minSetBy(compare)

/**
 * Return all minimum elements of an iterator, as determined by the key function.
 */
fun <T, K : Comparable<K>> Iterator<T>.minSetByKey(key: (T) -> K): List<T> =
    minSetImpl(this, key) { _, _, kx, ky -> kx.compareTo(ky) }

/**
 * Return all minimum elements of an iterable, as determined by the key function.
 */
fun <T, K : Comparable<K>> Iterable<T>.minSetByKey(key: (T) -> K): List<T> =
    iterator().minSetByKey(key)

/**
 * Return all maximum elements of an iterator.
 */
fun <T : Comparable<T>> Iterator<T>.maxSet(): List<T> =
    maxSetImpl(this, { }) { x, y, _, _ -> x.compareTo(y) }

/**
 * Return all maximum elements of an iterable.
 */
fun <T : Comparable<T>> Iterable<T>.maxSet(): List<T> =
    iterator().maxSet()

/**
 * Return all maximum elements of an iterator, as determined by the comparison function.
 */
fun <T> Iterator<T>.maxSetBy(compare: (T, T) -> Int): List<T> =
    maxSetImpl(this, { }) { x, y, _, _ -> compare(x, y) }

/**
 * Return all maximum elements of an iterable, as determined by the comparison function.
 */
fun <T> Iterable<T>.maxSetBy(compare: (T, T) -> Int): List<T> =
    iterator().maxSetBy(compare)

/**
 * Return all maximum elements of an iterator, as determined by the key function.
 */
fun <T, K : Comparable<K>> Iterator<T>.maxSetByKey(key: (T) -> K): List<T> =
    maxSetImpl(this, key) { _, _, kx, ky -> kx.compareTo(ky) }

/**
 * Return all maximum elements of an iterable, as determined by the key function.
 */
fun <T, K : Comparable<K>> Iterable<T>.maxSetByKey(key: (T) -> K): List<T> =
    iterator().maxSetByKey(key)
