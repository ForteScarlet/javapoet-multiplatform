package love.forte.codegentle.java.ref.internal

import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.java.InternalJavaCodeGentleApi
import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.spec.internal.emitJavaAnnotation
import love.forte.codegentle.java.writer.JavaCodeWriter

/**
 *
 * @author ForteScarlet
 */
internal data class JavaAnnotationRefImpl(
    override val className: JavaClassName,
    override val members: Map<String, List<JavaCodeValue>>
) : JavaAnnotationRef {

    @InternalJavaCodeGentleApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emitTo(codeWriter)
    }
}

internal fun AnnotationRef.emitTo(codeWriter: JavaCodeWriter, inline: Boolean = true) {
    codeWriter.emitJavaAnnotation(inline, className, members)
}
