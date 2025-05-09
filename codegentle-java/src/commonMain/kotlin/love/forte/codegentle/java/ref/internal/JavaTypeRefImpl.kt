package love.forte.codegentle.java.ref.internal

import love.forte.codegentle.java.InternalJavaCodePoetApi
import love.forte.codegentle.java.JavaCodeWriter
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaTypeNameRefStatus
import love.forte.codegentle.java.ref.JavaTypeRef

/**
 *
 * @author ForteScarlet
 */
internal data class JavaTypeRefImpl<T : JavaTypeName>(
    override val typeName: T,
    override val status: JavaTypeNameRefStatus
) : JavaTypeRef<T> {
    @InternalJavaCodePoetApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emitTo(codeWriter)
    }
}

internal data class JavaTypeNameRefStatusImpl(
    override val annotations: List<JavaAnnotationRef>
) : JavaTypeNameRefStatus

internal fun JavaTypeRef<*>.emitTo(codeWriter: JavaCodeWriter) {
    emitAnnotations(codeWriter)
    typeName.emit(codeWriter)
}

private fun JavaTypeRef<*>.emitAnnotations(codeWriter: JavaCodeWriter) {
    for (annotation in status.annotations) {
        annotation.emitTo(codeWriter)
        codeWriter.emit(" ")
    }
}
