// port-lint: source lib.rs
package io.github.kotlinmania.itertools

/**
 * An enum used for controlling the execution of `foldWhile`.
 *
 * See [foldWhile] for more information.
 */
sealed interface FoldWhile<out T> {
    /** Continue folding with this value. */
    data class Continue<T>(
        val value: T,
    ) : FoldWhile<T>

    /** Fold is complete and will return this value. */
    data class Done<T>(
        val value: T,
    ) : FoldWhile<T>

    /** Return the value in the continue or done. */
    fun intoInner(): T =
        when (this) {
            is Continue -> value
            is Done -> value
        }

    /** Return true if `this` is `Done`, false if it is `Continue`. */
    fun isDone(): Boolean = this is Done
}

/**
 * An iterator method that applies a function as long as it returns [FoldWhile.Continue],
 * and returns the final value.
 */
fun <T, B> Iterator<T>.foldWhile(initial: B, f: (B, T) -> FoldWhile<B>): FoldWhile<B> {
    var acc = initial
    while (hasNext()) {
        val nextVal = next()
        when (val res = f(acc, nextVal)) {
            is FoldWhile.Continue -> acc = res.value
            is FoldWhile.Done -> return res
        }
    }
    return FoldWhile.Continue(acc)
}

/**
 * An iterable method that applies a function as long as it returns [FoldWhile.Continue],
 * and returns the final value.
 */
fun <T, B> Iterable<T>.foldWhile(initial: B, f: (B, T) -> FoldWhile<B>): FoldWhile<B> =
    iterator().foldWhile(initial, f)
