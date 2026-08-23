// port-lint: source src/flatten_ok.rs
package io.github.kotlinmania.itertools

/**
 * A Result type with custom success and error types.
 */
sealed class ItemResult<out T, out E> {
    /** Successful variant containing [value]. */
    data class Ok<out T>(
        val value: T,
    ) : ItemResult<T, Nothing>()

    /** Error variant containing [error]. */
    data class Err<out E>(
        val error: E,
    ) : ItemResult<Nothing, E>()
}

/**
 * An iterator adaptor that flattens `Ok` values and allows `Err` values through unchanged.
 *
 * See [flattenOk] for more information.
 */
class FlattenOk<T, E>(
    private val iter: Iterator<ItemResult<Iterable<T>, E>>,
) : Iterator<ItemResult<T, E>> {
    private var currentInner: Iterator<T>? = null
    private var peeked: ItemResult<T, E>? = null

    private fun advance() {
        if (peeked != null) return
        while (true) {
            val inner = currentInner
            if (inner != null && inner.hasNext()) {
                peeked = ItemResult.Ok(inner.next())
                return
            }
            currentInner = null
            if (!iter.hasNext()) {
                return
            }
            when (val item = iter.next()) {
                is ItemResult.Ok -> {
                    currentInner = item.value.iterator()
                }
                is ItemResult.Err -> {
                    peeked = ItemResult.Err(item.error)
                    return
                }
            }
        }
    }

    override fun hasNext(): Boolean {
        advance()
        return peeked != null
    }

    override fun next(): ItemResult<T, E> {
        advance()
        val current =
            peeked
                ?: throw NoSuchElementException("FlattenOk exhausted")
        peeked = null
        return current
    }
}

/**
 * Create a new [FlattenOk] iterator adaptor.
 */
fun <T, E> flattenOk(iter: Iterator<ItemResult<Iterable<T>, E>>): FlattenOk<T, E> =
    FlattenOk(iter)

/**
 * Create a new [FlattenOk] iterator adaptor from an [Iterable].
 */
fun <T, E> flattenOk(iterable: Iterable<ItemResult<Iterable<T>, E>>): FlattenOk<T, E> =
    FlattenOk(iterable.iterator())
