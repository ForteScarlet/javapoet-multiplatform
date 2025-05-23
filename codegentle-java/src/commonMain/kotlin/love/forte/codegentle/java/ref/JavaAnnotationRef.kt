package love.forte.codegentle.java.ref

import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.java.spec.internal.emitJavaAnnotation
import love.forte.codegentle.java.writer.JavaCodeWriter

//
// import love.forte.codegentle.common.computeValueIfAbsent
// import love.forte.codegentle.common.naming.ClassName
// import love.forte.codegentle.common.ref.AnnotationRef
// import love.forte.codegentle.java.JavaCodeValue
// import love.forte.codegentle.java.ref.internal.JavaAnnotationRefImpl
// import love.forte.codegentle.java.writer.JavaCodeEmitter

// /**
//  * A Java annotation ref.
//  *
//  * @author ForteScarlet
//  */
// public interface JavaAnnotationRef : AnnotationRef, JavaCodeEmitter {
//     override val className: ClassName
//     override val members: Map<String, List<JavaCodeValue>>
// }
//
// public inline fun ClassName.annotationRef(block: JavaAnnotationRefBuilder.() -> Unit = {}): JavaAnnotationRef {
//     return JavaAnnotationRefBuilder(this@annotationRef).apply {
//         block()
//     }.build()
// }
//
// /**
//  * Builder for [JavaAnnotationRef].
//  */
// public class JavaAnnotationRefBuilder(public val className: ClassName) :
//     JavaAnnotationRefBuildable<JavaAnnotationRefBuilder> {
//     private val members: MutableMap<String, MutableList<JavaCodeValue>> = linkedMapOf()
//
//     override fun addMember(name: String, codeValue: JavaCodeValue): JavaAnnotationRefBuilder = apply {
//         val values = members.computeValueIfAbsent(name) { mutableListOf() }
//         values.add(codeValue)
//     }
//
//     public fun build(): JavaAnnotationRef {
//         return JavaAnnotationRefImpl(
//             className = className,
//             members = members.mapValues { it.value.toList() }
//         )
//     }
// }

internal fun AnnotationRef.emitTo(codeWriter: JavaCodeWriter, inline: Boolean = true) {
    codeWriter.emitJavaAnnotation(inline, className, members)
}
