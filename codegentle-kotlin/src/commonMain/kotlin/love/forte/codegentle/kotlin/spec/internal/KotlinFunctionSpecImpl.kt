package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.KotlinContextParameterSpec
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinValueParameterSpec

/**
 * Implementation of [KotlinFunctionSpec].
 *
 * @author ForteScarlet
 */
internal data class KotlinFunctionSpecImpl(
    override val name: String,
    override val returnType: TypeRef<*>,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val typeVariables: List<TypeRef<TypeVariableName>>,
    override val parameters: List<KotlinValueParameterSpec>,
    override val receiver: TypeRef<*>?,
    override val contextParameters: List<KotlinContextParameterSpec>,
    override val kDoc: CodeValue,
    override val code: CodeValue
) : KotlinFunctionSpec {
    override fun toString(): String {
        return "KotlinFunctionSpec(name='$name', returnType=${returnType.typeName})"
    }
}
