// port-lint: source src/intersperse.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.itertools

import kotlin.native.HiddenFromObjC

/**
 * Strategy that supplies the value to insert between adapted iterator
 * elements. Translated from the upstream `IntersperseElement<Item>` trait.
 *
 * Declared as a `fun interface` so Kotlin lambdas convert to it directly; this
 * also keeps the public surface compatible with the Swift Export gap #8b
 * recipe for `() -> T` function types in public APIs.
 *
 * Hidden from the Swift Export bridge: a `fun interface` SAM dodges the
 * Kotlin-function-type variant of gap #8, but the SAM's own `<T>` parameter
 * still triggers the generic-class variant. Hiding the type from the bridge
 * keeps the Kotlin surface strongly typed while avoiding the unchecked-cast
 * warnings the plugin emits when it erases `T` to `Any?`.
 */
@HiddenFromObjC
public fun interface IntersperseElement<T> {
    public fun generate(): T
}

/**
 * Strategy that yields the same supplied value between every pair of source
 * elements.
 *
 * The upstream Rust type bounds this constructor to `Item: Clone` and clones
 * the stored value on every call. In Kotlin there is no general `Clone`
 * mechanism; for immutable element types (primitives, [String], data classes
 * treated as values) returning the stored reference is equivalent.
 */
internal class IntersperseElementSimple<T>(private val item: T) : IntersperseElement<T> {
    override fun generate(): T = item
}

/**
 * An iterator adaptor to insert a particular value
 * between each element of the adapted iterator.
 *
 * Iterator element type is the source iterator's element type.
 *
 * This iterator is *fused*.
 *
 * See [intersperse] for more information.
 */

/** Create a new Intersperse iterator. */
public fun <T> intersperse(iter: Iterator<T>, elt: T): Iterator<T> =
    intersperseWith(iter, IntersperseElementSimple(elt))

/** Convenience overload that derives a source size hint from [iterable]. */
public fun <T> intersperse(iterable: Iterable<T>, elt: T): Iterator<T> =
    intersperseWith(iterable, IntersperseElementSimple(elt))

/**
 * An iterator adaptor to insert a particular value created by a function
 * between each element of the adapted iterator.
 *
 * Iterator element type is the source iterator's element type.
 *
 * This iterator is *fused*.
 *
 * See [intersperseWith] for more information.
 *
 * Upstream marks this `#[must_use = "iterator adaptors are lazy and do nothing
 * unless consumed"]`; Kotlin has no equivalent attribute, so callers carry the
 * same responsibility.
 */
internal class IntersperseWith<T>(
    private val element: IntersperseElement<T>,
    private val iter: Iterator<T>,
    private val initialSourceHint: SizeHint = 0 to null,
) : Iterator<T> {
    // Three-state machine mirroring upstream's `Option<Option<I::Item>>`:
    //   `peek == null`               — no item has been taken out of `iter` yet
    //   `peek == Empty`              — the last emit was a source item; next is a separator
    //   `peek == Filled(item)`       — a source item is buffered for the next emit
    private sealed class Slot<out T> {
        data object Empty : Slot<Nothing>()
        class Filled<T>(val value: T) : Slot<T>()
    }

    private var peek: Slot<T>? = null
    private var pending: Slot.Filled<T>? = null
    private var consumed: Int = 0

    private fun pullSource(): Slot<T> {
        if (!iter.hasNext()) return Slot.Empty
        consumed += 1
        return Slot.Filled(iter.next())
    }

    private fun fillNext(): Slot.Filled<T>? {
        val pre = pending
        if (pre != null) return pre
        val current = peek
        val produced: Slot.Filled<T>? = when (current) {
            is Slot.Filled -> {
                peek = Slot.Empty
                current
            }
            Slot.Empty -> when (val n = pullSource()) {
                is Slot.Filled -> {
                    peek = n
                    Slot.Filled(element.generate())
                }
                Slot.Empty -> null
            }
            null -> {
                peek = Slot.Empty
                when (val n = pullSource()) {
                    is Slot.Filled -> n
                    Slot.Empty -> null
                }
            }
        }
        pending = produced
        return produced
    }

    override fun hasNext(): Boolean = fillNext() != null

    override fun next(): T {
        val ready = fillNext() ?: throw NoSuchElementException("IntersperseWith exhausted")
        pending = null
        return ready.value
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    public fun sizeHint(): SizeHint {
        val sh = subScalar(initialSourceHint, consumed)
        val doubled = add(sh, sh)
        return when (peek) {
            is Slot.Filled -> addScalar(doubled, 1)
            Slot.Empty -> doubled
            null -> subScalar(doubled, 1)
        }
    }

    /**
     * Consumes the adaptor with a left fold, mirroring upstream's specialized
     * `Iterator::fold` impl.
     */
    public fun <B> fold(initial: B, operation: (B, T) -> B): B {
        var accum = initial
        if (hasNext()) {
            accum = operation(accum, next())
        }
        while (hasNext()) {
            accum = operation(accum, next())
        }
        return accum
    }
}

/** Create a new `IntersperseWith` iterator. */
public fun <T> intersperseWith(iter: Iterator<T>, elt: IntersperseElement<T>): Iterator<T> =
    IntersperseWith(elt, iter)

/** Convenience overload that derives a source size hint from [iterable]. */
public fun <T> intersperseWith(iterable: Iterable<T>, elt: IntersperseElement<T>): Iterator<T> =
    IntersperseWith(elt, iterable.iterator(), iterableSizeHint(iterable))

private fun iterableSizeHint(it: Iterable<*>): SizeHint = when (it) {
    is Collection<*> -> it.size to it.size
    else -> 0 to null
}
