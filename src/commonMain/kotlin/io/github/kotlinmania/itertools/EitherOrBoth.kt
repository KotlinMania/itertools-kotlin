// port-lint: source itertools/src/either_or_both.rs
package io.github.kotlinmania.itertools

/**
 * Value that either holds a single `A` or `B`, or both.
 */
sealed class EitherOrBoth<out A, out B> {
    /**
     * Both values are present.
     */
    data class Both<out A, out B>(
        val left: A,
        val right: B,
    ) : EitherOrBoth<A, B>()

    /**
     * Only the left value of type `A` is present.
     */
    data class Left<out A>(
        val value: A,
    ) : EitherOrBoth<A, Nothing>()

    /**
     * Only the right value of type `B` is present.
     */
    data class Right<out B>(
        val value: B,
    ) : EitherOrBoth<Nothing, B>()

    /**
     * If `Left`, or `Both`, return true. Otherwise, return false.
     */
    fun hasLeft(): Boolean =
        when (this) {
            is Left, is Both -> true
            is Right -> false
        }

    /**
     * If `Right`, or `Both`, return true, otherwise, return false.
     */
    fun hasRight(): Boolean =
        when (this) {
            is Right, is Both -> true
            is Left -> false
        }

    /**
     * If `Left`, return true. Otherwise, return false.
     * Exclusive version of [hasLeft].
     */
    fun isLeft(): Boolean = this is Left

    /**
     * If `Right`, return true. Otherwise, return false.
     * Exclusive version of [hasRight].
     */
    fun isRight(): Boolean = this is Right

    /**
     * If `Both`, return true. Otherwise, return false.
     */
    fun isBoth(): Boolean = this is Both

    /**
     * If `Left`, or `Both`, return the left value. Otherwise, return null.
     */
    fun left(): A? =
        when (this) {
            is Left -> this.value
            is Both -> this.left
            is Right -> null
        }

    /**
     * If `Right`, or `Both`, return the right value. Otherwise, return null.
     */
    fun right(): B? =
        when (this) {
            is Right -> this.value
            is Both -> this.right
            is Left -> null
        }

    /**
     * Return tuple of options corresponding to the left and right value respectively.
     *
     * If `Left` return `(value, null)`, if `Right` return `(null, value)`, else return `(left, right)`.
     */
    fun leftAndRight(): Pair<A?, B?> =
        when (this) {
            is Left -> Pair(this.value, null)
            is Right -> Pair(null, this.value)
            is Both -> Pair(this.left, this.right)
        }

    /**
     * If `Left`, return the left value. If `Right` or `Both`, return null.
     *
     * # Examples
     *
     * ```kotlin
     * val x: EitherOrBoth<String, Unit> = EitherOrBoth.Left("bonjour")
     * assertEquals("bonjour", x.justLeft())
     *
     * val y: EitherOrBoth<Unit, String> = EitherOrBoth.Right("hola")
     * assertNull(y.justLeft())
     *
     * val z = EitherOrBoth.Both("bonjour", "hola")
     * assertNull(z.justLeft())
     * ```
     */
    fun justLeft(): A? =
        when (this) {
            is Left -> this.value
            is Right, is Both -> null
        }

    /**
     * If `Right`, return the right value. If `Left` or `Both`, return null.
     *
     * # Examples
     *
     * ```kotlin
     * val x: EitherOrBoth<String, Unit> = EitherOrBoth.Left("auf wiedersehen")
     * assertNull(x.justRight())
     *
     * val y: EitherOrBoth<Unit, String> = EitherOrBoth.Right("adios")
     * assertEquals("adios", y.justRight())
     *
     * val z = EitherOrBoth.Both("auf wiedersehen", "adios")
     * assertNull(z.justRight())
     * ```
     */
    fun justRight(): B? =
        when (this) {
            is Right -> this.value
            is Left, is Both -> null
        }

    /**
     * If `Both`, return a pair containing the left and right values. Otherwise, return null.
     */
    fun both(): Pair<A, B>? =
        when (this) {
            is Both -> Pair(this.left, this.right)
            is Left, is Right -> null
        }

    /**
     * If `Left` or `Both`, return the left value. Otherwise, convert the right value and return it.
     */
    fun intoLeft(convertRight: (B) -> @UnsafeVariance A): A =
        when (this) {
            is Left -> this.value
            is Both -> this.left
            is Right -> convertRight(this.value)
        }

    /**
     * If `Right` or `Both`, return the right value. Otherwise, convert the left value and return it.
     */
    fun intoRight(convertLeft: (A) -> @UnsafeVariance B): B =
        when (this) {
            is Right -> this.value
            is Both -> this.right
            is Left -> convertLeft(this.value)
        }

    /**
     * Convert `EitherOrBoth<A, B>` to `EitherOrBoth<B, A>`.
     */
    fun flip(): EitherOrBoth<B, A> =
        when (this) {
            is Left -> Right(this.value)
            is Right -> Left(this.value)
            is Both -> Both(this.right, this.left)
        }

    /**
     * Apply the function `f` on the value `a` in `Left(a)` or `Both(a, b)` variants. If it is
     * present rewrapping the result in `this`'s original variant.
     */
    fun <M> mapLeft(f: (A) -> M): EitherOrBoth<M, B> =
        when (this) {
            is Both -> Both(f(this.left), this.right)
            is Left -> Left(f(this.value))
            is Right -> this
        }

    /**
     * Apply the function `f` on the value `b` in `Right(b)` or `Both(a, b)` variants.
     * If it is present rewrapping the result in `this`'s original variant.
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
     * Returns a tuple consisting of the `l` and `r` in `Both(l, r)`, if present.
     * Otherwise, returns the wrapped value for the present element, and the supplied
     * value for the other. The first (`l`) argument is used for a missing `Left`
     * value. The second (`r`) argument is used for a missing `Right` value.
     *
     * Arguments passed to `or` are eagerly evaluated; if you are passing
     * the result of a function call, it is recommended to use [orElse],
     * which is lazily evaluated.
     *
     * # Examples
     *
     * ```kotlin
     * assertEquals(Pair("tree", 1), EitherOrBoth.Both("tree", 1).or("stone", 5))
     * assertEquals(Pair("tree", 5), EitherOrBoth.Left("tree").or("stone", 5))
     * assertEquals(Pair("stone", 1), EitherOrBoth.Right(1).or("stone", 5))
     * ```
     */
    fun or(l: @UnsafeVariance A, r: @UnsafeVariance B): Pair<A, B> =
        when (this) {
            is Left -> Pair(this.value, r)
            is Right -> Pair(l, this.value)
            is Both -> Pair(this.left, this.right)
        }

    /**
     * Returns a tuple consisting of the `l` and `r` in `Both(l, r)`, if present.
     * Otherwise, returns the wrapped value for the present element, and computes the
     * missing value with the supplied closure. The first argument (`l`) is used for a
     * missing `Left` value. The second argument (`r`) is used for a missing `Right` value.
     *
     * # Examples
     *
     * ```kotlin
     * val k = 10
     * assertEquals(Pair("tree", 1), EitherOrBoth.Both("tree", 1).orElse({ "stone" }, { 2 * k }))
     * assertEquals(Pair("tree", 20), EitherOrBoth.Left("tree").orElse({ "stone" }, { 2 * k }))
     * assertEquals(Pair("stone", 1), EitherOrBoth.Right(1).orElse({ "stone" }, { 2 * k }))
     * ```
     */
    fun orElse(l: () -> @UnsafeVariance A, r: () -> @UnsafeVariance B): Pair<A, B> =
        when (this) {
            is Left -> Pair(this.value, r())
            is Right -> Pair(l(), this.value)
            is Both -> Pair(this.left, this.right)
        }

    /**
     * Converts from reference to self.
     */
    fun asRef(): EitherOrBoth<A, B> = this

    /**
     * Converts from mutable reference to self.
     */
    fun asMut(): EitherOrBoth<A, B> = this

    /**
     * Converts using dereferencing if applicable.
     */
    fun asDeref(): EitherOrBoth<A, B> = this

    /**
     * Converts using mutable dereferencing if applicable.
     */
    fun asDerefMut(): EitherOrBoth<A, B> = this

    /**
     * Returns a tuple consisting of the `l` and `r` in `Both(l, r)`, if present.
     * Otherwise, returns the wrapped value for the present element, and the supplied
     * default producer for the other.
     */
    fun orDefault(
        defaultA: () -> @UnsafeVariance A,
        defaultB: () -> @UnsafeVariance B,
    ): Pair<A, B> = orElse(defaultA, defaultB)

    /**
     * Returns a reference to the left value. If the left value is not present,
     * it is replaced with `val`.
     */
    fun leftOrInsert(value: @UnsafeVariance A): EitherOrBoth<A, B> = leftOrInsertWith { value }

    /**
     * Returns a reference to the right value. If the right value is not present,
     * it is replaced with `val`.
     */
    fun rightOrInsert(value: @UnsafeVariance B): EitherOrBoth<A, B> = rightOrInsertWith { value }

    /**
     * If the left value is not present, replace it with the value computed by the closure `f`.
     * Returns the now-present left value in the updated structure.
     */
    fun leftOrInsertWith(f: () -> @UnsafeVariance A): EitherOrBoth<A, B> =
        when (this) {
            is Left, is Both -> this
            is Right -> insertLeft(f())
        }

    /**
     * If the right value is not present, replace it with the value computed by the closure `f`.
     * Returns the now-present right value in the updated structure.
     */
    fun rightOrInsertWith(f: () -> @UnsafeVariance B): EitherOrBoth<A, B> =
        when (this) {
            is Right, is Both -> this
            is Left -> insertRight(f())
        }

    /**
     * Sets the `left` value of this instance, and returns the updated instance.
     * Does not affect the `right` value.
     *
     * # Examples
     * ```kotlin
     * // Overwriting a pre-existing value.
     * var either: EitherOrBoth<Int, Unit> = EitherOrBoth.Left(0)
     * either = either.insertLeft(69)
     * assertEquals(69, either.left())
     *
     * // Inserting a second value.
     * var either2: EitherOrBoth<String, String> = EitherOrBoth.Right("no")
     * either2 = either2.insertLeft("yes")
     * assertEquals(EitherOrBoth.Both("yes", "no"), either2)
     * ```
     */
    fun insertLeft(value: @UnsafeVariance A): EitherOrBoth<A, B> =
        when (this) {
            is Left -> Left(value)
            is Both -> Both(value, this.right)
            is Right -> Both(value, this.value)
        }

    /**
     * Sets the `right` value of this instance, and returns the updated instance.
     * Does not affect the `left` value.
     *
     * # Examples
     * ```kotlin
     * // Overwriting a pre-existing value.
     * var either: EitherOrBoth<Int, Unit> = EitherOrBoth.Left(0)
     * either = either.insertLeft(69)
     * assertEquals(69, either.left())
     *
     * // Inserting a second value.
     * var either2: EitherOrBoth<String, Int> = EitherOrBoth.Left("what's")
     * either2 = either2.insertRight(9 + 10)
     * assertEquals(EitherOrBoth.Both("what's", 19), either2)
     * ```
     */
    fun insertRight(value: @UnsafeVariance B): EitherOrBoth<A, B> =
        when (this) {
            is Right -> Right(value)
            is Both -> Both(this.left, value)
            is Left -> Both(this.value, value)
        }

    /**
     * Set `self` to `Both`, containing the specified left and right values,
     * and returns the updated structure.
     */
    fun insertBoth(
        left: @UnsafeVariance A,
        right: @UnsafeVariance B,
    ): Both<A, B> = Both(left, right)

    companion object {
        /**
         * Converts from [Either] to [EitherOrBoth].
         */
        fun <A, B> from(either: Either<A, B>): EitherOrBoth<A, B> =
            when (either) {
                is Either.Left -> Left(either.value)
                is Either.Right -> Right(either.value)
            }

        /**
         * Converts from [EitherOrBoth] to nullable [Either].
         */
        fun <A, B> from(value: EitherOrBoth<A, B>): Either<A, B>? =
            when (value) {
                is Left -> Either.Left(value.value)
                is Right -> Either.Right(value.value)
                is Both -> null
            }
    }
}

/**
 * Return either value of left, right, or apply a function `f` to both values if both are present.
 * The input function has to return the same type as both Right and Left carry.
 *
 * This function can be used to preferrably extract the left resp. right value,
 * but fall back to the other (i.e. right resp. left) if the preferred one is not present.
 *
 * # Examples
 * ```kotlin
 * assertEquals(7, EitherOrBoth.Both(3, 7).reduce { a, b -> maxOf(a, b) })
 * assertEquals(3, EitherOrBoth.Left(3).reduce { a, b -> maxOf(a, b) })
 * assertEquals(7, EitherOrBoth.Right(7).reduce { a, b -> maxOf(a, b) })
 *
 * // Extract the left value if present, fall back to the right otherwise.
 * assertEquals("left", EitherOrBoth.Left("left").reduce { l, _ -> l })
 * assertEquals("right", EitherOrBoth.Right("right").reduce { l, _ -> l })
 * assertEquals("left", EitherOrBoth.Both("left", "right").reduce { l, _ -> l })
 * ```
 */
fun <T> EitherOrBoth<T, T>.reduce(f: (T, T) -> T): T =
    when (this) {
        is EitherOrBoth.Left -> this.value
        is EitherOrBoth.Right -> this.value
        is EitherOrBoth.Both -> f(this.left, this.right)
    }

/**
 * Converts from [EitherOrBoth] to nullable [Either]. Returns `null` if [EitherOrBoth.Both].
 */
fun <A, B> EitherOrBoth<A, B>.toEither(): Either<A, B>? =
    when (this) {
        is EitherOrBoth.Left -> Either.Left(this.value)
        is EitherOrBoth.Right -> Either.Right(this.value)
        is EitherOrBoth.Both -> null
    }
