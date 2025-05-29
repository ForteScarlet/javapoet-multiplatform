package love.forte.codegentle.java

import love.forte.codegentle.common.code.emitString
import love.forte.codegentle.java.spec.JavaSimpleTypeSpec
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.spec.addMethod
import love.forte.codegentle.java.spec.addStatement
import love.forte.codegentle.java.writer.writeToJavaString
import kotlin.test.Test
import kotlin.test.assertEquals

class HelloWorldClassTests {

    @Test
    fun testHelloWorldClass() {
        val spec = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
            modifiers.public()
            modifiers.final()

            addMethod("main") {
                modifiers {
                    public()
                    static()
                }

                addStatement("System.out.println(%V)") {
                    emitString("Hello, World!")
                }
            }
        }

        assertEquals(
            """
                public final class HelloWorld {
                    public static void main() {
                        System.out.println("Hello, World!");
                    }
                }
                
            """.trimIndent(),
            spec.writeToJavaString()
        )
    }

}
