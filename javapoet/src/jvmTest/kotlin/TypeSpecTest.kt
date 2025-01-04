import com.squareup.javapoet.CodeBlock
import com.squareup.javapoet.FieldSpec
import com.squareup.javapoet.MethodSpec
import com.squareup.javapoet.TypeName
import com.squareup.javapoet.TypeSpec
import javax.lang.model.element.Modifier
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class TypeSpecTest {

    @Test
    fun testAnonymousTypeArguments() {
        println(
            TypeSpec
                .classBuilder("MyAnno")
                .addMethod(
                    MethodSpec.methodBuilder("methodPub")
                    .addModifiers(Modifier.PUBLIC).build())
                .addField(
                    FieldSpec.builder(
                        TypeName.INT, "valuePub",
                    Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL
                ).initializer("1").build())
                .addField(
                    FieldSpec.builder(
                        TypeName.INT, "valuePri",
                    Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
                ).initializer("1").build())
                .addInitializerBlock(CodeBlock.of("int a = 1;\n"))
                .build()
        )


    }

}
