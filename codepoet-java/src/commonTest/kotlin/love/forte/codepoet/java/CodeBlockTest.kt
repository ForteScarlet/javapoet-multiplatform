package love.forte.codepoet.java

import love.forte.codepoet.java.CodePart.Companion.string
import kotlin.test.Test
import kotlin.test.assertEquals


/**
 *
 * @author ForteScarlet
 */
class CodeBlockTest {

    @Test
    fun testEmit() {
        val code = CodeBlock {
            addStatement("String value = %V", string("Hello, World"))
        }

        assertEquals("String value = \"Hello, World\";\n", code.toString())
    }

}
