package love.forte.codegentle.common.writer

import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(InternalWriterApi::class)
class LineWrapperTest {

    @Test
    fun testSimpleAppend() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 100)

        wrapper.append("Hello, world!")
        wrapper.close()

        assertEquals("Hello, world!", out.toString())
    }

    @Test
    fun testAppendWithNewlines() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 100)

        wrapper.append("Hello,\nworld!")
        wrapper.close()

        assertEquals("Hello,\nworld!", out.toString())
    }

    @Test
    fun testWrappingSpace() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 20)

        // This should fit on one line
        wrapper.append("Hello,")
        wrapper.wrappingSpace(1)
        wrapper.append("world!")
        wrapper.close()

        assertEquals("Hello, world!", out.toString())
    }

    @Test
    fun testWrappingSpaceWithWrap() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 10)

        // This should wrap to the next line
        wrapper.append("Hello,")
        wrapper.wrappingSpace(1)
        wrapper.append("world!")
        wrapper.close()

        assertEquals("Hello,\n  world!", out.toString())
    }

    @Test
    fun testMultipleWrappingSpaces() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 10)

        // This should wrap multiple times
        wrapper.append("Hello,")
        wrapper.wrappingSpace(1)
        wrapper.append("beautiful")
        wrapper.wrappingSpace(1)
        wrapper.append("world!")
        wrapper.close()

        assertEquals("Hello,\n  beautiful\n  world!", out.toString())
    }

    @Test
    fun testZeroWidthSpace() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 20)

        // This should fit on one line
        wrapper.append("Hello,")
        wrapper.zeroWidthSpace(1)
        wrapper.append("world!")
        wrapper.close()

        assertEquals("Hello,world!", out.toString())
    }

    @Test
    fun testZeroWidthSpaceWithWrap() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 10)

        // This should wrap to the next line
        wrapper.append("Hello,")
        wrapper.zeroWidthSpace(1)
        wrapper.append("world!")
        wrapper.close()

        assertEquals("Hello,\n  world!", out.toString())
    }

    @Test
    fun testMixedSpaces() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 20)

        // This should have a mix of wrapping and zero-width spaces
        wrapper.append("Hello,")
        wrapper.wrappingSpace(1)
        wrapper.append("beautiful")
        wrapper.zeroWidthSpace(1)
        wrapper.append("world!")
        wrapper.close()

        assertEquals("Hello, beautiful\n  world!", out.toString())
    }

    @Test
    fun testLongLineWithoutSpaces() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 10)

        // This should not wrap even though it's longer than the column limit
        wrapper.append("ThisIsAVeryLongLineWithoutAnySpaces")
        wrapper.close()

        assertEquals("ThisIsAVeryLongLineWithoutAnySpaces", out.toString())
    }

    @Test
    fun testMultipleIndentLevels() {
        val out = StringBuilder()
        val wrapper = LineWrapper.create(out, "  ", 20)

        // This should have different indent levels
        wrapper.append("if (condition) {")
        wrapper.append("\n")
        wrapper.append("  ")
        wrapper.append("someCode();")
        wrapper.wrappingSpace(2)
        wrapper.append("moreLongCode();")
        wrapper.append("\n")
        wrapper.append("}")
        wrapper.close()

        assertEquals("if (condition) {\n  someCode();\n    moreLongCode();\n}", out.toString())
    }
}
