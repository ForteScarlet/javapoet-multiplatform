package love.forte.codepoet.java.ref.internal

import love.forte.codepoet.common.ref.AnnotationRef
import love.forte.codepoet.java.InternalJavaCodePoetApi
import love.forte.codepoet.java.JavaCodeValue
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.ref.JavaAnnotationRef
import love.forte.codepoet.java.spec.internal.emitJavaAnnotation

/**
 *
 * @author ForteScarlet
 */
internal data class JavaAnnotationRefImpl(
    override val className: JavaClassName,
    override val members: Map<String, List<JavaCodeValue>>
) : JavaAnnotationRef {

    @InternalJavaCodePoetApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emitTo(codeWriter)
    }
}

internal fun AnnotationRef.emitTo(codeWriter: JavaCodeWriter, inline: Boolean = true) {
    codeWriter.emitJavaAnnotation(inline, className, members)
}
