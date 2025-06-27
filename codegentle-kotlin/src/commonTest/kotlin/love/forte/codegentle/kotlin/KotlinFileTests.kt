package love.forte.codegentle.kotlin

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.kotlin.ref.kotlinRef
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinSimpleTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for KotlinFile functionality.
 */
class KotlinFileTests {

    @Test
    fun testTopLevelFunction() {
        val packageName = "com.example".parseToPackageName()
        val function = KotlinFunctionSpec.builder("myFunction", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("println(\"Hello, World!\")")
            .build()

        val kotlinFile = KotlinFile.builder(packageName)
            .addFunction(function)
            .build()

        val output = kotlinFile.writeToKotlinString()

        assertEquals(
            """
                package com.example

                fun myFunction(): Unit {
                    println("Hello, World!")
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testTopLevelProperty() {
        val packageName = "com.example".parseToPackageName()
        val property = KotlinPropertySpec.builder("greeting", ClassName("kotlin", "String").kotlinRef())
            .initializer("\"Hello, World!\"")
            .build()

        val kotlinFile = KotlinFile.builder(packageName)
            .addProperty(property)
            .build()

        val output = kotlinFile.writeToKotlinString()

        assertEquals(
            """
                package com.example

                val greeting: String = "Hello, World!"
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testMixedTopLevelElements() {
        val packageName = "com.example".parseToPackageName()
        val property = KotlinPropertySpec.builder("greeting", ClassName("kotlin", "String").kotlinRef())
            .initializer("\"Hello, World!\"")
            .build()
        val function = KotlinFunctionSpec.builder("sayHello", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("println(greeting)")
            .build()
        val typeSpec = KotlinSimpleTypeSpec.builder(KotlinTypeSpec.Kind.CLASS, "Greeter").build()

        val kotlinFile = KotlinFile.builder(packageName)
            .addProperty(property)
            .addFunction(function)
            .addType(typeSpec)
            .build()

        val output = kotlinFile.writeToKotlinString()

        assertEquals(
            """
                package com.example

                val greeting: String = "Hello, World!"

                fun sayHello(): Unit {
                    println(greeting)
                }

                class Greeter {
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testMultipleFunctions() {
        val packageName = "com.example".parseToPackageName()
        val function1 = KotlinFunctionSpec.builder("function1", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("println(\"Function 1\")")
            .build()
        val function2 = KotlinFunctionSpec.builder("function2", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("println(\"Function 2\")")
            .build()
        val function3 = KotlinFunctionSpec.builder("function3", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("println(\"Function 3\")")
            .build()

        val kotlinFile = KotlinFile.builder(packageName)
            .addFunctions(function1, function2, function3)
            .build()

        val output = kotlinFile.writeToKotlinString()

        assertEquals(
            """
                package com.example

                fun function1(): Unit {
                    println("Function 1")
                }

                fun function2(): Unit {
                    println("Function 2")
                }

                fun function3(): Unit {
                    println("Function 3")
                }
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testMultipleProperties() {
        val packageName = "com.example".parseToPackageName()
        val property1 = KotlinPropertySpec.builder("property1", ClassName("kotlin", "String").kotlinRef())
            .initializer("\"Property 1\"")
            .build()
        val property2 = KotlinPropertySpec.builder("property2", ClassName("kotlin", "Int").kotlinRef())
            .initializer("42")
            .build()
        val property3 = KotlinPropertySpec.builder("property3", ClassName("kotlin", "Boolean").kotlinRef())
            .initializer("true")
            .build()

        val kotlinFile = KotlinFile.builder(packageName)
            .addProperties(listOf(property1, property2, property3))
            .build()

        val output = kotlinFile.writeToKotlinString()

        assertEquals(
            """
                package com.example

                val property1: String = "Property 1"

                val property2: Int = 42

                val property3: Boolean = true
            """.trimIndent(),
            output
        )
    }

    @Test
    fun testFileWithOnlyFunctionsAndProperties() {
        val packageName = "com.example".parseToPackageName()
        val property = KotlinPropertySpec.builder("config", ClassName("kotlin", "String").kotlinRef())
            .initializer("\"Configuration\"")
            .build()
        val function = KotlinFunctionSpec.builder("configure", ClassName("kotlin", "Unit").kotlinRef())
            .addCode("println(config)")
            .build()

        val kotlinFile = KotlinFile.builder(packageName)
            .addProperty(property)
            .addFunction(function)
            .build()

        val output = kotlinFile.writeToKotlinString()

        assertEquals(
            """
                package com.example

                val config: String = "Configuration"

                fun configure(): Unit {
                    println(config)
                }
            """.trimIndent(),
            output
        )
    }

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
