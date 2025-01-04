package love.forte.javapoet

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
            addStatement("String value = \$S", "Hello, World")
        }

        assertEquals("String value = \"Hello, World\";\n", code.toString())
    }

}
