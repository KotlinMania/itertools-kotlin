// port-lint: source src/concat_impl.rs
package io.github.kotlinmania.itertools

/**
 * Combine all an iterator's elements into one element by using [MutableList.addAll].
 *
 * [Iterable]-enabled version of [Itertools.concat].
 *
 * This combinator will extend the first item with each of the rest of the
 * items of the iterator. If the iterator is empty, an empty list is returned
 * (the Kotlin counterpart of Rust's `I::Item::default()` for the `Vec<T>` case
 * that drives every realistic call site).
 *
 * ```
 * val input = listOf(listOf(1), listOf(2, 3), listOf(4, 5, 6))
 * check(concat(input) == listOf(1, 2, 3, 4, 5, 6))
 * ```
 */
fun <T> concat(iterable: Iterable<Iterable<T>>): List<T> {
    val iterator = iterable.iterator()
    if (!iterator.hasNext()) return emptyList()
    val acc = iterator.next().toMutableList()
    while (iterator.hasNext()) {
        acc.addAll(iterator.next())
    }
    return acc
}
