// port-lint: source src/iter_index.rs
package io.github.kotlinmania.itertools

/*
 * Upstream re-exports `core::iter::{Skip, Take}` and
 * `core::ops::{Range, RangeFrom, RangeFull, RangeInclusive, RangeTo, RangeToInclusive}`.
 * Kotlin has no equivalent skip/take iterator structs, so every `index` implementation
 * returns a plain `Iterator<T>`. The upstream `private_iter_index::Sealed` marker is
 * preserved through the `IteratorIndex` sealed interface: only the six index shapes
 * declared in this file may implement it.
 */

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
 * Index by an exclusive `start..end` range of `usize`.
 *
 * The Kotlin port uses [Int] for the upstream `usize`; [start] is inclusive and
 * [end] is exclusive, matching `core::ops::Range<usize>`.
 */
public data class Range(
    public val start: Int,
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from.itTake(end).itSkip(start)
}

/**
 * Index by an inclusive `start..=end` range of `usize`.
 *
 * The Kotlin port mirrors `core::ops::RangeInclusive<usize>` with `Int.MAX_VALUE`
 * standing in for the upstream `usize::MAX` overflow guard.
 */
public data class RangeInclusive(
    public val start: Int,
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> {
        // end - start + 1 without overflowing if possible
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

/** Index by a `..end` range of `usize`, where `end` is exclusive. */
public data class RangeTo(
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from.itTake(end)
}

/** Index by a `..=end` range of `usize`, where `end` is inclusive. */
public data class RangeToInclusive(
    public val end: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> {
        check(end != Int.MAX_VALUE) { "RangeToInclusive.index: end must not be Int.MAX_VALUE" }
        return from.itTake(end + 1)
    }
}

/** Index by a `start..` range of `usize`. */
public data class RangeFrom(
    public val start: Int,
) : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from.itSkip(start)
}

/** Index by a `..` full range. */
public data object RangeFull : IteratorIndex {
    override fun <T> index(from: Iterator<T>): Iterator<T> = from
}

/**
 * Returns an adapted iterator for the slice described by [index] applied to [iter].
 *
 * Mirrors upstream `pub fn get<I, R>(iter: I, index: R) -> R::Output where I: IntoIterator`.
 */
public fun <T> get(iter: Iterable<T>, index: IteratorIndex): Iterator<T> = index.index(iter.iterator())
