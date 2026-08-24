// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.ItemResult
import io.github.kotlinmania.itertools.SizeHint

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

    fun sizeHint(): SizeHint = SizeHint(0, null)

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
 * An iterator adapter that yields elements while [predicate] returns true, passing references.
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
}

/**
 * Create a new [TakeWhileRef] iterator.
 */
fun <T> takeWhileRef(iterable: Iterable<T>, predicate: (T) -> Boolean): TakeWhileRef<T> =
    TakeWhileRef(iterable.iterator(), predicate)

/**
 * An iterator that produces 1-combinations (single element tuples).
 */
class Tuple1Combination<T>(
    private val iter: Iterator<T>,
) : Iterator<T> {
    override fun hasNext(): Boolean = iter.hasNext()

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("Tuple1Combination exhausted")
        }
        return iter.next()
    }

    fun sizeHint(): SizeHint = SizeHint(0, null)

    fun count(): Int {
        var c = 0
        while (iter.hasNext()) {
            iter.next()
            c++
        }
        return c
    }

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
) : Iterator<Pair<T, T>> {
    private var i = 0
    private var j = 1

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

    fun sizeHint(): SizeHint {
        val rem = countRemaining()
        return SizeHint(rem, rem)
    }

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
) : Iterator<Triple<T, T, T>> {
    private var i = 0
    private var j = 1
    private var k = 2

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

    fun sizeHint(): SizeHint {
        val rem = countRemaining()
        return SizeHint(rem, rem)
    }

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

    fun <B> fold(init: B, f: (B, Triple<T, T, T>) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }
}

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
