package love.forte.codegentle.java

import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.ref.annotationRef
import love.forte.codegentle.java.ref.ref
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
        val ref = className.ref {
            addAnnotationRef(JavaClassName("com.example.anno", "Anno").annotationRef())
        }

        assertEquals(
            "@com.example.anno.Anno com.example.Example",
            ref.toString()
        )
    }
}
