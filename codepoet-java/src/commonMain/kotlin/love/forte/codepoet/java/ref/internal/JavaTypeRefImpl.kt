package love.forte.codepoet.java.ref.internal

import love.forte.codepoet.common.naming.TypeName
import love.forte.codepoet.common.ref.AnnotationRef
import love.forte.codepoet.java.ref.JavaTypeNameRefStatus
import love.forte.codepoet.java.ref.JavaTypeRef

/**
 *
 * @author ForteScarlet
 */
internal data class JavaTypeRefImpl(
    override val typeName: TypeName,
    override val status: JavaTypeNameRefStatus
) : JavaTypeRef

internal data class JavaTypeNameRefStatusImpl(
    override val annotations: List<AnnotationRef>
) : JavaTypeNameRefStatus
