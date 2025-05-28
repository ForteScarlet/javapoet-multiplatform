package love.forte.codegentle.common.naming

import kotlin.test.Test
import kotlin.test.assertEquals

/**
 *
 * @author ForteScarlet
 */
class ClassNameTest {

    @Test
    fun classNameToString() {
        assertEquals(
            "java.lang.String",
            ClassName(packageName = "java.lang", simpleName = "String").canonicalName
        )

        assertEquals(
            PackageName(listOf("java", "lang")),
            ClassName(packageName = "java.lang", simpleName = "String").packageName
        )
    }
}
