// port-lint: source format.rs
package io.github.kotlinmania.itertools

/**
 * Non-generic carrier returned by [newFormat] and [newFormatDefault].
 *
 * Callers format it with `toString()` exactly once; the
 * once-only contract is preserved on the underlying implementation.
 */
public class Formatted internal constructor(
    private val producer: () -> String,
) {
    override fun toString(): String = producer()
}

/**
 * Format all iterator elements lazily, separated by `sep`.
 *
 * The format value can only be formatted once, after that the iterator is
 * exhausted.
 *
 * See `Itertools.formatWith` for more information.
 */
internal class FormatWith<T> internal constructor(
    private val sep: String,
    iter: Iterator<T>,
    f: (T, (Any?) -> Unit) -> Unit,
) {
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
internal class Format<T> internal constructor(
    private val sep: String,
    iter: Iterator<T>,
) {
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
 * Construct a lazy renderer that walks the iterator using the supplied
 * formatter callback when its `toString` is invoked.
 */
public fun <T> newFormat(
    iter: Iterator<T>,
    separator: String,
    f: (T, (Any?) -> Unit) -> Unit,
): Formatted {
    val impl = FormatWith(separator, iter, f)
    return Formatted { impl.toString() }
}

/**
 * Construct a lazy renderer that walks the iterator and stringifies each item
 * via its own `toString` when its `toString` is invoked.
 */
public fun <T> newFormatDefault(
    iter: Iterator<T>,
    separator: String,
): Formatted {
    val impl = Format(separator, iter)
    return Formatted { impl.toString() }
}

/**
 * Format all iterator elements lazily, separated by [separator].
 */
public fun <T> Iterator<T>.format(separator: String): Formatted =
    newFormatDefault(this, separator)

/**
 * Format all iterable elements lazily, separated by [separator].
 */
public fun <T> Iterable<T>.format(separator: String): Formatted =
    iterator().format(separator)

/**
 * Format all iterator elements lazily, separated by [separator], using [f].
 */
public fun <T> Iterator<T>.formatWith(separator: String, f: (T, (Any?) -> Unit) -> Unit): Formatted =
    newFormat(this, separator, f)

/**
 * Format all iterable elements lazily, separated by [separator], using [f].
 */
public fun <T> Iterable<T>.formatWith(separator: String, f: (T, (Any?) -> Unit) -> Unit): Formatted =
    iterator().formatWith(separator, f)
