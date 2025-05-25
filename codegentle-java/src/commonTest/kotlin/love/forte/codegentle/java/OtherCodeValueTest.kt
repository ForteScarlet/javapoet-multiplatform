package love.forte.codegentle.java

import love.forte.codegentle.common.code.*
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.writer.writeToJavaString
import kotlin.test.Test
import kotlin.test.assertEquals


class OtherCodeValueTest {

    @Test
    fun testCodeValueFormat() {
        with(
            CodeValue("%V%V = %V%V;") {
                type(ClassName("java.lang", "String"))
                literal(" name")
                literal("")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 6)

            assertEquals(CodePart.type(ClassName("java.lang", "String")), parts[0])
            assertEquals(CodePart.literal(" name"), parts[1])
            assertEquals(CodePart.simple(" = "), parts[2])
            assertEquals(CodePart.literal(""), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals(CodePart.simple(";"), parts[5])
            assertEquals("java.lang.String name = \"Hello World\";", writeToJavaString())
        }

        with(
            CodeValue("%V%V = %V%V") {
                type(ClassName("java.lang", "String"))
                literal(" name")
                literal("")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 5)

            assertEquals(CodePart.type(ClassName("java.lang", "String")), parts[0])
            assertEquals(CodePart.literal(" name"), parts[1])
            assertEquals(CodePart.simple(" = "), parts[2])
            assertEquals(CodePart.literal(""), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals("java.lang.String name = \"Hello World\"", writeToJavaString())
        }

        with(
            CodeValue("%V %V = %V;") {
                type(ClassName("java.lang", "String"))
                literal("name")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 6)

            assertEquals(CodePart.type(ClassName("java.lang", "String")), parts[0])
            assertEquals(CodePart.simple(" "), parts[1])
            assertEquals(CodePart.literal("name"), parts[2])
            assertEquals(CodePart.simple(" = "), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals(CodePart.simple(";"), parts[5])
            assertEquals("java.lang.String name = \"Hello World\";", writeToJavaString())
        }

        with(
            CodeValue("%V %V = %V") {
                type(ClassName("java.lang", "String"))
                literal("name")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 5)

            assertEquals(CodePart.type(ClassName("java.lang", "String")), parts[0])
            assertEquals(CodePart.simple(" "), parts[1])
            assertEquals(CodePart.literal("name"), parts[2])
            assertEquals(CodePart.simple(" = "), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals("java.lang.String name = \"Hello World\"", writeToJavaString())
        }

        with(
            CodeValue("String %V = %V;") {
                literal("name")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 5)

            assertEquals(CodePart.simple("String "), parts[0])
            assertEquals(CodePart.literal("name"), parts[1])
            assertEquals(CodePart.simple(" = "), parts[2])
            assertEquals(CodePart.string("Hello World"), parts[3])
            assertEquals(CodePart.simple(";"), parts[4])
            assertEquals("String name = \"Hello World\";", writeToJavaString())
        }

        with(
            CodeValue("String %V = %V") {
                literal("name")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 4)

            assertEquals(CodePart.simple("String "), parts[0])
            assertEquals(CodePart.literal("name"), parts[1])
            assertEquals(CodePart.simple(" = "), parts[2])
            assertEquals(CodePart.string("Hello World"), parts[3])
            assertEquals("String name = \"Hello World\"", writeToJavaString())
        }


    }


}
