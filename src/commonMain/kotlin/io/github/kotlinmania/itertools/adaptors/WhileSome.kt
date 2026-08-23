// port-lint: source adaptors/mod.rs
package io.github.kotlinmania.itertools.adaptors

/**
 * An iterator adaptor that filters nullable iterator elements and produces non-null elements.
 * Stops on the first null encountered.
 */
class WhileSome<T : Any>(
    private val iter: Iterator<T?>,
) : Iterator<T> {
    private var nextItem: T? = null
    private var hasNextCalculated = false
    private var exhausted = false

    override fun hasNext(): Boolean {
        if (exhausted) return false
        if (!hasNextCalculated) {
            if (iter.hasNext()) {
                val item = iter.next()
                if (item != null) {
                    nextItem = item
                    hasNextCalculated = true
                } else {
                    exhausted = true
                    return false
                }
            } else {
                exhausted = true
                return false
            }
        }
        return true
    }

    override fun next(): T {
        if (!hasNext()) {
            throw NoSuchElementException("WhileSome iterator exhausted")
        }
        val item = nextItem ?: throw NoSuchElementException("WhileSome iterator exhausted")
        nextItem = null
        hasNextCalculated = false
        return item
    }
}

/**
 * Create a new [WhileSome] iterator.
 */
fun <T : Any> whileSome(iter: Iterator<T?>): WhileSome<T> = WhileSome(iter)

/**
 * Create a new [WhileSome] iterator from an [Iterable].
 */
fun <T : Any> whileSome(iterable: Iterable<T?>): WhileSome<T> = WhileSome(iterable.iterator())
