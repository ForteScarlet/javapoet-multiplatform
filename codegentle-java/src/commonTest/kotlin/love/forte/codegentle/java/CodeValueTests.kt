package love.forte.codegentle.java

import love.forte.codegentle.common.code.CodePart.Companion.string
import kotlin.test.Test
import kotlin.test.assertEquals


class CodeValueTests {

    @Test
    fun testEmit() {
        val code = JavaCodeValue {
            addStatement("String value = %V", string("Hello, World"))
        }

        assertEquals("String value = \"Hello, World\";\n", code.toString())
    }

}
