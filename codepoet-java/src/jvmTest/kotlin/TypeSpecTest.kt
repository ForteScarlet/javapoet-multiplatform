import love.forte.codepoet.java.*
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class TypeSpecTest {

    @Test
    fun testPrintTypeSpec() {
        val simpleType = SimpleTypeSpec(TypeSpec.Kind.CLASS, "MyClass") {
            modifiers += Modifier.PUBLIC

            addMethod("methodPub") {
                modifiers { public() }
            }

            addField(TypeName.Builtins.INT, "valuePub") {
                modifiers {
                    public()
                    static()
                    final()
                }
                initializer("1")
            }

            addField(TypeName.Builtins.INT, "valuePri") {
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
