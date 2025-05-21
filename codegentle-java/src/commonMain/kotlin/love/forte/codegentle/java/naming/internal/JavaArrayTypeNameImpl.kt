// package love.forte.codegentle.java.naming.internal
//
// import love.forte.codegentle.common.naming.ArrayTypeName
// import love.forte.codegentle.java.naming.JavaArrayTypeName
// import love.forte.codegentle.java.ref.JavaTypeRef
// import love.forte.codegentle.java.writer.JavaCodeWriter
// import love.forte.codegentle.java.writer.emitToString
//
//
// /**
//  *
//  * @author ForteScarlet
//  */
// internal class JavaArrayTypeNameImpl(
//     override val componentType: JavaTypeRef<*>,
// ) : JavaArrayTypeName {
//     override fun emit(codeWriter: JavaCodeWriter, varargs: Boolean) {
//         emitLeafType(codeWriter)
//         emitBrackets(codeWriter, varargs)
//     }
//
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (other !is JavaArrayTypeName) return false
//
//         if (componentType != other.componentType) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         val result = componentType.hashCode()
//         return result
//     }
//
//     override fun toString(): String {
//         return emitToString()
//     }
// }
//
// private fun ArrayTypeName.emitLeafType(out: JavaCodeWriter) {
//     val asArray = componentType.typeName as? ArrayTypeName
//     if (asArray != null) {
//         return asArray.emitLeafType(out)
//     }
//     TODO("return componentType.emit(out)")
// }
//
// private fun ArrayTypeName.emitBrackets(out: JavaCodeWriter, varargs: Boolean) {
//
//     val asArray = componentType as? ArrayTypeName
//
//     if (asArray == null) {
//         // Last bracket.
//         out.emit(if (varargs) "..." else "[]")
//         return
//     }
//
//     out.emit("[]")
//     return asArray.emitBrackets(out, varargs)
// }
