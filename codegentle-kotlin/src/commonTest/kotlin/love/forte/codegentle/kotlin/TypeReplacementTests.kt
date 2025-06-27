package love.forte.codegentle.kotlin

import love.forte.codegentle.common.code.CodePart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.kotlin.ref.kotlinRef
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinSimpleTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinValueParameterSpec
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Tests for automatic imports with `%V` type replacement in various contexts.
 */
class TypeReplacementTests {

    @Test
    fun testPropertyTypeReplacement() {
        // Create a property with a type from another package
        val listType = ClassName("kotlin.collections", "List").kotlinRef()
        val property = KotlinPropertySpec.builder("items", listType).build()

        // Create a class with the property
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass")
            .addProperty(property)
            .build()

        // Create a file with the class
        val kotlinFile = KotlinFile("com.example", typeSpec)

        // Get the output
        val output = kotlinFile.writeToKotlinString()

        // Verify the complete output
        assertEquals(
            """
                package com.example

                import kotlin.collections.List

                class MyClass {
                    val items: List
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testFunctionReturnTypeReplacement() {
        // Create a function with a return type from another package
        val mapType = ClassName("kotlin.collections", "Map").kotlinRef()
        val function = KotlinFunctionSpec.builder("getMap", mapType).build()

        // Create a class with the function
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass")
            .addFunction(function)
            .build()

        // Create a file with the class
        val kotlinFile = KotlinFile("com.example", typeSpec)

        // Get the output
        val output = kotlinFile.writeToKotlinString()

        // Verify the complete output
        assertEquals(
            """
                package com.example

                import kotlin.collections.Map

                class MyClass {
                    fun getMap(): Map
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testParameterTypeReplacement() {
        // Create a parameter with a type from another package
        val setType = ClassName("kotlin.collections", "Set").kotlinRef()
        val parameter = KotlinValueParameterSpec.builder("items", setType).build()

        // Create a function with the parameter
        val function = KotlinFunctionSpec.builder("processItems", ClassName("kotlin", "Unit").kotlinRef())
            .addParameter(parameter)
            .build()

        // Create a class with the function
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass")
            .addFunction(function)
            .build()

        // Create a file with the class
        val kotlinFile = KotlinFile("com.example", typeSpec)

        // Get the output
        val output = kotlinFile.writeToKotlinString()

        // Verify the complete output
        assertEquals(
            """
                package com.example

                import kotlin.collections.Set

                class MyClass {
                    fun processItems(items: Set): Unit
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testCodeValueTypeReplacement() {
        // Create a function with code that uses a type from another package
        val mutableListType = ClassName("kotlin.collections", "MutableList").kotlinRef()
        val function = KotlinFunctionSpec.builder("createMutableList", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("val list = %V<String>()", CodePart.type(mutableListType))
            .build()

        // Create a class with the function
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass")
            .addFunction(function)
            .build()

        // Create a file with the class
        val kotlinFile = KotlinFile("com.example", typeSpec)

        // Get the output
        val output = kotlinFile.writeToKotlinString()

        // Verify the complete output
        assertEquals(
            """
                package com.example

                import kotlin.collections.MutableList

                class MyClass {
                    fun createMutableList(): Unit {
                        val list = MutableList<String>()
                    }
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testMultipleTypesWithImports() {
        // Create a class that uses types from another package
        val class1 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class1")
            .addProperty(KotlinPropertySpec.builder("list", ClassName("kotlin.collections", "List").kotlinRef()).build())
            .build()

        // Create another class that uses different types
        val class2 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class2")
            .addProperty(KotlinPropertySpec.builder("map", ClassName("kotlin.collections", "Map").kotlinRef()).build())
            .build()

        // Create a file with both classes
        val kotlinFile = KotlinFile("com.example", class1, class2)

        // Get the output
        val output = kotlinFile.writeToKotlinString()

        // Verify the complete output
        assertEquals(
            """
                package com.example

                import kotlin.collections.List
                import kotlin.collections.Map

                class Class1 {
                    val list: List
                }

                class Class2 {
                    val map: Map
                }
            """.trimIndent(),
            output
        )
    }
}
