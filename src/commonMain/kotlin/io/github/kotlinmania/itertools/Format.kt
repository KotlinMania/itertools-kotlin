// port-lint: source src/format.rs
package io.github.kotlinmania.itertools

/**
 * Format all iterator elements lazily, separated by `sep`.
 *
 * The format value can only be formatted once, after that the iterator is
 * exhausted.
 *
 * See `Itertools.formatWith` for more information.
 */
public class FormatWith<T> internal constructor(
    private val sep: String,
    iter: Iterator<T>,
    f: (T, (Any?) -> Unit) -> Unit,
) {
    // `FormatWith` uses interior mutability because `toString` takes the
    // receiver immutably. The Kotlin port keeps the same one-shot semantics
    // by holding `inner` in a private nullable field and clearing it on use.
    private var inner: Pair<Iterator<T>, (T, (Any?) -> Unit) -> Unit>? = iter to f

    override fun toString(): String {
        val taken = inner ?: throw IllegalStateException("FormatWith: was already formatted once")
        inner = null
        val (iter, format) = taken
        val sb = StringBuilder()
        val emit: (Any?) -> Unit = { value -> sb.append(value) }
        if (iter.hasNext()) {
            val first = iter.next()
            format(first, emit)
            while (iter.hasNext()) {
                if (sep.isNotEmpty()) sb.append(sep)
                val element = iter.next()
                format(element, emit)
            }
        }
        return sb.toString()
    }
}

/**
 * Format all iterator elements lazily, separated by `sep`.
 *
 * The format value can only be formatted once, after that the iterator is
 * exhausted.
 *
 * See `Itertools.format` for more information.
 */
public class Format<T> internal constructor(
    private val sep: String,
    iter: Iterator<T>,
) {
    // `Format` uses interior mutability because `toString` takes the receiver
    // immutably; the Kotlin port keeps the same one-shot semantics by holding
    // `inner` in a private nullable field and clearing it on use.
    private var inner: Iterator<T>? = iter

    override fun toString(): String = formatInner { element, sb -> sb.append(element) }

    private inline fun formatInner(cb: (T, StringBuilder) -> Unit): String {
        val taken = inner ?: throw IllegalStateException("Format: was already formatted once")
        inner = null
        val sb = StringBuilder()
        if (taken.hasNext()) {
            val first = taken.next()
            cb(first, sb)
            while (taken.hasNext()) {
                if (sep.isNotEmpty()) sb.append(sep)
                val element = taken.next()
                cb(element, sb)
            }
        }
        return sb.toString()
    }
}

/**
 * Construct a [FormatWith] that lazily renders the iterator using the supplied
 * formatter callback when its `toString` is invoked.
 *
 * The callback receives each item plus an emit function that writes its
 * stringified form into the underlying buffer; this mirrors the upstream
 * `&mut dyn FnMut(&dyn Display) -> Result` callback shape.
 */
public fun <T> newFormat(
    iter: Iterator<T>,
    separator: String,
    f: (T, (Any?) -> Unit) -> Unit,
): FormatWith<T> = FormatWith(separator, iter, f)

/**
 * Construct a [Format] that lazily renders the iterator using each item's
 * `toString` when its `toString` is invoked.
 */
public fun <T> newFormatDefault(
    iter: Iterator<T>,
    separator: String,
): Format<T> = Format(separator, iter)
