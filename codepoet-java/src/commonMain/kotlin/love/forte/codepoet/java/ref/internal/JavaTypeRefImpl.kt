package love.forte.codepoet.java.ref.internal

import love.forte.codepoet.common.ref.AnnotationRef
import love.forte.codepoet.java.InternalJavaCodePoetApi
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.ref.JavaTypeNameRefStatus
import love.forte.codepoet.java.ref.JavaTypeRef

/**
 *
 * @author ForteScarlet
 */
internal data class JavaTypeRefImpl(
    override val typeName: JavaTypeName,
    override val status: JavaTypeNameRefStatus
) : JavaTypeRef {
    @InternalJavaCodePoetApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emitTo(codeWriter)
    }
}

internal data class JavaTypeNameRefStatusImpl(
    override val annotations: List<AnnotationRef>
) : JavaTypeNameRefStatus

internal fun JavaTypeRef.emitTo(codeWriter: JavaCodeWriter) {
    emitAnnotations(codeWriter)
    typeName.emit(codeWriter)
}

private fun JavaTypeRef.emitAnnotations(codeWriter: JavaCodeWriter) {
    for (annotation in status.annotations) {
        annotation.emitTo(codeWriter)
        codeWriter.emit(" ")
    }
}
