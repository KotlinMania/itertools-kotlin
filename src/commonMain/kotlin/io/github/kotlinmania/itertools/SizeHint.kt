// port-lint: source src/size_hint.rs
package io.github.kotlinmania.itertools

/**
 * Arithmetic on `Iterator.sizeHint()` values.
 */

/** `SizeHint` is the return type of `Iterator.sizeHint()`. */
typealias SizeHint = Pair<Int, Int?>

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
    val min = saturatingAdd(a.first, b.first)
    val max = if (a.second != null && b.second != null) {
        checkedAdd(a.second!!, b.second!!)
    } else {
        null
    }
    return min to max
}

/** Add `x` correctly to a `SizeHint`. */
fun addScalar(sh: SizeHint, x: Int): SizeHint {
    val low = saturatingAdd(sh.first, x)
    val hi = sh.second?.let { checkedAdd(it, x) }
    return low to hi
}

/** Subtract `x` correctly from a `SizeHint`. */
fun subScalar(sh: SizeHint, x: Int): SizeHint {
    val low = saturatingSub(sh.first, x)
    val hi = sh.second?.let { saturatingSub(it, x) }
    return low to hi
}

/** Multiply `SizeHint` correctly */
fun mul(a: SizeHint, b: SizeHint): SizeHint {
    val low = saturatingMul(a.first, b.first)
    val au = a.second
    val bu = b.second
    val hi = when {
        au != null && bu != null -> checkedMul(au, bu)
        (au == 0 && bu == null) || (au == null && bu == 0) -> 0
        else -> null
    }
    return low to hi
}

/** Multiply `x` correctly with a `SizeHint`. */
fun mulScalar(sh: SizeHint, x: Int): SizeHint {
    val low = saturatingMul(sh.first, x)
    val hi = sh.second?.let { checkedMul(it, x) }
    return low to hi
}

/** Return the maximum */
fun max(a: SizeHint, b: SizeHint): SizeHint {
    val (aLower, aUpper) = a
    val (bLower, bUpper) = b

    val lower = kotlin.math.max(aLower, bLower)

    val upper = if (aUpper != null && bUpper != null) {
        kotlin.math.max(aUpper, bUpper)
    } else {
        null
    }

    return lower to upper
}

/** Return the minimum */
fun min(a: SizeHint, b: SizeHint): SizeHint {
    val (aLower, aUpper) = a
    val (bLower, bUpper) = b
    val lower = kotlin.math.min(aLower, bLower)
    val upper = if (aUpper != null && bUpper != null) {
        kotlin.math.min(aUpper, bUpper)
    } else {
        aUpper ?: bUpper
    }
    return lower to upper
}
