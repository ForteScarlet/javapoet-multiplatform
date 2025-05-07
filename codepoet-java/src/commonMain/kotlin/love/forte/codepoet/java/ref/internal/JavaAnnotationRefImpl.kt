package love.forte.codepoet.java.ref.internal

import love.forte.codepoet.common.naming.ClassName
import love.forte.codepoet.java.InternalJavaCodePoetApi
import love.forte.codepoet.java.JavaCodeValue
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.ref.JavaAnnotationRef
import love.forte.codepoet.java.spec.internal.emitAnnotation

/**
 *
 * @author ForteScarlet
 */
internal data class JavaAnnotationRefImpl(
    override val className: ClassName,
    override val members: Map<String, List<JavaCodeValue>>
) : JavaAnnotationRef {

    @InternalJavaCodePoetApi
    override fun emit(codeWriter: JavaCodeWriter) {
        codeWriter.emitAnnotation(true, className, members)
    }
}
