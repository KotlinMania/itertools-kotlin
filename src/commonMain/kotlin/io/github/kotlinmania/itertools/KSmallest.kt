// port-lint: source src/k_smallest.rs
package io.github.kotlinmania.itertools

/** Consumes a given iterator, returning the minimum elements in **ascending** order. */
internal fun <T> kSmallestGeneral(
    iter: Iterator<T>,
    k: Int,
    comparator: Comparator<T>,
): MutableList<T> {
    /**
     * Sift the element currently at `origin` away from the root until it is properly
     * ordered.
     *
     * This will leave **larger** elements closer to the root of the heap.
     */
    fun siftDown(
        heap: MutableList<T>,
        isLessThan: (T, T) -> Boolean,
        originIn: Int,
    ) {
        fun childrenOf(n: Int): Pair<Int, Int> = (2 * n + 1) to (2 * n + 2)

        var origin = originIn
        while (origin < heap.size) {
            val (leftIdx, rightIdx) = childrenOf(origin)
            if (leftIdx >= heap.size) return

            val replacementIdx =
                if (rightIdx < heap.size && isLessThan(heap[leftIdx], heap[rightIdx])) {
                    rightIdx
                } else {
                    leftIdx
                }

            if (isLessThan(heap[origin], heap[replacementIdx])) {
                val tmp = heap[origin]
                heap[origin] = heap[replacementIdx]
                heap[replacementIdx] = tmp
                origin = replacementIdx
            } else {
                return
            }
        }
    }

    if (k == 0) {
        while (iter.hasNext()) iter.next()
        return mutableListOf()
    }
    if (k == 1) {
        if (!iter.hasNext()) return mutableListOf()
        var best = iter.next()
        while (iter.hasNext()) {
            val next = iter.next()
            if (comparator.compare(next, best) < 0) best = next
        }
        return mutableListOf(best)
    }
    val storage: MutableList<T> = mutableListOf()
    while (storage.size < k && iter.hasNext()) {
        storage.add(iter.next())
    }

    val isLessThan: (T, T) -> Boolean = { a, b -> comparator.compare(a, b) < 0 }

    // Rearrange the storage into a valid heap by reordering from the second-bottom-most
    // layer up to the root.
    // Slightly faster than ordering on each insert, but only by a factor of lg(k).
    // The resulting heap has the **largest** item on top.
    for (i in (storage.size / 2) downTo 0) {
        siftDown(storage, isLessThan, i)
    }

    while (iter.hasNext()) {
        val value = iter.next()
        check(storage.size == k)
        if (isLessThan(value, storage[0])) {
            // Treating this as a push-and-pop saves having to write a sift-up implementation.
            // https://en.wikipedia.org/wiki/Binary_heap#Insert_then_extract
            storage[0] = value
            // We retain the smallest items we've seen so far, but ordered largest first so
            // we can drop the largest efficiently.
            siftDown(storage, isLessThan, 0)
        }
    }

    // Ultimately the items need to be in least-first, strict order, but the heap is
    // currently largest-first.
    // To achieve this, repeatedly,
    // 1) "pop" the largest item off the heap into the tail slot of the underlying storage,
    // 2) shrink the logical size of the heap by 1,
    // 3) restore the heap property over the remaining items.
    var heap: MutableList<T> = storage
    while (heap.size > 1) {
        val lastIdx = heap.size - 1
        val tmp = heap[0]
        heap[0] = heap[lastIdx]
        heap[lastIdx] = tmp
        // Sifting over a truncated slice means that the sifting will not disturb already
        // popped elements.
        heap = heap.subList(0, lastIdx)
        siftDown(heap, isLessThan, 0)
    }

    return storage
}

internal fun <T> kSmallestRelaxedGeneral(
    iter: Iterator<T>,
    k: Int,
    comparator: Comparator<T>,
): MutableList<T> {
    if (k == 0) {
        while (iter.hasNext()) iter.next()
        return mutableListOf()
    }

    val buf: MutableList<T> = mutableListOf()
    while (buf.size < 2 * k && iter.hasNext()) {
        buf.add(iter.next())
    }

    if (buf.size < k) {
        buf.sortWith(comparator)
        return buf
    }

    // Kotlin stdlib has no `selectNthUnstableBy` equivalent; fall back to a full sort
    // followed by truncation, which preserves the upstream invariant that the first `k`
    // elements are the `k` smallest in sorted order.
    buf.sortWith(comparator)
    while (buf.size > k) buf.removeAt(buf.size - 1)

    while (iter.hasNext()) {
        val value = iter.next()
        if (comparator.compare(value, buf[k - 1]) >= 0) {
            continue
        }

        buf.add(value)

        if (buf.size == 2 * k) {
            buf.sortWith(comparator)
            while (buf.size > k) buf.removeAt(buf.size - 1)
        }
    }

    buf.sortWith(comparator)
    while (buf.size > k) buf.removeAt(buf.size - 1)
    return buf
}

internal fun <T, K : Comparable<K>> keyToCmp(key: (T) -> K): Comparator<T> =
    Comparator { a, b -> key(a).compareTo(key(b)) }
