package love.forte.codepoet.java

import kotlin.test.Test

/**
 *
 * @author ForteScarlet
 */
class ClassNameTest {

    @Test
    fun justTest() {
        println(ClassName::class)
        println(ClassName.Builtins.STRING)

        // println(formatIsoControlCode(68))
        // println('a'.characterLiteralWithoutSingleQuotes())
        // println("ABC".literalWithDoubleQuotes(" "))

        println(ClassName(packageName = "java.lang", simpleName = "String"))
    }

    // @Test
    // fun classNameToString() {
    //     assertEquals("java.lang.String", ClassName("java.lang", "String").toString())
    //     assertEquals("java.lang.String", ClassName.Builtins.STRING.toString())
    //
    //     assertEquals(
    //         "import java.lang.String;",
    //         CodeBlock("import %V;") {
    //             literal(ClassName("java.lang", "String"))
    //         }.toString()
    //     )
    //
    //     assertEquals(
    //         "import java.lang.String;",
    //         CodeBlock("import %V;") {
    //             literal(ClassName("java.lang", "String").withoutAnnotations())
    //         }.toString()
    //     )
    // }

}