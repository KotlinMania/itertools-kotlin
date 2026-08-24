// port-lint: source kmerge_impl.rs
package io.github.kotlinmania.itertools

/**
 * Head element and Tail iterator pair.
 */
internal class HeadTail<T>(
    var head: T,
    val tail: Iterator<T>,
) {
    fun next(): T? {
        if (tail.hasNext()) {
            val oldHead = head
            head = tail.next()
            return oldHead
        }
        return null
    }

    /** Hints at the size of the sequence, same as the Iterator method. */
    fun sizeHint(tailHint: SizeHint = SizeHint(0, null)): SizeHint =
        addScalar(tailHint, 1)

    companion object {
        fun <T> create(it: Iterator<T>): HeadTail<T>? {
            if (!it.hasNext()) return null
            return HeadTail(it.next(), it)
        }

        fun <T> new(it: Iterator<T>): HeadTail<T>? = create(it)
    }
}

/** Sift down element at [index] in a min-heap. */
private fun <T> siftDown(heap: MutableList<HeadTail<T>>, index: Int, lessThan: (T, T) -> Boolean) {
    var pos = index
    var child = 2 * pos + 1
    while (child + 1 < heap.size) {
        if (lessThan(heap[child + 1].head, heap[child].head)) {
            child += 1
        }
        if (!lessThan(heap[child].head, heap[pos].head)) {
            return
        }
        val tmp = heap[pos]
        heap[pos] = heap[child]
        heap[child] = tmp
        pos = child
        child = 2 * pos + 1
    }
    if (child + 1 == heap.size && lessThan(heap[child].head, heap[pos].head)) {
        val tmp = heap[pos]
        heap[pos] = heap[child]
        heap[child] = tmp
    }
}

/** Make list into a min-heap. */
private fun <T> heapify(data: MutableList<HeadTail<T>>, lessThan: (T, T) -> Boolean) {
    for (i in (data.size / 2 - 1) downTo 0) {
        siftDown(data, i, lessThan)
    }
}

/**
 * An iterator adaptor that merges an arbitrary number of base iterators in ascending order.
 */
typealias KMerge<T> = KMergeBy<T>

fun interface KMergePredicate<T> {
    fun kmergePred(a: T, b: T): Boolean
}

class KMergeByLt<T : Comparable<*>> : KMergePredicate<T> {
    override fun kmergePred(a: T, b: T): Boolean =
        compareValues(a, b) < 0
}

/**
 * An iterator adaptor that merges an arbitrary number of base iterators
 * according to an ordering function.
 *
 * See [kmerge] and [kmergeBy] for more information.
 */
class KMergeBy<T> internal constructor(
    private val heap: MutableList<HeadTail<T>>,
    private val lessThan: (T, T) -> Boolean,
) : Iterator<T> {
    override fun hasNext(): Boolean = heap.isNotEmpty()

    override fun next(): T {
        if (heap.isEmpty()) {
            throw NoSuchElementException("KMergeBy exhausted")
        }
        val first = heap[0]
        val result =
            if (first.tail.hasNext()) {
                val old = first.head
                first.head = first.tail.next()
                old
            } else {
                val lastIdx = heap.size - 1
                val removed = heap[0].head
                if (lastIdx > 0) {
                    heap[0] = heap.removeAt(lastIdx)
                } else {
                    heap.removeAt(0)
                }
                removed
            }
        if (heap.isNotEmpty()) {
            siftDown(heap, 0, lessThan)
        }
        return result
    }

    /** Returns the size hint for the merged iterators. */
    fun sizeHint(): SizeHint =
        SizeHint(heap.size, null)
}

/**
 * Create an iterator that merges elements of the contained iterators.
 */
fun <T : Comparable<*>> kmerge(iterable: Iterable<Iterable<T>>): KMergeBy<T> =
    kmergeBy(iterable) { a, b -> compareValues(a, b) < 0 }

/**
 * Create an iterator that merges elements of the contained iterators using [lessThan].
 */
fun <T> kmergeBy(iterable: Iterable<Iterable<T>>, lessThan: (T, T) -> Boolean): KMergeBy<T> {
    val heap = mutableListOf<HeadTail<T>>()
    for (it in iterable) {
        val ht = HeadTail.create(it.iterator())
        if (ht != null) {
            heap.add(ht)
        }
    }
    heapify(heap, lessThan)
    return KMergeBy(heap, lessThan)
}
