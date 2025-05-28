package love.forte.codegentle.java.writer

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.string
import love.forte.codegentle.common.code.type
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.common.ref.annotationRef
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.strategy.JavaWriteStrategy
import love.forte.codegentle.java.strategy.ToStringJavaWriteStrategy
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

/**
 * Tests for JavaCodeWriter functionality.
 *
 * @author AI Assistant
 */
class JavaCodeWriterTests {

    @Test
    fun testEmitString() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        writer.emit("Hello, World!")

        assertEquals("Hello, World!", out.toString())
    }

    @Test
    fun testEmitWithIndentation() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        writer.emitNewLine("public class Test {")
        writer.indent(1)
        writer.emitAndIndent("void method() {")
        writer.emitNewLine()
        writer.indent(1)
        writer.emitAndIndent("System.out.println(\"Hello\");")
        writer.emitNewLine()
        writer.unindent(1)
        writer.emitAndIndent("}")
        writer.emitNewLine()
        writer.unindent(1)
        writer.emitAndIndent("}")
        writer.emitNewLine()

        val expected = """
            |public class Test {
            |    void method() {
            |        System.out.println("Hello");
            |    }
            |}
            |
        """.trimMargin()

        assertEquals(expected, out.toString())
    }

    @Test
    fun testEmitCodeValue() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val codeValue = CodeValue("%V.out.println(%V)") {
            type(ClassName("java.lang", "System"))
            string("Hello, World!")
        }

        writer.emit(codeValue)

        assertEquals("System.out.println(\"Hello, World!\")", out.toString())
    }

    @Test
    fun testEmitTypeName() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val className = ClassName("java.util", "List")

        writer.emit(className)

        assertEquals("java.util.List", out.toString())
    }

    @Test
    fun testEmitAnnotationRef() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val annotationRef = ClassName("java.lang", "Override").annotationRef()

        writer.emit(annotationRef)

        assertEquals("@Override", out.toString())
    }

    @Test
    fun testEmitAnnotationRefWithJavaLang() {
        val out = StringBuilder()

        val writer = JavaCodeWriter.create(out, toStringStrategyWithJavaLang)
        val annotationRef = ClassName("java.lang", "Override").annotationRef()

        writer.emit(annotationRef)

        assertEquals("@java.lang.Override", out.toString())
    }

    @Test
    fun testEmitTypeRef() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val typeRef = ClassName("java.lang", "String").javaRef()

        writer.emit(typeRef)

        assertEquals("String", out.toString())
    }

    private val toStringStrategyWithJavaLang: JavaWriteStrategy
        get() {
            val strategy = object : JavaWriteStrategy by ToStringJavaWriteStrategy {
                override fun omitJavaLangPackage(): Boolean = false
            }
            return strategy
        }

    @Test
    fun testEmitTypeRefWithJavaLang() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, toStringStrategyWithJavaLang)
        val typeRef = ClassName("java.lang", "String").javaRef()

        writer.emit(typeRef)

        assertEquals("java.lang.String", out.toString())
    }

    @Test
    fun testEmitModifiers() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val modifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC, JavaModifier.FINAL)

        writer.emitModifiers(modifiers)

        assertEquals("public static final ", out.toString())
    }

    @Test
    fun testEmitComment() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val comment = CodeValue("This is a comment")

        writer.emitComment(comment)

        val result = out.toString()
        assertTrue(result.contains("// This is a comment"))
    }

    @Test
    fun testEmitDoc() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val doc = CodeValue("This is a javadoc comment")

        writer.emitDoc(doc)

        val result = out.toString()
        assertTrue(result.contains("/**"))
        assertTrue(result.contains(" * This is a javadoc comment"))
        assertTrue(result.contains(" */"))
    }

    @Test
    fun testEmitWithPackage() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, toStringStrategyWithJavaLang)
        val packageName = "com.example".parseToPackageName()

        writer.pushPackage(packageName)
        writer.emit(ClassName("java.lang", "String"))
        writer.popPackage()

        assertEquals("java.lang.String", out.toString())
    }

    @Test
    fun testEmitWithDiffPackage() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val packageName = "love.forte".parseToPackageName()

        writer.pushPackage(packageName)
        writer.emit(ClassName("love.forte", "Example"))
        writer.popPackage()

        assertEquals("Example", out.toString())
    }

    @Test
    fun testEmitWithSamePackage() {
        val out = StringBuilder()
        val writer = JavaCodeWriter.create(out, ToStringJavaWriteStrategy)
        val packageName = "love.example".parseToPackageName()

        writer.pushPackage(packageName)
        writer.emit(ClassName("love.forte", "Example"))
        writer.popPackage()

        assertEquals("love.forte.Example", out.toString())
    }

    @Test
    fun testEmitLiteral() {
        val out1 = StringBuilder()
        val writer = JavaCodeWriter.create(out1, ToStringJavaWriteStrategy)

        writer.emitLiteral(42)
        assertEquals("42", out1.toString())

        val out2 = StringBuilder()
        val writer2 = JavaCodeWriter.create(out2, ToStringJavaWriteStrategy)
        writer2.emitLiteral("Hello")
        assertEquals("Hello", out2.toString())

        val out3 = StringBuilder()
        val writer3 = JavaCodeWriter.create(out3, ToStringJavaWriteStrategy)
        writer3.emitLiteral(true)
        assertEquals("true", out3.toString())
    }
}
