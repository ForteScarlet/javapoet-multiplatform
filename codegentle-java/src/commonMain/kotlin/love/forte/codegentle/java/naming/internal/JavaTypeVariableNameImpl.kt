// package love.forte.codegentle.java.naming.internal
//
// import love.forte.codegentle.common.ref.TypeRef
// import love.forte.codegentle.java.naming.JavaTypeVariableName
// import love.forte.codegentle.java.ref.JavaTypeRef
// import love.forte.codegentle.java.writer.JavaCodeWriter
// import love.forte.codegentle.java.writer.emitToString
//
//
// /**
//  *
//  * @author ForteScarlet
//  */
// internal class JavaTypeVariableNameImpl(
//     override val name: String,
//     override val bounds: List<TypeRef<*>> = emptyList(),
// ) : JavaTypeVariableName {
//     override fun withBounds(bounds: List<TypeRef<*>>): JavaTypeVariableName {
//         return JavaTypeVariableNameImpl(name, this.bounds + bounds)
//     }
//
//     override fun emit(codeWriter: JavaCodeWriter) {
//         codeWriter.emitAndIndent(name)
//     }
//
//     override fun toString(): String {
//         return emitToString()
//     }
//
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (other !is JavaTypeVariableNameImpl) return false
//
//         if (name != other.name) return false
//         if (bounds != other.bounds) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         var result = name.hashCode()
//         result = 31 * result + bounds.hashCode()
//         return result
//     }
// }
