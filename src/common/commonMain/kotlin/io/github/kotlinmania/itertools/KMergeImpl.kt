// port-lint: source kmerge_impl.rs
package io.github.kotlinmania.itertools

/**
 * Head element and Tail iterator pair.
 *
 * Comparisons are based on first items (which are guaranteed to exist).
 * The comparison logic turns the heap used in `KMerge` into a min-heap.
 */
internal class HeadTail<T>(
    var head: T,
    val tail: Iterator<T>,
    private val tailHint: SizeHint = SizeHint(0, null),
) {
    private var tailConsumed: Int = 0

    /**
     * Get the next element and update [head], returning the old head in `Some`.
     * Returns `null` when the tail is exhausted (only [head] then remains).
     */
    fun next(): T? {
        if (tail.hasNext()) {
            val oldHead = head
            head = tail.next()
            tailConsumed += 1
            return oldHead
        }
        return null
    }

    /** Hints at the size of the sequence, same as the Iterator method. */
    fun sizeHint(): SizeHint =
        addScalar(subScalar(tailHint, tailConsumed), 1)

    companion object {
        /** Constructs a [HeadTail] from an [Iterator]. Returns `null` if the [Iterator] is empty. */
        fun <T> create(it: Iterator<T>, hint: SizeHint = SizeHint(0, null)): HeadTail<T>? {
            if (!it.hasNext()) return null
            return HeadTail(it.next(), it, hint)
        }

        /** Constructs a [HeadTail] from an [Iterator]. Returns `null` if the [Iterator] is empty. */
        fun <T> new(it: Iterator<T>, hint: SizeHint = SizeHint(0, null)): HeadTail<T>? = create(it, hint)
    }
}

/** Make list into a min-heap. */
private fun <T> heapify(data: MutableList<HeadTail<T>>, lessThan: (T, T) -> Boolean) {
    for (i in (data.size / 2 - 1) downTo 0) {
        siftDown(data, i, lessThan)
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

/**
 * An iterator adaptor that merges an arbitrary number of base iterators in ascending order.
 * If all base iterators are sorted (ascending), the result is sorted.
 *
 * Iterator element type is `T`.
 *
 * See [kmerge] for more information.
 */
typealias KMerge<T> = KMergeBy<T>

/**
 * Predicate interface for comparing elements in [KMergeBy].
 */
fun interface KMergePredicate<T> {
    /** Returns true if [a] should be ordered before [b]. */
    fun kmergePred(a: T, b: T): Boolean
}

/**
 * Default ascending ordering predicate for [Comparable] elements.
 */
class KMergeByLt<T : Comparable<*>> : KMergePredicate<T> {
    override fun kmergePred(a: T, b: T): Boolean =
        compareValues(a, b) < 0
}

/**
 * An iterator adaptor that merges an arbitrary number of base iterators
 * according to an ordering function.
 *
 * Iterator element type is `T`.
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
    fun sizeHint(): SizeHint {
        if (heap.isEmpty()) return SizeHint(0, 0)
        return heap.map { it.sizeHint() }.reduce { a, b -> add(a, b) }
    }
}

/**
 * Create an iterator that merges elements of the contained iterators.
 *
 * See [kmerge] for more details.
 */
fun <T : Comparable<*>> kmerge(iterable: Iterable<Iterable<T>>): KMergeBy<T> =
    kmergeBy(iterable) { a, b -> compareValues(a, b) < 0 }

/**
 * Create an iterator that merges elements of the contained iterators using [lessThan].
 */
fun <T> kmergeBy(iterable: Iterable<Iterable<T>>, lessThan: (T, T) -> Boolean): KMergeBy<T> {
    val heap = mutableListOf<HeadTail<T>>()
    for (it in iterable) {
        val hint =
            when (it) {
                is Collection<*> -> SizeHint((it.size - 1).coerceAtLeast(0), (it.size - 1).coerceAtLeast(0))
                else -> SizeHint(0, null)
            }
        val ht = HeadTail.create(it.iterator(), hint)
        if (ht != null) {
            heap.add(ht)
        }
    }
    heapify(heap, lessThan)
    return KMergeBy(heap, lessThan)
}

/**
 * Create an iterator that merges elements of the contained iterators.
 */
fun <T : Comparable<*>> kmergeIterators(iterators: Iterable<Iterator<T>>): KMergeBy<T> =
    kmergeIteratorsBy(iterators) { a, b -> compareValues(a, b) < 0 }

/**
 * Create an iterator that merges elements of the contained iterators using [lessThan].
 */
fun <T> kmergeIteratorsBy(iterators: Iterable<Iterator<T>>, lessThan: (T, T) -> Boolean): KMergeBy<T> {
    val heap = mutableListOf<HeadTail<T>>()
    for (it in iterators) {
        val ht = HeadTail.create(it)
        if (ht != null) {
            heap.add(ht)
        }
    }
    heapify(heap, lessThan)
    return KMergeBy(heap, lessThan)
}
