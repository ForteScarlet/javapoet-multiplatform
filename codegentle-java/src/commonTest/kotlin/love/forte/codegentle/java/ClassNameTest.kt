package love.forte.codegentle.java

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.literal
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.writer.writeToJavaString
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class ClassNameTest {

    @Test
    fun classNameToString() {
        assertEquals(
            "java.lang.String",
            ClassName(packageName = "java.lang", simpleName = "String").writeToJavaString()
        )
        assertEquals("java.lang.String", JavaClassNames.STRING.writeToJavaString())

        assertEquals(
            "import java.lang.String;",
            CodeValue("import %V;") {
                literal(ClassName("java.lang", "String"))
            }.writeToJavaString()
        )

        assertEquals(
            "import java.lang.String;",
            CodeValue("import %V;") {
                literal(ClassName("java.lang", "String"))
            }.writeToJavaString()
        )
    }

}
