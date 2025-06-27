package love.forte.codegentle.java.ref

import love.forte.codegentle.common.code.CodePart.Companion.literal
import love.forte.codegentle.common.code.CodePart.Companion.string
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.ref.annotationRef
import love.forte.codegentle.java.writer.writeToJavaString
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for JavaAnnotationRef functionality.
 */
class JavaAnnotationRefTests {

    @Test
    fun testEmptyAnnotation() {
        val anno = ClassName("com.example.anno", "Anno").annotationRef()

        assertEquals(
            "@com.example.anno.Anno",
            anno.writeToJavaString()
        )
    }

    @Test
    fun testAnnotationWithSingleValueMember() {
        val anno = ClassName("com.example.anno", "Anno").annotationRef {
            addMember("value", "%V", string("test"))
        }

        assertEquals(
            "@com.example.anno.Anno(\"test\")",
            anno.writeToJavaString()
        )
    }

    @Test
    fun testAnnotationWithMultipleMembers() {
        val anno = ClassName("com.example.anno", "Anno").annotationRef {
            addMember("name", "%V", string("test"))
            addMember("value", "%V", literal(42))
        }

        assertEquals(
            "@com.example.anno.Anno(name = \"test\", value = 42)",
            anno.writeToJavaString()
        )
    }

    @Test
    fun testAnnotationWithArrayValues() {
        val anno = ClassName("com.example.anno", "Anno").annotationRef {
            addMember("value", "%V", string("test1"))
            addMember("value", "%V", string("test2"))
        }

        assertEquals(
            "@com.example.anno.Anno({\"test1\", \"test2\"})",
            anno.writeToJavaString()
        )
    }

    @Test
    fun testAnnotationWithMixedArrayValues() {
        val anno = ClassName("com.example.anno", "Anno").annotationRef {
            addMember("values", "%V", literal(1))
            addMember("values", "%V", literal(2))
            addMember("values", "%V", literal(3))
            addMember("name", "%V", string("test"))
        }

        assertEquals(
            "@com.example.anno.Anno(values = {1, 2, 3}, name = \"test\")",
            anno.writeToJavaString()
        )
    }
}
