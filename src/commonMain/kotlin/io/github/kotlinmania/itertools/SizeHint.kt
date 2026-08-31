// port-lint: source size_hint.rs
package io.github.kotlinmania.itertools

import kotlin.math.max as cmpMax
import kotlin.math.min as cmpMin

/**
 * `SizeHint` is the return type of `Iterator.sizeHint()`.
 */
data class SizeHint(
    val lower: Int,
    val upper: Int?,
)

private fun Int.saturatingAdd(other: Int): Int {
    val sum = this.toLong() + other.toLong()
    return if (sum > Int.MAX_VALUE.toLong()) Int.MAX_VALUE else sum.toInt()
}

private fun Int.saturatingSub(other: Int): Int {
    val diff = this.toLong() - other.toLong()
    return if (diff < 0L) 0 else diff.toInt()
}

private fun Int.saturatingMul(other: Int): Int {
    val product = this.toLong() * other.toLong()
    return if (product > Int.MAX_VALUE.toLong()) Int.MAX_VALUE else product.toInt()
}

private fun Int.checkedAdd(other: Int): Int? {
    val sum = this.toLong() + other.toLong()
    return if (sum > Int.MAX_VALUE.toLong()) null else sum.toInt()
}

private fun Int.checkedMul(other: Int): Int? {
    val product = this.toLong() * other.toLong()
    return if (product > Int.MAX_VALUE.toLong()) null else product.toInt()
}

/** Add `SizeHint` correctly. */
fun add(a: SizeHint, b: SizeHint): SizeHint {
    val min = a.lower.saturatingAdd(b.lower)
    val max =
        if (a.upper != null && b.upper != null) {
            val x = a.upper
            val y = b.upper
            x.checkedAdd(y)
        } else {
            null
        }

    return SizeHint(min, max)
}

/** Add `x` correctly to a `SizeHint`. */
fun addScalar(sh: SizeHint, x: Int): SizeHint {
    var (low, hi) = sh
    low = low.saturatingAdd(x)
    hi = hi?.let { elt -> elt.checkedAdd(x) }
    return SizeHint(low, hi)
}

/** Subtract `x` correctly from a `SizeHint`. */
fun subScalar(sh: SizeHint, x: Int): SizeHint {
    var (low, hi) = sh
    low = low.saturatingSub(x)
    hi = hi?.let { elt -> elt.saturatingSub(x) }
    return SizeHint(low, hi)
}

/** Multiply `SizeHint` correctly */
fun mul(a: SizeHint, b: SizeHint): SizeHint {
    val low = a.lower.saturatingMul(b.lower)
    val hi =
        if (a.upper != null && b.upper != null) {
            val x = a.upper
            val y = b.upper
            x.checkedMul(y)
        } else if ((a.upper == 0 && b.upper == null) || (a.upper == null && b.upper == 0)) {
            0
        } else {
            null
        }
    return SizeHint(low, hi)
}

/** Multiply `x` correctly with a `SizeHint`. */
fun mulScalar(sh: SizeHint, x: Int): SizeHint {
    var (low, hi) = sh
    low = low.saturatingMul(x)
    hi = hi?.let { elt -> elt.checkedMul(x) }
    return SizeHint(low, hi)
}

/** Return the maximum */
fun max(a: SizeHint, b: SizeHint): SizeHint {
    val (aLower, aUpper) = a
    val (bLower, bUpper) = b

    val lower = cmpMax(aLower, bLower)

    val upper =
        if (aUpper != null && bUpper != null) {
            cmpMax(aUpper, bUpper)
        } else {
            null
        }

    return SizeHint(lower, upper)
}

/** Return the minimum */
fun min(a: SizeHint, b: SizeHint): SizeHint {
    val (aLower, aUpper) = a
    val (bLower, bUpper) = b
    val lower = cmpMin(aLower, bLower)
    val upper =
        if (aUpper != null && bUpper != null) {
            val u1 = aUpper
            val u2 = bUpper
            cmpMin(u1, u2)
        } else {
            aUpper ?: bUpper
        }
    return SizeHint(lower, upper)
}

