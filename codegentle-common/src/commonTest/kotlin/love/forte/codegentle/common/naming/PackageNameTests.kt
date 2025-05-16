package love.forte.codegentle.common.naming

import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals
import kotlin.test.assertSame

/**
 *
 * @author ForteScarlet
 */
class PackageNameTests {
    private fun PackageName.assertNameAndToString(name: String, path: String) {
        assertEquals(name, this.name)
        assertEquals(path, this.toString())
    }

    @Test
    fun testEmptyPackageName() {
        assertSame(PackageName.EMPTY, PackageName())
        assertSame(PackageName.EMPTY, PackageName(""))
        assertSame(PackageName.EMPTY, PackageName(null, ""))
        assertSame(PackageName.EMPTY, PackageName(emptyList()))
        assertSame(PackageName(), PackageName())
        assertSame(PackageName(), PackageName(""))
        assertSame(PackageName(), PackageName(null, ""))
        assertSame(PackageName(), PackageName(emptyList()))
    }

    @Test
    fun testRootPackageName() {
        PackageName("love").assertNameAndToString("love", "love")
        PackageName(null, "love").assertNameAndToString("love", "love")
    }

    @Test
    fun testMultiLevelPackageName() {
        val loveForteCodegentle = PackageName(listOf("love", "forte", "codegentle"))

        loveForteCodegentle.assertNameAndToString("codegentle", "love.forte.codegentle")

        val loveForte = PackageName(listOf("love", "forte"))

        loveForte.assertNameAndToString("forte", "love.forte")

        val loveForteCodegentle2 = PackageName(loveForte, "codegentle")

        loveForteCodegentle2.assertNameAndToString("codegentle", "love.forte.codegentle")

        assertEquals(loveForteCodegentle, loveForteCodegentle2)
        assertEquals(loveForte, loveForteCodegentle.previous)
        assertEquals(loveForte, loveForteCodegentle2.previous)
    }

    @Test
    fun testPackageNameTop() {
        PackageName.EMPTY.top().assertNameAndToString("", "")
        PackageName("love").top().assertNameAndToString("love", "love")
        PackageName(listOf("love", "forte")).top().assertNameAndToString("love", "love")
        PackageName(listOf("love", "forte", "codeGentle")).top().assertNameAndToString("love", "love")
    }

    @Test
    fun testPackageNameParse() {
        "love.forte.codegentle".parseToPackageName().assertNameAndToString("codegentle", "love.forte.codegentle")
        "love.forte".parseToPackageName().assertNameAndToString("forte", "love.forte")

        assertEquals(
            PackageName(listOf("love")),
            "love".parseToPackageName()
        )

        assertEquals(
            PackageName(listOf("love", "forte")),
            "love.forte".parseToPackageName()
        )

        assertEquals(
            PackageName(listOf("love", "forte", "codegentle")),
            "love.forte.codegentle".parseToPackageName()
        )

        assertEquals(
            PackageName("love"),
            "love".parseToPackageName()
        )

        assertEquals(
            PackageName(),
            "".parseToPackageName()
        )

        assertEquals(
            PackageName(""),
            "".parseToPackageName()
        )
    }

    @Test
    fun testPackageNamePlus() {
        (PackageName() + "love").assertNameAndToString("love", "love")
        ((PackageName() + "love") + "forte").assertNameAndToString("forte", "love.forte")
        (PackageName("love") + "forte").assertNameAndToString("forte", "love.forte")

        assertSame(PackageName.EMPTY, PackageName.EMPTY + PackageName.EMPTY)

        val p1 = PackageName(listOf("love", "forte"))
        assertSame(p1, PackageName.EMPTY + p1)
        assertSame(p1, p1 + PackageName.EMPTY)

        (PackageName(listOf("love", "forte")) + PackageName("codegentle"))
            .assertNameAndToString("codegentle", "love.forte.codegentle")

        (PackageName(listOf("love", "forte")) + "codegentle.naming".parseToPackageName())
            .assertNameAndToString("naming", "love.forte.codegentle.naming")
    }

    @Test
    fun testPackageNameNames() {
        "love.forte.codegentle".parseToPackageName()
            .apply {
                assertContentEquals(listOf("love", "forte", "codegentle"), names().map { it.name }.toList())
            }
        "love".parseToPackageName()
            .apply {
                assertContentEquals(listOf("love"), names().map { it.name }.toList())
            }
        "".parseToPackageName()
            .apply {
                assertContentEquals(emptyList(), names().map { it.name }.toList())
            }

        assertContentEquals(emptyList(), PackageName.EMPTY.names().map { it.name }.toList())
    }
}
