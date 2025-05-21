package love.forte.codegentle.java

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.naming.java
import love.forte.codegentle.java.ref.annotationRef
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
        val className = ClassName("com.example", "Example")
        val ref = className.java().javaRef {
            addAnnotationRef(ClassName("com.example.anno", "Anno").annotationRef())
        }

        assertEquals(
            "@com.example.anno.Anno com.example.Example",
            ref.toString()
        )
    }
}
