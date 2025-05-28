package love.forte.codegentle.common.code

import love.forte.codegentle.common.naming.ClassName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class CodePartTest {

    @Test
    fun testSimplePart() {
        val part = CodePart.simple("test")
        assertEquals("test", part.value)
        assertEquals("CodeSimplePart(value='test')", part.toString())

        // Test equality
        assertEquals(CodePart.simple("test"), part)
        assertNotEquals(CodePart.simple("other"), part)
    }

    @Test
    fun testLiteralPart() {
        val part = CodePart.literal("test")
        assertEquals("test", (part as CodeArgumentPart.Literal).value)
        assertEquals("Literal(value=test)", part.toString())

        // Test equality
        assertEquals(CodePart.literal("test"), part)
        assertNotEquals(CodePart.literal("other"), part)

        // Test with different types
        val intPart = CodePart.literal(42)
        assertEquals(42, (intPart as CodeArgumentPart.Literal).value)

        val nullPart = CodePart.literal(null)
        assertEquals(null, (nullPart as CodeArgumentPart.Literal).value)
    }

    @Test
    fun testNamePart() {
        val part = CodePart.name(name = "test")
        assertEquals("test", (part as CodeArgumentPart.Name).name)
        assertEquals("Name(name=test)", part.toString())

        // Test equality
        assertEquals(CodePart.name(name = "test"), part)
        assertNotEquals(CodePart.name(name = "other"), part)

        // Test with null
        val nullPart = CodePart.name(name = null)
        assertEquals(null, (nullPart as CodeArgumentPart.Name).name)
    }

    @Test
    fun testStringPart() {
        val part = CodePart.string("test")
        assertEquals("test", (part as CodeArgumentPart.Str).value)
        assertEquals("Str(value=test)", part.toString())

        // Test equality
        assertEquals(CodePart.string("test"), part)
        assertNotEquals(CodePart.string("other"), part)

        // Test with null
        val nullPart = CodePart.string(null)
        assertEquals(null, (nullPart as CodeArgumentPart.Str).value)
    }

    @Test
    fun testTypePart() {
        val className = ClassName("test", "Test")
        val part = CodePart.type(className)
        assertEquals(className, (part as CodeArgumentPart.Type).type)
        assertEquals("Type(type=$className)", part.toString())

        // Test equality
        assertEquals(CodePart.type(className), part)
        assertNotEquals(CodePart.type(ClassName("other", "Other")), part)
    }

    @Test
    fun testIndentPart() {
        val part = CodePart.indent(2)
        assertEquals(2, (part as CodeArgumentPart.Indent).levels)
        assertEquals("Indent(levels=2)", part.toString())

        // Test equality
        assertEquals(CodePart.indent(2), part)
        assertNotEquals(CodePart.indent(1), part)

        // Test default value
        val defaultPart = CodePart.indent()
        assertEquals(1, (defaultPart as CodeArgumentPart.Indent).levels)
    }

    @Test
    fun testUnindentPart() {
        val part = CodePart.unindent(2)
        assertEquals(2, (part as CodeArgumentPart.Unindent).levels)
        assertEquals("Unindent(levels=2)", part.toString())

        // Test equality
        assertEquals(CodePart.unindent(2), part)
        assertNotEquals(CodePart.unindent(1), part)

        // Test default value
        val defaultPart = CodePart.unindent()
        assertEquals(1, (defaultPart as CodeArgumentPart.Unindent).levels)
    }

    @Test
    fun testStatementParts() {
        val beginPart = CodePart.statementBegin()
        val endPart = CodePart.statementEnd()

        assertEquals(CodeArgumentPart.StatementBegin, beginPart)
        assertEquals(CodeArgumentPart.StatementEnd, endPart)
    }

    @Test
    fun testSpaceParts() {
        val wrappingPart = CodePart.wrappingSpace()
        val zeroWidthPart = CodePart.zeroWidthSpace()

        assertEquals(CodeArgumentPart.WrappingSpace, wrappingPart)
        assertEquals(CodeArgumentPart.ZeroWidthSpace, zeroWidthPart)
    }

    @Test
    fun testSkipPart() {
        val part = CodePart.skip()
        assertEquals(CodeArgumentPart.Skip, part)
    }

    @Test
    fun testOtherCodeValuePart() {
        val codeValue = CodeValue("test")
        val part = CodePart.otherCodeValue(codeValue)
        assertEquals(codeValue, (part as CodeArgumentPart.OtherCodeValue).value)
        assertEquals("OtherCodeValue(value=$codeValue)", part.toString())

        // Test equality
        assertEquals(CodePart.otherCodeValue(codeValue), part)
        assertNotEquals(CodePart.otherCodeValue(CodeValue("other")), part)
    }
}
