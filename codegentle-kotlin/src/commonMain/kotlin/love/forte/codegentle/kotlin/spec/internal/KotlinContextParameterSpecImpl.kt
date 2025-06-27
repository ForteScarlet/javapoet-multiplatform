package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.spec.KotlinContextParameterSpec

/**
 *
 * @author ForteScarlet
 */
internal data class KotlinContextParameterSpecImpl(
    override val name: String?,
    override val typeRef: TypeRef<*>,
) : KotlinContextParameterSpec {


    override fun toString(): String {
        return "KotlinContextParameterSpec(name='$name', type=${typeRef.typeName})"
    }
}
