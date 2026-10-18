package io.github.kotlinmania.itertools.adaptors

import io.github.kotlinmania.itertools.SizeHint

internal fun sizeHintOf(it: Any?): SizeHint =
    when (it) {
        is Collection<*> -> SizeHint(it.size, it.size)
        is Interleave<*> -> it.sizeHint()
        is InterleaveShortest<*> -> it.sizeHint()
        is PutBack<*> -> it.sizeHint()
        is Product<*, *> -> it.sizeHint()
        is TakeWhileRef<*> -> it.sizeHint()
        is WhileSome<*> -> it.sizeHint()
        is Tuple1Combination<*> -> it.sizeHint()
        is Tuple2Combination<*> -> it.sizeHint()
        is Tuple3Combination<*> -> it.sizeHint()
        is BaseTupleKCombination<*> -> it.sizeHint()
        is TupleCombinations<*, *> -> it.sizeHint()
        is FilterOk<*, *> -> it.sizeHint()
        is FilterMapOk<*, *, *> -> it.sizeHint()
        is Positions<*> -> it.sizeHint()
        is Update<*> -> it.sizeHint()
        else -> SizeHint(0, null)
    }
