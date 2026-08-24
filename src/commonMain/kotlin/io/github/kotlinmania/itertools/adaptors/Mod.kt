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
    private var top: T? = null
    private var hasTop: Boolean = false

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
    fun intoParts(): Pair<T?, Iterator<T>> = Pair(if (hasTop) top else null, iter)

    /**
     * Put back a single value to the front of the iterator.
     * If a value is already in the put back slot, it is returned.
     */
    fun putBack(x: T): T? {
        val old = if (hasTop) top else null
        top = x
        hasTop = true
        return old
    }

    override fun hasNext(): Boolean = hasTop || iter.hasNext()

    override fun next(): T {
        if (hasTop) {
            hasTop = false
            val value = top
            top = null
            @Suppress("UNCHECKED_CAST")
            return value as T
        }
        return iter.next()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(if (hasTop) 1 else 0, null)

    /** Count remaining elements. */
    fun count(): Int {
        var c = if (hasTop) 1 else 0
        while (iter.hasNext()) {
            iter.next()
            c++
        }
        return c
    }

    /** Return the last element. */
    fun last(): T? {
        var lastVal: T? = if (hasTop) top else null
        while (iter.hasNext()) {
            lastVal = iter.next()
        }
        return lastVal
    }

    /** Return the nth element. */
    fun nth(n: Int): T? {
        var rem = n
        if (hasTop) {
            if (rem == 0) {
                return next()
            }
            hasTop = false
            top = null
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
    private var curA: A? = null
    private var hasCurA: Boolean = false
    private var curIterB: Iterator<B> = iterBFactory()

    override fun hasNext(): Boolean {
        if (!hasCurA) {
            if (iterA.hasNext()) {
                curA = iterA.next()
                hasCurA = true
                curIterB = iterBFactory()
            } else {
                return false
            }
        }
        while (!curIterB.hasNext()) {
            if (iterA.hasNext()) {
                curA = iterA.next()
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
        @Suppress("UNCHECKED_CAST")
        return Pair(curA as A, curIterB.next())
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
    private var peeked: T? = null
    private var hasPeeked: Boolean = false
    private var done: Boolean = false

    override fun hasNext(): Boolean {
        if (done) return false
        if (hasPeeked) return true
        if (!iter.hasNext()) {
            done = true
            return false
        }
        val nextVal = iter.next()
        if (predicate(nextVal)) {
            peeked = nextVal
            hasPeeked = true
            return true
        }
        done = true
        return false
    }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("TakeWhileRef exhausted")
        }
        hasPeeked = false
        val value = peeked
        peeked = null
        @Suppress("UNCHECKED_CAST")
        return value as T
    }
}

/**
 * Create a new [TakeWhileRef] iterator.
 */
fun <T> takeWhileRef(iterable: Iterable<T>, predicate: (T) -> Boolean): TakeWhileRef<T> =
    TakeWhileRef(iterable.iterator(), predicate)

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
