import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.modifiers
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.spec.JavaSimpleTypeSpec
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.spec.addField
import love.forte.codegentle.java.spec.addMethod
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class TypeSpecTest {

    @Test
    fun testPrintTypeSpec() {
        val simpleType = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "MyClass") {
            modifiers += JavaModifier.PUBLIC

            addMethod("methodPub") {
                modifiers { public() }
            }

            addField(JavaTypeName.Builtins.INT, "valuePub") {
                modifiers {
                    public()
                    static()
                    final()
                }
                initializer("1")
            }

            addField(JavaTypeName.Builtins.INT, "valuePri") {
                modifiers {
                    private()
                    static()
                    final()
                }
                initializer("2")
            }
        }

        println(simpleType)

        assertEquals(
            """
                public class MyClass {
                    public static final int valuePub = 1;

                    private static final int valuePri = 2;

                    public void methodPub() {
                    }
                }
                
            """.trimIndent(),
            simpleType.toString()
        )

    }

}
