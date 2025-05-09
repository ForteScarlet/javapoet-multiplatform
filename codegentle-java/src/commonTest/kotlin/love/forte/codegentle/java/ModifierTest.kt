package love.forte.codegentle.java

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue


/**
 *
 * @author ForteScarlet
 */
class ModifierTest {

    @Test
    fun testModifierSet() {
        ModifierSet().also { set ->
            assertEquals(0, set.size)

            set.addAll(JavaModifier.PUBLIC, JavaModifier.ABSTRACT, JavaModifier.NATIVE)

            assertEquals(3, set.size)

            val iterator = set.iterator()

            assertTrue(iterator.hasNext())
            assertEquals(JavaModifier.PUBLIC, iterator.next())

            assertTrue(iterator.hasNext())
            assertEquals(JavaModifier.ABSTRACT, iterator.next())

            assertTrue(iterator.hasNext())
            assertEquals(JavaModifier.NATIVE, iterator.next())

            assertFalse(iterator.hasNext())
        }

        ModifierSet().also { set ->
            assertEquals(0, set.size)

            set.addAll(JavaModifier.NATIVE, JavaModifier.ABSTRACT, JavaModifier.PUBLIC)

            assertEquals(3, set.size)

            val iterator = set.iterator()

            // 顺序是enum的顺序而不是添加顺序

            assertTrue(iterator.hasNext())
            assertEquals(JavaModifier.PUBLIC, iterator.next())

            assertTrue(iterator.hasNext())
            assertEquals(JavaModifier.ABSTRACT, iterator.next())

            assertTrue(iterator.hasNext())
            assertEquals(JavaModifier.NATIVE, iterator.next())

            assertFalse(iterator.hasNext())
        }

        ModifierSet().also { set ->
            assertEquals(0, set.size)
            val iterator = set.iterator()
            assertFalse(iterator.hasNext())
        }
    }

    @Test
    fun testToString() {
        assertEquals(
            "[]",
            ModifierSet().toString()
        )

        assertEquals(
            "[PUBLIC, ABSTRACT, NATIVE]",
            ModifierSet().apply {
                addAll(JavaModifier.NATIVE, JavaModifier.ABSTRACT, JavaModifier.PUBLIC)
            }.toString()
        )

        assertEquals(
            "[PUBLIC, ABSTRACT, NATIVE]",
            ModifierSet().apply {
                addAll(JavaModifier.PUBLIC, JavaModifier.ABSTRACT, JavaModifier.NATIVE)
            }.toString()
        )
    }

}
