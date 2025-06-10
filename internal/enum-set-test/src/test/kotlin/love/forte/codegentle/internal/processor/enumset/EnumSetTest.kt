package love.forte.codegentle.internal.processor.enumset

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

/**
 * Test for the EnumSet processor.
 * This test verifies that the generated EnumSet classes can be used correctly.
 * 
 * Note: The EnumSet classes (TestEnumSet, InternalTestEnumSet, BigTestEnumSet) will be generated
 * during the build process when KSP runs. The unresolved references in this test are expected
 * until the code is generated.
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
}
