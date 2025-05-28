package love.forte.codegentle.java.writer

import love.forte.codegentle.common.code.CodePart.Companion.literal
import love.forte.codegentle.common.code.CodePart.Companion.string
import love.forte.codegentle.common.code.CodePart.Companion.type
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.ref.annotationRef
import love.forte.codegentle.common.ref.status
import love.forte.codegentle.java.ref.javaRef
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for JavaWriter extension functions like writeToJavaString.
 *
 * @author AI Assistant
 */
class JavaWriterExtensionsTests {

    @Test
    fun testCodeValueWriteToJavaString() {
        val codeValue = CodeValue("%V.out.println(%V)") {
            type(ClassName("java.lang", "System"))
            string("Hello, World!")
        }

        assertEquals("System.out.println(\"Hello, World!\")", codeValue.writeToJavaString())
    }

    @Test
    fun testTypeNameWriteToJavaString() {
        val className = ClassName("java.util", "List")

        assertEquals("java.util.List", className.writeToJavaString())
    }

    @Test
    fun testAnnotationRefWriteToJavaString() {
        val annotationRef = ClassName("java.lang", "Override").annotationRef()

        assertEquals("@java.lang.Override", annotationRef.writeToJavaString())
    }

    @Test
    fun testTypeRefWriteToJavaString() {
        val typeRef = ClassName("java.lang", "String").javaRef()

        assertEquals("java.lang.String", typeRef.writeToJavaString())
    }

    @Test
    fun testComplexCodeValueWriteToJavaString() {
        val codeValue = CodeValue("%V %V = %V;") {
            type(ClassName("java.lang", "String"))
            literal("name")
            string("Hello World")
        }

        assertEquals("java.lang.String name = \"Hello World\";", codeValue.writeToJavaString())
    }

    @Test
    fun testCodeValueWithMultipleFormatsWriteToJavaString() {
        val codeValue = CodeValue("for (%V %V : %V) {\n  %V.%V(%V);\n}") {
            type(ClassName("java.lang", "String"))
            literal("item")
            literal("items")
            literal("System")
            literal("out.println")
            literal("item")
        }

        val expected = "for (java.lang.String item : items) {\n  System.out.println(item);\n}"
        assertEquals(expected, codeValue.writeToJavaString())
    }

    @Test
    fun testAnnotationRefWithMembersWriteToJavaString() {
        val annotationRef = ClassName("javax.annotation", "Resource").annotationRef {
            addMember("name", "%V", string("myResource"))
            addMember("type", "%V", type(ClassName("java.lang", "String")))
        }

        assertEquals(
            "@javax.annotation.Resource(name = \"myResource\", type = java.lang.String)",
            annotationRef.writeToJavaString()
        )
    }

    @Test
    fun testTypeRefWithAnnotationsWriteToJavaString() {
        val annotationRef = ClassName("javax.annotation", "Nonnull").annotationRef()
        val typeRef = ClassName("java.lang", "String").javaRef {
            status {
                addAnnotationRef(annotationRef)
            }
        }

        assertEquals("@javax.annotation.Nonnull java.lang.String", typeRef.writeToJavaString())
    }
}
