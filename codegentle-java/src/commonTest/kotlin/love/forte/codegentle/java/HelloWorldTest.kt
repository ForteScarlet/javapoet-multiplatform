package love.forte.codegentle.java

import love.forte.codegentle.java.naming.JavaArrayTypeName
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.strategy.ToStringJavaWriteStrategy
import kotlin.test.Test
import kotlin.test.assertEquals


class HelloWorldTest {

    @Test
    fun simpleHelloWorldClass() {
        val method = JavaMethodSpec("main") {
            addModifiers(JavaModifier.PUBLIC, JavaModifier.STATIC)
            returns(JavaTypeName.Builtins.VOID)
            addParameter(JavaParameterSpec(JavaArrayTypeName(JavaClassName.Builtins.STRING).javaRef(), "args"))
            addStatement("%V.out.println(%V)") {
                type(JavaClassName("java.lang", "System"))
                string("Hello, World!")
            }
        }

        val type = JavaSimpleTypeSpec(JavaTypeSpec.Kind.CLASS, "HelloWorld") {
            addModifiers(JavaModifier.PUBLIC, JavaModifier.FINAL)
            addMethod(method)
        }

        val file = JavaFile("com.example.helloworld", type)

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
