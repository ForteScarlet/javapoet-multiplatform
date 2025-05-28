package love.forte.codegentle.common.codepoint

import kotlin.test.Test
import kotlin.test.assertEquals

class CodePointTest {

    @Test
    fun testCharCountCommon() {
        // BMP character (Basic Multilingual Plane, U+0000 to U+FFFF)
        val bmpCodePoint = CodePoint(0x0041u) // 'A'
        assertEquals(1, bmpCodePoint.charCountCommon())

        // Supplementary character (beyond BMP, U+10000 to U+10FFFF)
        val supplementaryCodePoint = CodePoint(0x1F600u) // 😀 GRINNING FACE
        assertEquals(2, supplementaryCodePoint.charCountCommon())
    }

    @Test
    fun testAppendCodePointCommon() {
        // BMP character
        val bmpCodePoint = CodePoint(0x0041u) // 'A'
        val bmpBuilder = StringBuilder()
        bmpBuilder.appendCodePointCommon(bmpCodePoint)
        assertEquals("A", bmpBuilder.toString())

        // Supplementary character
        val supplementaryCodePoint = CodePoint(0x1F600u) // 😀 GRINNING FACE
        val supplementaryBuilder = StringBuilder()
        supplementaryBuilder.appendCodePointCommon(supplementaryCodePoint)
        assertEquals("😀", supplementaryBuilder.toString())

        // Multiple code points
        val builder = StringBuilder()
        builder.appendCodePointCommon(CodePoint(0x0041u)) // 'A'
        builder.appendCodePointCommon(CodePoint(0x0042u)) // 'B'
        builder.appendCodePointCommon(CodePoint(0x1F600u)) // 😀
        assertEquals("AB😀", builder.toString())
    }

    @Test
    fun testCategory() {
        // BMP characters
        val letterCodePoint = CodePoint(0x0041u) // 'A'
        assertEquals(CharCategory.UPPERCASE_LETTER, letterCodePoint.category())

        val digitCodePoint = CodePoint(0x0030u) // '0'
        assertEquals(CharCategory.DECIMAL_DIGIT_NUMBER, digitCodePoint.category())

        val spaceCodePoint = CodePoint(0x0020u) // ' '
        assertEquals(CharCategory.SPACE_SEPARATOR, spaceCodePoint.category())

        // Supplementary characters
        val supplementaryLetterCodePoint = CodePoint(0x10330u) // GOTHIC LETTER AHSA
        assertEquals(CharCategory.OTHER_LETTER, supplementaryLetterCodePoint.category())

        val supplementarySymbolCodePoint = CodePoint(0x1F000u) // MAHJONG TILE EAST WIND
        assertEquals(CharCategory.OTHER_SYMBOL, supplementarySymbolCodePoint.category())
    }

    @Test
    fun testCodePointCreation() {
        val codePoint = CodePoint(0x0041u) // 'A'
        assertEquals(0x0041u, codePoint.code)

        val supplementaryCodePoint = CodePoint(0x1F600u) // 😀
        assertEquals(0x1F600u, supplementaryCodePoint.code)
    }
}
