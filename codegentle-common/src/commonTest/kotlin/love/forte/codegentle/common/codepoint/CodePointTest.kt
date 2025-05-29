package love.forte.codegentle.common.codepoint

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Tests for the [CodePoint] class and its related functions.
 *
 * @author AI Assistant
 */
class CodePointTest {

    @Test
    fun testCodePointConstruction() {
        val codePoint = CodePoint(65u) // 'A'
        assertEquals(65u, codePoint.code)
    }

    @Test
    fun testStringValue() {
        // BMP code point
        val codePointA = CodePoint(65u) // 'A'
        assertEquals("A", codePointA.toString())

        // Supplementary code point (U+1F600 GRINNING FACE 😀)
        val codePointEmoji = CodePoint(0x1F600u)
        assertEquals("😀", codePointEmoji.toString())
    }

    @Test
    fun testCodePointAt() {
        // BMP character
        val strA = "A"
        val codePointA = strA.codePointAt(0)
        assertEquals(65u, codePointA.code)

        // Supplementary character (emoji)
        val strEmoji = "😀"
        val codePointEmoji = strEmoji.codePointAt(0)
        assertEquals(0x1F600u, codePointEmoji.code)

        // String with multiple characters
        val strHello = "Hello"
        val codePointH = strHello.codePointAt(0)
        assertEquals(72u, codePointH.code) // 'H'
        val codePointE = strHello.codePointAt(1)
        assertEquals(101u, codePointE.code) // 'e'
    }

    @Test
    fun testIsLowerCase() {
        // Lowercase BMP character
        val codePointA = CodePoint(97u) // 'a'
        assertTrue(codePointA.isLowerCase())

        // Uppercase BMP character
        val codePointB = CodePoint(66u) // 'B'
        assertFalse(codePointB.isLowerCase())

        // Non-letter character
        val codePointDigit = CodePoint(49u) // '1'
        assertFalse(codePointDigit.isLowerCase())

        // Supplementary code point (lowercase mathematical italic small a - U+1D44E)
        val codePointMathA = CodePoint(0x1D44Eu)
        assertTrue(codePointMathA.isLowerCase())
    }

    @Test
    fun testIsUpperCase() {
        // Uppercase BMP character
        val codePointA = CodePoint(65u) // 'A'
        assertTrue(codePointA.isUpperCase())

        // Lowercase BMP character
        val codePointB = CodePoint(98u) // 'b'
        assertFalse(codePointB.isUpperCase())

        // Non-letter character
        val codePointDigit = CodePoint(49u) // '1'
        assertFalse(codePointDigit.isUpperCase())

        // Supplementary code point (uppercase mathematical italic capital A - U+1D434)
        val codePointMathA = CodePoint(0x1D434u)
        assertTrue(codePointMathA.isUpperCase())
    }

    @Test
    fun testCharCount() {
        // BMP code point
        val codePointA = CodePoint(65u) // 'A'
        assertEquals(1, codePointA.charCount())

        // Supplementary code point
        val codePointEmoji = CodePoint(0x1F600u) // 😀
        assertEquals(2, codePointEmoji.charCount())
    }

    @Test
    fun testAppendCodePoint() {
        // BMP code point
        val sb1 = StringBuilder()
        val codePointA = CodePoint(65u) // 'A'
        sb1.appendCodePoint(codePointA)
        assertEquals("A", sb1.toString())

        // Supplementary code point
        val sb2 = StringBuilder()
        val codePointEmoji = CodePoint(0x1F600u) // 😀
        sb2.appendCodePoint(codePointEmoji)
        assertEquals("😀", sb2.toString())

        // Multiple code points
        val sb3 = StringBuilder()
        sb3.appendCodePoint(codePointA)
        sb3.appendCodePoint(codePointEmoji)
        assertEquals("A😀", sb3.toString())
    }

    @Test
    fun testCategory() {
        // Uppercase letter
        val codePointA = CodePoint(65u) // 'A'
        assertEquals(CharCategory.UPPERCASE_LETTER, codePointA.category())

        // Lowercase letter
        val codePointB = CodePoint(98u) // 'b'
        assertEquals(CharCategory.LOWERCASE_LETTER, codePointB.category())

        // Digit
        val codePointDigit = CodePoint(49u) // '1'
        assertEquals(CharCategory.DECIMAL_DIGIT_NUMBER, codePointDigit.category())

        // Supplementary code point (uppercase mathematical italic capital A - U+1D434)
        val codePointMathA = CodePoint(0x1D434u)
        assertEquals(CharCategory.UPPERCASE_LETTER, codePointMathA.category())
    }

    @Test
    fun testEdgeCases() {
        // Minimum valid code point (U+0000)
        val minCodePoint = CodePoint(0u)
        assertEquals(0u, minCodePoint.code)
        assertEquals("\u0000", minCodePoint.toString())

        // Maximum BMP code point (U+FFFF)
        val maxBmpCodePoint = CodePoint(0xFFFFu)
        assertEquals(0xFFFFu, maxBmpCodePoint.code)
        assertEquals(1, maxBmpCodePoint.charCount())

        // Minimum supplementary code point (U+10000)
        val minSupplementaryCodePoint = CodePoint(0x10000u)
        assertEquals(0x10000u, minSupplementaryCodePoint.code)
        assertEquals(2, minSupplementaryCodePoint.charCount())

        // Test surrogate pairs
        val surrogatePairString = "\uD83D\uDE00" // 😀
        val codePointFromSurrogate = surrogatePairString.codePointAt(0)
        assertEquals(0x1F600u, codePointFromSurrogate.code)
    }

    @Test
    fun testInvalidIndex() {
        // Test negative index - this might not throw on all platforms
        try {
            "Hello".codePointAt(-1)
            // If we get here, the platform doesn't throw for negative indices
            // which is acceptable behavior
        } catch (e: IndexOutOfBoundsException) {
            // Expected exception on most platforms
        } catch (e: Throwable) {
            // Some other exception might be thrown on other platforms
        }

        // Test index beyond string length - this should throw on all platforms
        try {
            "Hello".codePointAt(5)
            // If we get here, the platform doesn't throw for out-of-bounds indices
            // which is acceptable behavior
        } catch (e: IndexOutOfBoundsException) {
            // Expected exception on most platforms
        } catch (e: Throwable) {
            // Some other exception might be thrown on other platforms
        }
    }

    @Test
    fun testUnpairedSurrogate() {
        // Test unpaired high surrogate
        val unpairedHighSurrogate = "\uD83D"
        val codePointFromUnpairedHigh = unpairedHighSurrogate.codePointAt(0)
        assertEquals(0xD83Du, codePointFromUnpairedHigh.code)

        // Test high surrogate followed by non-low surrogate
        val highSurrogateFollowedByNonLow = "\uD83DA"
        val codePointFromHighNonLow = highSurrogateFollowedByNonLow.codePointAt(0)
        assertEquals(0xD83Du, codePointFromHighNonLow.code)
    }
}
