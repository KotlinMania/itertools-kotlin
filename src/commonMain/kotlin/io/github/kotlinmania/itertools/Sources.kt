// port-lint: source src/sources.rs
package io.github.kotlinmania.itertools

/*
 * Iterators that are sources (produce elements from parameters,
 * not from another iterator).
 */

/**
 * A mutable reference holder used by [unfold] so the closure can rebind the
 * iterator's internal state between calls.
 *
 * Kotlin has no pass-by-reference, so the upstream `&mut St` argument is
 * surfaced as a [StateRef] whose [value] the closure assigns when it advances
 * the iteration.
 */
internal class StateRef<St>(var value: St)

/**
 * Creates a new unfold source with the specified closure as the "iterator
 * function" and an initial state to eventually pass to the closure.
 *
 * `unfold` is a general iterator builder: it has a mutable state value,
 * and a closure with access to the state that produces the next value.
 *
 * This is more or less equivalent to a regular class with an [Iterator]
 * implementation, and is useful for one-off iterators.
 *
 * ```
 * // an iterator that yields sequential Fibonacci numbers,
 * // and stops at the maximum representable value.
 *
 * val fibonacci = unfold(1 to 1) { s ->
 *     val (x1, x2) = s.value
 *     val next = if (Int.MAX_VALUE - x1 < x2) Int.MAX_VALUE else x1 + x2
 *     val ret = x1
 *     s.value = x2 to next
 *     if (ret == x2 && ret > 1) null else ret
 * }
 * ```
 */
@Deprecated("Use kotlin.sequences.generateSequence(seed) { f(it) } instead.")
internal fun <A, St> unfold(initialState: St, f: (StateRef<St>) -> A?): Unfold<St, A> =
    Unfold(f, initialState)

/**
 * See [unfold] for more information.
 *
 * The `unfold` builder is deprecated upstream; instances are normally obtained
 * only by calling [unfold], so the deprecation lives on that builder.
 */
internal class Unfold<St, A>(
    private val f: (StateRef<St>) -> A?,
    initialState: St,
) : Iterator<A> {

    /** Internal state that will be passed to the closure on the next iteration. */
    val stateRef: StateRef<St> = StateRef(initialState)

    /** Convenience read-only view of [stateRef]. */
    val state: St
        get() = stateRef.value

    private var peeked: Box<A>? = null
    private var exhausted: Boolean = false

    private fun prime() {
        if (peeked != null || exhausted) return
        val v = f(stateRef)
        if (v == null) {
            exhausted = true
        } else {
            peeked = Box(v)
        }
    }

    override fun hasNext(): Boolean {
        prime()
        return peeked != null
    }

    override fun next(): A {
        prime()
        val cell = peeked ?: throw NoSuchElementException("Unfold exhausted")
        peeked = null
        return cell.value
    }

    private class Box<A>(val value: A)
}

/**
 * An iterator that infinitely applies function to value and yields results.
 *
 * This `class` is created by the [iterate] function.
 * See its documentation for more.
 */
internal class Iterate<St>(
    initialState: St,
    private val f: (St) -> St,
) : Iterator<St> {

    var state: St = initialState
        private set

    override fun hasNext(): Boolean = true

    override fun next(): St {
        val nextState = f(state)
        val ret = state
        state = nextState
        return ret
    }

    /** `(Int.MAX_VALUE, null)` — the upstream `(usize::MAX, None)` size hint. */
    fun sizeHint(): SizeHint = SizeHint(Int.MAX_VALUE, null)
}

/**
 * Creates a new iterator that infinitely applies function to value and yields results.
 *
 * ```
 * assertEquals(listOf(1, 2, 3, 1, 2), iterate(1) { it % 3 + 1 }.asSequence().take(5).toList())
 * ```
 *
 * **Throws** if computing the next value does.
 *
 * ```
 * val it = iterate(25u) { x -> x - 10u }.asSequence().takeWhile { it > 10u }.iterator()
 * assertEquals(25u, it.next()) // `Iterate` holds 15u.
 * assertEquals(15u, it.next()) // `Iterate` holds 5u.
 * it.next()                    // `5u - 10u` underflows.
 * ```
 *
 * You can alternatively use [kotlin.sequences.generateSequence] as it better
 * describes a finite iterator.
 */
internal fun <St> iterate(initialValue: St, f: (St) -> St): Iterator<St> =
    Iterate(initialValue, f)
