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
public class FormatWith<T> internal constructor(
    private val sep: String,
    iter: Iterator<T>,
    f: (T, (Any?) -> Unit) -> Unit,
) {
    private var inner: Pair<Iterator<T>, (T, (Any?) -> Unit) -> Unit>? = iter to f

    /**
     * Preserves state on drop/clone.
     */
    internal class PutBackOnDrop<T>(
        private val into: FormatWith<T>,
        private var saved: Pair<Iterator<T>, (T, (Any?) -> Unit) -> Unit>?,
    ) {
        fun drop() {
            into.inner = saved
        }
    }

    /**
     * Formats the remaining elements into the provided [StringBuilder] or formatter.
     */
    public fun fmt(f: StringBuilder): StringBuilder {
        val taken = inner ?: throw IllegalStateException("FormatWith: was already formatted once")
        inner = null
        val (iter, format) = taken
        val emit: (Any?) -> Unit = { value -> f.append(value) }
        if (iter.hasNext()) {
            val first = iter.next()
            format(first, emit)
            while (iter.hasNext()) {
                if (sep.isNotEmpty()) f.append(sep)
                val element = iter.next()
                format(element, emit)
            }
        }
        return f
    }

    public fun clone(): FormatWith<T> {
        val taken = inner
        return FormatWith(sep, taken?.first ?: emptyList<T>().iterator(), taken?.second ?: { _, _ -> })
    }

    override fun toString(): String {
        val sb = StringBuilder()
        fmt(sb)
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
    private var inner: Iterator<T>? = iter

    /**
     * Preserves state on drop/clone.
     */
    internal class PutBackOnDrop<T>(
        private val into: Format<T>,
        private var saved: Iterator<T>?,
    ) {
        fun drop() {
            into.inner = saved
        }
    }

    /**
     * Formats the elements using a custom callback.
     */
    public fun format(f: StringBuilder, cb: (T, StringBuilder) -> Unit): StringBuilder {
        val taken = inner ?: throw IllegalStateException("Format: was already formatted once")
        inner = null
        if (taken.hasNext()) {
            val first = taken.next()
            cb(first, f)
            while (taken.hasNext()) {
                if (sep.isNotEmpty()) f.append(sep)
                val element = taken.next()
                cb(element, f)
            }
        }
        return f
    }

    /**
     * Formats using the default string representation.
     */
    public fun fmt(f: StringBuilder): StringBuilder = format(f) { element, sb -> sb.append(element) }

    public fun clone(): Format<T> {
        val taken = inner
        return Format(sep, taken ?: emptyList<T>().iterator())
    }

    override fun toString(): String {
        val sb = StringBuilder()
        fmt(sb)
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
