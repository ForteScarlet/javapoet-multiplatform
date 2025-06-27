package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.KotlinAnnotationTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec

/**
 * Implementation of [KotlinAnnotationTypeSpec].
 *
 * @author ForteScarlet
 */
internal data class KotlinAnnotationTypeSpecImpl(
    override val name: String,
    override val kDoc: CodeValue,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val typeVariables: List<TypeRef<TypeVariableName>>,
    override val properties: List<KotlinPropertySpec>,
    override val functions: List<KotlinFunctionSpec>,
    override val subtypes: List<KotlinTypeSpec>
) : KotlinAnnotationTypeSpec {
    override val initializerBlock: CodeValue = CodeValue()

    override fun toString(): String {
        return "KotlinAnnotationTypeSpec(name='$name', kind=$kind)"
    }
}
