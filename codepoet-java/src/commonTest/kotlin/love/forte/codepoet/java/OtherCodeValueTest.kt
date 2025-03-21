package love.forte.codepoet.java

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
            assertEquals(CodeSimplePart(" = "), parts[2])
            assertEquals(CodePart.literal(""), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals(CodeSimplePart(";"), parts[5])
            assertEquals("java.lang.String name = \"Hello World\";", toString())
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
            assertEquals(CodeSimplePart(" = "), parts[2])
            assertEquals(CodePart.literal(""), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals("java.lang.String name = \"Hello World\"", toString())
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
            assertEquals(CodeSimplePart(" "), parts[1])
            assertEquals(CodePart.literal("name"), parts[2])
            assertEquals(CodeSimplePart(" = "), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals(CodeSimplePart(";"), parts[5])
            assertEquals("java.lang.String name = \"Hello World\";", toString())
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
            assertEquals(CodeSimplePart(" "), parts[1])
            assertEquals(CodePart.literal("name"), parts[2])
            assertEquals(CodeSimplePart(" = "), parts[3])
            assertEquals(CodePart.string("Hello World"), parts[4])
            assertEquals("java.lang.String name = \"Hello World\"", toString())
        }

        with(
            CodeValue("String %V = %V;") {
                literal("name")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 5)

            assertEquals(CodeSimplePart("String "), parts[0])
            assertEquals(CodePart.literal("name"), parts[1])
            assertEquals(CodeSimplePart(" = "), parts[2])
            assertEquals(CodePart.string("Hello World"), parts[3])
            assertEquals(CodeSimplePart(";"), parts[4])
            assertEquals("String name = \"Hello World\";", toString())
        }

        with(
            CodeValue("String %V = %V") {
                literal("name")
                string("Hello World")
            }
        ) {
            assertEquals(parts.size, 4)

            assertEquals(CodeSimplePart("String "), parts[0])
            assertEquals(CodePart.literal("name"), parts[1])
            assertEquals(CodeSimplePart(" = "), parts[2])
            assertEquals(CodePart.string("Hello World"), parts[3])
            assertEquals("String name = \"Hello World\"", toString())
        }


    }


}
