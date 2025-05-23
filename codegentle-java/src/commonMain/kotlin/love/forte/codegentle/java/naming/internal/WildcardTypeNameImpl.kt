// package love.forte.codegentle.java.naming.internal
//
// import love.forte.codegentle.java.naming.JavaClassNames
// import love.forte.codegentle.java.naming.JavaSubtypeWildcardTypeName
// import love.forte.codegentle.java.naming.JavaSupertypeWildcardTypeName
// import love.forte.codegentle.java.ref.JavaTypeRef
// import love.forte.codegentle.java.type
// import love.forte.codegentle.java.writer.JavaCodeWriter
// import love.forte.codegentle.java.writer.emit
// import love.forte.codegentle.java.writer.emitToString
//
// internal class JavaSubtypeWildcardTypeNameImpl(
//     override val bounds: List<JavaTypeRef<*>>,
// ) : JavaSubtypeWildcardTypeName {
//     override fun emit(codeWriter: JavaCodeWriter) {
//         if (lowerBounds.isNotEmpty()) {
//             lowerBounds.forEachIndexed { index, typeRef ->
//                 if (index == 0) {
//                     // first
//                     codeWriter.emit("? super %V") {
//                         type(typeRef)
//                     }
//                 } else {
//                     codeWriter.emit(" & %V") {
//                         type(typeRef)
//                     }
//                 }
//             }
//         }
//     }
//
//     override fun toString(): String {
//         return emitToString()
//     }
//
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (other !is JavaSubtypeWildcardTypeNameImpl) return false
//
//         if (lowerBounds != other.lowerBounds) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         return lowerBounds.hashCode()
//     }
// }
//
// internal class JavaSupertypeWildcardTypeNameImpl(
//     override val bounds: List<JavaTypeRef<*>>,
// ) : JavaSupertypeWildcardTypeName {
//     override fun emit(codeWriter: JavaCodeWriter) {
//         if (upperBounds.isNotEmpty()) {
//             var extends = false
//             upperBounds.forEachIndexed { index, typeRef ->
//                 if (index == 0) {
//                     // first
//                     codeWriter.emit("?")
//                 }
//
//                 if (typeRef.typeName == JavaClassNames.OBJECT) {
//                     // continue
//                     return@forEachIndexed
//                 }
//
//                 if (!extends) {
//                     codeWriter.emit(" extends %V") { type(typeRef) }
//                     extends = true
//                 } else {
//                     codeWriter.emit(" & %V") { type(typeRef) }
//                 }
//             }
//         }
//
//     }
//
//     override fun toString(): String {
//         return emitToString()
//     }
//
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (other !is JavaSupertypeWildcardTypeNameImpl) return false
//
//         if (upperBounds != other.upperBounds) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         return upperBounds.hashCode()
//     }
// }
