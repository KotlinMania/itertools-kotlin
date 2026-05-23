// port-lint: source src/format.rs
package io.github.kotlinmania.itertools

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class FormatTest {
    @Test
    fun formatRendersElementsSeparated() {
        val rendered = newFormatDefault(listOf(1, 2, 3).iterator(), ", ").toString()
        assertEquals("1, 2, 3", rendered)
    }

    @Test
    fun formatRendersEmptyIteratorAsEmptyString() {
        val rendered = newFormatDefault(emptyList<Int>().iterator(), ", ").toString()
        assertEquals("", rendered)
    }

    @Test
    fun formatRendersWithoutSeparatorWhenSepIsEmpty() {
        val rendered = newFormatDefault(listOf("a", "b", "c").iterator(), "").toString()
        assertEquals("abc", rendered)
    }

    @Test
    fun formatRejectsSecondInvocation() {
        val once = newFormatDefault(listOf(1, 2).iterator(), "-")
        assertEquals("1-2", once.toString())
        assertFailsWith<IllegalStateException> { once.toString() }
    }

    @Test
    fun formatWithUsesCustomFormatterCallback() {
        val rendered = newFormat(listOf(1, 2, 3).iterator(), "-") { item, emit ->
            emit("0x")
            emit(item)
        }.toString()
        assertEquals("0x1-0x2-0x3", rendered)
    }

    @Test
    fun formatWithEmptyIteratorIsEmptyString() {
        val rendered = newFormat(emptyList<Int>().iterator(), ", ") { _, emit -> emit("nope") }
            .toString()
        assertEquals("", rendered)
    }

    @Test
    fun formatWithSingleElementHasNoSeparator() {
        val rendered = newFormat(listOf(42).iterator(), ", ") { item, emit -> emit(item) }
            .toString()
        assertEquals("42", rendered)
    }

    @Test
    fun formatWithRejectsSecondInvocation() {
        val once = newFormat(listOf(1).iterator(), "/") { item, emit -> emit(item) }
        assertEquals("1", once.toString())
        assertFailsWith<IllegalStateException> { once.toString() }
    }
}
