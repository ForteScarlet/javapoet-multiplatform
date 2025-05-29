package love.forte.codegentle.java

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.emitLiteral
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.strategy.ToStringJavaWriteStrategy
import love.forte.codegentle.java.strategy.WrapperJavaWriteStrategy
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
            "String",
            ClassName(packageName = "java.lang", simpleName = "String").writeToJavaString()
        )
        assertEquals("String", JavaClassNames.STRING.writeToJavaString())

        val toStringWithJavaLang =
            object : WrapperJavaWriteStrategy(ToStringJavaWriteStrategy) {
                override fun omitJavaLangPackage(): Boolean = false
            }

        assertEquals(
            "java.lang.String",
            ClassName(packageName = "java.lang", simpleName = "String")
                .writeToJavaString(toStringWithJavaLang)
        )
        assertEquals(
            "java.lang.String",
            JavaClassNames.STRING.writeToJavaString(toStringWithJavaLang)
        )

        assertEquals(
            "import java.lang.String;",
            CodeValue("import %V;") {
                emitLiteral(ClassName("java.lang", "String"))
            }.writeToJavaString()
        )

        assertEquals(
            "import java.lang.String;",
            CodeValue("import %V;") {
                emitLiteral(ClassName("java.lang", "String"))
            }.writeToJavaString()
        )
    }

}
