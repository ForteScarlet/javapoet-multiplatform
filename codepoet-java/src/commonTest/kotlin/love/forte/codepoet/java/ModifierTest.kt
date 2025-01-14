// package love.forte.codepoet.java
//
// import kotlin.test.Test
// import kotlin.test.assertEquals
// import kotlin.test.assertFalse
// import kotlin.test.assertTrue
//
//
// /**
//  *
//  * @author ForteScarlet
//  */
// class ModifierTest {
//
//     @Test
//     fun testModifierSet() {
//         ModifierSet().also { set ->
//             assertEquals(0, set.size)
//
//             set.addAll(Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.NATIVE)
//
//             assertEquals(3, set.size)
//
//             val iterator = set.iterator()
//
//             assertTrue(iterator.hasNext())
//             assertEquals(Modifier.PUBLIC, iterator.next())
//
//             assertTrue(iterator.hasNext())
//             assertEquals(Modifier.ABSTRACT, iterator.next())
//
//             assertTrue(iterator.hasNext())
//             assertEquals(Modifier.NATIVE, iterator.next())
//
//             assertFalse(iterator.hasNext())
//         }
//
//         ModifierSet().also { set ->
//             assertEquals(0, set.size)
//
//             set.addAll(Modifier.NATIVE, Modifier.ABSTRACT, Modifier.PUBLIC)
//
//             assertEquals(3, set.size)
//
//             val iterator = set.iterator()
//
//             // 顺序是enum的顺序而不是添加顺序
//
//             assertTrue(iterator.hasNext())
//             assertEquals(Modifier.PUBLIC, iterator.next())
//
//             assertTrue(iterator.hasNext())
//             assertEquals(Modifier.ABSTRACT, iterator.next())
//
//             assertTrue(iterator.hasNext())
//             assertEquals(Modifier.NATIVE, iterator.next())
//
//             assertFalse(iterator.hasNext())
//         }
//
//         ModifierSet().also { set ->
//             assertEquals(0, set.size)
//             val iterator = set.iterator()
//             assertFalse(iterator.hasNext())
//         }
//     }
//
//     @Test
//     fun testToString() {
//         assertEquals(
//             "[]",
//             ModifierSet().toString()
//         )
//
//         assertEquals(
//             "[PUBLIC, ABSTRACT, NATIVE]",
//             ModifierSet().apply {
//                 addAll(Modifier.NATIVE, Modifier.ABSTRACT, Modifier.PUBLIC)
//             }.toString()
//         )
//
//         assertEquals(
//             "[PUBLIC, ABSTRACT, NATIVE]",
//             ModifierSet().apply {
//                 addAll(Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.NATIVE)
//             }.toString()
//         )
//     }
//
// }
