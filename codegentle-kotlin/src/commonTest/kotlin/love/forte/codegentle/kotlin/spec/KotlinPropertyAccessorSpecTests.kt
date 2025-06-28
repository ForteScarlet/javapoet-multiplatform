package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.writer.writeToKotlinString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

/**
 * Tests for [KotlinPropertyAccessorSpec] and its subclasses.
 */
class KotlinPropertyAccessorSpecTests {

    @Test
    fun testBasicGetter() {
        val getterSpec = KotlinPropertyAccessorSpec.getterBuilder()
            .build()

        assertNotNull(getterSpec)
        assertEquals(KotlinPropertyAccessorSpec.Kind.GETTER, getterSpec.kind)
        assertEquals(emptySet<KotlinModifier>(), getterSpec.modifiers)
        assertEquals(emptyList<KotlinValueParameterSpec>(), getterSpec.parameters)
    }

    @Test
    fun testGetterWithModifiers() {
        val getterSpec = KotlinPropertyAccessorSpec.getterBuilder()
            .addModifier(KotlinModifier.PRIVATE)
            .build()

        assertEquals(setOf(KotlinModifier.PRIVATE), getterSpec.modifiers)
    }

    @Test
    fun testGetterWithCode() {
        val getterSpec = KotlinPropertyAccessorSpec.getterBuilder()
            .addCode("return field")
            .build()

        assertNotNull(getterSpec.code)
        assertEquals("return field", getterSpec.code.writeToKotlinString())
    }

    @Test
    fun testGetterWithKDoc() {
        val getterSpec = KotlinPropertyAccessorSpec.getterBuilder()
            .addKDoc("This is a getter.")
            .build()

        assertNotNull(getterSpec.kDoc)
        assertEquals("This is a getter.", getterSpec.kDoc.writeToKotlinString())
    }

    @Test
    fun testBasicSetter() {
        val setterSpec = KotlinPropertyAccessorSpec.setterBuilder("value")
            .build()

        assertNotNull(setterSpec)
        assertEquals(KotlinPropertyAccessorSpec.Kind.SETTER, setterSpec.kind)
        assertEquals(emptySet<KotlinModifier>(), setterSpec.modifiers)
        assertEquals(emptyList<KotlinValueParameterSpec>(), setterSpec.parameters)
        assertEquals("value", setterSpec.parameterName)
    }

    @Test
    fun testSetterWithModifiers() {
        val setterSpec = KotlinPropertyAccessorSpec.setterBuilder("value")
            .addModifier(KotlinModifier.PRIVATE)
            .build()

        assertEquals(setOf(KotlinModifier.PRIVATE), setterSpec.modifiers)
    }

    @Test
    fun testSetterWithCode() {
        val setterSpec = KotlinPropertyAccessorSpec.setterBuilder("value")
            .addCode("field = value")
            .build()

        assertNotNull(setterSpec.code)
        assertEquals("field = value", setterSpec.code.writeToKotlinString())
    }

    @Test
    fun testSetterWithKDoc() {
        val setterSpec = KotlinPropertyAccessorSpec.setterBuilder("value")
            .addKDoc("This is a setter.")
            .build()

        assertNotNull(setterSpec.kDoc)
        assertEquals("This is a setter.", setterSpec.kDoc.writeToKotlinString())
    }
}
