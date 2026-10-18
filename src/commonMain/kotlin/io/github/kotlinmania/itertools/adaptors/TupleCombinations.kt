package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

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
    fun sizeHint(): SizeHint = sizeHintOf(iter)

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
open class BaseTupleKCombination<T>(
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
class Tuple4Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 4) {
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
class Tuple5Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 5) {
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
class Tuple6Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 6) {
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
class Tuple7Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 7) {
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
class Tuple8Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 8) {
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
class Tuple9Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 9) {
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
class Tuple10Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 10) {
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
class Tuple11Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 11) {
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
class Tuple12Combination<T>(
    items: List<T>,
) : BaseTupleKCombination<T>(items, 12) {
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
