package love.forte.codegentle.common.ref.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.ref.AnnotationRef

/**
 *
 * @author ForteScarlet
 */
internal data class AnnotationRefImpl(
    override val typeName: ClassName,
    override val members: Map<String, List<CodeValue>>
) : AnnotationRef

