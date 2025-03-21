package love.forte.codepoet.java

import love.forte.codepoet.java.CodePart.Companion.string
import kotlin.test.Test
import kotlin.test.assertEquals


class CodeValueTests {

    @Test
    fun testEmit() {
        val code = CodeValue {
            addStatement("String value = %V", string("Hello, World"))
        }

        assertEquals("String value = \"Hello, World\";\n", code.toString())
    }

}
