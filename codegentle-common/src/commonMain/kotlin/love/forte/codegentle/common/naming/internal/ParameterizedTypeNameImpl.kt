package love.forte.codegentle.common.naming.internal

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.ParameterizedTypeName
import love.forte.codegentle.common.ref.TypeRef

/**
 *
 * @author ForteScarlet
 */
internal class ParameterizedTypeNameImpl(
    override val enclosingType: ParameterizedTypeName?,
    override val rawType: ClassName,
    override val typeArguments: List<TypeRef<*>>
) : ParameterizedTypeName {
    override fun nestedClass(name: String): ParameterizedTypeName {
        return ParameterizedTypeNameImpl(this, rawType.nestedClass(name), emptyList())
    }

    override fun nestedClass(
        name: String,
        typeArguments: List<TypeRef<*>>
    ): ParameterizedTypeName {
        return ParameterizedTypeNameImpl(this, rawType.nestedClass(name), typeArguments)
    }
}
