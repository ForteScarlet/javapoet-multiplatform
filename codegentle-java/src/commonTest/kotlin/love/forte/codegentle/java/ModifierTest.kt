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
        MutableJavaModifierSet.empty().also { set ->
            assertEquals(0, set.size)

            set.addAll(arrayOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT, JavaModifier.NATIVE))

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

        MutableJavaModifierSet.empty().also { set ->
            assertEquals(0, set.size)

            set.addAll(arrayOf(JavaModifier.NATIVE, JavaModifier.ABSTRACT, JavaModifier.PUBLIC))

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

        MutableJavaModifierSet.empty().also { set ->
            assertEquals(0, set.size)
            val iterator = set.iterator()
            assertFalse(iterator.hasNext())
        }
    }

    @Test
    fun testToString() {
        assertEquals(
            "[]",
            MutableJavaModifierSet.empty().toString()
        )
        assertEquals(
            "[]",
            JavaModifierSet.empty().toString()
        )

        assertEquals(
            "[public, abstract, native]",
            MutableJavaModifierSet.empty().apply {
                addAll(arrayOf(JavaModifier.NATIVE, JavaModifier.ABSTRACT, JavaModifier.PUBLIC))
            }.toString()
        )

        assertEquals(
            "[public, abstract, native]",
            MutableJavaModifierSet.empty().apply {
                addAll(arrayOf(JavaModifier.NATIVE, JavaModifier.ABSTRACT, JavaModifier.PUBLIC))
            }.immutable().toString()
        )

        assertEquals(
            "[public, abstract, native]",
            MutableJavaModifierSet.empty().apply {
                addAll(arrayOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT, JavaModifier.NATIVE))
            }.toString()
        )

        assertEquals(
            "[public, abstract, native]",
            MutableJavaModifierSet.empty().apply {
                addAll(arrayOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT, JavaModifier.NATIVE))
            }.immutable().toString()
        )
    }

}
