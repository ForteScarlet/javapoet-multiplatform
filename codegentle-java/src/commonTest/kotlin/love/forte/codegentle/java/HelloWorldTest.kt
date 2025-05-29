package love.forte.codegentle.java

import love.forte.codegentle.common.code.emitString
import love.forte.codegentle.common.code.emitType
import love.forte.codegentle.common.naming.ArrayTypeName
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.naming.JavaPrimitiveTypeNames
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.strategy.ToStringJavaWriteStrategy
import kotlin.test.Test
import kotlin.test.assertEquals


class HelloWorldTest {

    @Test
    fun testHelloWorldClassWithImport() {
        val method = JavaMethodSpec("main") {
            addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC)
            returns(JavaPrimitiveTypeNames.VOID)
            addParameter(JavaParameterSpec(ArrayTypeName(JavaClassNames.STRING.javaRef()).javaRef(), "args"))
            addStatement("%V.out.println(%V)") {
                emitType(ClassName("java.lang".parseToPackageName(), "System"))
                emitString("Hello, World!")
            }
        }

        val forteType = ClassName(packageName = "love.forte", simpleName = "Forte")
        val field = JavaFieldSpec(
            forteType.javaRef(),
            "forte"
        ) {
            modifiers {
                private()
            }
            // Forte.getInstance()
            initializer("%V.getInstance()") {
                emitType(forteType)
            }
        }

        val type = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
            addModifiers(JavaModifier.PUBLIC, JavaModifier.FINAL)
            addMethod(method)
            addField(field)
        }

        val file = JavaFile("com.example.helloworld".parseToPackageName(), type)

        val str = buildString { file.writeTo(this, ToStringJavaWriteStrategy) }

        assertEquals(
            """
                |package com.example.helloworld;
                |
                |import love.forte.Forte;
                |
                |public final class HelloWorld {
                |    private Forte forte = Forte.getInstance();
                |
                |    public static void main(String[] args) {
                |        System.out.println("Hello, World!");
                |    }
                |}
                |
            """.trimMargin(),
            str
        )
    }

    @Test
    fun testSimpleHelloWorldClass() {
        val method = JavaMethodSpec("main") {
            addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC)
            returns(JavaPrimitiveTypeNames.VOID)
            addParameter(JavaParameterSpec(ArrayTypeName(JavaClassNames.STRING.javaRef()).javaRef(), "args"))
            addStatement("%V.out.println(%V)") {
                emitType(ClassName("java.lang".parseToPackageName(), "System"))
                emitString("Hello, World!")
            }
        }

        val type = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
            addModifiers(JavaModifier.PUBLIC, JavaModifier.FINAL)
            addMethod(method)
        }

        val file = JavaFile("com.example.helloworld".parseToPackageName(), type)

        val str = buildString { file.writeTo(this, ToStringJavaWriteStrategy) }

        println(str)

        assertEquals(
            """
                |package com.example.helloworld;
                |
                |public final class HelloWorld {
                |    public static void main(String[] args) {
                |        System.out.println("Hello, World!");
                |    }
                |}
                |
            """.trimMargin(),
            str
        )
    }

}
