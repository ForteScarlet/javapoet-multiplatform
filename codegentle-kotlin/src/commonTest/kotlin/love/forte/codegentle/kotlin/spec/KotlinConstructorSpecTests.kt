package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.ref.kotlinRef
import love.forte.codegentle.kotlin.writer.writeToKotlinString
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

/**
 * Tests for [KotlinConstructorSpec].
 */
class KotlinConstructorSpecTests {

    @Test
    fun testBasicConstructor() {
        val constructorSpec = KotlinConstructorSpec.builder()
            .build()

        assertNotNull(constructorSpec)
        assertEquals(emptySet<KotlinModifier>(), constructorSpec.modifiers)
        assertEquals(emptyList<KotlinValueParameterSpec>(), constructorSpec.parameters)
        assertNull(constructorSpec.constructorDelegation)
    }

    @Test
    fun testConstructorWithModifiers() {
        val constructorSpec = KotlinConstructorSpec.builder()
            .addModifier(KotlinModifier.PRIVATE)
            .build()

        assertEquals(setOf(KotlinModifier.PRIVATE), constructorSpec.modifiers)
    }

    @Test
    fun testConstructorWithParameters() {
        val stringType = ClassName("kotlin", "String").kotlinRef()
        val intType = ClassName("kotlin", "Int").kotlinRef()

        val param1 = KotlinValueParameterSpec.builder("param1", stringType)
            .build()
        val param2 = KotlinValueParameterSpec.builder("param2", intType)
            .defaultValue("0")
            .build()

        val constructorSpec = KotlinConstructorSpec.builder()
            .addParameter(param1)
            .addParameter(param2)
            .build()

        assertEquals(2, constructorSpec.parameters.size)
        assertEquals("param1", constructorSpec.parameters[0].name)
        assertEquals("param2", constructorSpec.parameters[1].name)
    }

    @Test
    fun testConstructorWithThisDelegation() {
        val constructorSpec = KotlinConstructorSpec.builder()
            .thisConstructorDelegation {
                addArgument("\"default\"")
                addArgument("0")
            }
            .build()

        assertNotNull(constructorSpec.constructorDelegation)
        assertEquals(ConstructorDelegation.Kind.THIS, constructorSpec.constructorDelegation?.kind)
        assertEquals(2, constructorSpec.constructorDelegation?.arguments?.size)
    }

    @Test
    fun testConstructorWithSuperDelegation() {
        val constructorSpec = KotlinConstructorSpec.builder()
            .superConstructorDelegation {
                addArgument("\"parent\"")
            }
            .build()

        assertNotNull(constructorSpec.constructorDelegation)
        assertEquals(ConstructorDelegation.Kind.SUPER, constructorSpec.constructorDelegation?.kind)
        assertEquals(1, constructorSpec.constructorDelegation?.arguments?.size)
    }

    @Test
    fun testConstructorWithCode() {
        val constructorSpec = KotlinConstructorSpec.builder()
            .addCode("println(\"Constructor called\")")
            .build()

        assertNotNull(constructorSpec.code)
        assertEquals("println(\"Constructor called\")", constructorSpec.code.writeToKotlinString())
    }

    @Test
    fun testConstructorWithKDoc() {
        val constructorSpec = KotlinConstructorSpec.builder()
            .addKDoc("This is a constructor.")
            .build()

        assertNotNull(constructorSpec.kDoc)
        assertEquals("This is a constructor.", constructorSpec.kDoc.writeToKotlinString())
    }
}
