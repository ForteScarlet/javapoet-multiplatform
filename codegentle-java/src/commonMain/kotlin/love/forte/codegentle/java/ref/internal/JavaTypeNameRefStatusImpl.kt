package love.forte.codegentle.java.ref.internal

import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.java.ref.JavaTypeNameRefStatus

/**
 *
 * @author ForteScarlet
 */
internal data class JavaTypeNameRefStatusImpl(
    override val annotations: List<AnnotationRef>
) : JavaTypeNameRefStatus
