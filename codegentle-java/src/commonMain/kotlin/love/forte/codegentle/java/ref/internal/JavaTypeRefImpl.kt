// package love.forte.codegentle.java.ref.internal
//
// import emitTo
// import love.forte.codegentle.common.ref.AnnotationRef
// import love.forte.codegentle.common.ref.JavaTypeNameRefStatus
// import love.forte.codegentle.java.InternalJavaCodeGentleApi
// import love.forte.codegentle.java.naming.JavaTypeName
// import love.forte.codegentle.java.ref.JavaAnnotationRef
// import love.forte.codegentle.java.ref.JavaTypeNameRefStatus
// import love.forte.codegentle.java.ref.JavaTypeRef
// import love.forte.codegentle.java.writer.JavaCodeWriter
// import love.forte.codegentle.java.writer.emitToString
//
// /**
//  *
//  * @author ForteScarlet
//  */
// internal data class JavaTypeRefImpl<T : JavaTypeName>(
//     override val typeName: T,
//     override val status: JavaTypeNameRefStatus
// ) : JavaTypeRef<T> {
//     @InternalJavaCodeGentleApi
//     override fun emit(codeWriter: JavaCodeWriter) {
//         emitTo(codeWriter)
//     }
//
//     override fun toString(): String = emitToString()
// }
//
// internal data class JavaTypeNameRefStatusImpl(
//     val annotations: List<AnnotationRef>
// ) //  : JavaTypeNameRefStatus
//

