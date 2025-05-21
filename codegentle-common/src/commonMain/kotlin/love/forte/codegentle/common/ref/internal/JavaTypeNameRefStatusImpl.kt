package love.forte.codegentle.common.ref.internal

import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.JavaTypeNameRefStatus

/**
 *
 * @author ForteScarlet
 */
internal data class JavaTypeNameRefStatusImpl(
    override val annotations: List<AnnotationRef>
) : JavaTypeNameRefStatus
