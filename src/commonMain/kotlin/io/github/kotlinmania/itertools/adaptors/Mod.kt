// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult
import io.github.kotlinmania.itertools.SizeHint

/**
 * An iterator adaptor that alternates elements from two iterators until both run out.
 *
 * This iterator is *fused*.
 */
class Interleave<T>(
    private val i: Iterator<T>,
    private val j: Iterator<T>,
) : Iterator<T> {
    private var nextComingFromJ = false

    override fun hasNext(): Boolean = i.hasNext() || j.hasNext()

    override fun next(): T {
        nextComingFromJ = !nextComingFromJ
        return if (nextComingFromJ) {
            if (i.hasNext()) {
                i.next()
            } else if (j.hasNext()) {
                j.next()
            } else {
                throw NoSuchElementException("Interleave exhausted")
            }
        } else {
            if (j.hasNext()) {
                j.next()
            } else if (i.hasNext()) {
                i.next()
            } else {
                throw NoSuchElementException("Interleave exhausted")
            }
        }
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val iHint = if (i is SizedIterator<*>) i.sizeHint() else SizeHint(0, null)
        val jHint = if (j is SizedIterator<*>) j.sizeHint() else SizeHint(0, null)
        return io.github.kotlinmania.itertools
            .add(iHint, jHint)
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create an iterator that interleaves elements in [i] and [j].
 */
fun <T> interleave(i: Iterator<T>, j: Iterator<T>): Interleave<T> = Interleave(i, j)

/**
 * Create an iterator that interleaves elements in [i] and [j].
 */
fun <T> interleave(i: Iterable<T>, j: Iterable<T>): Interleave<T> = Interleave(i.iterator(), j.iterator())

/**
 * An iterator adaptor that alternates elements from the two iterators until one of them runs out.
 *
 * This iterator is *fused*.
 */
class InterleaveShortest<T>(
    private val i: Iterator<T>,
    private val j: Iterator<T>,
) : Iterator<T> {
    private var nextComingFromJ = false

    override fun hasNext(): Boolean =
        if (nextComingFromJ) {
            j.hasNext()
        } else {
            i.hasNext()
        }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("InterleaveShortest exhausted")
        }
        val e =
            if (nextComingFromJ) {
                j.next()
            } else {
                i.next()
            }
        nextComingFromJ = !nextComingFromJ
        return e
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val iHint = if (i is SizedIterator<*>) i.sizeHint() else SizeHint(0, null)
        val jHint = if (j is SizedIterator<*>) j.sizeHint() else SizeHint(0, null)
        val (currHint, nextHint) = if (nextComingFromJ) Pair(jHint, iHint) else Pair(iHint, jHint)
        val minHint =
            io.github.kotlinmania.itertools
                .min(currHint, nextHint)
        val combined =
            io.github.kotlinmania.itertools
                .mulScalar(minHint, 2)
        val lower = if (currHint.lower > nextHint.lower) combined.lower + 1 else combined.lower
        val upper =
            if (currHint.upper != null && nextHint.upper != null && currHint.upper > nextHint.upper) {
                combined.upper?.plus(1)
            } else {
                combined.upper
            }
        return SizeHint(lower, upper)
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create a new [InterleaveShortest] iterator.
 */
fun <T> interleaveShortest(i: Iterator<T>, j: Iterator<T>): InterleaveShortest<T> = InterleaveShortest(i, j)

/**
 * Create a new [InterleaveShortest] iterator from iterables.
 */
fun <T> interleaveShortest(i: Iterable<T>, j: Iterable<T>): InterleaveShortest<T> =
    InterleaveShortest(i.iterator(), j.iterator())

/**
 * An iterator adaptor that allows putting back a single item to the front of the iterator.
 */
class PutBack<T>(
    private val iter: Iterator<T>,
) : Iterator<T> {
    private val top: ArrayDeque<T> = ArrayDeque(1)

    /**
     * Put back value [value] (builder method).
     */
    fun withValue(value: T): PutBack<T> {
        putBack(value)
        return this
    }

    /**
     * Split the [PutBack] into its parts.
     */
    fun intoParts(): Pair<T?, Iterator<T>> = Pair(top.firstOrNull(), iter)

    /**
     * Put back a single value to the front of the iterator.
     * If a value is already in the put back slot, it is returned.
     */
    fun putBack(x: T): T? {
        val old = if (top.isNotEmpty()) top.removeFirst() else null
        top.addLast(x)
        return old
    }

    override fun hasNext(): Boolean = top.isNotEmpty() || iter.hasNext()

    override fun next(): T {
        if (top.isNotEmpty()) {
            return top.removeFirst()
        }
        return iter.next()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(if (top.isNotEmpty()) 1 else 0, null)

    /** Count remaining elements. */
    fun count(): Int {
        var c = if (top.isNotEmpty()) 1 else 0
        while (iter.hasNext()) {
            iter.next()
            c++
        }
        return c
    }

    /** Return the last element. */
    fun last(): T? {
        var lastVal: T? = if (top.isNotEmpty()) top.first() else null
        while (iter.hasNext()) {
            lastVal = iter.next()
        }
        return lastVal
    }

    /** Return the nth element. */
    fun nth(n: Int): T? {
        var rem = n
        if (top.isNotEmpty()) {
            if (rem == 0) {
                return next()
            }
            top.removeFirst()
            rem--
        }
        while (iter.hasNext()) {
            val v = iter.next()
            if (rem == 0) return v
            rem--
        }
        return null
    }

    /** Test if all elements satisfy predicate. */
    fun all(predicate: (T) -> Boolean): Boolean {
        while (hasNext()) {
            if (!predicate(next())) return false
        }
        return true
    }

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, T) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create an iterator where you can put back a single item.
 */
fun <T> putBack(iterable: Iterable<T>): PutBack<T> = PutBack(iterable.iterator())

/**
 * Create an iterator where you can put back a single item.
 */
fun <T> putBack(iter: Iterator<T>): PutBack<T> = PutBack(iter)

/**
 * An iterator adaptor that iterates over the cartesian product of two iterators.
 */
class Product<A, B>(
    private val iterA: Iterator<A>,
    private val iterBFactory: () -> Iterator<B>,
) : Iterator<Pair<A, B>> {
    private val curA: ArrayDeque<A> = ArrayDeque(1)
    private var curIterB: Iterator<B> = iterBFactory()

    override fun hasNext(): Boolean {
        if (curA.isEmpty()) {
            if (iterA.hasNext()) {
                curA.addLast(iterA.next())
                curIterB = iterBFactory()
            } else {
                return false
            }
        }
        while (!curIterB.hasNext()) {
            if (iterA.hasNext()) {
                curA.removeFirst()
                curA.addLast(iterA.next())
                curIterB = iterBFactory()
            } else {
                return false
            }
        }
        return true
    }

    override fun next(): Pair<A, B> {
        if (!hasNext()) {
            throw NoSuchElementException("Product exhausted")
        }
        return Pair(curA.first(), curIterB.next())
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, Pair<A, B>) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create a new cartesian product iterator.
 */
fun <A, B> cartesianProduct(a: Iterable<A>, b: Iterable<B>): Product<A, B> =
    Product(a.iterator()) { b.iterator() }

/**
 * Create a new cartesian product iterator from iterators.
 */
fun <A, B> cartesianProduct(a: Iterator<A>, bFactory: () -> Iterator<B>): Product<A, B> =
    Product(a, bFactory)

/**
 * A "meta iterator adaptor". Its closure receives a reference to the iterator
 * and may pick off as many elements as it likes, to produce the next iterator element.
 */
class Batching<T, R>(
    private val iter: Iterator<T>,
    private val f: (Iterator<T>) -> R?,
) : Iterator<R> {
    private var nextItem: R? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            nextItem = f(iter)
            hasNextCalculated = true
        }
        return nextItem != null
    }

    override fun next(): R {
        if (!hasNext()) {
            throw NoSuchElementException("Batching iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("Batching iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }
}

/**
 * Create a new [Batching] iterator.
 */
fun <T, R> batching(iter: Iterator<T>, f: (Iterator<T>) -> R?): Batching<T, R> = Batching(iter, f)

/**
 * Create a new [Batching] iterator from an [Iterable].
 */
fun <T, R> batching(iterable: Iterable<T>, f: (Iterator<T>) -> R?): Batching<T, R> =
    Batching(iterable.iterator(), f)

/**
 * An iterator adapter that yields elements while [predicate] returns true.
 */
class TakeWhileRef<T>(
    private val iter: Iterator<T>,
    private val predicate: (T) -> Boolean,
) : Iterator<T> {
    private val peeked: ArrayDeque<T> = ArrayDeque(1)
    private var done: Boolean = false

    override fun hasNext(): Boolean {
        if (done) return false
        if (peeked.isNotEmpty()) return true
        if (!iter.hasNext()) {
            done = true
            return false
        }
        val nextVal = iter.next()
        if (predicate(nextVal)) {
            peeked.addLast(nextVal)
            return true
        }
        done = true
        return false
    }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("TakeWhileRef exhausted")
        }
        return peeked.removeFirst()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)
}

/**
 * Create a new [TakeWhileRef] iterator.
 */
fun <T> takeWhileRef(iterable: Iterable<T>, predicate: (T) -> Boolean): TakeWhileRef<T> =
    TakeWhileRef(iterable.iterator(), predicate)

/**
 * Create a new [TakeWhileRef] iterator from an [Iterator].
 */
fun <T> takeWhileRef(iter: Iterator<T>, predicate: (T) -> Boolean): TakeWhileRef<T> =
    TakeWhileRef(iter, predicate)

/**
 * An iterator adaptor that filters nullable iterator elements and produces non-null elements.
 * Stops on the first null encountered.
 */
class WhileSome<T : Any>(
    private val iter: Iterator<T?>,
) : Iterator<T> {
    private var nextItem: T? = null
    private var hasNextCalculated = false
    private var exhausted = false

    override fun hasNext(): Boolean {
        if (exhausted) return false
        if (!hasNextCalculated) {
            if (iter.hasNext()) {
                val item = iter.next()
                if (item != null) {
                    nextItem = item
                    hasNextCalculated = true
                } else {
                    exhausted = true
                    return false
                }
            } else {
                exhausted = true
                return false
            }
        }
        return true
    }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("WhileSome iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("WhileSome iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create a new [WhileSome] iterator.
 */
fun <T : Any> whileSome(iter: Iterator<T?>): WhileSome<T> = WhileSome(iter)

/**
 * Create a new [WhileSome] iterator from an [Iterable].
 */
fun <T : Any> whileSome(iterable: Iterable<T?>): WhileSome<T> = WhileSome(iterable.iterator())

/**
 * Trait/interface for combination types.
 */
interface HasCombination<T>

/**
 * Combination type alias.
 */
typealias Combination<T> = Iterator<T>

/**
 * Item type alias for adaptors.
 */
typealias Item = Any?

/**
 * An iterator that produces 1-combinations (single element tuples).
 */
class Tuple1Combination<T>(
    private val iter: Iterator<T>,
) : Iterator<T>,
    HasCombination<T> {
    companion object {
        /** Create from an iterator. */
        fun <T> from(iter: Iterator<T>): Tuple1Combination<T> = Tuple1Combination(iter)
    }

    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("Tuple1Combination exhausted")
        }
        return iter.next()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Count remaining elements. */
    fun count(): Int {
        var c = 0
        while (iter.hasNext()) {
            iter.next()
            c++
        }
        return c
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * An iterator that produces 2-combinations (pairs) from a sequence of elements.
 */
class Tuple2Combination<T>(
    private val items: List<T>,
) : Iterator<Pair<T, T>>,
    HasCombination<T> {
    private var i = 0
    private var j = 1

    companion object {
        /** Create from a list. */
        fun <T> from(items: List<T>): Tuple2Combination<T> = Tuple2Combination(items)

        /** Create from an iterator. */
        fun <T> from(iter: Iterator<T>): Tuple2Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple2Combination(list)
        }
    }

    override fun hasNext(): Boolean {
        while (i < items.size) {
            if (j < items.size) return true
            i++
            j = i + 1
        }
        return false
    }

    override fun next(): Pair<T, T> {
        if (!hasNext()) {
            throw NoSuchElementException("Tuple2Combination exhausted")
        }
        val result = Pair(items[i], items[j])
        j++
        return result
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val rem = countRemaining()
        return SizeHint(rem, rem)
    }

    /** Count remaining elements. */
    fun count(): Int = countRemaining()

    private fun countRemaining(): Int {
        val n = items.size
        if (i >= n) return 0
        var total = 0
        if (j < n) total += (n - j)
        for (idx in (i + 1) until n) {
            total += (n - idx - 1)
        }
        return total
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, Pair<T, T>) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * An iterator that produces 3-combinations (triples) from a sequence of elements.
 */
class Tuple3Combination<T>(
    private val items: List<T>,
) : Iterator<Triple<T, T, T>>,
    HasCombination<T> {
    private var i = 0
    private var j = 1
    private var k = 2

    companion object {
        /** Create from a list. */
        fun <T> from(items: List<T>): Tuple3Combination<T> = Tuple3Combination(items)

        /** Create from an iterator. */
        fun <T> from(iter: Iterator<T>): Tuple3Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple3Combination(list)
        }
    }

    override fun hasNext(): Boolean {
        val n = items.size
        while (i < n) {
            while (j < n) {
                if (k < n) return true
                j++
                k = j + 1
            }
            i++
            j = i + 1
            k = j + 1
        }
        return false
    }

    override fun next(): Triple<T, T, T> {
        if (!hasNext()) {
            throw NoSuchElementException("Tuple3Combination exhausted")
        }
        val result = Triple(items[i], items[j], items[k])
        k++
        return result
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val rem = countRemaining()
        return SizeHint(rem, rem)
    }

    /** Count remaining elements. */
    fun count(): Int = countRemaining()

    private fun countRemaining(): Int {
        val n = items.size
        if (i >= n) return 0
        var total = 0
        var curI = i
        var curJ = j
        var curK = k
        while (curI < n) {
            while (curJ < n) {
                if (curK < n) {
                    total += (n - curK)
                }
                curJ++
                curK = curJ + 1
            }
            curI++
            curJ = curI + 1
            curK = curJ + 1
        }
        return total
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, Triple<T, T, T>) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Base class for k-combination iterators where k >= 4.
 */
abstract class BaseTupleKCombination<T>(
    protected val items: List<T>,
    protected val k: Int,
) : Iterator<List<T>>,
    HasCombination<T> {
    protected var indices: IntArray? = if (items.size >= k) IntArray(k) { it } else null

    override fun hasNext(): Boolean = indices != null

    override fun next(): List<T> {
        val idx = indices ?: throw NoSuchElementException("TupleCombination exhausted")
        val result = List(k) { items[idx[it]] }

        var i = k - 1
        while (i >= 0 && idx[i] == i + items.size - k) {
            i--
        }
        if (i < 0) {
            indices = null
        } else {
            idx[i]++
            for (j in i + 1 until k) {
                idx[j] = idx[j - 1] + 1
            }
        }
        return result
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint {
        val rem = countRemaining()
        return SizeHint(rem, rem)
    }

    /** Count remaining elements. */
    fun count(): Int = countRemaining()

    protected open fun countRemaining(): Int {
        val cur = indices ?: return 0
        var total = 1
        val cloneIdx = cur.copyOf()
        while (true) {
            var p = k - 1
            while (p >= 0 && cloneIdx[p] == p + items.size - k) {
                p--
            }
            if (p < 0) break
            cloneIdx[p] = cloneIdx[p] + 1
            for (j in p + 1 until k) {
                cloneIdx[j] = cloneIdx[j - 1] + 1
            }
            total++
        }
        return total
    }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, List<T>) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/** An iterator that produces 4-combinations from a sequence. */
class Tuple4Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 4) {
    companion object {
        fun <T> from(items: List<T>): Tuple4Combination<T> = Tuple4Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple4Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple4Combination(list)
        }
    }
}

/** An iterator that produces 5-combinations from a sequence. */
class Tuple5Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 5) {
    companion object {
        fun <T> from(items: List<T>): Tuple5Combination<T> = Tuple5Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple5Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple5Combination(list)
        }
    }
}

/** An iterator that produces 6-combinations from a sequence. */
class Tuple6Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 6) {
    companion object {
        fun <T> from(items: List<T>): Tuple6Combination<T> = Tuple6Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple6Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple6Combination(list)
        }
    }
}

/** An iterator that produces 7-combinations from a sequence. */
class Tuple7Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 7) {
    companion object {
        fun <T> from(items: List<T>): Tuple7Combination<T> = Tuple7Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple7Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple7Combination(list)
        }
    }
}

/** An iterator that produces 8-combinations from a sequence. */
class Tuple8Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 8) {
    companion object {
        fun <T> from(items: List<T>): Tuple8Combination<T> = Tuple8Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple8Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple8Combination(list)
        }
    }
}

/** An iterator that produces 9-combinations from a sequence. */
class Tuple9Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 9) {
    companion object {
        fun <T> from(items: List<T>): Tuple9Combination<T> = Tuple9Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple9Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple9Combination(list)
        }
    }
}

/** An iterator that produces 10-combinations from a sequence. */
class Tuple10Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 10) {
    companion object {
        fun <T> from(items: List<T>): Tuple10Combination<T> = Tuple10Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple10Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple10Combination(list)
        }
    }
}

/** An iterator that produces 11-combinations from a sequence. */
class Tuple11Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 11) {
    companion object {
        fun <T> from(items: List<T>): Tuple11Combination<T> = Tuple11Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple11Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple11Combination(list)
        }
    }
}

/** An iterator that produces 12-combinations from a sequence. */
class Tuple12Combination<T>(items: List<T>) : BaseTupleKCombination<T>(items, 12) {
    companion object {
        fun <T> from(items: List<T>): Tuple12Combination<T> = Tuple12Combination(items)
        fun <T> from(iter: Iterator<T>): Tuple12Combination<T> {
            val list = mutableListOf<T>()
            while (iter.hasNext()) list.add(iter.next())
            return Tuple12Combination(list)
        }
    }
}

/**
 * An iterator adaptor that produces combinations of elements.
 */
class TupleCombinations<T, C>(
    private val combination: Iterator<C>,
) : Iterator<C> {
    override fun hasNext(): Boolean = combination.hasNext()

    override fun next(): C = combination.next()

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint =
        when (combination) {
            is Tuple1Combination<*> -> combination.sizeHint()
            is Tuple2Combination<*> -> combination.sizeHint()
            is Tuple3Combination<*> -> combination.sizeHint()
            is BaseTupleKCombination<*> -> combination.sizeHint()
            else -> SizeHint(0, null)
        }

    /** Count remaining elements. */
    fun count(): Int =
        when (combination) {
            is Tuple1Combination<*> -> combination.count()
            is Tuple2Combination<*> -> combination.count()
            is Tuple3Combination<*> -> combination.count()
            is BaseTupleKCombination<*> -> combination.count()
            else -> {
                var c = 0
                while (combination.hasNext()) {
                    combination.next()
                    c++
                }
                c
            }
        }

    /** Fold over elements. */
    fun <B> fold(init: B, f: (B, C) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

/**
 * Create a new [TupleCombinations] iterator for 2-combinations.
 */
fun <T> tupleCombinations(iterable: Iterable<T>): Tuple2Combination<T> =
    Tuple2Combination(iterable.toList())

/**
 * An iterator to iterate through all combinations in a sequence that produces tuples of size 2.
 */
fun <T> tupleCombinations2(iterable: Iterable<T>): Tuple2Combination<T> =
    Tuple2Combination(iterable.toList())

/**
 * An iterator to iterate through all combinations in a sequence that produces tuples of size 3.
 */
fun <T> tupleCombinations3(iterable: Iterable<T>): Tuple3Combination<T> =
    Tuple3Combination(iterable.toList())

/**
 * An iterator to iterate through all combinations in a sequence that produces tuples of size 4.
 */
fun <T> tupleCombinations4(iterable: Iterable<T>): Tuple4Combination<T> =
    Tuple4Combination(iterable.toList())

/**
 * Calculates binomial coefficient n choose k safely.
 */
fun checkedBinomial(n: Int, k: Int): Int? {
    if (n < k) return 0
    val kSym = minOf(n - k, k)
    var c = 1L
    var curN = n.toLong()
    for (i in 1..kSym) {
        val div = (c / i) * curN
        val rem = ((c % i) * curN) / i
        c = div + rem
        if (c > Int.MAX_VALUE) return null
        curN -= 1
    }
    return c.toInt()
}

/**
 * An iterator adapter to filter values within a nested [ItemResult.Ok].
 */
class FilterOk<T, E> internal constructor(
    private val iter: Iterator<ItemResult<T, E>>,
    private val predicate: (T) -> Boolean,
) : Iterator<ItemResult<T, E>> {
    private var nextItem: ItemResult<T, E>? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            while (iter.hasNext()) {
                val item = iter.next()
                when (item) {
                    is ItemResult.Ok -> {
                        if (predicate(item.value)) {
                            nextItem = item
                            hasNextCalculated = true
                            return true
                        }
                    }
                    is ItemResult.Err -> {
                        nextItem = item
                        hasNextCalculated = true
                        return true
                    }
                }
            }
            return false
        }
        return true
    }

    override fun next(): ItemResult<T, E> {
        if (!hasNext()) {
            throw NoSuchElementException("FilterOk iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("FilterOk iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, ItemResult<T, E>) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }

    /** Collect remaining elements into a list. */
    fun collect(): List<ItemResult<T, E>> {
        val result = mutableListOf<ItemResult<T, E>>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    /** Yield the next element from the back if double-ended. */
    fun nextBack(): ItemResult<T, E>? {
        val list = collect()
        return list.lastOrNull()
    }

    /** Fold from the back. */
    fun <Acc> rfold(init: Acc, f: (Acc, ItemResult<T, E>) -> Acc): Acc {
        val list = collect()
        var acc = init
        for (i in list.indices.reversed()) {
            acc = f(acc, list[i])
        }
        return acc
    }
}

/**
 * Create a new [FilterOk] iterator.
 */
fun <T, E> filterOk(iter: Iterator<ItemResult<T, E>>, predicate: (T) -> Boolean): FilterOk<T, E> =
    FilterOk(iter, predicate)

/**
 * Create a new [FilterOk] iterator from an [Iterable].
 */
fun <T, E> filterOk(iterable: Iterable<ItemResult<T, E>>, predicate: (T) -> Boolean): FilterOk<T, E> =
    FilterOk(iterable.iterator(), predicate)

/**
 * An iterator adapter to filter and apply a transformation on values within a nested [ItemResult.Ok].
 */
class FilterMapOk<T, U, E> internal constructor(
    private val iter: Iterator<ItemResult<T, E>>,
    private val transform: (T) -> U?,
) : Iterator<ItemResult<U, E>> {
    private var nextItem: ItemResult<U, E>? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            while (iter.hasNext()) {
                val item = iter.next()
                when (item) {
                    is ItemResult.Ok -> {
                        val mapped = transform(item.value)
                        if (mapped != null) {
                            nextItem = ItemResult.Ok(mapped)
                            hasNextCalculated = true
                            return true
                        }
                    }
                    is ItemResult.Err -> {
                        nextItem = ItemResult.Err(item.error)
                        hasNextCalculated = true
                        return true
                    }
                }
            }
            return false
        }
        return true
    }

    override fun next(): ItemResult<U, E> {
        if (!hasNext()) {
            throw NoSuchElementException("FilterMapOk iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("FilterMapOk iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, f: (Acc, ItemResult<U, E>) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }

    /** Collect remaining elements into a list. */
    fun collect(): List<ItemResult<U, E>> {
        val result = mutableListOf<ItemResult<U, E>>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    /** Yield the next element from the back if double-ended. */
    fun nextBack(): ItemResult<U, E>? {
        val list = collect()
        return list.lastOrNull()
    }

    /** Fold from the back. */
    fun <Acc> rfold(init: Acc, f: (Acc, ItemResult<U, E>) -> Acc): Acc {
        val list = collect()
        var acc = init
        for (i in list.indices.reversed()) {
            acc = f(acc, list[i])
        }
        return acc
    }
}

/**
 * Create a new [FilterMapOk] iterator.
 */
fun <T, U, E> filterMapOk(iter: Iterator<ItemResult<T, E>>, transform: (T) -> U?): FilterMapOk<T, U, E> =
    FilterMapOk(iter, transform)

/**
 * Create a new [FilterMapOk] iterator from an [Iterable].
 */
fun <T, U, E> filterMapOk(iterable: Iterable<ItemResult<T, E>>, transform: (T) -> U?): FilterMapOk<T, U, E> =
    FilterMapOk(iterable.iterator(), transform)

/**
 * An iterator adapter to get the positions of each element that matches a predicate.
 */
class Positions<T>(
    private val iter: Iterator<T>,
    private val predicate: (T) -> Boolean,
) : Iterator<Int> {
    private var currentIndex = 0
    private var nextIndex: Int? = null
    private var hasNextCalculated = false

    override fun hasNext(): Boolean {
        if (!hasNextCalculated) {
            while (iter.hasNext()) {
                val item = iter.next()
                val idx = currentIndex
                currentIndex++
                if (predicate(item)) {
                    nextIndex = idx
                    hasNextCalculated = true
                    return true
                }
            }
            return false
        }
        return true
    }

    override fun next(): Int {
        if (!hasNext()) {
            throw NoSuchElementException("Positions iterator exhausted")
        }
        val idx = nextIndex ?: throw NoSuchElementException("Positions iterator exhausted")
        nextIndex = null
        hasNextCalculated = false
        return idx
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Yield the previous matching position from the back. */
    fun nextBack(): Int? {
        val list = mutableListOf<Int>()
        while (hasNext()) {
            list.add(next())
        }
        return list.lastOrNull()
    }

    /** Fold over matching positions from the back. */
    fun <B> rfold(init: B, f: (B, Int) -> B): B {
        val list = mutableListOf<Int>()
        while (hasNext()) {
            list.add(next())
        }
        var acc = init
        for (i in list.indices.reversed()) {
            acc = f(acc, list[i])
        }
        return acc
    }
}

/**
 * Create a new [Positions] iterator.
 */
fun <T> positions(iter: Iterator<T>, predicate: (T) -> Boolean): Positions<T> =
    Positions(iter, predicate)

/**
 * Create a new [Positions] iterator from an [Iterable].
 */
fun <T> positions(iterable: Iterable<T>, predicate: (T) -> Boolean): Positions<T> =
    Positions(iterable.iterator(), predicate)

/**
 * An iterator adapter to apply a mutating function to each element before yielding it.
 */
class Update<T>(
    private val iter: Iterator<T>,
    private val action: (T) -> Unit,
) : Iterator<T> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): T {
        val item = iter.next()
        action(item)
        return item
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Fold over elements. */
    fun <Acc> fold(init: Acc, g: (Acc, T) -> Acc): Acc {
        var acc = init
        while (hasNext()) {
            val item = iter.next()
            action(item)
            acc = g(acc, item)
        }
        return acc
    }

    /** Collect remaining elements. */
    fun collect(): List<T> {
        val result = mutableListOf<T>()
        while (hasNext()) {
            result.add(next())
        }
        return result
    }

    /** Yield the previous element from the back. */
    fun nextBack(): T? {
        val list = collect()
        return list.lastOrNull()
    }
}

/**
 * Create a new [Update] iterator.
 */
fun <T> update(iter: Iterator<T>, action: (T) -> Unit): Update<T> =
    Update(iter, action)

/**
 * Create a new [Update] iterator from an [Iterable].
 */
fun <T> update(iterable: Iterable<T>, action: (T) -> Unit): Update<T> =
    Update(iterable.iterator(), action)

/**
 * Transposes an ItemResult of an Iterator into an Iterator of ItemResult.
 */
fun <T, E> transposeResult(result: ItemResult<Iterator<T>, E>): Iterator<ItemResult<T, E>> =
    when (result) {
        is ItemResult.Ok ->
            object : Iterator<ItemResult<T, E>> {
                private val it = result.value

                override fun hasNext(): Boolean = it.hasNext()

                override fun next(): ItemResult<T, E> = ItemResult.Ok(it.next())
            }
        is ItemResult.Err ->
            object : Iterator<ItemResult<T, E>> {
                private var yielded = false

                override fun hasNext(): Boolean = !yielded

                override fun next(): ItemResult<T, E> {
                    if (yielded) throw NoSuchElementException()
                    yielded = true
                    return ItemResult.Err(result.error)
                }
            }
    }

private interface SizedIterator<T> : Iterator<T> {
    fun sizeHint(): SizeHint
}
