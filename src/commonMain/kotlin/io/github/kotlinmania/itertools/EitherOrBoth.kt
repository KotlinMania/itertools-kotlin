// port-lint: source either_or_both.rs
package io.github.kotlinmania.itertools

/**
 * Value that either holds a single `A` or `B`, or both.
 */
sealed class EitherOrBoth<out A, out B> {
    /** Both values are present. */
    data class Both<out A, out B>(
        val left: A,
        val right: B,
    ) : EitherOrBoth<A, B>()

    /** Only the left value of type `A` is present. */
    data class Left<out A>(
        val value: A,
    ) : EitherOrBoth<A, Nothing>()

    /** Only the right value of type `B` is present. */
    data class Right<out B>(
        val value: B,
    ) : EitherOrBoth<Nothing, B>()

    /** If `Left`, or `Both`, return true. Otherwise, return false. */
    fun hasLeft(): Boolean =
        when (this) {
            is Left, is Both -> true
            is Right -> false
        }

    /** If `Right`, or `Both`, return true, otherwise, return false. */
    fun hasRight(): Boolean =
        when (this) {
            is Right, is Both -> true
            is Left -> false
        }

    /** If `Left`, return true. Otherwise, return false. */
    fun isLeft(): Boolean = this is Left

    /** If `Right`, return true. Otherwise, return false. */
    fun isRight(): Boolean = this is Right

    /** If `Both`, return true. Otherwise, return false. */
    fun isBoth(): Boolean = this is Both

    /** If `Left`, or `Both`, return the left value. Otherwise, return null. */
    fun left(): A? =
        when (this) {
            is Left -> this.value
            is Both -> this.left
            is Right -> null
        }

    /** If `Right`, or `Both`, return the right value. Otherwise, return null. */
    fun right(): B? =
        when (this) {
            is Right -> this.value
            is Both -> this.right
            is Left -> null
        }

    /**
     * Return pair of nullables corresponding to the left and right value respectively.
     *
     * If `Left` return `(value, null)`, if `Right` return `(null, value)`, else return `(left, right)`.
     */
    fun leftAndRight(): Pair<A?, B?> =
        when (this) {
            is Left -> Pair(this.value, null)
            is Right -> Pair(null, this.value)
            is Both -> Pair(this.left, this.right)
        }

    /** If `Left`, return the left value. If `Right` or `Both`, return null. */
    fun justLeft(): A? =
        when (this) {
            is Left -> this.value
            is Right, is Both -> null
        }

    /** If `Right`, return the right value. If `Left` or `Both`, return null. */
    fun justRight(): B? =
        when (this) {
            is Right -> this.value
            is Left, is Both -> null
        }

    /** If `Both`, return a pair containing the left and right values. Otherwise, return null. */
    fun both(): Pair<A, B>? =
        when (this) {
            is Both -> Pair(this.left, this.right)
            is Left, is Right -> null
        }

    /** If `Left` or `Both`, return the left value. Otherwise, convert the right value and return it. */
    fun intoLeft(convertRight: (B) -> @UnsafeVariance A): A =
        when (this) {
            is Left -> this.value
            is Both -> this.left
            is Right -> convertRight(this.value)
        }

    /** If `Right` or `Both`, return the right value. Otherwise, convert the left value and return it. */
    fun intoRight(convertLeft: (A) -> @UnsafeVariance B): B =
        when (this) {
            is Right -> this.value
            is Both -> this.right
            is Left -> convertLeft(this.value)
        }

    /** Convert `EitherOrBoth<A, B>` to `EitherOrBoth<B, A>`. */
    fun flip(): EitherOrBoth<B, A> =
        when (this) {
            is Left -> Right(this.value)
            is Right -> Left(this.value)
            is Both -> Both(this.right, this.left)
        }

    /**
     * Apply the function `f` on the value `a` in `Left(a)` or `Both(a, b)` variants.
     * Rewraps the result in `this`'s original variant.
     */
    fun <M> mapLeft(f: (A) -> M): EitherOrBoth<M, B> =
        when (this) {
            is Both -> Both(f(this.left), this.right)
            is Left -> Left(f(this.value))
            is Right -> this
        }

    /**
     * Apply the function `f` on the value `b` in `Right(b)` or `Both(a, b)` variants.
     * Rewraps the result in `this`'s original variant.
     */
    fun <M> mapRight(f: (B) -> M): EitherOrBoth<A, M> =
        when (this) {
            is Left -> this
            is Right -> Right(f(this.value))
            is Both -> Both(this.left, f(this.right))
        }

    /**
     * Apply the functions `f` and `g` on the value `a` and `b` respectively;
     * found in `Left(a)`, `Right(b)`, or `Both(a, b)` variants.
     * The result is rewrapped in `this`'s original variant.
     */
    fun <L, R> mapAny(f: (A) -> L, g: (B) -> R): EitherOrBoth<L, R> =
        when (this) {
            is Left -> Left(f(this.value))
            is Right -> Right(g(this.value))
            is Both -> Both(f(this.left), g(this.right))
        }

    /**
     * Apply the function `f` on the value `a` in `Left(a)` or `Both(a, _)` variants if it is present.
     */
    fun <L> leftAndThen(f: (A) -> EitherOrBoth<L, @UnsafeVariance B>): EitherOrBoth<L, B> =
        when (this) {
            is Left -> f(this.value)
            is Both -> f(this.left)
            is Right -> this
        }

    /**
     * Apply the function `f` on the value `b` in `Right(b)` or `Both(_, b)` variants if it is present.
     */
    fun <R> rightAndThen(f: (B) -> EitherOrBoth<@UnsafeVariance A, R>): EitherOrBoth<A, R> =
        when (this) {
            is Left -> this
            is Right -> f(this.value)
            is Both -> f(this.right)
        }

    /**
     * Returns a pair consisting of the `l` and `r` in `Both(l, r)`, if present.
     * Otherwise, returns the wrapped value for the present element, and the supplied
     * value for the other.
     */
    fun or(l: @UnsafeVariance A, r: @UnsafeVariance B): Pair<A, B> =
        when (this) {
            is Left -> Pair(this.value, r)
            is Right -> Pair(l, this.value)
            is Both -> Pair(this.left, this.right)
        }

    /**
     * Returns a pair consisting of the `l` and `r` in `Both(l, r)`, if present.
     * Otherwise, returns the wrapped value for the present element, and computes the
     * missing value with the supplied lambda.
     */
    fun orElse(l: () -> @UnsafeVariance A, r: () -> @UnsafeVariance B): Pair<A, B> =
        when (this) {
            is Left -> Pair(this.value, r())
            is Right -> Pair(l(), this.value)
            is Both -> Pair(this.left, this.right)
        }

    /** Return reference to self. */
    fun asRef(): EitherOrBoth<A, B> = this

    /** Return mutable reference to self. */
    fun asMut(): EitherOrBoth<A, B> = this

    /** Return dereferenced representation of self. */
    fun asDeref(): EitherOrBoth<A, B> = this

    /** Return mutable dereferenced representation of self. */
    fun asDerefMut(): EitherOrBoth<A, B> = this

    /**
     * Returns a pair consisting of the `l` and `r` in `Both(l, r)`, if present.
     * Otherwise, returns the wrapped value for the present element, and the supplied
     * default producer for the other.
     */
    fun orDefault(
        defaultA: () -> @UnsafeVariance A,
        defaultB: () -> @UnsafeVariance B,
    ): Pair<A, B> = orElse(defaultA, defaultB)

    /**
     * Returns the left value, or if not present, replaces with [value].
     */
    fun leftOrInsert(value: @UnsafeVariance A): EitherOrBoth<A, B> = leftOrInsertWith { value }

    /**
     * Returns the right value, or if not present, replaces with [value].
     */
    fun rightOrInsert(value: @UnsafeVariance B): EitherOrBoth<A, B> = rightOrInsertWith { value }

    /**
     * If the left value is not present, replace it with the value computed by [f].
     */
    fun leftOrInsertWith(f: () -> @UnsafeVariance A): EitherOrBoth<A, B> =
        when (this) {
            is Left, is Both -> this
            is Right -> insertLeft(f())
        }

    /**
     * If the right value is not present, replace it with the value computed by [f].
     */
    fun rightOrInsertWith(f: () -> @UnsafeVariance B): EitherOrBoth<A, B> =
        when (this) {
            is Right, is Both -> this
            is Left -> insertRight(f())
        }

    /**
     * Sets the `left` value of this instance. Does not affect the `right` value.
     */
    fun insertLeft(value: @UnsafeVariance A): EitherOrBoth<A, B> =
        when (this) {
            is Left -> Left(value)
            is Both -> Both(value, this.right)
            is Right -> Both(value, this.value)
        }

    /**
     * Sets the `right` value of this instance. Does not affect the `left` value.
     */
    fun insertRight(value: @UnsafeVariance B): EitherOrBoth<A, B> =
        when (this) {
            is Right -> Right(value)
            is Both -> Both(this.left, value)
            is Left -> Both(this.value, value)
        }

    /**
     * Set `self` to `Both`, containing the specified left and right values.
     */
    fun insertBoth(
        left: @UnsafeVariance A,
        right: @UnsafeVariance B,
    ): Both<A, B> = Both(left, right)

    companion object {
        fun <A, B> from(either: Either<A, B>): EitherOrBoth<A, B> =
            when (either) {
                is Either.Left -> Left(either.value)
                is Either.Right -> Right(either.value)
            }
    }
}

/**
 * Return either value of left, right, or apply a function `f` to both values if both are present.
 * The input function has to return the same type as both Right and Left carry.
 */
fun <T> EitherOrBoth<T, T>.reduce(f: (T, T) -> T): T =
    when (this) {
        is EitherOrBoth.Left -> this.value
        is EitherOrBoth.Right -> this.value
        is EitherOrBoth.Both -> f(this.left, this.right)
    }

fun <A, B> EitherOrBoth<A, B>.toEither(): Either<A, B>? =
    when (this) {
        is EitherOrBoth.Left -> Either.Left(this.value)
        is EitherOrBoth.Right -> Either.Right(this.value)
        is EitherOrBoth.Both -> null
    }
