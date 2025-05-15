package love.forte.codegentle.java

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.naming.JavaClassNames
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class ClassNameTest {

    @Test
    fun justTest() {
        println(ClassName::class)
        println(JavaClassNames.STRING)

        println(formatIsoControlCode(68))
        println('a'.characterLiteralWithoutSingleQuotes())
        println("ABC".literalWithDoubleQuotes(" "))

        println("ClassName: " + ClassName(packageName = "java.lang", simpleName = "String"))
    }

    @Test
    fun classNameToString() {
        assertEquals("java.lang.String", ClassName(packageName = "java.lang", simpleName = "String").toString())
        assertEquals("java.lang.String", JavaClassNames.STRING.toString())

        assertEquals(
            "import java.lang.String;",
            JavaCodeValue("import %V;") {
                literal(ClassName("java.lang", "String"))
            }.toString()
        )

        assertEquals(
            "import java.lang.String;",
            JavaCodeValue("import %V;") {
                literal(ClassName("java.lang", "String"))
            }.toString()
        )
    }

}
