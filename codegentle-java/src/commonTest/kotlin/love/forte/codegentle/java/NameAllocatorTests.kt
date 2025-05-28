package love.forte.codegentle.java

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

/**
 * Tests for NameAllocator functionality.
 *
 * @author AI Assistant
 */
class NameAllocatorTests {

    @Test
    fun testNewNameWithSuggestion() {
        val allocator = NameAllocator()
        val name = allocator.newName("test")

        // The name should start with "test"
        assertTrue(name.startsWith("test"))
    }

    @Test
    fun testNewNameWithTag() {
        val allocator = NameAllocator()
        val name = allocator.newName("test", "tag1")

        // The name should be retrievable by tag
        assertEquals(name, allocator.get("tag1"))
    }

    @Test
    fun testNewNameWithInvalidJavaIdentifier() {
        val allocator = NameAllocator()
        val name = allocator.newName("123test")

        // The name should be a valid Java identifier (shouldn't start with a number)
        assertTrue(!name.startsWith("123"))
        assertTrue(name.contains("test"))
    }

    @Test
    fun testNewNameWithJavaKeyword() {
        val allocator = NameAllocator()
        val name = allocator.newName("class")

        // The name should not be a Java keyword
        assertTrue(name != "class")
        assertTrue(name.startsWith("class"))
    }

    @Test
    fun testNewNameWithDuplicateSuggestion() {
        val allocator = NameAllocator()
        val name1 = allocator.newName("test")
        val name2 = allocator.newName("test")

        // The names should be different
        assertTrue(name1 != name2)
    }

    @Test
    fun testNewNameWithDuplicateTag() {
        val allocator = NameAllocator()
        allocator.newName("test1", "tag")

        // Using the same tag for a different name should throw an exception
        assertFailsWith<IllegalArgumentException> {
            allocator.newName("test2", "tag")
        }
    }

    @Test
    fun testGetWithUnknownTag() {
        val allocator = NameAllocator()

        // Getting a name for an unknown tag should throw an exception
        assertFailsWith<IllegalArgumentException> {
            allocator.get("unknown")
        }
    }

    @Test
    fun testSpecialCharactersInName() {
        val allocator = NameAllocator()
        val name = allocator.newName("test@123")

        // Special characters should be replaced with underscores
        assertTrue(!name.contains("@"))
        assertTrue(name.contains("_"))
    }
}
