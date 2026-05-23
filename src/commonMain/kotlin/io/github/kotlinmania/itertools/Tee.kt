// port-lint: source src/tee.rs
package io.github.kotlinmania.itertools

/**
 * Common buffer object for the two tee halves.
 *
 * Both [Tee] handles share a single instance and read from / write to the
 * same backlog. The upstream Rust crate wraps this struct in `Rc<RefCell<…>>`
 * to share single-threaded mutability between the two halves; Kotlin gets the
 * same effect by handing both halves a reference to one mutable object.
 *
 * The `owner` flag records which of the two handles should read the next
 * element from `backlog`. The other handle pulled the most recent value from
 * the source iterator and pushed a clone here for its sibling.
 *
 * `iterConsumed` counts elements that have been removed from `iter` so that
 * each half can derive a size hint relative to the iterator's original
 * length.
 */
internal class TeeBuffer<T>(
    val backlog: ArrayDeque<T>,
    val iter: Iterator<T>,
    var owner: Boolean,
    var iterConsumed: Int,
)

/**
 * One half of an iterator pair where both return the same elements.
 *
 * See `Itertools.tee()` for more information.
 */
class Tee<T> internal constructor(
    private val buffer: TeeBuffer<T>,
    private val id: Boolean,
    private val sourceHint: SizeHint,
) : Iterator<T> {

    override fun hasNext(): Boolean {
        if (buffer.owner == id && buffer.backlog.isNotEmpty()) return true
        return buffer.iter.hasNext()
    }

    override fun next(): T {
        if (buffer.owner == id && buffer.backlog.isNotEmpty()) {
            return buffer.backlog.removeFirst()
        }
        val element = buffer.iter.next()
        buffer.iterConsumed += 1
        buffer.backlog.addLast(element)
        buffer.owner = !id
        return element
    }

    /** Equivalent to upstream `Iterator::size_hint`. */
    fun sizeHint(): SizeHint {
        val iterRemaining = subScalar(sourceHint, buffer.iterConsumed)
        return if (buffer.owner == id) {
            addScalar(iterRemaining, buffer.backlog.size)
        } else {
            iterRemaining
        }
    }
}

/**
 * Splits [iter] into two iterators that both yield the same elements.
 *
 * Mirrors `itertools::tee::new` from upstream Rust.
 */
fun <T> teeNew(iter: Iterator<T>, sourceHint: SizeHint = 0 to null): Pair<Tee<T>, Tee<T>> {
    val buffer = TeeBuffer(ArrayDeque<T>(), iter, owner = false, iterConsumed = 0)
    val t1 = Tee(buffer, id = true, sourceHint = sourceHint)
    val t2 = Tee(buffer, id = false, sourceHint = sourceHint)
    return t1 to t2
}

/**
 * Convenience overload that derives a size hint from [iterable] when possible.
 */
fun <T> tee(iterable: Iterable<T>): Pair<Tee<T>, Tee<T>> {
    val hint: SizeHint = when (iterable) {
        is Collection<*> -> iterable.size to iterable.size
        else -> 0 to null
    }
    return teeNew(iterable.iterator(), hint)
}
