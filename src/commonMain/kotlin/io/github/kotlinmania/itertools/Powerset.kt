// port-lint: source powerset.rs
package io.github.kotlinmania.itertools

/**
 * An iterator to iterate through the powerset of the elements from an iterator.
 *
 * See [powerset] for more information.
 */
class Powerset<T>(
    src: Iterator<T>,
    sourceHint: SizeHint = SizeHint(0, null),
) : Iterator<List<T>> {
    private val combs: Combinations<T> = Combinations(src, 0, sourceHint)
    private var hasMore: Boolean = true

    private fun incrementK(): Boolean =
        if (combs.k() < combs.n() || combs.k() == 0) {
            combs.reset(combs.k() + 1)
            true
        } else {
            false
        }

    override fun hasNext(): Boolean {
        if (!hasMore) return false
        if (combs.hasNext()) return true
        if (incrementK()) {
            return combs.hasNext()
        }
        hasMore = false
        return false
    }

    override fun next(): List<T> {
        if (combs.hasNext()) {
            return combs.next()
        } else if (incrementK()) {
            return combs.next()
        } else {
            hasMore = false
            throw NoSuchElementException("Powerset exhausted")
        }
    }

    /** Returns the n-th element of the powerset. */
    fun nth(n: Int): List<T>? {
        var nRemaining = n
        while (true) {
            when (val res = combs.tryNthResult(nRemaining)) {
                is ItemResult.Ok -> return res.value
                is ItemResult.Err -> {
                    if (!incrementK()) {
                        return null
                    }
                    nRemaining -= res.error
                }
            }
        }
    }

    /** Size hint for remaining powerset elements. */
    fun sizeHint(): SizeHint {
        val k = combs.k()
        val (nMin, nMax) = combs.src().sizeHint()
        val low = remainingFor(nMin, k) ?: Int.MAX_VALUE
        val upp = nMax?.let { remainingFor(it, k) }
        return add(combs.sizeHint(), SizeHint(low, upp))
    }

    /** Returns the count of remaining powerset elements. */
    fun count(): Int {
        val k = combs.k()
        val (n, combsCount) = combs.nAndCount()
        return combsCount + (remainingFor(n, k) ?: 0)
    }

    /** Folds the elements of the powerset. */
    fun <B> fold(init: B, f: (B, List<T>) -> B): B {
        var acc = init
        if (combs.k() == 0) {
            while (combs.hasNext()) {
                acc = f(acc, combs.next())
            }
            combs.reset(1)
        }
        while (combs.hasNext()) {
            acc = f(acc, combs.next())
        }
        val n = combs.n()
        for (k in (combs.k() + 1)..n) {
            combs.reset(k)
            while (combs.hasNext()) {
                acc = f(acc, combs.next())
            }
        }
        return acc
    }
}

private fun remainingFor(n: Int, k: Int): Int? {
    var sum = 0
    for (i in (k + 1)..n) {
        val bin = checkedBinomial(n, i) ?: return null
        val newSum = sum.toLong() + bin.toLong()
        if (newSum > Int.MAX_VALUE.toLong()) return null
        sum = newSum.toInt()
    }
    return sum
}

/**
 * Create a new [Powerset] from an [Iterable].
 */
fun <T> powerset(src: Iterable<T>): Powerset<T> {
    val hint =
        when (src) {
            is Collection<*> -> SizeHint(src.size, src.size)
            else -> SizeHint(0, null)
        }
    return Powerset(src.iterator(), hint)
}

/**
 * Create a new [Powerset] from an [Iterator].
 */
fun <T> powerset(src: Iterator<T>, hint: SizeHint = SizeHint(0, null)): Powerset<T> =
    Powerset(src, hint)
