// package love.forte.codepoet.java
//
// import kotlin.test.Test
// import kotlin.test.assertEquals
//
//
// /**
//  *
//  * @author ForteScarlet
//  */
// class HelloWorldTest {
//
//     @Test
//     fun simpleHelloWorldClass() {
//         val method = MethodSpec("main") {
//             addModifiers(Modifier.PUBLIC, Modifier.STATIC)
//             returns(TypeName.Builtins.VOID)
//             addParameter(ParameterSpec(ArrayTypeName(ClassName.Builtins.STRING), "args"))
//             addStatement("%V.out.println(%V)") {
//                 type(ClassName("java.lang", "System"))
//                 string("Hello, World!")
//             }
//         }
//
//         val type = SimpleTypeSpec(TypeSpec.Kind.CLASS, "HelloWorld") {
//             addModifiers(Modifier.PUBLIC, Modifier.FINAL)
//             addMethod(method)
//         }
//
//         val file = JavaFile("com.example.helloworld", type)
//
//         val str = buildString { file.writeTo(this) }
//
//         println(str)
//
//         assertEquals(
//             """
//                 |package com.example.helloworld;
//                 |
//                 |public final class HelloWorld {
//                 |    public static void main(String[] args) {
//                 |        System.out.println("Hello, World!");
//                 |    }
//                 |}
//                 |
//             """.trimMargin(),
//             str
//         )
//
//     }
//
// }
