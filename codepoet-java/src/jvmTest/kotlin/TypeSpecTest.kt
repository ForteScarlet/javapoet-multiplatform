import love.forte.codepoet.java.*
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class TypeSpecTest {

    @Test
    fun testPrintTypeSpec() {
        val simpleType = SimpleTypeSpec(TypeSpec.Kind.CLASS, "MyAnno") {
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
            }

            addInitializerBlock("int a = 1;\n")
        }

        println(simpleType)
    }

}
