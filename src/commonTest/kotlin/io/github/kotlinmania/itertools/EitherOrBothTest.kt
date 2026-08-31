// port-lint: tests either_or_both.rs
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

    @Test
    fun testIntoAndAndThen() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("42")
        val both: EitherOrBoth<Int, String> = EitherOrBoth.Both(1, "42")

        assertEquals(1, left.intoLeft { it.toInt() })
        assertEquals(42, right.intoLeft { it.toInt() })
        assertEquals(1, both.intoLeft { it.toInt() })

        assertEquals("1", left.intoRight { it.toString() })
        assertEquals("42", right.intoRight { it.toString() })
        assertEquals("42", both.intoRight { it.toString() })

        assertEquals(EitherOrBoth.Left(10), left.leftAndThen { EitherOrBoth.Left(it * 10) })
        assertEquals(EitherOrBoth.Right("42"), right.leftAndThen { EitherOrBoth.Left(999) })
        assertEquals(EitherOrBoth.Left(10), both.leftAndThen { EitherOrBoth.Left(it * 10) })

        assertEquals(EitherOrBoth.Left(1), left.rightAndThen { EitherOrBoth.Right("new") })
        assertEquals(EitherOrBoth.Right("42!"), right.rightAndThen { EitherOrBoth.Right(it + "!") })
        assertEquals(EitherOrBoth.Right("42!"), both.rightAndThen { EitherOrBoth.Right(it + "!") })
    }

    @Test
    fun testReferencesAndDefaults() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("a")
        val both: EitherOrBoth<Int, String> = EitherOrBoth.Both(1, "a")

        assertEquals(left, left.asRef())
        assertEquals(left, left.asMut())
        assertEquals(left, left.asDeref())
        assertEquals(left, left.asDerefMut())

        assertEquals(Pair(1, "defaultB"), left.orDefault({ 0 }, { "defaultB" }))
        assertEquals(Pair(0, "a"), right.orDefault({ 0 }, { "defaultB" }))
        assertEquals(Pair(1, "a"), both.orDefault({ 0 }, { "defaultB" }))
    }

    @Test
    fun testInsertions() {
        val left: EitherOrBoth<Int, String> = EitherOrBoth.Left(1)
        val right: EitherOrBoth<Int, String> = EitherOrBoth.Right("a")

        assertEquals(EitherOrBoth.Left(1), left.leftOrInsert(99))
        assertEquals(EitherOrBoth.Both(99, "a"), right.leftOrInsert(99))
        assertEquals(EitherOrBoth.Both(1, "b"), left.rightOrInsert("b"))
        assertEquals(EitherOrBoth.Right("a"), right.rightOrInsert("b"))

        assertEquals(EitherOrBoth.Left(1), left.leftOrInsertWith { 99 })
        assertEquals(EitherOrBoth.Both(99, "a"), right.leftOrInsertWith { 99 })
        assertEquals(EitherOrBoth.Both(1, "b"), left.rightOrInsertWith { "b" })
        assertEquals(EitherOrBoth.Right("a"), right.rightOrInsertWith { "b" })

        assertEquals(EitherOrBoth.Left(42), left.insertLeft(42))
        assertEquals(EitherOrBoth.Both(42, "a"), right.insertLeft(42))
        assertEquals(EitherOrBoth.Both(1, "z"), left.insertRight("z"))
        assertEquals(EitherOrBoth.Right("z"), right.insertRight("z"))

        assertEquals(EitherOrBoth.Both(10, "hello"), left.insertBoth(10, "hello"))
    }

    @Test
    fun testFromAndToEither() {
        val eitherLeft: Either<Int, String> = Either.Left(10)
        val eitherRight: Either<Int, String> = Either.Right("foo")

        val fromLeft = EitherOrBoth.from(eitherLeft)
        val fromRight = EitherOrBoth.from(eitherRight)

        assertEquals(EitherOrBoth.Left(10), fromLeft)
        assertEquals(EitherOrBoth.Right("foo"), fromRight)

        assertEquals(Either.Left(10), fromLeft.toEither())
        assertEquals(Either.Right("foo"), fromRight.toEither())
        assertNull(EitherOrBoth.Both(10, "foo").toEither())
    }
}
