package love.forte.codegentle.kotlin

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.kotlin.ref.kotlinRef
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinSimpleTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for KotlinFile functionality.
 *
 * @author AI Assistant
 */
class KotlinFileTests {

    @Test
    fun testMultipleTypesInKotlinFile() {
        val class1 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class1").build()
        val class2 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class2").build()
        val interface1 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.INTERFACE, "Interface1").build()

        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName) {
            addType(class1)
            addType(class2)
            addType(interface1)
        }

        val output = kotlinFile.writeToKotlinString()

        assertTrue(output.contains("class Class1"))
        assertTrue(output.contains("class Class2"))
        assertTrue(output.contains("interface Interface1"))

        // Check that there are newlines between the types
        val lines = output.lines()
        var class1Line = -1
        var class2Line = -1
        var interface1Line = -1

        for ((index, line) in lines.withIndex()) {
            if (line.contains("class Class1")) {
                class1Line = index
            } else if (line.contains("class Class2")) {
                class2Line = index
            } else if (line.contains("interface Interface1")) {
                interface1Line = index
            }
        }

        assertTrue(class1Line >= 0, "Class1 not found in output")
        assertTrue(class2Line >= 0, "Class2 not found in output")
        assertTrue(interface1Line >= 0, "Interface1 not found in output")

        // Verify that there are at least 2 lines between each type (for the closing brace and a blank line)
        assertTrue(class2Line - class1Line >= 3, "Not enough separation between Class1 and Class2")
        assertTrue(interface1Line - class2Line >= 3, "Not enough separation between Class2 and Interface1")
    }

    @Test
    fun testMultipleTypesWithVarargs() {
        val class1 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class1").build()
        val class2 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class2").build()

        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName, class1, class2)

        val output = kotlinFile.writeToKotlinString()

        assertTrue(output.contains("class Class1"))
        assertTrue(output.contains("class Class2"))
    }

    @Test
    fun testMultipleTypesWithIterable() {
        val class1 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class1").build()
        val class2 = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Class2").build()

        val packageName = "com.example".parseToPackageName()
        val types = listOf(class1, class2)

        val kotlinFile = KotlinFile(packageName, types)

        val output = kotlinFile.writeToKotlinString()

        assertTrue(output.contains("class Class1"))
        assertTrue(output.contains("class Class2"))
    }

    @Test
    fun testBasicKotlinFile() {
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass").build()
        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName, typeSpec)

        val output = kotlinFile.writeToKotlinString()

        assertTrue(output.contains("package com.example"))
        assertTrue(output.contains("class MyClass"))
    }

    @Test
    fun testKotlinFileWithComment() {
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass").build()
        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName, typeSpec) {
            addFileComment("This is a test file.")
        }

        val output = kotlinFile.writeToKotlinString()

        assertTrue(output.contains("// This is a test file."))

        assertEquals(
            """
                // This is a test file.
                package com.example

                class MyClass {
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testKotlinFileWithStaticImports() {
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass").build()
        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName, typeSpec) {
            addStaticImport(ClassName("kotlin.collections", "Collections"), "emptyList")
            addStaticImport(ClassName("kotlin.collections", "Collections"), "emptyMap")
        }

        val output = kotlinFile.writeToKotlinString()

        assertTrue(output.contains("import kotlin.collections.Collections.emptyList"))
        assertTrue(output.contains("import kotlin.collections.Collections.emptyMap"))

        assertEquals(
            """
                package com.example

                import kotlin.collections.Collections.emptyList
                import kotlin.collections.Collections.emptyMap

                class MyClass {
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testKotlinFileWithSkipKotlinImports() {
        val property = KotlinPropertySpec.builder("name", ClassName("kotlin", "String").kotlinRef()).build()
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass")
            .addProperty(property)
            .build()
        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName, typeSpec) {
            skipKotlinImports(true)
        }

        val output = kotlinFile.writeToKotlinString()

        // Should not contain import for kotlin.String
        assertTrue(!output.contains("import kotlin.String"))

        assertEquals(
            """
                package com.example

                class MyClass {
                    val name: String
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testKotlinFileWithCustomIndent() {
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "MyClass").build()
        val packageName = "com.example".parseToPackageName()

        val kotlinFile = KotlinFile(packageName, typeSpec) {
            indent("\t") // Use tab for indentation
        }

        val output = kotlinFile.writeToKotlinString()

        // Check that the class body is indented with a tab
        assertTrue(output.contains("class MyClass") || output.contains("class MyClass {"))

        assertEquals(
            """
                package com.example

                class MyClass {
                }
            """.trimIndent(),
            output
        )
    }
}
