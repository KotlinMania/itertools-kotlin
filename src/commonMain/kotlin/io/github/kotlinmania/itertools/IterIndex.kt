// port-lint: source iter_index.rs
package io.github.kotlinmania.itertools

/**
 * Used by `Itertools.get` to know which iterator to turn different ranges into.
 *
 * Prefer calling `Itertools.get` instead of calling [index] directly.
 */
public sealed interface IteratorIndex {
    /** Returns an adapted iterator for the current index applied to [from]. */
    public fun <T> index(from: Iterator<T>): Iterator<T>
}

private fun <T> Iterator<T>.itTake(n: Int): Iterator<T> {
    var remaining = if (n < 0) 0 else n
    val source = this
    return object : Iterator<T> {
        override fun hasNext(): Boolean = remaining > 0 && source.hasNext()

        override fun next(): T {
            if (remaining <= 0) throw NoSuchElementException()
            remaining -= 1
            return source.next()
        }
    }
}

private fun <T> Iterator<T>.itSkip(n: Int): Iterator<T> {
    var toSkip = if (n < 0) 0 else n
    while (toSkip > 0 && hasNext()) {
        next()
        toSkip -= 1
    }
    return this
}

/**
 * Index by an exclusive `start until end` range.
 *
 * [start] is inclusive and [end] is exclusive.
 */
public data class Range(
    public val start: Int,
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from.itTake(end).itSkip(start)
}

/**
 * Index by an inclusive `start..end` range.
 */
public data class RangeInclusive(
    public val start: Int,
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> {
        val length =
            if (end == Int.MAX_VALUE) {
                check(start != 0) {
                    "RangeInclusive.index: start must not be 0 when end == Int.MAX_VALUE"
                }
                end - start + 1
            } else {
                val plusOne = end + 1
                if (plusOne < start) 0 else plusOne - start
            }
        return from.itSkip(start).itTake(length)
    }
}

/** Index by an exclusive prefix range up to [end]. */
public data class RangeTo(
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from.itTake(end)
}

/** Index by an inclusive prefix range up to [end]. */
public data class RangeToInclusive(
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> {
        check(end != Int.MAX_VALUE) { "RangeToInclusive.index: end must not be Int.MAX_VALUE" }
        return from.itTake(end + 1)
    }
}

/** Index by a range starting from [start] to the end. */
public data class RangeFrom(
    public val start: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from.itSkip(start)
}

/** Index by a full range. */
public data object RangeFull : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from
}

/**
 * Returns an adapted iterator for the slice described by [index] applied to [iter].
 */
public fun <T> get(iter: Iterable<T>, index: IteratorIndex): Iterator<T> = index.index(iter.iterator())
