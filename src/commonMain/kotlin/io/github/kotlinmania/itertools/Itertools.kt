// port-lint: source lib.rs
package io.github.kotlinmania.itertools

import io.github.kotlinmania.itertools.adaptors.Batching
import io.github.kotlinmania.itertools.adaptors.CoalesceBy
import io.github.kotlinmania.itertools.adaptors.CoalesceResult
import io.github.kotlinmania.itertools.adaptors.DedupBy
import io.github.kotlinmania.itertools.adaptors.DedupByWithCount
import io.github.kotlinmania.itertools.adaptors.FilterMapOk
import io.github.kotlinmania.itertools.adaptors.FilterOk
import io.github.kotlinmania.itertools.adaptors.Interleave
import io.github.kotlinmania.itertools.adaptors.InterleaveShortest
import io.github.kotlinmania.itertools.adaptors.MapOk
import io.github.kotlinmania.itertools.adaptors.MultiProduct
import io.github.kotlinmania.itertools.adaptors.Positions
import io.github.kotlinmania.itertools.adaptors.Update
import io.github.kotlinmania.itertools.adaptors.WhileSome

/**
 * An enum used for controlling the execution of `foldWhile`.
 *
 * See [foldWhile] for more information.
 */
sealed interface FoldWhile<out T> {
    /** Continue folding with this value. */
    data class Continue<T>(
        val value: T,
    ) : FoldWhile<T>

    /** Fold is complete and will return this value. */
    data class Done<T>(
        val value: T,
    ) : FoldWhile<T>

    /** Return the value in the continue or done. */
    fun intoInner(): T =
        when (this) {
            is Continue -> value
            is Done -> value
        }

    /** Return true if `this` is `Done`, false if it is `Continue`. */
    fun isDone(): Boolean = this is Done
}

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
 * Alternate or simultaneous values of two types [L] and [R].
 */
sealed class Either<out L, out R> {
    /** The left value. */
    data class Left<out L>(
        val value: L,
    ) : Either<L, Nothing>()

    /** The right value. */
    data class Right<out R>(
        val value: R,
    ) : Either<Nothing, R>()
}

// ---------------------------------------------------------------------------
// Adaptor Methods on Iterator and Iterable
// ---------------------------------------------------------------------------

/**
 * Alternate elements from two iterators until both have run out.
 */
fun <T> Iterator<T>.interleave(other: Iterator<T>): Interleave<T> =
    Interleave(this, other)

/**
 * Alternate elements from two iterables until both have run out.
 */
fun <T> Iterable<T>.interleave(other: Iterable<T>): Interleave<T> =
    Interleave(iterator(), other.iterator())

/**
 * Alternate elements from two iterators until one of them has run out.
 */
fun <T> Iterator<T>.interleaveShortest(other: Iterator<T>): InterleaveShortest<T> =
    InterleaveShortest(this, other)

/**
 * Alternate elements from two iterables until one of them has run out.
 */
fun <T> Iterable<T>.interleaveShortest(other: Iterable<T>): InterleaveShortest<T> =
    InterleaveShortest(iterator(), other.iterator())

/**
 * An iterator adaptor to insert a particular element between each element of the adapted iterator.
 */
fun <T> Iterator<T>.intersperse(element: T): Iterator<T> =
    io.github.kotlinmania.itertools
        .intersperse(this, element)

/**
 * An iterable adaptor to insert a particular element between each element of the adapted iterable.
 */
fun <T> Iterable<T>.intersperse(element: T): Iterator<T> =
    io.github.kotlinmania.itertools
        .intersperse(iterator(), element)

/**
 * An iterator adaptor to insert a particular value created by a function between each element of the adapted iterator.
 */
fun <T> Iterator<T>.intersperseWith(element: () -> T): Iterator<T> =
    io.github.kotlinmania.itertools
        .intersperseWith(this) { element() }

/**
 * An iterable adaptor to insert a particular value created by a function between each element of the adapted iterable.
 */
fun <T> Iterable<T>.intersperseWith(element: () -> T): Iterator<T> =
    io.github.kotlinmania.itertools
        .intersperseWith(iterator()) { element() }

/**
 * Create an iterator adaptor that steps over elements of an iterator in chunks.
 */
fun <T, R> Iterator<T>.batching(f: (Iterator<T>) -> R?): Batching<T, R> =
    Batching(this, f)

/**
 * Create an iterator adaptor that steps over elements of an iterable in chunks.
 */
fun <T, R> Iterable<T>.batching(f: (Iterator<T>) -> R?): Batching<T, R> =
    iterator().batching(f)

/**
 * Group iterator elements into dynamic chunks that are yielded successively.
 */
fun <T, K> Iterator<T>.chunkBy(keySelector: (T) -> K): ChunkBy<K, T> =
    ChunkBy(this, keySelector)

/**
 * Group iterable elements into dynamic chunks that are yielded successively.
 */
fun <T, K> Iterable<T>.chunkBy(keySelector: (T) -> K): ChunkBy<K, T> =
    ChunkBy(iterator(), keySelector)

/**
 * Group iterator elements into dynamic chunks that are yielded successively.
 */
fun <T, K> Iterator<T>.groupBy(keySelector: (T) -> K): ChunkBy<K, T> =
    chunkBy(keySelector)

/**
 * Group iterable elements into dynamic chunks that are yielded successively.
 */
fun <T, K> Iterable<T>.groupBy(keySelector: (T) -> K): ChunkBy<K, T> =
    chunkBy(keySelector)

/**
 * Return an iterator adaptor that yields contiguous chunks of size [size].
 */
fun <T> Iterator<T>.chunks(size: Int): IntoChunks<T> =
    IntoChunks(this, size)

/**
 * Return an iterator adaptor that yields contiguous chunks of size [size].
 */
fun <T> Iterable<T>.chunks(size: Int): IntoChunks<T> =
    IntoChunks(iterator(), size)

/**
 * Return an iterator adaptor that applies a function to the contained value of [ItemResult.Ok].
 */
fun <T, U, E> Iterator<ItemResult<T, E>>.mapOk(f: (T) -> U): MapOk<T, U, E> =
    MapOk(this, f)

/**
 * Return an iterator adaptor that applies a function to the contained value of [ItemResult.Ok].
 */
fun <T, U, E> Iterable<ItemResult<T, E>>.mapOk(f: (T) -> U): MapOk<T, U, E> =
    MapOk(iterator(), f)

/**
 * Return an iterator adaptor that filters based on the value of [ItemResult.Ok].
 */
fun <T, E> Iterator<ItemResult<T, E>>.filterOk(predicate: (T) -> Boolean): FilterOk<T, E> =
    FilterOk(this, predicate)

/**
 * Return an iterator adaptor that filters based on the value of [ItemResult.Ok].
 */
fun <T, E> Iterable<ItemResult<T, E>>.filterOk(predicate: (T) -> Boolean): FilterOk<T, E> =
    FilterOk(iterator(), predicate)

/**
 * Return an iterator adaptor that filter-maps based on the value of [ItemResult.Ok].
 */
fun <T, U, E> Iterator<ItemResult<T, E>>.filterMapOk(transform: (T) -> U?): FilterMapOk<T, U, E> =
    FilterMapOk(this, transform)

/**
 * Return an iterator adaptor that filter-maps based on the value of [ItemResult.Ok].
 */
fun <T, U, E> Iterable<ItemResult<T, E>>.filterMapOk(transform: (T) -> U?): FilterMapOk<T, U, E> =
    FilterMapOk(iterator(), transform)

/**
 * Return an iterator adaptor that flattens [ItemResult.Ok] values.
 */
fun <T, E> Iterator<ItemResult<Iterable<T>, E>>.flattenOk(): FlattenOk<T, E> =
    io.github.kotlinmania.itertools
        .flattenOk(this)

/**
 * Return an iterator adaptor that flattens [ItemResult.Ok] values.
 */
fun <T, E> Iterable<ItemResult<Iterable<T>, E>>.flattenOk(): FlattenOk<T, E> =
    io.github.kotlinmania.itertools
        .flattenOk(this)

/**
 * Merge two iterators in ascending order.
 */
fun <T : Comparable<T>> Iterator<T>.merge(other: Iterator<T>): MergeBy<T> =
    io.github.kotlinmania.itertools
        .merge(this, other)

/**
 * Merge two iterables in ascending order.
 */
fun <T : Comparable<T>> Iterable<T>.merge(other: Iterable<T>): MergeBy<T> =
    io.github.kotlinmania.itertools
        .merge(this, other)

/**
 * Merge two iterators using a custom comparator.
 */
fun <T> Iterator<T>.mergeBy(other: Iterator<T>, isLte: (T, T) -> Boolean): MergeBy<T> =
    io.github.kotlinmania.itertools
        .mergeBy(this, other, isLte)

/**
 * Merge two iterables using a custom comparator.
 */
fun <T> Iterable<T>.mergeBy(other: Iterable<T>, isLte: (T, T) -> Boolean): MergeBy<T> =
    io.github.kotlinmania.itertools
        .mergeBy(this, other, isLte)

/**
 * Merge-join items from two iterators in ascending order.
 */
fun <L, R> Iterator<L>.mergeJoinBy(other: Iterator<R>, cmp: (L, R) -> Int): MergeJoinBy<L, R> =
    io.github.kotlinmania.itertools
        .mergeJoinBy(this, other, cmp)

/**
 * Merge-join items from two iterables in ascending order.
 */
fun <L, R> Iterable<L>.mergeJoinBy(other: Iterable<R>, cmp: (L, R) -> Int): MergeJoinBy<L, R> =
    io.github.kotlinmania.itertools
        .mergeJoinBy(this, other, cmp)

/**
 * Merge multiple iterators into one sorted iterator.
 */
fun <T : Comparable<*>> Iterable<Iterator<T>>.kmerge(): KMergeBy<T> =
    io.github.kotlinmania.itertools
        .kmergeIterators(this)

/**
 * Merge multiple iterators using a custom comparison function.
 */
fun <T> Iterable<Iterator<T>>.kmergeBy(first: (T, T) -> Boolean): KMergeBy<T> =
    io.github.kotlinmania.itertools
        .kmergeIteratorsBy(this, first)

/**
 * Return an iterator adaptor that iterates over the cartesian product of two iterators.
 */
fun <A, B> Iterator<A>.cartesianProduct(other: Iterator<B>): Iterator<Pair<A, B>> {
    val bList = other.asSequence().toList()
    val aList = this.asSequence().toList()
    return sequence {
        for (a in aList) {
            for (b in bList) {
                yield(Pair(a, b))
            }
        }
    }.iterator()
}

/**
 * Return an iterator adaptor that iterates over the cartesian product of two iterables.
 */
fun <A, B> Iterable<A>.cartesianProduct(other: Iterable<B>): Iterator<Pair<A, B>> =
    iterator().cartesianProduct(other.iterator())

/**
 * Return an iterator adaptor that iterates over the cartesian product of multiple iterables.
 */
fun <T> Iterable<Iterable<T>>.multiCartesianProduct(): MultiProduct<T> =
    io.github.kotlinmania.itertools.adaptors
        .multiCartesianProduct(this)

/**
 * Return an iterator adaptor that joins together adjacent elements.
 */
fun <T> Iterator<T>.coalesce(f: (T, T) -> CoalesceResult<T>): CoalesceBy<T> =
    CoalesceBy(this, f)

/**
 * Return an iterator adaptor that joins together adjacent elements.
 */
fun <T> Iterable<T>.coalesce(f: (T, T) -> CoalesceResult<T>): CoalesceBy<T> =
    CoalesceBy(iterator(), f)

/**
 * Return an iterator adaptor that removes repeated duplicate elements.
 */
fun <T> Iterator<T>.dedup(): DedupBy<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedup(this)

/**
 * Return an iterator adaptor that removes repeated duplicate elements.
 */
fun <T> Iterable<T>.dedup(): DedupBy<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedup(this)

/**
 * Return an iterator adaptor that removes repeated duplicate elements using a comparison function.
 */
fun <T> Iterator<T>.dedupBy(same: (T, T) -> Boolean): DedupBy<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedupBy(this, same)

/**
 * Return an iterator adaptor that removes repeated duplicate elements using a comparison function.
 */
fun <T> Iterable<T>.dedupBy(same: (T, T) -> Boolean): DedupBy<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedupBy(this, same)

/**
 * Return an iterator adaptor that removes repeated duplicates and counts them.
 */
fun <T> Iterator<T>.dedupWithCount(): DedupByWithCount<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedupWithCount(this)

/**
 * Return an iterator adaptor that removes repeated duplicates and counts them.
 */
fun <T> Iterable<T>.dedupWithCount(): DedupByWithCount<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedupWithCount(this)

/**
 * Return an iterator adaptor that removes repeated duplicates with a count using a comparison function.
 */
fun <T> Iterator<T>.dedupByWithCount(same: (T, T) -> Boolean): DedupByWithCount<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedupByWithCount(this, same)

/**
 * Return an iterator adaptor that removes repeated duplicates with a count using a comparison function.
 */
fun <T> Iterable<T>.dedupByWithCount(same: (T, T) -> Boolean): DedupByWithCount<T> =
    io.github.kotlinmania.itertools.adaptors
        .dedupByWithCount(this, same)

/**
 * Return an iterator adaptor that takes items while a predicate holds.
 */
fun <T> PeekingNext<T>.peekingTakeWhile(accept: (T) -> Boolean): PeekingTakeWhile<T> =
    PeekingTakeWhile(this, accept)

/**
 * Return an iterator adaptor that takes items while a predicate holds.
 */
fun <T> Iterator<T>.peekingTakeWhile(accept: (T) -> Boolean): PeekingTakeWhile<T> =
    PeekingTakeWhile(this.peekable(), accept)

/**
 * Return an iterator adaptor that takes items while a predicate holds.
 */
fun <T> Iterable<T>.peekingTakeWhile(accept: (T) -> Boolean): PeekingTakeWhile<T> =
    PeekingTakeWhile(this.peekable(), accept)

/**
 * Return an iterator adaptor that takes items while a predicate holds by reference.
 */
fun <T> PeekingNext<T>.takeWhileRef(accept: (T) -> Boolean): PeekingTakeWhile<T> =
    peekingTakeWhile(accept)

/**
 * Return an iterator adaptor that takes items while a predicate holds by reference.
 */
fun <T> Iterator<T>.takeWhileRef(accept: (T) -> Boolean): PeekingTakeWhile<T> =
    peekingTakeWhile(accept)

/**
 * Return an iterator adaptor that takes items while a predicate holds by reference.
 */
fun <T> Iterable<T>.takeWhileRef(accept: (T) -> Boolean): PeekingTakeWhile<T> =
    peekingTakeWhile(accept)

/**
 * Return an iterator adaptor that yields elements until encountering null.
 */
fun <T : Any> Iterator<T?>.whileSome(): WhileSome<T> =
    io.github.kotlinmania.itertools.adaptors
        .whileSome(this)

/**
 * Return an iterator adaptor that yields elements until encountering null.
 */
fun <T : Any> Iterable<T?>.whileSome(): WhileSome<T> =
    io.github.kotlinmania.itertools.adaptors
        .whileSome(this)

/**
 * Return an iterator adaptor that yields all combinations of size [k].
 */
fun <T> Iterator<T>.combinations(k: Int): Combinations<T> =
    io.github.kotlinmania.itertools
        .combinations(this, k)

/**
 * Return an iterator adaptor that yields all combinations of size [k].
 */
fun <T> Iterable<T>.combinations(k: Int): Combinations<T> =
    io.github.kotlinmania.itertools
        .combinations(this, k)

/**
 * Return an iterator adaptor that yields all fixed-size combinations of size [k].
 */
fun <T> Iterator<T>.arrayCombinations(k: Int): Combinations<T> =
    io.github.kotlinmania.itertools
        .arrayCombinations(this, k)

/**
 * Return an iterator adaptor that yields all fixed-size combinations of size [k].
 */
fun <T> Iterable<T>.arrayCombinations(k: Int): Combinations<T> =
    io.github.kotlinmania.itertools
        .arrayCombinations(this, k)

/**
 * Return an iterator adaptor that yields all combinations with replacement of size [k].
 */
fun <T> Iterator<T>.combinationsWithReplacement(k: Int): CombinationsWithReplacement<T> =
    io.github.kotlinmania.itertools
        .combinationsWithReplacement(this, k)

/**
 * Return an iterator adaptor that yields all combinations with replacement of size [k].
 */
fun <T> Iterable<T>.combinationsWithReplacement(k: Int): CombinationsWithReplacement<T> =
    io.github.kotlinmania.itertools
        .combinationsWithReplacement(this, k)

/**
 * Return an iterator adaptor that yields all permutations of size [k].
 */
fun <T> Iterator<T>.permutations(k: Int): Permutations<T> =
    io.github.kotlinmania.itertools
        .permutations(this, k)

/**
 * Return an iterator adaptor that yields all permutations of size [k].
 */
fun <T> Iterable<T>.permutations(k: Int): Permutations<T> =
    io.github.kotlinmania.itertools
        .permutations(this, k)

/**
 * Return an iterator adaptor that yields all subsets of the iterator.
 */
fun <T> Iterator<T>.powerset(): Powerset<T> =
    io.github.kotlinmania.itertools
        .powerset(this)

/**
 * Return an iterator adaptor that yields all subsets of the iterable.
 */
fun <T> Iterable<T>.powerset(): Powerset<T> =
    io.github.kotlinmania.itertools
        .powerset(this)

/**
 * Return an iterator adaptor that yields indices of matching elements.
 */
fun <T> Iterator<T>.positions(predicate: (T) -> Boolean): Positions<T> =
    Positions(this, predicate)

/**
 * Return an iterator adaptor that yields indices of matching elements.
 */
fun <T> Iterable<T>.positions(predicate: (T) -> Boolean): Positions<T> =
    Positions(iterator(), predicate)

/**
 * Return an iterator adaptor that applies a side-effecting function to each element.
 */
fun <T> Iterator<T>.update(updater: (T) -> Unit): Update<T> =
    Update(this, updater)

/**
 * Return an iterator adaptor that applies a side-effecting function to each element.
 */
fun <T> Iterable<T>.update(updater: (T) -> Unit): Update<T> =
    Update(iterator(), updater)

// ---------------------------------------------------------------------------
// Non-adaptor Methods on Iterator and Iterable
// ---------------------------------------------------------------------------

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
 * If there are elements and they are not all equal, return [AllEqualValueResult.NotEqual].
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

/**
 * Find the position and value of the first element matching [predicate].
 */
fun <T> Iterator<T>.findPosition(predicate: (T) -> Boolean): Pair<Int, T>? {
    var index = 0
    while (hasNext()) {
        val item = next()
        if (predicate(item)) {
            return Pair(index, item)
        }
        index += 1
    }
    return null
}

/**
 * Find the position and value of the first element matching [predicate].
 */
fun <T> Iterable<T>.findPosition(predicate: (T) -> Boolean): Pair<Int, T>? =
    iterator().findPosition(predicate)

/**
 * Return the first element matching [predicate] or the last element in the iterator.
 */
fun <T> Iterator<T>.findOrLast(predicate: (T) -> Boolean): T? {
    var last: T? = null
    while (hasNext()) {
        val item = next()
        if (predicate(item)) return item
        last = item
    }
    return last
}

/**
 * Return the first element matching [predicate] or the last element in the iterable.
 */
fun <T> Iterable<T>.findOrLast(predicate: (T) -> Boolean): T? =
    iterator().findOrLast(predicate)

/**
 * Return the first element matching [predicate] or the first element in the iterator.
 */
fun <T> Iterator<T>.findOrFirst(predicate: (T) -> Boolean): T? {
    if (!hasNext()) return null
    val first = next()
    if (predicate(first)) return first
    while (hasNext()) {
        val item = next()
        if (predicate(item)) return item
    }
    return first
}

/**
 * Return the first element matching [predicate] or the first element in the iterable.
 */
fun <T> Iterable<T>.findOrFirst(predicate: (T) -> Boolean): T? =
    iterator().findOrFirst(predicate)

/**
 * Return `true` if the iterator contains an element equal to [query].
 */
fun <T> Iterator<T>.contains(query: T): Boolean {
    while (hasNext()) {
        if (next() == query) return true
    }
    return false
}

/**
 * Drop the first [n] elements of the iterator.
 */
fun <T> Iterator<T>.dropping(n: Int): Iterator<T> {
    var count = 0
    while (count < n && hasNext()) {
        next()
        count += 1
    }
    return this
}

/**
 * Drop the first [n] elements of the iterable.
 */
fun <T> Iterable<T>.dropping(n: Int): Iterator<T> =
    iterator().dropping(n)

/**
 * Drop the last [n] elements of the list.
 */
fun <T> List<T>.droppingBack(n: Int): List<T> {
    if (n <= 0) return this
    if (n >= size) return emptyList()
    return subList(0, size - n)
}

/**
 * Flatten an iterator of iterables into a single list.
 */
fun <T> Iterator<Iterable<T>>.concat(): List<T> =
    io.github.kotlinmania.itertools
        .concat(this)

/**
 * Flatten an iterable of iterables into a single list.
 */
fun <T> Iterable<Iterable<T>>.concat(): List<T> =
    io.github.kotlinmania.itertools
        .concat(this)

/**
 * Collect the iterator into a [List].
 */
fun <T> Iterator<T>.collectVec(): List<T> =
    asSequence().toList()

/**
 * Collect all elements into a [List] if all are [ItemResult.Ok].
 */
fun <T, E> Iterator<ItemResult<T, E>>.tryCollect(): ItemResult<List<T>, E> {
    val list = mutableListOf<T>()
    while (hasNext()) {
        when (val item = next()) {
            is ItemResult.Ok -> list.add(item.value)
            is ItemResult.Err -> return ItemResult.Err(item.error)
        }
    }
    return ItemResult.Ok(list)
}

/**
 * Set the elements of this mutable list from the given iterable.
 */
fun <T> MutableList<T>.setFrom(from: Iterable<T>): Int {
    var count = 0
    val it = from.iterator()
    for (i in indices) {
        if (it.hasNext()) {
            this[i] = it.next()
            count += 1
        } else {
            break
        }
    }
    return count
}

/**
 * Format all iterator elements into a string separated by [separator].
 */
fun <T> Iterator<T>.join(separator: String = ", "): String =
    asSequence().joinToString(separator)

/**
 * Format all iterable elements into a string separated by [separator].
 */
fun <T> Iterable<T>.join(separator: String = ", "): String =
    joinToString(separator)

/**
 * Fold over [ItemResult.Ok] values, short-circuiting on [ItemResult.Err].
 */
fun <T, E, B> Iterator<ItemResult<T, E>>.foldOk(start: B, f: (B, T) -> B): ItemResult<B, E> {
    var acc = start
    while (hasNext()) {
        when (val item = next()) {
            is ItemResult.Ok -> acc = f(acc, item.value)
            is ItemResult.Err -> return ItemResult.Err(item.error)
        }
    }
    return ItemResult.Ok(acc)
}

/**
 * Fold over non-null values, short-circuiting on null.
 */
fun <T : Any, B> Iterator<T?>.foldOptions(start: B, f: (B, T) -> B): B? {
    var acc = start
    while (hasNext()) {
        val item = next() ?: return null
        acc = f(acc, item)
    }
    return acc
}

/**
 * Fold all elements using the first element as the initial accumulator.
 */
fun <T> Iterator<T>.fold1(f: (T, T) -> T): T? {
    if (!hasNext()) return null
    var acc = next()
    while (hasNext()) {
        acc = f(acc, next())
    }
    return acc
}

/**
 * Fold all elements using the first element as the initial accumulator.
 */
fun <T> Iterable<T>.fold1(f: (T, T) -> T): T? =
    iterator().fold1(f)

/**
 * Tree reduce combining adjacent elements in a balanced binary tree fashion.
 */
fun <T> Iterator<T>.treeReduce(f: (T, T) -> T): T? {
    fun inner0(it: Iterator<T>): Result<T, T?> {
        if (!it.hasNext()) return Result.Err(null)
        val a = it.next()
        if (!it.hasNext()) return Result.Err(a)
        val b = it.next()
        return Result.Ok(f(a, b))
    }

    fun inner(stop: Int, it: Iterator<T>): Result<T, T?> {
        val first = inner0(it)
        var x =
            when (first) {
                is Result.Ok -> first.value
                is Result.Err -> return first
            }
        for (height in 0 until stop) {
            val next = if (height == 0) inner0(it) else inner(height, it)
            when (next) {
                is Result.Ok -> x = f(x, next.value)
                is Result.Err -> {
                    val y = next.value
                    return if (y == null) Result.Err(x) else Result.Err(f(x, y))
                }
            }
        }
        return Result.Ok(x)
    }

    return when (val res = inner(Int.MAX_VALUE, this)) {
        is Result.Ok -> res.value
        is Result.Err -> res.value
    }
}

private sealed class Result<out T, out E> {
    data class Ok<out T>(
        val value: T,
    ) : Result<T, Nothing>()

    data class Err<out E>(
        val value: E,
    ) : Result<Nothing, E>()
}

private typealias State<T> = Result<T, T?>

/**
 * Synonym for [treeReduce].
 */
fun <T> Iterator<T>.treeFold1(f: (T, T) -> T): T? = treeReduce(f)

/**
 * Synonym for [treeReduce].
 */
fun <T> Iterable<T>.treeFold1(f: (T, T) -> T): T? = iterator().treeReduce(f)

/**
 * An iterator method that applies a function as long as it returns [FoldWhile.Continue],
 * and returns the final value.
 */
fun <T, B> Iterator<T>.foldWhile(initial: B, f: (B, T) -> FoldWhile<B>): FoldWhile<B> {
    var acc = initial
    while (hasNext()) {
        val nextVal = next()
        when (val res = f(acc, nextVal)) {
            is FoldWhile.Continue -> acc = res.value
            is FoldWhile.Done -> return res
        }
    }
    return FoldWhile.Continue(acc)
}

/**
 * An iterable method that applies a function as long as it returns [FoldWhile.Continue],
 * and returns the final value.
 */
fun <T, B> Iterable<T>.foldWhile(initial: B, f: (B, T) -> FoldWhile<B>): FoldWhile<B> =
    iterator().foldWhile(initial, f)

/**
 * Sum all elements in the iterator if non-empty.
 */
fun Iterator<Int>.sum1(): Int? {
    if (!hasNext()) return null
    var sum = next()
    while (hasNext()) {
        sum += next()
    }
    return sum
}

/**
 * Sum all elements in the iterable if non-empty.
 */
fun Iterable<Int>.sum1(): Int? = iterator().sum1()

/**
 * Multiply all elements in the iterator if non-empty.
 */
fun Iterator<Int>.product1(): Int? {
    if (!hasNext()) return null
    var prod = next()
    while (hasNext()) {
        prod *= next()
    }
    return prod
}

/**
 * Multiply all elements in the iterable if non-empty.
 */
fun Iterable<Int>.product1(): Int? = iterator().product1()

/**
 * Sort all elements in ascending order.
 */
fun <T : Comparable<T>> Iterator<T>.sorted(): List<T> =
    asSequence().toMutableList().apply { sort() }

/**
 * Sort all elements in ascending order.
 */
fun <T : Comparable<T>> Iterable<T>.sorted(): List<T> =
    toMutableList().apply { sort() }

/**
 * Sort all elements using a comparator.
 */
fun <T> Iterator<T>.sortedBy(cmp: Comparator<in T>): List<T> =
    asSequence().toMutableList().apply { sortWith(cmp) }

/**
 * Sort all elements using a comparator.
 */
fun <T> Iterable<T>.sortedBy(cmp: Comparator<in T>): List<T> =
    toMutableList().apply { sortWith(cmp) }

/**
 * Sort all elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.sortedByKey(key: (T) -> K): List<T> =
    asSequence().toMutableList().apply { sortBy(key) }

/**
 * Sort all elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.sortedByKey(key: (T) -> K): List<T> =
    toMutableList().apply { sortBy(key) }

/**
 * Unstable sort of elements into a new list.
 */
fun <T : Comparable<T>> Iterator<T>.sortedUnstable(): List<T> =
    asSequence().toMutableList().apply { sort() }

/**
 * Unstable sort of elements into a new list.
 */
fun <T : Comparable<T>> Iterable<T>.sortedUnstable(): List<T> =
    toMutableList().apply { sort() }

/**
 * Unstable sort of elements using a comparator.
 */
fun <T> Iterator<T>.sortedUnstableBy(cmp: Comparator<in T>): List<T> =
    asSequence().toMutableList().apply { sortWith(cmp) }

/**
 * Unstable sort of elements using a comparator.
 */
fun <T> Iterable<T>.sortedUnstableBy(cmp: Comparator<in T>): List<T> =
    toMutableList().apply { sortWith(cmp) }

/**
 * Unstable sort of elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.sortedUnstableByKey(key: (T) -> K): List<T> =
    asSequence().toMutableList().apply { sortBy(key) }

/**
 * Unstable sort of elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.sortedUnstableByKey(key: (T) -> K): List<T> =
    toMutableList().apply { sortBy(key) }

/**
 * Sort all elements using a cached key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.sortedByCachedKey(key: (T) -> K): List<T> =
    asSequence().toMutableList().apply { sortBy(key) }

/**
 * Sort all elements using a cached key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.sortedByCachedKey(key: (T) -> K): List<T> =
    toMutableList().apply { sortBy(key) }

/**
 * Sort the [k] smallest elements into a new list in ascending order.
 */
fun <T : Comparable<T>> Iterator<T>.kSmallest(k: Int): List<T> =
    kSmallestGeneral(this, k, naturalOrder())

/**
 * Sort the [k] smallest elements into a new list in ascending order.
 */
fun <T : Comparable<T>> Iterable<T>.kSmallest(k: Int): List<T> =
    iterator().kSmallest(k)

/**
 * Sort the [k] smallest elements using a comparator.
 */
fun <T> Iterator<T>.kSmallestBy(k: Int, cmp: (T, T) -> Int): List<T> =
    kSmallestGeneral(this, k, Comparator(cmp))

/**
 * Sort the [k] smallest elements using a comparator.
 */
fun <T> Iterable<T>.kSmallestBy(k: Int, cmp: (T, T) -> Int): List<T> =
    iterator().kSmallestBy(k, cmp)

/**
 * Sort the [k] smallest elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.kSmallestByKey(k: Int, key: (T) -> K): List<T> =
    kSmallestGeneral(this, k, compareBy(key))

/**
 * Sort the [k] smallest elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.kSmallestByKey(k: Int, key: (T) -> K): List<T> =
    iterator().kSmallestByKey(k, key)

/**
 * Sort the [k] smallest elements relaxing memory.
 */
fun <T : Comparable<T>> Iterator<T>.kSmallestRelaxed(k: Int): List<T> =
    kSmallestRelaxedGeneral(this, k, naturalOrder())

/**
 * Sort the [k] smallest elements relaxing memory.
 */
fun <T : Comparable<T>> Iterable<T>.kSmallestRelaxed(k: Int): List<T> =
    iterator().kSmallestRelaxed(k)

/**
 * Sort the [k] smallest elements relaxing memory using a comparator.
 */
fun <T> Iterator<T>.kSmallestRelaxedBy(k: Int, cmp: (T, T) -> Int): List<T> =
    kSmallestRelaxedGeneral(this, k, Comparator(cmp))

/**
 * Sort the [k] smallest elements relaxing memory using a comparator.
 */
fun <T> Iterable<T>.kSmallestRelaxedBy(k: Int, cmp: (T, T) -> Int): List<T> =
    iterator().kSmallestRelaxedBy(k, cmp)

/**
 * Sort the [k] smallest elements relaxing memory using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.kSmallestRelaxedByKey(k: Int, key: (T) -> K): List<T> =
    kSmallestRelaxedGeneral(this, k, compareBy(key))

/**
 * Sort the [k] smallest elements relaxing memory using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.kSmallestRelaxedByKey(k: Int, key: (T) -> K): List<T> =
    iterator().kSmallestRelaxedByKey(k, key)

/**
 * Sort the [k] largest elements in descending order.
 */
fun <T : Comparable<T>> Iterator<T>.kLargest(k: Int): List<T> =
    kSmallestGeneral(this, k, reverseOrder())

/**
 * Sort the [k] largest elements in descending order.
 */
fun <T : Comparable<T>> Iterable<T>.kLargest(k: Int): List<T> =
    iterator().kLargest(k)

/**
 * Sort the [k] largest elements using a comparator.
 */
fun <T> Iterator<T>.kLargestBy(k: Int, cmp: (T, T) -> Int): List<T> =
    kSmallestGeneral(this, k, Comparator { a, b -> -cmp(a, b) })

/**
 * Sort the [k] largest elements using a comparator.
 */
fun <T> Iterable<T>.kLargestBy(k: Int, cmp: (T, T) -> Int): List<T> =
    iterator().kLargestBy(k, cmp)

/**
 * Sort the [k] largest elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.kLargestByKey(k: Int, key: (T) -> K): List<T> =
    kSmallestGeneral(this, k, compareByDescending(key))

/**
 * Sort the [k] largest elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.kLargestByKey(k: Int, key: (T) -> K): List<T> =
    iterator().kLargestByKey(k, key)

/**
 * Sort the [k] largest elements relaxing memory.
 */
fun <T : Comparable<T>> Iterator<T>.kLargestRelaxed(k: Int): List<T> =
    kSmallestRelaxedGeneral(this, k, reverseOrder())

/**
 * Sort the [k] largest elements relaxing memory.
 */
fun <T : Comparable<T>> Iterable<T>.kLargestRelaxed(k: Int): List<T> =
    iterator().kLargestRelaxed(k)

/**
 * Sort the [k] largest elements relaxing memory using a comparator.
 */
fun <T> Iterator<T>.kLargestRelaxedBy(k: Int, cmp: (T, T) -> Int): List<T> =
    kSmallestRelaxedGeneral(this, k, Comparator { a, b -> -cmp(a, b) })

/**
 * Sort the [k] largest elements relaxing memory using a comparator.
 */
fun <T> Iterable<T>.kLargestRelaxedBy(k: Int, cmp: (T, T) -> Int): List<T> =
    iterator().kLargestRelaxedBy(k, cmp)

/**
 * Sort the [k] largest elements relaxing memory using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.kLargestRelaxedByKey(k: Int, key: (T) -> K): List<T> =
    kSmallestRelaxedGeneral(this, k, compareByDescending(key))

/**
 * Sort the [k] largest elements relaxing memory using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.kLargestRelaxedByKey(k: Int, key: (T) -> K): List<T> =
    iterator().kLargestRelaxedByKey(k, key)

/**
 * Return the last [n] elements of the iterator.
 */
fun <T> Iterator<T>.tail(n: Int): List<T> {
    if (n <= 0) return emptyList()
    val deque = ArrayDeque<T>(n)
    while (hasNext()) {
        if (deque.size == n) {
            deque.removeFirst()
        }
        deque.addLast(next())
    }
    return deque.toList()
}

/**
 * Return the last [n] elements of the iterable.
 */
fun <T> Iterable<T>.tail(n: Int): List<T> = iterator().tail(n)

/**
 * Collect iterator elements into one of two partitions based on [predicate].
 */
fun <T, L, R> Iterator<T>.partitionMap(predicate: (T) -> Either<L, R>): Pair<List<L>, List<R>> {
    val left = mutableListOf<L>()
    val right = mutableListOf<R>()
    while (hasNext()) {
        when (val res = predicate(next())) {
            is Either.Left -> left.add(res.value)
            is Either.Right -> right.add(res.value)
        }
    }
    return Pair(left, right)
}

/**
 * Collect iterable elements into one of two partitions based on [predicate].
 */
fun <T, L, R> Iterable<T>.partitionMap(predicate: (T) -> Either<L, R>): Pair<List<L>, List<R>> =
    iterator().partitionMap(predicate)

/**
 * Partition a sequence of [ItemResult]s into a list of [ItemResult.Ok] values and a list of [ItemResult.Err] errors.
 */
fun <T, E> Iterator<ItemResult<T, E>>.partitionResult(): Pair<List<T>, List<E>> {
    val oks = mutableListOf<T>()
    val errs = mutableListOf<E>()
    while (hasNext()) {
        when (val item = next()) {
            is ItemResult.Ok -> oks.add(item.value)
            is ItemResult.Err -> errs.add(item.error)
        }
    }
    return Pair(oks, errs)
}

/**
 * Partition an iterable of [ItemResult]s into a list of [ItemResult.Ok] values and a list of [ItemResult.Err] errors.
 */
fun <T, E> Iterable<ItemResult<T, E>>.partitionResult(): Pair<List<T>, List<E>> =
    iterator().partitionResult()

/**
 * Return the position of the maximum element in the iterator.
 */
fun <T : Comparable<T>> Iterator<T>.positionMax(): Int? {
    var maxIdx: Int? = null
    var maxVal: T? = null
    var idx = 0
    while (hasNext()) {
        val item = next()
        if (maxVal == null || item >= maxVal) {
            maxVal = item
            maxIdx = idx
        }
        idx += 1
    }
    return maxIdx
}

/**
 * Return the position of the maximum element in the iterable.
 */
fun <T : Comparable<T>> Iterable<T>.positionMax(): Int? = iterator().positionMax()

/**
 * Return the position of the maximum element using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.positionMaxByKey(key: (T) -> K): Int? {
    var maxIdx: Int? = null
    var maxKey: K? = null
    var idx = 0
    while (hasNext()) {
        val item = next()
        val k = key(item)
        if (maxKey == null || k >= maxKey) {
            maxKey = k
            maxIdx = idx
        }
        idx += 1
    }
    return maxIdx
}

/**
 * Return the position of the maximum element using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.positionMaxByKey(key: (T) -> K): Int? =
    iterator().positionMaxByKey(key)

/**
 * Return the position of the maximum element using a comparator.
 */
fun <T> Iterator<T>.positionMaxBy(cmp: (T, T) -> Int): Int? {
    var maxIdx: Int? = null
    var maxVal: T? = null
    var idx = 0
    while (hasNext()) {
        val item = next()
        if (maxVal == null || cmp(item, maxVal) >= 0) {
            maxVal = item
            maxIdx = idx
        }
        idx += 1
    }
    return maxIdx
}

/**
 * Return the position of the maximum element using a comparator.
 */
fun <T> Iterable<T>.positionMaxBy(cmp: (T, T) -> Int): Int? =
    iterator().positionMaxBy(cmp)

/**
 * Return the position of the minimum element in the iterator.
 */
fun <T : Comparable<T>> Iterator<T>.positionMin(): Int? {
    var minIdx: Int? = null
    var minVal: T? = null
    var idx = 0
    while (hasNext()) {
        val item = next()
        if (minVal == null || item < minVal) {
            minVal = item
            minIdx = idx
        }
        idx += 1
    }
    return minIdx
}

/**
 * Return the position of the minimum element in the iterable.
 */
fun <T : Comparable<T>> Iterable<T>.positionMin(): Int? = iterator().positionMin()

/**
 * Return the position of the minimum element using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.positionMinByKey(key: (T) -> K): Int? {
    var minIdx: Int? = null
    var minKey: K? = null
    var idx = 0
    while (hasNext()) {
        val item = next()
        val k = key(item)
        if (minKey == null || k < minKey) {
            minKey = k
            minIdx = idx
        }
        idx += 1
    }
    return minIdx
}

/**
 * Return the position of the minimum element using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.positionMinByKey(key: (T) -> K): Int? =
    iterator().positionMinByKey(key)

/**
 * Return the position of the minimum element using a comparator.
 */
fun <T> Iterator<T>.positionMinBy(cmp: (T, T) -> Int): Int? {
    var minIdx: Int? = null
    var minVal: T? = null
    var idx = 0
    while (hasNext()) {
        val item = next()
        if (minVal == null || cmp(item, minVal) < 0) {
            minVal = item
            minIdx = idx
        }
        idx += 1
    }
    return minIdx
}

/**
 * Return the position of the minimum element using a comparator.
 */
fun <T> Iterable<T>.positionMinBy(cmp: (T, T) -> Int): Int? =
    iterator().positionMinBy(cmp)

/**
 * Return the positions of the minimum and maximum elements in the iterator.
 */
fun <T : Comparable<T>> Iterator<T>.positionMinmax(): MinMaxResult<Int> {
    val list = asSequence().toList()
    if (list.isEmpty()) return MinMaxResult.NoElements
    if (list.size == 1) return MinMaxResult.OneElement(0)
    var minPos = 0
    var maxPos = 0
    var minVal = list[0]
    var maxVal = list[0]
    for (i in 1 until list.size) {
        val v = list[i]
        if (v < minVal) {
            minVal = v
            minPos = i
        }
        if (v >= maxVal) {
            maxVal = v
            maxPos = i
        }
    }
    return MinMaxResult.MinMax(minPos, maxPos)
}

/**
 * Return the positions of the minimum and maximum elements in the iterable.
 */
fun <T : Comparable<T>> Iterable<T>.positionMinmax(): MinMaxResult<Int> =
    iterator().positionMinmax()

/**
 * Return the positions of the minimum and maximum elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterator<T>.positionMinmaxByKey(key: (T) -> K): MinMaxResult<Int> {
    val list = asSequence().toList()
    if (list.isEmpty()) return MinMaxResult.NoElements
    if (list.size == 1) return MinMaxResult.OneElement(0)
    var minPos = 0
    var maxPos = 0
    var minKey = key(list[0])
    var maxKey = minKey
    for (i in 1 until list.size) {
        val k = key(list[i])
        if (k < minKey) {
            minKey = k
            minPos = i
        }
        if (k >= maxKey) {
            maxKey = k
            maxPos = i
        }
    }
    return MinMaxResult.MinMax(minPos, maxPos)
}

/**
 * Return the positions of the minimum and maximum elements using a key selector.
 */
fun <T, K : Comparable<K>> Iterable<T>.positionMinmaxByKey(key: (T) -> K): MinMaxResult<Int> =
    iterator().positionMinmaxByKey(key)

/**
 * Return the positions of the minimum and maximum elements using a comparator.
 */
fun <T> Iterator<T>.positionMinmaxBy(cmp: (T, T) -> Int): MinMaxResult<Int> {
    val list = asSequence().toList()
    if (list.isEmpty()) return MinMaxResult.NoElements
    if (list.size == 1) return MinMaxResult.OneElement(0)
    var minPos = 0
    var maxPos = 0
    var minVal = list[0]
    var maxVal = list[0]
    for (i in 1 until list.size) {
        val v = list[i]
        if (cmp(v, minVal) < 0) {
            minVal = v
            minPos = i
        }
        if (cmp(v, maxVal) >= 0) {
            maxVal = v
            maxPos = i
        }
    }
    return MinMaxResult.MinMax(minPos, maxPos)
}

/**
 * Return the positions of the minimum and maximum elements using a comparator.
 */
fun <T> Iterable<T>.positionMinmaxBy(cmp: (T, T) -> Int): MinMaxResult<Int> =
    iterator().positionMinmaxBy(cmp)

/**
 * Return the single element of the iterator or an error if length != 1.
 */
fun <T> Iterator<T>.exactlyOne(): ItemResult<T, ExactlyOneError<T>> =
    io.github.kotlinmania.itertools
        .exactlyOne(this)

/**
 * Return the single element of the iterable or an error if length != 1.
 */
fun <T> Iterable<T>.exactlyOne(): ItemResult<T, ExactlyOneError<T>> =
    io.github.kotlinmania.itertools
        .exactlyOne(this)

/**
 * Return the single element or null of the iterator or an error if length > 1.
 */
fun <T> Iterator<T>.atMostOne(): ItemResult<T?, ExactlyOneError<T>> =
    io.github.kotlinmania.itertools
        .atMostOne(this)

/**
 * Return the single element or null of the iterable or an error if length > 1.
 */
fun <T> Iterable<T>.atMostOne(): ItemResult<T?, ExactlyOneError<T>> =
    io.github.kotlinmania.itertools
        .atMostOne(this)

/**
 * Return an iterator adaptor that allows peeking multiple elements ahead.
 */
fun <T> Iterator<T>.multipeek(): MultiPeek<T> =
    io.github.kotlinmania.itertools
        .multipeek(this)

/**
 * Return an iterator adaptor that allows peeking multiple elements ahead.
 */
fun <T> Iterable<T>.multipeek(): MultiPeek<T> =
    io.github.kotlinmania.itertools
        .multipeek(this)

/**
 * Collect the items and count frequencies of each element.
 */
fun <T> Iterator<T>.counts(): Map<T, Int> {
    val counts = mutableMapOf<T, Int>()
    while (hasNext()) {
        val item = next()
        counts[item] = (counts[item] ?: 0) + 1
    }
    return counts
}

/**
 * Collect the items and count frequencies of each element.
 */
fun <T> Iterable<T>.counts(): Map<T, Int> = iterator().counts()

/**
 * Collect the items and count frequencies by key.
 */
fun <T, K> Iterator<T>.countsBy(f: (T) -> K): Map<K, Int> {
    val counts = mutableMapOf<K, Int>()
    while (hasNext()) {
        val k = f(next())
        counts[k] = (counts[k] ?: 0) + 1
    }
    return counts
}

/**
 * Collect the items and count frequencies by key.
 */
fun <T, K> Iterable<T>.countsBy(f: (T) -> K): Map<K, Int> = iterator().countsBy(f)

/**
 * Unzip an iterator of pairs into two lists.
 */
fun <A, B> Iterator<Pair<A, B>>.multiunzip(): Pair<List<A>, List<B>> =
    io.github.kotlinmania.itertools
        .multiUnzip(asSequence().asIterable())

/**
 * Unzip an iterable of pairs into two lists.
 */
fun <A, B> Iterable<Pair<A, B>>.multiunzip(): Pair<List<A>, List<B>> =
    io.github.kotlinmania.itertools
        .multiUnzip(this)

/**
 * Return the length of the iterator or a [SizeHint] if not known exactly.
 */
fun <T> Iterator<T>.tryLen(): ItemResult<Int, SizeHint> = ItemResult.Err(SizeHint(0, null))

/**
 * Return the element at the given index in the iterator, or `null` if the index is out of bounds.
 */
fun <T> Iterator<T>.get(index: Int): T? {
    if (index < 0) return null
    var i = 0
    while (hasNext()) {
        val elt = next()
        if (i == index) return elt
        i += 1
    }
    return null
}

/**
 * Return the element at the given index in the iterable, or `null` if the index is out of bounds.
 */
fun <T> Iterable<T>.get(index: Int): T? = iterator().get(index)

/**
 * Advances the iterator and returns the next items grouped in a list of size [n], or `null` if not enough elements remain.
 */
fun <T> Iterator<T>.nextArray(n: Int): List<T>? {
    val list = ArrayList<T>(n)
    for (i in 0 until n) {
        if (!hasNext()) return null
        list.add(next())
    }
    return list
}

/**
 * Collects all items from the iterator into a list of size [n] if exactly [n] elements remain, or `null` otherwise.
 */
fun <T> Iterator<T>.collectArray(n: Int): List<T>? {
    val arr = nextArray(n) ?: return null
    if (hasNext()) return null
    return arr
}

/**
 * Advances the iterator and returns the next two items as a pair, or `null` if not enough elements remain.
 */
fun <T> Iterator<T>.nextTuple(): Pair<T, T>? {
    if (!hasNext()) return null
    val a = next()
    if (!hasNext()) return null
    val b = next()
    return Pair(a, b)
}

/**
 * Collects all items from the iterator into a pair if exactly two elements remain, or `null` otherwise.
 */
fun <T> Iterator<T>.collectTuple(): Pair<T, T>? {
    val tup = nextTuple() ?: return null
    if (hasNext()) return null
    return tup
}

/**
 * Return an iterator adaptor that maps each element using [transform].
 */
fun <T, R> Iterator<T>.mapInto(transform: (T) -> R): Iterator<R> =
    iterator {
        while (hasNext()) {
            yield(transform(next()))
        }
    }

/**
 * Return an iterable adaptor that maps each element using [transform].
 */
fun <T, R> Iterable<T>.mapInto(transform: (T) -> R): Iterator<R> =
    iterator().mapInto(transform)

/**
 * Create an iterator that iterates over both this and the specified iterator simultaneously,
 * yielding pairs of [EitherOrBoth].
 */
fun <T, U> Iterator<T>.zipLongest(other: Iterator<U>): ZipLongest<T, U> =
    io.github.kotlinmania.itertools.zipLongest(this, other)

/**
 * Create an iterator that iterates over both this and the specified iterable simultaneously,
 * yielding pairs of [EitherOrBoth].
 */
fun <T, U> Iterable<T>.zipLongest(other: Iterable<U>): ZipLongest<T, U> =
    iterator().zipLongest(other.iterator())

/**
 * Create an iterator which iterates over both this and the specified iterator simultaneously,
 * yielding pairs of elements.
 */
fun <T, U> Iterator<T>.zipEq(other: Iterator<U>): ZipEq<T, U> =
    io.github.kotlinmania.itertools.zipEq(this, other)

/**
 * Create an iterator which iterates over both this and the specified iterable simultaneously,
 * yielding pairs of elements.
 */
fun <T, U> Iterable<T>.zipEq(other: Iterable<U>): ZipEq<T, U> =
    iterator().zipEq(other.iterator())

/**
 * Return an iterator over all contiguous windows producing lists of a specific [size] (default 2).
 */
fun <T> Iterator<T>.tupleWindows(size: Int = 2): TupleWindows<Iterator<T>, T> =
    io.github.kotlinmania.itertools.tupleWindows(this, size)

/**
 * Return an iterator over all contiguous windows producing lists of a specific [size] (default 2).
 */
fun <T> Iterable<T>.tupleWindows(size: Int = 2): TupleWindows<Iterator<T>, T> =
    io.github.kotlinmania.itertools.tupleWindows(iterator(), size)

/**
 * Return an iterator over all windows, wrapping back to the first elements when the window would otherwise exceed the length of the iterator.
 */
fun <T> Iterator<T>.circularTupleWindows(size: Int = 2): CircularTupleWindows<Iterator<T>, T> =
    io.github.kotlinmania.itertools.circularTupleWindows(this, size)

/**
 * Return an iterator over all windows, wrapping back to the first elements when the window would otherwise exceed the length of the iterable.
 */
fun <T> Iterable<T>.circularTupleWindows(size: Int = 2): CircularTupleWindows<Iterator<T>, T> =
    io.github.kotlinmania.itertools.circularTupleWindows(iterator(), size)

/**
 * Return an iterator that groups the items in tuples of a specific [size] (default 2).
 */
fun <T> Iterator<T>.tuples(size: Int = 2): Tuples<Iterator<T>, T> =
    io.github.kotlinmania.itertools.tuples(this, size)

/**
 * Return an iterator that groups the items in tuples of a specific [size] (default 2).
 */
fun <T> Iterable<T>.tuples(size: Int = 2): Tuples<Iterator<T>, T> =
    io.github.kotlinmania.itertools.tuples(iterator(), size)

/**
 * Split into an iterator pair that both yield all elements from the original iterator.
 */
fun <T> Iterator<T>.tee(): Pair<Tee<T>, Tee<T>> =
    io.github.kotlinmania.itertools.tee(this)

/**
 * Split into an iterator pair that both yield all elements from the original iterable.
 */
fun <T> Iterable<T>.tee(): Pair<Tee<T>, Tee<T>> =
    io.github.kotlinmania.itertools.tee(this)

/**
 * “Lift” a function of the values of the current iterator so as to process an iterator of [ItemResult] values instead.
 */
fun <T, E, R> Iterator<ItemResult<T, E>>.processResults(processor: (Iterator<T>) -> R): ItemResult<R, E> =
    io.github.kotlinmania.itertools.processResults(asSequence().asIterable(), processor)

/**
 * “Lift” a function of the values of the current iterable so as to process an iterator of [ItemResult] values instead.
 */
fun <T, E, R> Iterable<ItemResult<T, E>>.processResults(processor: (Iterator<T>) -> R): ItemResult<R, E> =
    io.github.kotlinmania.itertools.processResults(this, processor)

/**
 * Filter duplicate elements from this iterator, keeping only elements seen more than once.
 */
fun <T> Iterator<T>.duplicates(): Duplicates<T> =
    io.github.kotlinmania.itertools.duplicates(this)

/**
 * Filter duplicate elements from this iterable, keeping only elements seen more than once.
 */
fun <T> Iterable<T>.duplicates(): Duplicates<T> =
    io.github.kotlinmania.itertools.duplicates(this)

/**
 * Filter duplicate elements from this iterator, keeping only elements seen more than once, compared by key produced by [f].
 */
fun <T, K> Iterator<T>.duplicatesBy(f: (T) -> K): DuplicatesBy<T, K> =
    io.github.kotlinmania.itertools.duplicatesBy(this, SizeHint(0, null), f)

/**
 * Filter duplicate elements from this iterable, keeping only elements seen more than once, compared by key produced by [f].
 */
fun <T, K> Iterable<T>.duplicatesBy(f: (T) -> K): DuplicatesBy<T, K> =
    io.github.kotlinmania.itertools.duplicatesBy(this, f)

/**
 * Return an iterator adaptor that filters out elements that have already been produced once.
 */
fun <T> Iterator<T>.unique(): Unique<T> =
    io.github.kotlinmania.itertools.unique(this)

/**
 * Return an iterator adaptor that filters out elements that have already been produced once.
 */
fun <T> Iterable<T>.unique(): Unique<T> =
    io.github.kotlinmania.itertools.unique(this)

/**
 * Return an iterator adaptor that filters out elements that have already been produced once, comparing using the key returned by [f].
 */
fun <T, V> Iterator<T>.uniqueBy(f: (T) -> V): UniqueBy<T, V> =
    io.github.kotlinmania.itertools.uniqueBy(this, SizeHint(0, null), f)

/**
 * Return an iterator adaptor that filters out elements that have already been produced once, comparing using the key returned by [f].
 */
fun <T, V> Iterable<T>.uniqueBy(f: (T) -> V): UniqueBy<T, V> =
    io.github.kotlinmania.itertools.uniqueBy(this, f)

/**
 * An iterator adaptor that consumes elements while [predicate] returns `true`, including the element for which it first returns `false`.
 */
fun <T> Iterator<T>.takeWhileInclusive(predicate: (T) -> Boolean): TakeWhileInclusive<T> =
    TakeWhileInclusive(this, predicate)

/**
 * An iterable adaptor that consumes elements while [predicate] returns `true`, including the element for which it first returns `false`.
 */
fun <T> Iterable<T>.takeWhileInclusive(predicate: (T) -> Boolean): TakeWhileInclusive<T> =
    takeWhileInclusive(this, predicate)

/**
 * Return an iterator adaptor that produces all k-combinations of elements in the iterator as tuples.
 */
fun <T> Iterator<T>.tupleCombinations(): io.github.kotlinmania.itertools.adaptors.Tuple2Combination<T> =
    io.github.kotlinmania.itertools.adaptors.tupleCombinations(asSequence().asIterable())

/**
 * Return an iterator adaptor that produces all k-combinations of elements in the iterable as tuples.
 */
fun <T> Iterable<T>.tupleCombinations(): io.github.kotlinmania.itertools.adaptors.Tuple2Combination<T> =
    io.github.kotlinmania.itertools.adaptors.tupleCombinations(this)

/**
 * An iterator adaptor that pads a sequence to a minimum length by filling missing elements using a function.
 */
fun <T> Iterator<T>.padUsing(min: Int, filler: (Int) -> T): PadUsing<T> =
    io.github.kotlinmania.itertools.padUsing(this, min, filler)

/**
 * An iterable adaptor that pads a sequence to a minimum length by filling missing elements using a function.
 */
fun <T> Iterable<T>.padUsing(min: Int, filler: (Int) -> T): PadUsing<T> =
    io.github.kotlinmania.itertools.padUsing(this, min, filler)

/**
 * An iterator adaptor that wraps each element in a [Positioned] value.
 */
fun <T> Iterator<T>.withPosition(): WithPosition<T> =
    WithPosition(this)

/**
 * An iterable adaptor that wraps each element in a [Positioned] value.
 */
fun <T> Iterable<T>.withPosition(): WithPosition<T> =
    withPosition(this)

/**
 * Format all iterator elements lazily, separated by [separator].
 */
fun <T> Iterator<T>.format(separator: String): Formatted =
    newFormatDefault(this, separator)

/**
 * Format all iterable elements lazily, separated by [separator].
 */
fun <T> Iterable<T>.format(separator: String): Formatted =
    iterator().format(separator)

/**
 * Format all iterator elements lazily, separated by [separator], using [f].
 */
fun <T> Iterator<T>.formatWith(separator: String, f: (T, (Any?) -> Unit) -> Unit): Formatted =
    newFormat(this, separator, f)

/**
 * Format all iterable elements lazily, separated by [separator], using [f].
 */
fun <T> Iterable<T>.formatWith(separator: String, f: (T, (Any?) -> Unit) -> Unit): Formatted =
    iterator().formatWith(separator, f)

/**
 * Groups elements of this iterator of pairs into a Map of lists.
 */
fun <K, V> Iterator<Pair<K, V>>.intoGroupMap(): Map<K, List<V>> =
    intoGroupingMap().collect()

/**
 * Groups elements of this iterable of pairs into a Map of lists.
 */
fun <K, V> Iterable<Pair<K, V>>.intoGroupMap(): Map<K, List<V>> =
    iterator().intoGroupMap()

/**
 * Groups elements of this iterator by [keyMapper] into a Map of lists.
 */
fun <T, K> Iterator<T>.intoGroupMapBy(keyMapper: (T) -> K): Map<K, List<T>> =
    intoGroupingMapBy(keyMapper).collect()

/**
 * Groups elements of this iterable by [keyMapper] into a Map of lists.
 */
fun <T, K> Iterable<T>.intoGroupMapBy(keyMapper: (T) -> K): Map<K, List<T>> =
    iterator().intoGroupMapBy(keyMapper)

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

/**
 * Groups elements of this iterator by [keyMapper] into a [GroupingMap].
 */
fun <T, K> Iterator<T>.intoGroupingMapBy(keyMapper: (T) -> K): GroupingMap<K, T> =
    GroupingMap(newMapForGrouping(this, keyMapper))

/**
 * Groups elements of this iterable by [keyMapper] into a [GroupingMap].
 */
fun <T, K> Iterable<T>.intoGroupingMapBy(keyMapper: (T) -> K): GroupingMap<K, T> =
    GroupingMap(newMapForGrouping(this.iterator(), keyMapper))

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

/**
 * Return the minimum and maximum element of an iterator.
 */
fun <T : Comparable<T>> Iterator<T>.minmax(): MinMaxResult<T> =
    minmaxImpl(this, { }) { x, y, _, _ -> x < y }

/**
 * Return the minimum and maximum element of an iterable.
 */
fun <T : Comparable<T>> Iterable<T>.minmax(): MinMaxResult<T> =
    iterator().minmax()

/**
 * Return the minimum and maximum element of an iterator, as determined by the key function.
 */
fun <T, K : Comparable<K>> Iterator<T>.minmaxByKey(key: (T) -> K): MinMaxResult<T> =
    minmaxImpl(this, key) { _, _, kx, ky -> kx < ky }

/**
 * Return the minimum and maximum element of an iterable, as determined by the key function.
 */
fun <T, K : Comparable<K>> Iterable<T>.minmaxByKey(key: (T) -> K): MinMaxResult<T> =
    iterator().minmaxByKey(key)

/**
 * Return the minimum and maximum element of an iterator, as determined by the comparison function.
 */
fun <T> Iterator<T>.minmaxBy(compare: (T, T) -> Int): MinMaxResult<T> =
    minmaxImpl(this, { }) { x, y, _, _ -> compare(x, y) < 0 }

/**
 * Return the minimum and maximum element of an iterable, as determined by the comparison function.
 */
fun <T> Iterable<T>.minmaxBy(compare: (T, T) -> Int): MinMaxResult<T> =
    iterator().minmaxBy(compare)

// ---------------------------------------------------------------------------
// Free Functions
// ---------------------------------------------------------------------------

/**
 * Return `true` if both iterables produce equal sequences, `false` otherwise.
 */
fun <T> equal(a: Iterable<T>, b: Iterable<T>): Boolean {
    val ia = a.iterator()
    val ib = b.iterator()
    while (ia.hasNext() && ib.hasNext()) {
        if (ia.next() != ib.next()) return false
    }
    return !ia.hasNext() && !ib.hasNext()
}

/**
 * Assert that two iterables produce equal sequences.
 */
fun <T> assertEqual(a: Iterable<T>, b: Iterable<T>) {
    val ia = a.iterator()
    val ib = b.iterator()
    var i = 0
    while (true) {
        when {
            !ia.hasNext() && !ib.hasNext() -> return
            ia.hasNext() && ib.hasNext() -> {
                val va = ia.next()
                val vb = ib.next()
                if (va != vb) {
                    throw AssertionError("Failed assertion $va == $vb for iteration $i")
                }
                i += 1
            }
            else -> {
                throw AssertionError("Failed assertion: sequences have different lengths at iteration $i")
            }
        }
    }
}

/**
 * Partition a mutable list so that elements matching [predicate] are placed first.
 * Return the index of the split point.
 */
fun <T> partition(data: MutableList<T>, predicate: (T) -> Boolean): Int {
    var splitIndex = 0
    var rightIndex = data.size - 1
    while (splitIndex <= rightIndex) {
        if (predicate(data[splitIndex])) {
            splitIndex += 1
        } else {
            while (rightIndex > splitIndex && !predicate(data[rightIndex])) {
                rightIndex -= 1
            }
            if (rightIndex > splitIndex) {
                val tmp = data[splitIndex]
                data[splitIndex] = data[rightIndex]
                data[rightIndex] = tmp
                splitIndex += 1
                rightIndex -= 1
            } else {
                break
            }
        }
    }
    return splitIndex
}
