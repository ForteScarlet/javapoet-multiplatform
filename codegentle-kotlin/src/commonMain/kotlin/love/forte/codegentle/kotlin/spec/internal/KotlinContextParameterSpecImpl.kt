package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.spec.KotlinContextParameterSpec

/**
 *
 * @author ForteScarlet
 */
internal class KotlinContextParameterSpecImpl(
    override val name: String?,
    override val typeRef: TypeRef<*>,
) : KotlinContextParameterSpec {
}
