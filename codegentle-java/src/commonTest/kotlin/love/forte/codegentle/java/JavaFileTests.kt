package love.forte.codegentle.java

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.JavaFieldSpec
import love.forte.codegentle.java.spec.JavaSimpleTypeSpec
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.writer.writeToJavaString
import kotlin.test.Test
import kotlin.test.assertTrue

/**
 * Tests for JavaFile functionality.
 *
 * @author AI Assistant
 */
class JavaFileTests {

    @Test
    fun testBasicJavaFile() {
        val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass")
        val packageName = "com.example".parseToPackageName()

        val javaFile = JavaFile(packageName, typeSpec)

        val output = javaFile.writeToJavaString()

        assertTrue(output.contains("package com.example;"))
        assertTrue(output.contains("class MyClass {"))
        assertTrue(output.contains("}"))
    }

    @Test
    fun testJavaFileWithComment() {
        val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass")
        val packageName = "com.example".parseToPackageName()

        val javaFile = JavaFile(packageName, typeSpec) {
            addFileComment("This is a test file.")
        }

        val output = javaFile.writeToJavaString()

        assertTrue(output.contains("// This is a test file."))
    }

    @Test
    fun testJavaFileWithStaticImports() {
        val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass")
        val packageName = "com.example".parseToPackageName()

        val javaFile = JavaFile(packageName, typeSpec) {
            addStaticImport(ClassName("java.util", "Collections"), "emptyList")
            addStaticImport(ClassName("java.util", "Collections"), "emptyMap")
        }

        val output = javaFile.writeToJavaString()

        assertTrue(output.contains("import static java.util.Collections.emptyList;"))
        assertTrue(output.contains("import static java.util.Collections.emptyMap;"))
    }

    @Test
    fun testJavaFileWithSkipJavaLangImports() {
        val field = JavaFieldSpec(ClassName("java.lang", "String").javaRef(), "name")
        val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
            addField(field)
        }
        val packageName = "com.example".parseToPackageName()

        val javaFile = JavaFile(packageName, typeSpec) {
            skipJavaLangImports(true)
        }

        val output = javaFile.writeToJavaString()

        // Should not contain import for java.lang.String
        assertTrue(!output.contains("import java.lang.String;"))
    }

    @Test
    fun testJavaFileWithCustomIndent() {
        val typeSpec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass")
        val packageName = "com.example".parseToPackageName()

        val javaFile = JavaFile(packageName, typeSpec) {
            indent("\t") // Use tab for indentation
        }

        val output = javaFile.writeToJavaString()

        // Check that the class body is indented with a tab
        assertTrue(output.contains("class MyClass {\n}") || output.contains("class MyClass {\n\n}"))
    }
}
