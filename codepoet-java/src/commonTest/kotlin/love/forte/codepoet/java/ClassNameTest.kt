package love.forte.codepoet.java

import love.forte.codepoet.java.naming.JavaClassName
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class ClassNameTest {

    @Test
    fun justTest() {
        println(JavaClassName::class)
        println(JavaClassName.Builtins.STRING)

        println(formatIsoControlCode(68))
        println('a'.characterLiteralWithoutSingleQuotes())
        println("ABC".literalWithDoubleQuotes(" "))

        println("ClassName: " + JavaClassName(packageName = "java.lang", simpleName = "String"))
    }

    @Test
    fun classNameToString() {
        assertEquals("java.lang.String", JavaClassName(packageName = "java.lang", simpleName = "String").toString())
        assertEquals("java.lang.String", JavaClassName.Builtins.STRING.toString())

        assertEquals(
            "import java.lang.String;",
            JavaCodeValue("import %V;") {
                literal(JavaClassName("java.lang", "String"))
            }.toString()
        )

        assertEquals(
            "import java.lang.String;",
            JavaCodeValue("import %V;") {
                literal(JavaClassName("java.lang", "String").withoutAnnotations())
            }.toString()
        )
    }

}
