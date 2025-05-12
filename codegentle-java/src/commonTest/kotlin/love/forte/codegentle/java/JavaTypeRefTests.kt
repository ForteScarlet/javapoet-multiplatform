package love.forte.codegentle.java

import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.ref.javaAnnotationRef
import love.forte.codegentle.java.ref.javaRef
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class JavaTypeRefTests {
    @Test
    fun testJavaClassNameRefString() {
        val className = JavaClassName("com.example", "Example")
        val ref = className.javaRef {
            addAnnotationRef(JavaClassName("com.example.anno", "Anno").javaAnnotationRef())
        }

        assertEquals(
            "@com.example.anno.Anno com.example.Example",
            ref.toString()
        )
    }
}
