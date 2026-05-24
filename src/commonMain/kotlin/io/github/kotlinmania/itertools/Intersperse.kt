// port-lint: source src/intersperse.rs
@file:OptIn(kotlin.experimental.ExperimentalObjCRefinement::class)

package io.github.kotlinmania.itertools

import kotlin.native.HiddenFromObjC

/**
 * Strategy that supplies the value to insert between adapted iterator
 * elements.
 *
 * Declared as a `fun interface` so Kotlin lambdas convert to it directly; this
 * also keeps the public surface compatible with the Swift Export function-type
 * gap recipe.
 *
 * Hidden from the Swift Export bridge: a SAM interface avoids exposing a
 * Kotlin function type directly, but the SAM's own generic parameter still
 * triggers the generic-class bridge gap. Hiding the type keeps the Kotlin
 * surface strongly typed while avoiding bridge casts when the plugin erases
 * the element type.
 */
@HiddenFromObjC
public fun interface IntersperseElement<T> {
    public fun generate(): T
}

/**
 * Strategy that yields the same supplied value between every pair of source
 * elements.
 *
 * The upstream type clones the stored separator on every call. Kotlin has no
 * general clone mechanism; for immutable element types, returning the stored
 * reference is equivalent.
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
 * This iterator is fused.
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
 * This iterator is fused.
 *
 * See [intersperseWith] for more information.
 *
 * Upstream marks this adaptor as lazy; Kotlin callers carry the same
 * responsibility to consume it.
 */
internal class IntersperseWith<T>(
    private val element: IntersperseElement<T>,
    private val iter: Iterator<T>,
    private val initialSourceHint: SizeHint = 0 to null,
) : Iterator<T> {
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

    /** Equivalent to upstream iterator size hint calculation. */
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
     * fold implementation.
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
