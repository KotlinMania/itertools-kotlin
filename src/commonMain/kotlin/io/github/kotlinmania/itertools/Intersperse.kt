// port-lint: source src/intersperse.rs
package io.github.kotlinmania.itertools

/**
 * Strategy used by [IntersperseWith] to materialize the separator value placed
 * between adjacent source elements.
 */
fun interface IntersperseElement<T> {
    fun generate(): T
}

/**
 * Cloning separator strategy: yields the held value on every call. The
 * upstream Rust `IntersperseElementSimple<Item: Clone>` clones the seed each
 * time; in Kotlin the value is shared by reference, matching the behavior for
 * immutable element types (which is the only kind upstream `Clone` callers
 * meaningfully use).
 */
class IntersperseElementSimple<T>(private val value: T) : IntersperseElement<T> {
    override fun generate(): T = value
}

/**
 * An iterator adaptor to insert a particular value
 * between each element of the adapted iterator.
 *
 * Iterator element type is the source iterator's element type.
 *
 * This iterator is *fused*.
 *
 * See `Itertools.intersperse` for more information.
 */
// Upstream defines `Intersperse<I>` as a type alias for
// `IntersperseWith<I, IntersperseElementSimple<I::Item>>`. Kotlin expresses
// the same shape with a typealias.
typealias Intersperse<T> = IntersperseWith<T, IntersperseElementSimple<T>>

/**
 * Create a new [Intersperse] iterator.
 */
fun <T> intersperse(iter: Iterator<T>, elt: T): Intersperse<T> =
    intersperseWith(iter, IntersperseElementSimple(elt))

/**
 * An iterator adaptor to insert a particular value created by a function
 * between each element of the adapted iterator.
 *
 * Iterator element type is the source iterator's element type.
 *
 * This iterator is *fused*.
 *
 * See `Itertools.intersperseWith` for more information.
 */
class IntersperseWith<T, E : IntersperseElement<T>> internal constructor(
    private val element: E,
    private val iter: Iterator<T>,
    private val sourceHint: SizeHint,
) : Iterator<T> {

    // The upstream Rust crate models its lookahead state with an
    // `Option<Option<I::Item>>`:
    //  * `None`             — no element pulled yet (we have not started)
    //  * `Some(None)`       — the previous yield was a source element; next is
    //                         the separator slot
    //  * `Some(Some(item))` — separator was just yielded; the buffered source
    //                         element comes next
    // Kotlin's `Iterator` has a separate `hasNext`/`next` pair, so we express
    // the same machine with a one-slot `ArrayDeque` (the buffered source item)
    // plus two flags. `started` distinguishes the initial state from the
    // separator-slot state; `iterExhausted` records that the source has been
    // drained.
    private val buffered: ArrayDeque<T> = ArrayDeque()
    private var started: Boolean = false
    private var iterExhausted: Boolean = false
    private var iterConsumed: Int = 0

    override fun hasNext(): Boolean {
        if (buffered.isNotEmpty()) return true
        if (!started) return sourceHasNext()
        // Separator slot: only yields a separator if the source has another
        // value to follow it. A trailing separator is never emitted.
        return sourceHasNext()
    }

    override fun next(): T {
        if (buffered.isNotEmpty()) {
            return buffered.removeFirst()
        }
        if (!started) {
            started = true
            return pullSource()
        }
        // Separator slot: pull the next source element first so we know whether
        // a separator is even warranted; on success buffer it for the next
        // call.
        if (!sourceHasNext()) {
            throw NoSuchElementException()
        }
        val next = pullSource()
        buffered.addLast(next)
        return element.generate()
    }

    private fun sourceHasNext(): Boolean {
        if (iterExhausted) return false
        if (iter.hasNext()) return true
        iterExhausted = true
        return false
    }

    private fun pullSource(): T {
        val v = iter.next()
        iterConsumed += 1
        return v
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    fun sizeHint(): SizeHint {
        val sourceRemaining = subScalar(sourceHint, iterConsumed)
        // Upstream forms `sh = iter.size_hint(); sh = add(sh, sh);` (i.e. 2x)
        // then adjusts for the peek state.
        var sh = add(sourceRemaining, sourceRemaining)
        sh = when {
            buffered.isNotEmpty() -> addScalar(sh, 1) // Filled: one extra yield queued
            started -> sh                              // EmptySlot
            else -> subScalar(sh, 1)                   // Unset: first yield is a source item, no leading separator
        }
        return sh
    }
}

/**
 * Create a new [IntersperseWith] iterator.
 */
fun <T, E : IntersperseElement<T>> intersperseWith(
    iter: Iterator<T>,
    elt: E,
    sourceHint: SizeHint = 0 to null,
): IntersperseWith<T, E> = IntersperseWith(elt, iter, sourceHint)

/**
 * Convenience overload that derives a size hint from [iterable] when possible.
 */
fun <T> intersperse(iterable: Iterable<T>, elt: T): Intersperse<T> {
    val hint = hintOfIterable(iterable)
    return intersperseWith(iterable.iterator(), IntersperseElementSimple(elt), hint)
}

/**
 * Convenience overload that derives a size hint from [iterable] and accepts a
 * lambda separator factory.
 */
fun <T> intersperseWith(iterable: Iterable<T>, eltFn: () -> T): IntersperseWith<T, IntersperseElement<T>> {
    val hint = hintOfIterable(iterable)
    return intersperseWith(iterable.iterator(), IntersperseElement { eltFn() }, hint)
}

private fun hintOfIterable(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> it.size to it.size
    else -> 0 to null
}
