// port-lint: source itertools/src/ziptuple.rs
package io.github.kotlinmania.itertools

/**
 * An iterator adaptor for running multiple iterators in lockstep.
 */
class Zip<T>(
    val t: T,
)

/**
 * An iterator that generalizes `.zip()` and allows running multiple iterators in lockstep.
 */
class Zip2<A, B>(
    private val a: Iterator<A>,
    private val b: Iterator<B>,
    private val aList: List<A>? = null,
    private val bList: List<B>? = null,
) : Iterator<Pair<A, B>> {
    private var aStart: Int = 0
    private var aEnd: Int = aList?.size ?: 0
    private var bStart: Int = 0
    private var bEnd: Int = bList?.size ?: 0
    private var peeked: Pair<A, B>? = null
    private var exhausted = false

    init {
        if (aList != null && bList != null) {
            val minLen = minOf(aList.size, bList.size)
            aEnd = minLen
            bEnd = minLen
        }
    }

    constructor(aList: List<A>, bList: List<B>) : this(
        a = aList.iterator(),
        b = bList.iterator(),
        aList = aList,
        bList = bList,
    )

    private fun advance(): Boolean {
        if (peeked != null) return true
        if (exhausted) return false
        if (aList != null && bList != null) {
            if (aStart >= aEnd || bStart >= bEnd) {
                exhausted = true
                return false
            }
            peeked = Pair(aList[aStart++], bList[bStart++])
            return true
        }
        if (!a.hasNext()) {
            exhausted = true
            return false
        }
        val first = a.next()
        if (!b.hasNext()) {
            exhausted = true
            return false
        }
        val second = b.next()
        peeked = Pair(first, second)
        return true
    }

    override fun hasNext(): Boolean = advance()

    override fun next(): Pair<A, B> {
        if (!advance()) {
            throw NoSuchElementException("Zip2 exhausted")
        }
        val result = peeked ?: throw NoSuchElementException("Zip2 exhausted")
        peeked = null
        return result
    }

    /**
     * Returns the next element from the back when double-ended iteration is available.
     */
    fun nextBack(): Pair<A, B>? {
        if (aList == null || bList == null) return null
        if (aStart >= aEnd || bStart >= bEnd) return null
        return Pair(aList[--aEnd], bList[--bEnd])
    }
}

/**
 * An iterator that generalizes `.zip()` and allows running 3 iterators in lockstep.
 */
class Zip3<A, B, C>(
    private val a: Iterator<A>,
    private val b: Iterator<B>,
    private val c: Iterator<C>,
) : Iterator<Triple<A, B, C>> {
    private var peeked: Triple<A, B, C>? = null
    private var exhausted = false

    private fun advance(): Boolean {
        if (peeked != null) return true
        if (exhausted) return false
        if (!a.hasNext()) {
            exhausted = true
            return false
        }
        val first = a.next()
        if (!b.hasNext()) {
            exhausted = true
            return false
        }
        val second = b.next()
        if (!c.hasNext()) {
            exhausted = true
            return false
        }
        val third = c.next()
        peeked = Triple(first, second, third)
        return true
    }

    override fun hasNext(): Boolean = advance()

    override fun next(): Triple<A, B, C> {
        if (!advance()) {
            throw NoSuchElementException("Zip3 exhausted")
        }
        val result = peeked ?: throw NoSuchElementException("Zip3 exhausted")
        peeked = null
        return result
    }
}

/**
 * An iterator that generalizes `.zip()` over multiple iterators represented as a list.
 */
class MultiZip<T>(
    private val iters: List<Iterator<T>>,
) : Iterator<List<T>> {
    private var peeked: List<T>? = null
    private var exhausted = false

    private fun advance(): Boolean {
        if (peeked != null) return true
        if (exhausted || iters.isEmpty()) return false
        val list = ArrayList<T>(iters.size)
        for (iter in iters) {
            if (!iter.hasNext()) {
                exhausted = true
                return false
            }
            list.add(iter.next())
        }
        peeked = list
        return true
    }

    override fun hasNext(): Boolean = advance()

    override fun next(): List<T> {
        if (!advance()) {
            throw NoSuchElementException("MultiZip exhausted")
        }
        val result = peeked ?: throw NoSuchElementException("MultiZip exhausted")
        peeked = null
        return result
    }
}

/**
 * Multizip 2 iterators.
 */
fun <A, B> multizip(a: Iterator<A>, b: Iterator<B>): Zip2<A, B> =
    Zip2(a, b)

/**
 * Multizip 2 iterables.
 */
fun <A, B> multizip(a: Iterable<A>, b: Iterable<B>): Zip2<A, B> =
    if (a is List<A> && b is List<B>) Zip2(a, b) else Zip2(a.iterator(), b.iterator())

/**
 * Multizip a pair of iterables.
 */
fun <A, B> multizip(pair: Pair<Iterable<A>, Iterable<B>>): Zip2<A, B> =
    multizip(pair.first, pair.second)

/**
 * Multizip 3 iterators.
 */
fun <A, B, C> multizip(a: Iterator<A>, b: Iterator<B>, c: Iterator<C>): Zip3<A, B, C> =
    Zip3(a, b, c)

/**
 * Multizip 3 iterables.
 */
fun <A, B, C> multizip(a: Iterable<A>, b: Iterable<B>, c: Iterable<C>): Zip3<A, B, C> =
    Zip3(a.iterator(), b.iterator(), c.iterator())

/**
 * Multizip a triple of iterables.
 */
fun <A, B, C> multizip(triple: Triple<Iterable<A>, Iterable<B>, Iterable<C>>): Zip3<A, B, C> =
    multizip(triple.first, triple.second, triple.third)

/**
 * Multizip a list of iterables of homogeneous type.
 */
fun <T> multizip(iters: Iterable<Iterable<T>>): MultiZip<T> =
    MultiZip(iters.map { it.iterator() })

/**
 * Zip this iterator with another iterator into pairs.
 */
fun <A, B> Iterator<A>.zip(other: Iterator<B>): Zip2<A, B> =
    Zip2(this, other)

/**
 * Create an iterator running two iterators in lockstep into pairs.
 */
fun <A, B> izip(a: Iterator<A>, b: Iterator<B>): Zip2<A, B> = multizip(a, b)

/**
 * Create an iterator running two iterables in lockstep into pairs.
 */
fun <A, B> izip(a: Iterable<A>, b: Iterable<B>): Zip2<A, B> = multizip(a, b)

/**
 * Create an iterator running three iterators in lockstep into triples.
 */
fun <A, B, C> izip(a: Iterator<A>, b: Iterator<B>, c: Iterator<C>): Zip3<A, B, C> = multizip(a, b, c)

/**
 * Create an iterator running three iterables in lockstep into triples.
 */
fun <A, B, C> izip(a: Iterable<A>, b: Iterable<B>, c: Iterable<C>): Zip3<A, B, C> = multizip(a, b, c)

