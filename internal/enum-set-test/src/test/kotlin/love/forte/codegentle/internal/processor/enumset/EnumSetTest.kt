package love.forte.codegentle.internal.processor.enumset

import love.forte.codegentle.common.BuilderDsl
import kotlin.test.*

/**
 * Test for the EnumSet processor.
 * This test verifies that the generated EnumSet classes can be used correctly.
 *
 * Note: The EnumSet classes (TestEnumSet, InternalTestEnumSet, BigTestEnumSet, etc.) will be generated
 * during the build process when KSP runs. The unresolved references in this test are expected
 * until the code is generated.
 *
 * The tests for OperatorsTestEnum and CustomAdderTestEnum verify that:
 * 1. The container interface is generated with the correct function names
 * 2. The value class methods use the correct function names
 * 3. The function names in the value class are fully lowercase
 *
 * The test for KeywordTestEnum verifies that:
 * 1. Enum entries that become Kotlin keywords when lowercase are properly escaped with backticks
 *    in the generated value class, allowing them to be used as function names without compilation errors
 *
 * Note that extension properties and functions are no longer generated, so they must be accessed directly.
 */
class EnumSetTest {

    @Test
    fun testEnumSet() {
        // Test empty set
        val emptySet = TestEnumSet.empty()
        assertTrue(emptySet.isEmpty())
        assertEquals(0, emptySet.size)

        // Test set with elements
        val set = TestEnumSet.of(TestEnum.A, TestEnum.C)
        assertFalse(set.isEmpty())
        assertEquals(2, set.size)
        assertTrue(set.contains(TestEnum.A))
        assertTrue(set.contains(TestEnum.C))
        assertFalse(set.contains(TestEnum.B))

        // Test mutable set
        val mutableSet = set.mutable()
        assertEquals(2, mutableSet.size)
        mutableSet.add(TestEnum.B)
        assertEquals(3, mutableSet.size)
        assertTrue(mutableSet.contains(TestEnum.B))

        // Test immutable conversion
        val immutableSet = mutableSet.immutable()
        assertEquals(3, immutableSet.size)
        assertTrue(immutableSet.contains(TestEnum.B))
    }

    @Test
    fun testInternalEnumSet() {
        // Test empty set
        val emptySet = InternalTestEnumSet.empty()
        assertTrue(emptySet.isEmpty())
        assertEquals(0, emptySet.size)

        // Test set with elements
        val set = InternalTestEnumSet.of(InternalTestEnum.A, InternalTestEnum.C)
        assertFalse(set.isEmpty())
        assertEquals(2, set.size)
        assertTrue(set.contains(InternalTestEnum.A))
        assertTrue(set.contains(InternalTestEnum.C))
        assertFalse(set.contains(InternalTestEnum.B))
    }

    @Test
    fun testBigEnumSet() {
        // Test empty set
        val emptySet = BigTestEnumSet.empty()
        assertTrue(emptySet.isEmpty())
        assertEquals(0, emptySet.size)

        // Test set with elements
        val set = BigTestEnumSet.of(BigTestEnum.A1, BigTestEnum.C5, BigTestEnum.G10)
        assertFalse(set.isEmpty())
        assertEquals(3, set.size)
        assertTrue(set.contains(BigTestEnum.A1))
        assertTrue(set.contains(BigTestEnum.C5))
        assertTrue(set.contains(BigTestEnum.G10))
        assertFalse(set.contains(BigTestEnum.B2))
    }

    @Test
    fun testContainerEnumSet() {
        // Test that the container interface is generated and implements BuilderDsl
        val container: TestEnumBuilderContainer = object : TestEnumBuilderContainer {
            private val modifiers = mutableSetOf<ContainerTestEnum>()

            override fun addModifier(modifier: ContainerTestEnum): TestEnumBuilderContainer {
                modifiers.add(modifier)
                return this
            }

            override fun addModifiers(vararg modifiers: ContainerTestEnum): TestEnumBuilderContainer {
                this.modifiers.addAll(modifiers)
                return this
            }

            override fun addModifiers(modifiers: Iterable<ContainerTestEnum>): TestEnumBuilderContainer {
                this.modifiers.addAll(modifiers)
                return this
            }
        }

        // Verify that the container implements BuilderDsl
        assertIs<BuilderDsl>(container)

        // Test adding modifiers
        container.addModifier(ContainerTestEnum.ONE)
        container.addModifiers(ContainerTestEnum.TWO, ContainerTestEnum.THREE)
    }

    @Test
    fun testOperatorsEnumSet() {
        // Test that the value class is generated
        val container: OperatorsTestEnumBuilderContainer = object : OperatorsTestEnumBuilderContainer {
            private val modifiers = mutableSetOf<OperatorsTestEnum>()

            override fun addModifier(modifier: OperatorsTestEnum): OperatorsTestEnumBuilderContainer {
                modifiers.add(modifier)
                return this
            }

            override fun addModifiers(vararg modifiers: OperatorsTestEnum): OperatorsTestEnumBuilderContainer {
                this.modifiers.addAll(modifiers)
                return this
            }

            override fun addModifiers(modifiers: Iterable<OperatorsTestEnum>): OperatorsTestEnumBuilderContainer {
                this.modifiers.addAll(modifiers)
                return this
            }
        }

        // Test using the value class directly
        val operators = OperatorsTestEnumModifiers(container)
        operators.alpha()
        operators.beta()
        operators.gamma()
    }

    @Test
    fun testCustomAdderEnumSet() {
        // Test that the container interface is generated with custom adder function names
        val container: CustomAdderTestEnumBuilderContainer = object : CustomAdderTestEnumBuilderContainer {
            private val elements = mutableSetOf<CustomAdderTestEnum>()

            override fun addElement(modifier: CustomAdderTestEnum): CustomAdderTestEnumBuilderContainer {
                elements.add(modifier)
                return this
            }

            override fun addElements(vararg modifiers: CustomAdderTestEnum): CustomAdderTestEnumBuilderContainer {
                this.elements.addAll(modifiers)
                return this
            }

            override fun addElements(modifiers: Iterable<CustomAdderTestEnum>): CustomAdderTestEnumBuilderContainer {
                this.elements.addAll(modifiers)
                return this
            }
        }

        // Verify that the container implements BuilderDsl
        assertIs<BuilderDsl>(container)

        // Test adding elements using the custom adder functions
        container.addElement(CustomAdderTestEnum.ONE)
        container.addElements(CustomAdderTestEnum.TWO, CustomAdderTestEnum.THREE)

        // Test using the value class directly
        val operators = CustomAdderTestEnumModifiers(container)
        operators.one()
        operators.two()
        operators.three()
    }

    @Test
    fun testKeywordEnumSet() {
        // Test that the value class is generated with backticks for Kotlin keywords
        val container: KeywordTestEnumBuilderContainer = object : KeywordTestEnumBuilderContainer {
            private val modifiers = mutableSetOf<KeywordTestEnum>()

            override fun addModifier(modifier: KeywordTestEnum): KeywordTestEnumBuilderContainer {
                modifiers.add(modifier)
                return this
            }

            override fun addModifiers(vararg modifiers: KeywordTestEnum): KeywordTestEnumBuilderContainer {
                this.modifiers.addAll(modifiers)
                return this
            }

            override fun addModifiers(modifiers: Iterable<KeywordTestEnum>): KeywordTestEnumBuilderContainer {
                this.modifiers.addAll(modifiers)
                return this
            }
        }

        // Verify that the container implements BuilderDsl
        assertIs<BuilderDsl>(container)

        // Test using the value class directly
        // These function calls would cause compilation errors if the keywords weren't properly escaped with backticks
        val operators = KeywordTestEnumModifiers(container)

        // Call methods that should be wrapped in backticks because they are Kotlin keywords
        operators.`fun`()
        operators.`in`()
        operators.`is`()
        operators.`as`()
        operators.`object`()
        operators.`class`()
        operators.`interface`()
        operators.`val`()
        operators.`var`()
        operators.`when`()
        operators.`if`()
        operators.`else`()
        operators.`return`()
    }
}
