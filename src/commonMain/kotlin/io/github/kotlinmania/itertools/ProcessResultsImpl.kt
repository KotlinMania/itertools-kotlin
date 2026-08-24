// port-lint: source process_results_impl.rs
package io.github.kotlinmania.itertools

internal class ErrorHolder<E> {
    var error: E? = null
}

/**
 * An iterator that produces only the `T` values as long as the
 * inner iterator produces `ItemResult.Ok(T)`.
 *
 * Used by [processResults].
 */
class ProcessResults<T, E> internal constructor(
    private val iter: Iterator<ItemResult<T, E>>,
    private val errorHolder: ErrorHolder<E>,
) : Iterator<T> {
    private val nextQueue: ArrayDeque<T> = ArrayDeque(1)

    private fun advance() {
        if (nextQueue.isNotEmpty() || errorHolder.error != null) return
        if (!iter.hasNext()) return
        val item = nextBody(iter.next())
        if (item != null) {
            nextQueue.addLast(item)
        }
    }

    private fun nextBody(item: ItemResult<T, E>?): T? =
        when (item) {
            is ItemResult.Ok -> item.value
            is ItemResult.Err -> {
                errorHolder.error = item.error
                null
            }
            null -> null
        }

    override fun hasNext(): Boolean {
        advance()
        return nextQueue.isNotEmpty()
    }

    override fun next(): T {
        advance()
        if (nextQueue.isEmpty()) {
            throw NoSuchElementException("ProcessResults exhausted")
        }
        return nextQueue.removeFirst()
    }

    /** Returns the size hint for this iterator. */
    fun sizeHint(): SizeHint = SizeHint(0, null)

    /** Consumes the iterator and folds elements with [init] and [f]. */
    fun <B> fold(init: B, f: (B, T) -> B): B {
        var acc = init
        while (hasNext()) {
            acc = f(acc, next())
        }
        return acc
    }

    /** Returns the next element from the back if available. */
    fun nextBack(): T? {
        if (!hasNext()) return null
        return next()
    }

    /** Folds elements in reverse order. */
    fun <B> rfold(init: B, f: (B, T) -> B): B {
        val items = asSequence().toList()
        var acc = init
        for (i in items.indices.reversed()) {
            acc = f(acc, items[i])
        }
        return acc
    }
}

/**
 * “Lift” a function of the values of an iterator so that it can process
 * an iterator of [ItemResult] values instead.
 */
fun <T, E, R> processResults(
    iterable: Iterable<ItemResult<T, E>>,
    processor: (Iterator<T>) -> R,
): ItemResult<R, E> {
    val errorHolder = ErrorHolder<E>()
    val pr = ProcessResults(iterable.iterator(), errorHolder)
    val result = processor(pr)
    val err = errorHolder.error
    return if (err != null) {
        ItemResult.Err(err)
    } else {
        ItemResult.Ok(result)
    }
}

/**
 * “Lift” a function of the values of an iterator so that it can process
 * an iterator of [ItemResult] values instead.
 */
fun <T, E, R> processResults(
    iterator: Iterator<ItemResult<T, E>>,
    processor: (Iterator<T>) -> R,
): ItemResult<R, E> {
    val errorHolder = ErrorHolder<E>()
    val pr = ProcessResults(iterator, errorHolder)
    val result = processor(pr)
    val err = errorHolder.error
    return if (err != null) {
        ItemResult.Err(err)
    } else {
        ItemResult.Ok(result)
    }
}
