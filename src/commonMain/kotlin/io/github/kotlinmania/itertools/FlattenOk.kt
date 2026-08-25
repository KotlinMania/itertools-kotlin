// port-lint: source flatten_ok.rs
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
class FlattenOk<T, E> internal constructor(
    private val iter: Iterator<ItemResult<Iterable<T>, E>>,
    private val doubleEndedIter: ListIterator<ItemResult<Iterable<T>, E>>? = null,
) : Iterator<ItemResult<T, E>> {
    private var innerFront: ListIterator<T>? = null
    private var innerBack: ListIterator<T>? = null
    private var peeked: ItemResult<T, E>? = null

    private fun advance() {
        if (peeked != null) return
        while (true) {
            val inner = innerFront
            if (inner != null && inner.hasNext()) {
                peeked = ItemResult.Ok(inner.next())
                return
            }
            innerFront = null
            if (!iter.hasNext()) {
                val back = innerBack
                if (back != null && back.hasNext()) {
                    peeked = ItemResult.Ok(back.next())
                    return
                }
                innerBack = null
                return
            }
            when (val item = iter.next()) {
                is ItemResult.Ok -> {
                    innerFront = item.value.toList().listIterator()
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

    /**
     * Yields the next element from the back of the iterator.
     */
    fun nextBack(): ItemResult<T, E>? {
        while (true) {
            val back = innerBack
            if (back != null && back.hasPrevious()) {
                return ItemResult.Ok(back.previous())
            }
            innerBack = null

            val deIter = doubleEndedIter
            if (deIter != null && deIter.hasPrevious()) {
                when (val item = deIter.previous()) {
                    is ItemResult.Ok -> {
                        val list = item.value.toList()
                        val li = list.listIterator(list.size)
                        if (li.hasPrevious()) {
                            innerBack = li
                            return ItemResult.Ok(li.previous())
                        }
                    }
                    is ItemResult.Err -> {
                        return ItemResult.Err(item.error)
                    }
                }
            } else {
                val front = innerFront
                if (front != null && front.hasPrevious()) {
                    return ItemResult.Ok(front.previous())
                }
                innerFront = null
                return null
            }
        }
    }

    /**
     * Folds every element into an accumulator by applying an operation.
     */
    fun <B> fold(init: B, f: (B, ItemResult<T, E>) -> B): B {
        var acc = init
        val front = innerFront
        if (front != null) {
            while (front.hasNext()) {
                acc = f(acc, ItemResult.Ok(front.next()))
            }
            innerFront = null
        }
        while (iter.hasNext()) {
            when (val item = iter.next()) {
                is ItemResult.Ok -> {
                    for (v in item.value) {
                        acc = f(acc, ItemResult.Ok(v))
                    }
                }
                is ItemResult.Err -> {
                    acc = f(acc, ItemResult.Err(item.error))
                }
            }
        }
        val back = innerBack
        if (back != null) {
            while (back.hasNext()) {
                acc = f(acc, ItemResult.Ok(back.next()))
            }
            innerBack = null
        }
        return acc
    }

    /**
     * Folds every element from the back into an accumulator by applying an operation.
     */
    fun <B> rfold(init: B, f: (B, ItemResult<T, E>) -> B): B {
        var acc = init
        val back = innerBack
        if (back != null) {
            while (back.hasPrevious()) {
                acc = f(acc, ItemResult.Ok(back.previous()))
            }
            innerBack = null
        }
        val deIter = doubleEndedIter
        if (deIter != null) {
            while (deIter.hasPrevious()) {
                when (val item = deIter.previous()) {
                    is ItemResult.Ok -> {
                        val list = item.value.toList()
                        for (i in list.indices.reversed()) {
                            acc = f(acc, ItemResult.Ok(list[i]))
                        }
                    }
                    is ItemResult.Err -> {
                        acc = f(acc, ItemResult.Err(item.error))
                    }
                }
            }
        }
        val front = innerFront
        if (front != null) {
            while (front.hasPrevious()) {
                acc = f(acc, ItemResult.Ok(front.previous()))
            }
            innerFront = null
        }
        return acc
    }

    /**
     * Size hint for the iterator adaptor.
     */
    fun sizeHint(): SizeHint = SizeHint(0, null)
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
    if (iterable is List<ItemResult<Iterable<T>, E>>) {
        FlattenOk(iterable.iterator(), iterable.listIterator(iterable.size))
    } else {
        FlattenOk(iterable.iterator())
    }
