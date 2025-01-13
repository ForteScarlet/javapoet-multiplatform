package love.forte.codepoet.java

import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
class ClassNameTest {

    @Test
    fun classNameToString() {
        assertEquals("java.lang.String", ClassName("java.lang", "String").toString())
        assertEquals("java.lang.String", ClassName.Builtins.STRING.toString())

        assertEquals(
            "import java.lang.String;",
            CodeBlock("import %V;") {
                literal(ClassName("java.lang", "String"))
            }.toString()
        )

        assertEquals(
            "import java.lang.String;",
            CodeBlock("import %V;") {
                literal(ClassName("java.lang", "String").withoutAnnotations())
            }.toString()
        )
    }

}
