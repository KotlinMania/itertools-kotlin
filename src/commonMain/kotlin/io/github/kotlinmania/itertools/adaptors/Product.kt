package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

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
    fun sizeHint(): SizeHint {
        val aHint = sizeHintOf(iterA)
        val bOrigHint = sizeHintOf(curIterB)
        var sh =
            io.github.kotlinmania.itertools
                .mul(aHint, bOrigHint)
        if (curA.isNotEmpty()) {
            sh =
                io.github.kotlinmania.itertools
                    .add(sh, sizeHintOf(curIterB))
        }
        return sh
    }

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
