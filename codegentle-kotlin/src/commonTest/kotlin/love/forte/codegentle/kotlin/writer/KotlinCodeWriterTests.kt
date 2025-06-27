package love.forte.codegentle.kotlin.writer

import love.forte.codegentle.common.code.emitName
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.kotlin.ref.kotlinRef
import love.forte.codegentle.kotlin.spec.*
import love.forte.codegentle.kotlin.spec.internal.emitTo
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for [KotlinCodeWriter].
 */
class KotlinCodeWriterTests {

    @Test
    fun testSimpleClass() {
        val className = ClassName("com.example", "MyClass")
        val typeRef = className.kotlinRef()

        val propertySpec = KotlinPropertySpec.builder("myProperty", typeRef)
            .addKDoc("This is a property.")
            .build()

        val functionSpec = KotlinFunctionSpec.builder("myFunction", typeRef)
            .addKDoc("This is a function.")
            .addCode("return %V") {
                emitName("myProperty")
            }
            .build()

        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass")
            .addKDoc("This is a class.")
            .addProperty(propertySpec)
            .addFunction(functionSpec)
            .build()

        val result = buildString {
            val writer = KotlinCodeWriter.create(this)
            typeSpec.emitTo(writer)
        }

        val expected = """
            /**
             * This is a class.
             */
            class MyClass {
                /**
                 * This is a property.
                 */
                val myProperty: com.example.MyClass

                /**
                 * This is a function.
                 */
                fun myFunction(): com.example.MyClass {
                    return myProperty
                }
            }
        """.trimIndent()

        assertEquals(expected, result)
    }
}
