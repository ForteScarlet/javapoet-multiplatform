package love.forte.codegentle.java

import love.forte.codegentle.common.code.CodePart.Companion.string
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.java.writer.writeToJavaString
import kotlin.test.Test
import kotlin.test.assertEquals


class CodeValueTests {

    @Test
    fun testEmit() {
        val code = CodeValue {
            addStatement("String value = %V", string("Hello, World"))
        }

        assertEquals(
            "String value = \"Hello, World\";\n",
            code.writeToJavaString()
        )
    }

}
