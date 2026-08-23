// port-lint: source lib.rs
package io.github.kotlinmania.itertools

/**
 * Result of checking whether all elements in an iterator/iterable are equal.
 */
sealed class AllEqualValueResult<out T> {
    /** The iterator was empty. */
    object Empty : AllEqualValueResult<Nothing>()

    /** All elements in the iterator are equal to [value]. */
    data class AllEqual<out T>(
        val value: T,
    ) : AllEqualValueResult<T>()

    /** Found at least two distinct elements: [first] and [other]. */
    data class NotEqual<out T>(
        val first: T,
        val other: T,
    ) : AllEqualValueResult<T>()
}

/**
 * Test whether all elements in the iterator are equal.
 */
fun <T> Iterator<T>.allEqual(): Boolean {
    if (!hasNext()) return true
    val first = next()
    while (hasNext()) {
        if (next() != first) return false
    }
    return true
}

/**
 * Test whether all elements in the iterable are equal.
 */
fun <T> Iterable<T>.allEqual(): Boolean = iterator().allEqual()

/**
 * If there are elements and they are all equal, return [AllEqualValueResult.AllEqual].
 * If there are no elements, return [AllEqualValueResult.Empty].
 * If there are elements and they are not all equal, return [AllEqualValueResult.NotEqual] containing
 * the first element and the first non-equal element found.
 */
fun <T> Iterator<T>.allEqualValue(): AllEqualValueResult<T> {
    if (!hasNext()) return AllEqualValueResult.Empty
    val first = next()
    while (hasNext()) {
        val other = next()
        if (other != first) {
            return AllEqualValueResult.NotEqual(first, other)
        }
    }
    return AllEqualValueResult.AllEqual(first)
}

/**
 * If there are elements and they are all equal, return [AllEqualValueResult.AllEqual].
 * If there are no elements, return [AllEqualValueResult.Empty].
 * If there are elements and they are not all equal, return [AllEqualValueResult.NotEqual].
 */
fun <T> Iterable<T>.allEqualValue(): AllEqualValueResult<T> = iterator().allEqualValue()

/**
 * Check whether all elements are unique (non-equal).
 */
fun <T> Iterator<T>.allUnique(): Boolean {
    val seen = mutableSetOf<T>()
    while (hasNext()) {
        if (!seen.add(next())) return false
    }
    return true
}

/**
 * Check whether all elements are unique (non-equal).
 */
fun <T> Iterable<T>.allUnique(): Boolean = iterator().allUnique()
