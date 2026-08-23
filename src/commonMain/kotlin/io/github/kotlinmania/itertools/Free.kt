// port-lint: source src/free.rs
package io.github.kotlinmania.itertools

/**
 * Free functions that create iterator adaptors or call iterator methods.
 *
 * The benefit of free functions is that they accept any [Iterable] as
 * argument, so the resulting code may be easier to read.
 */

/**
 * Iterate [iterable] with a running index.
 */
fun <T> enumerate(iterable: Iterable<T>): Iterator<IndexedValue<T>> =
    iterable.withIndex().iterator()

/**
 * Iterate [iterable] with a running index.
 */
fun <T> enumerate(iterator: Iterator<T>): Iterator<IndexedValue<T>> =
    iterator.asSequence().withIndex().iterator()

/**
 * Iterate [iterable] in reverse.
 */
fun <T> rev(iterable: List<T>): Iterator<T> =
    iterable.asReversed().iterator()

/**
 * Converts the arguments to iterators and zips them.
 */
fun <A, B> zip(i: Iterable<A>, j: Iterable<B>): Iterator<Pair<A, B>> =
    i.zip(j).iterator()

/**
 * Converts the arguments to iterators and zips them.
 */
fun <A, B> zip(i: Iterator<A>, j: Iterator<B>): Iterator<Pair<A, B>> =
    i.asSequence().zip(j.asSequence()).iterator()

/**
 * Takes two iterables and creates a new iterator over both in sequence.
 */
fun <T> chain(i: Iterable<T>, j: Iterable<T>): Iterator<T> =
    (i.asSequence() + j.asSequence()).iterator()

/**
 * Takes two iterators and creates a new iterator over both in sequence.
 */
fun <T> chain(i: Iterator<T>, j: Iterator<T>): Iterator<T> =
    (i.asSequence() + j.asSequence()).iterator()

/**
 * Create an iterator that clones each element.
 */
fun <T> cloned(iterable: Iterable<T>): Iterator<T> =
    iterable.iterator()

/**
 * Create an iterator that clones each element.
 */
fun <T> cloned(iterator: Iterator<T>): Iterator<T> =
    iterator

/**
 * Perform a fold operation over the iterable.
 */
fun <T, B> fold(iterable: Iterable<T>, init: B, f: (B, T) -> B): B =
    iterable.fold(init, f)

/**
 * Perform a fold operation over the iterator.
 */
fun <T, B> fold(iterator: Iterator<T>, init: B, f: (B, T) -> B): B {
    var acc = init
    while (iterator.hasNext()) {
        acc = f(acc, iterator.next())
    }
    return acc
}

/**
 * Test whether the predicate holds for all elements in the iterable.
 */
fun <T> all(iterable: Iterable<T>, f: (T) -> Boolean): Boolean =
    iterable.all(f)

/**
 * Test whether the predicate holds for all elements in the iterator.
 */
fun <T> all(iterator: Iterator<T>, f: (T) -> Boolean): Boolean {
    while (iterator.hasNext()) {
        if (!f(iterator.next())) {
            return false
        }
    }
    return true
}

/**
 * Test whether the predicate holds for any elements in the iterable.
 */
fun <T> any(iterable: Iterable<T>, f: (T) -> Boolean): Boolean =
    iterable.any(f)

/**
 * Test whether the predicate holds for any elements in the iterator.
 */
fun <T> any(iterator: Iterator<T>, f: (T) -> Boolean): Boolean {
    while (iterator.hasNext()) {
        if (f(iterator.next())) {
            return true
        }
    }
    return false
}

/**
 * Return the maximum value of the iterable.
 */
fun <T : Comparable<T>> max(iterable: Iterable<T>): T? =
    iterable.maxOrNull()

/**
 * Return the maximum value of the iterator.
 */
fun <T : Comparable<T>> max(iterator: Iterator<T>): T? {
    if (!iterator.hasNext()) return null
    var maxVal = iterator.next()
    while (iterator.hasNext()) {
        val nextVal = iterator.next()
        if (nextVal > maxVal) {
            maxVal = nextVal
        }
    }
    return maxVal
}

/**
 * Return the minimum value of the iterable.
 */
fun <T : Comparable<T>> min(iterable: Iterable<T>): T? =
    iterable.minOrNull()

/**
 * Return the minimum value of the iterator.
 */
fun <T : Comparable<T>> min(iterator: Iterator<T>): T? {
    if (!iterator.hasNext()) return null
    var minVal = iterator.next()
    while (iterator.hasNext()) {
        val nextVal = iterator.next()
        if (nextVal < minVal) {
            minVal = nextVal
        }
    }
    return minVal
}

/**
 * Combine all iterator elements into one `String`, separated by [sep].
 */
fun <T> join(iterable: Iterable<T>, sep: String): String =
    iterable.joinToString(separator = sep)

/**
 * Combine all iterator elements into one `String`, separated by [sep].
 */
fun <T> join(iterator: Iterator<T>, sep: String): String =
    iterator.asSequence().joinToString(separator = sep)

/**
 * Sort all iterator elements into a new iterator in ascending order.
 */
fun <T : Comparable<T>> sorted(iterable: Iterable<T>): Iterator<T> =
    iterable.sorted().iterator()

/**
 * Sort all iterator elements into a new iterator in ascending order.
 */
fun <T : Comparable<T>> sorted(iterator: Iterator<T>): Iterator<T> =
    iterator.asSequence().toList().sorted().iterator()

/**
 * Sort all iterator elements into a new iterator in ascending order.
 * This sort is unstable (i.e., may reorder equal elements).
 */
fun <T : Comparable<T>> sortedUnstable(iterable: Iterable<T>): Iterator<T> =
    iterable.sorted().iterator()

/**
 * Sort all iterator elements into a new iterator in ascending order.
 * This sort is unstable (i.e., may reorder equal elements).
 */
fun <T : Comparable<T>> sortedUnstable(iterator: Iterator<T>): Iterator<T> =
    iterator.asSequence().toList().sorted().iterator()
