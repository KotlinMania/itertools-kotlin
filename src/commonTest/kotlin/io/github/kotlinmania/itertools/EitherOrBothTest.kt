// port-lint: source src/either_or_both.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class EitherOrBothTest {
    @Test
    fun testVariantsAndPredicates() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("a")
        val both: EitherOrBoth<Int, String> = EitherOrBoth.Both(1, "a")

        assertTrue(left.hasLeft())
        assertFalse(left.hasRight())
        assertTrue(left.isLeft())
        assertFalse(left.isRight())
        assertFalse(left.isBoth())

        assertFalse(right.hasLeft())
        assertTrue(right.hasRight())
        assertFalse(right.isLeft())
        assertTrue(right.isRight())
        assertFalse(right.isBoth())

        assertTrue(both.hasLeft())
        assertTrue(both.hasRight())
        assertFalse(both.isLeft())
        assertFalse(both.isRight())
        assertTrue(both.isBoth())
    }

    @Test
    fun testAccessors() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("a")
        val both: EitherOrBoth<Int, String> = EitherOrBoth.Both(1, "a")

        assertEquals(1, left.left())
        assertNull(left.right())
        assertEquals(Pair(1, null), left.leftAndRight())
        assertEquals(1, left.justLeft())
        assertNull(left.justRight())
        assertNull(left.both())

        assertNull(right.left())
        assertEquals("a", right.right())
        assertEquals(Pair(null, "a"), right.leftAndRight())
        assertNull(right.justLeft())
        assertEquals("a", right.justRight())
        assertNull(right.both())

        assertEquals(1, both.left())
        assertEquals("a", both.right())
        assertEquals(Pair(1, "a"), both.leftAndRight())
        assertNull(both.justLeft())
        assertNull(both.justRight())
        assertEquals(Pair(1, "a"), both.both())
    }

    @Test
    fun testTransformations() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("a")
        val both: EitherOrBoth<Int, String> = EitherOrBoth.Both(1, "a")

        assertEquals(EitherOrBoth.Right(1), left.flip())
        assertEquals(EitherOrBoth.Left("a"), right.flip())
        assertEquals(EitherOrBoth.Both("a", 1), both.flip())

        assertEquals(EitherOrBoth.Left(2), left.mapLeft { it + 1 })
        assertEquals(EitherOrBoth.Right("a"), right.mapLeft { it + 1 })
        assertEquals(EitherOrBoth.Both(2, "a"), both.mapLeft { it + 1 })

        assertEquals(EitherOrBoth.Left(1), left.mapRight { it + "!" })
        assertEquals(EitherOrBoth.Right("a!"), right.mapRight { it + "!" })
        assertEquals(EitherOrBoth.Both(1, "a!"), both.mapRight { it + "!" })

        assertEquals(EitherOrBoth.Left(2), left.mapAny({ it + 1 }, { it + "!" }))
        assertEquals(EitherOrBoth.Right("a!"), right.mapAny({ it + 1 }, { it + "!" }))
        assertEquals(EitherOrBoth.Both(2, "a!"), both.mapAny({ it + 1 }, { it + "!" }))
    }

    @Test
    fun testOrAndReduce() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("a")
        val both: EitherOrBoth<Int, String> = EitherOrBoth.Both(1, "a")

        assertEquals(Pair(1, "b"), left.or(9, "b"))
        assertEquals(Pair(9, "a"), right.or(9, "b"))
        assertEquals(Pair(1, "a"), both.or(9, "b"))

        assertEquals(Pair(1, "b"), left.orElse({ 9 }, { "b" }))
        assertEquals(Pair(9, "a"), right.orElse({ 9 }, { "b" }))
        assertEquals(Pair(1, "a"), both.orElse({ 9 }, { "b" }))

        val pairBoth: EitherOrBoth<Int, Int> = EitherOrBoth.Both(2, 3)
        val pairLeft: EitherOrBoth<Int, Int> = EitherOrBoth.Left(2)
        val pairRight: EitherOrBoth<Int, Int> = EitherOrBoth.Right(3)

        assertEquals(5, pairBoth.reduce { a, b -> a + b })
        assertEquals(2, pairLeft.reduce { a, b -> a + b })
        assertEquals(3, pairRight.reduce { a, b -> a + b })
    }
}
