import love.forte.codepoet.java.JavaModifier
import love.forte.codepoet.java.modifiers
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.JavaSimpleTypeSpec
import love.forte.codepoet.java.spec.JavaTypeSpec
import love.forte.codepoet.java.spec.addField
import love.forte.codepoet.java.spec.addMethod
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
