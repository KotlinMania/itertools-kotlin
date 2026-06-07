// port-lint: source src/size_hint.rs
package io.github.kotlinmania.itertools

/**
 * Arithmetic on `Iterator.sizeHint()` values.
 */

/**
 * The result of an [Iterator.sizeHint] call.
 *
 * [lower] is the minimum number of elements remaining; [upper] is the
 * maximum, or `null` when the upper bound is unknown (infinite or
 * uncomputable).
 */
data class SizeHint(val lower: Int, val upper: Int?)

private fun saturatingAdd(a: Int, b: Int): Int {
    val sum = a.toLong() + b.toLong()
    return if (sum > Int.MAX_VALUE.toLong()) Int.MAX_VALUE else sum.toInt()
}

private fun saturatingSub(a: Int, b: Int): Int {
    val diff = a.toLong() - b.toLong()
    return if (diff < 0L) 0 else diff.toInt()
}

private fun saturatingMul(a: Int, b: Int): Int {
    val product = a.toLong() * b.toLong()
    return if (product > Int.MAX_VALUE.toLong()) Int.MAX_VALUE else product.toInt()
}

private fun checkedAdd(a: Int, b: Int): Int? {
    val sum = a.toLong() + b.toLong()
    return if (sum > Int.MAX_VALUE.toLong()) null else sum.toInt()
}

private fun checkedMul(a: Int, b: Int): Int? {
    val product = a.toLong() * b.toLong()
    return if (product > Int.MAX_VALUE.toLong()) null else product.toInt()
}

/** Add `SizeHint` correctly. */
fun add(a: SizeHint, b: SizeHint): SizeHint {
    val lo = saturatingAdd(a.lower, b.lower)
    val hi = if (a.upper != null && b.upper != null) {
        checkedAdd(a.upper, b.upper)
    } else {
        null
    }
    return SizeHint(lo, hi)
}

/** Add `x` correctly to a `SizeHint`. */
fun addScalar(sh: SizeHint, x: Int): SizeHint {
    val lo = saturatingAdd(sh.lower, x)
    val hi = sh.upper?.let { checkedAdd(it, x) }
    return SizeHint(lo, hi)
}

/** Subtract `x` correctly from a `SizeHint`. */
fun subScalar(sh: SizeHint, x: Int): SizeHint {
    val lo = saturatingSub(sh.lower, x)
    val hi = sh.upper?.let { saturatingSub(it, x) }
    return SizeHint(lo, hi)
}

/** Multiply `SizeHint` correctly */
fun mul(a: SizeHint, b: SizeHint): SizeHint {
    val lo = saturatingMul(a.lower, b.lower)
    val au = a.upper
    val bu = b.upper
    val hi = when {
        au != null && bu != null -> checkedMul(au, bu)
        (au == 0 && bu == null) || (au == null && bu == 0) -> 0
        else -> null
    }
    return SizeHint(lo, hi)
}

/** Multiply `x` correctly with a `SizeHint`. */
fun mulScalar(sh: SizeHint, x: Int): SizeHint {
    val lo = saturatingMul(sh.lower, x)
    val hi = sh.upper?.let { checkedMul(it, x) }
    return SizeHint(lo, hi)
}

/** Return the maximum */
fun max(a: SizeHint, b: SizeHint): SizeHint {
    val lower = kotlin.math.max(a.lower, b.lower)

    val upper = if (a.upper != null && b.upper != null) {
        kotlin.math.max(a.upper, b.upper)
    } else {
        null
    }

    return SizeHint(lower, upper)
}

/** Return the minimum */
fun min(a: SizeHint, b: SizeHint): SizeHint {
    val lower = kotlin.math.min(a.lower, b.lower)
    val upper = if (a.upper != null && b.upper != null) {
        kotlin.math.min(a.upper, b.upper)
    } else {
        a.upper ?: b.upper
    }
    return SizeHint(lower, upper)
}
