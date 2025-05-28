package love.forte.codegentle.common.code

import love.forte.codegentle.common.naming.ClassName
import kotlin.test.*

class CodeValueTest {
    
    @Test
    fun testEmptyCodeValue() {
        val emptyValue = CodeValue()
        assertTrue(emptyValue.isEmpty)
        assertEquals(0, emptyValue.parts.size)
    }
    
    @Test
    fun testCodeValueWithSimplePart() {
        val value = CodeValue(CodePart.simple("test"))
        assertFalse(value.isEmpty)
        assertEquals(1, value.parts.size)
        assertEquals(CodePart.simple("test"), value.parts[0])
    }
    
    @Test
    fun testCodeValueWithMultipleParts() {
        val parts = listOf(
            CodePart.simple("test1"),
            CodePart.simple("test2")
        )
        val value = CodeValue(parts)
        assertFalse(value.isEmpty)
        assertEquals(2, value.parts.size)
        assertEquals(parts, value.parts)
    }
    
    @Test
    fun testCodeValuePlusOperator() {
        val value1 = CodeValue(CodePart.simple("test1"))
        val value2 = CodeValue(CodePart.simple("test2"))
        val combined = value1 + value2
        
        assertEquals(2, combined.parts.size)
        assertEquals(CodePart.simple("test1"), combined.parts[0])
        assertEquals(CodePart.simple("test2"), combined.parts[1])
    }
    
    @Test
    fun testCodeValueWithFormat() {
        val value = CodeValue("%V, %V") {
            string("hello")
            string("world")
        }
        
        assertEquals(3, value.parts.size)
        assertEquals(CodePart.string("hello"), value.parts[0])
        assertEquals(CodePart.simple(", "), value.parts[1])
        assertEquals(CodePart.string("world"), value.parts[2])
    }
    
    @Test
    fun testCodeValueWithFormatAndVarargs() {
        val value = CodeValue("%V, %V", CodePart.string("hello"), CodePart.string("world"))
        
        assertEquals(3, value.parts.size)
        assertEquals(CodePart.string("hello"), value.parts[0])
        assertEquals(CodePart.simple(", "), value.parts[1])
        assertEquals(CodePart.string("world"), value.parts[2])
    }
    
    @Test
    fun testCodeValueBuilder() {
        val value = CodeValue.builder().apply {
            add("Hello, ")
            add("%V", CodePart.string("world"))
            add("!")
        }.build()
        
        assertEquals(3, value.parts.size)
        assertEquals(CodePart.simple("Hello, "), value.parts[0])
        assertEquals(CodePart.string("world"), value.parts[1])
        assertEquals(CodePart.simple("!"), value.parts[2])
    }
    
    @Test
    fun testCodeValueBuilderWithControlFlow() {
        val value = CodeValue.builder().apply {
            beginControlFlow("if (condition)")
            addStatement("%V.doSomething()", CodePart.string("object"))
            endControlFlow()
        }.build()
        
        // Check that the control flow structure is correct
        assertTrue(value.parts.contains(CodePart.indent()))
        assertTrue(value.parts.contains(CodePart.unindent()))
    }
    
    @Test
    fun testCodeValueSingleFormatBuilder() {
        val builder = CodeValue.builder("%V.%V(%V)")
        builder.type(ClassName("test", "Test"))
        builder.literal("method")
        builder.string("arg")
        
        val value = builder.build()
        
        assertEquals(6, value.parts.size)
        assertTrue(value.parts[0] is CodeArgumentPart.Type)
        assertEquals(CodePart.simple("."), value.parts[1])
        assertEquals(CodePart.literal("method"), value.parts[2])
        assertEquals(CodePart.simple("("), value.parts[3])
        assertEquals(CodePart.string("arg"), value.parts[4])
        assertEquals(CodePart.simple(")"), value.parts[5])
    }
    
    @Test
    fun testCodeValueSingleFormatBuilderWithMissingArgument() {
        val builder = CodeValue.builder("%V.%V(%V)")
        builder.type(ClassName("test", "Test"))
        builder.literal("method")
        
        try {
            builder.build()
            kotlin.test.fail("Expected an exception for missing argument")
        } catch (e: IllegalStateException) {
            // Expected exception
        }
    }
    
    @Test
    fun testCodeValueSingleFormatBuilderWithRedundantArgument() {
        val builder = CodeValue.builder("%V.%V")
        builder.type(ClassName("test", "Test"))
        builder.literal("method")
        builder.string("redundant")

        assertFailsWith<IllegalStateException> {
            builder.build()
            kotlin.test.fail("Expected an exception for redundant argument")
        }
    }
}
