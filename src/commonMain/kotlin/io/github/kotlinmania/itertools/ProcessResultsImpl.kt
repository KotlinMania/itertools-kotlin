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
        when (val item = iter.next()) {
            is ItemResult.Ok -> {
                nextQueue.addLast(item.value)
            }
            is ItemResult.Err -> {
                errorHolder.error = item.error
            }
        }
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
