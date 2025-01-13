import com.squareup.javapoet.*
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class TypeSpecTest {

    @Test
    fun testAnonymousTypeArguments() {

        SourceVersion.RELEASE_0

        object : javax.annotation.processing.AbstractProcessor() {
            override fun process(annotations: MutableSet<out TypeElement>?, roundEnv: RoundEnvironment?): Boolean {

                TODO("Not yet implemented")
            }


        }

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
