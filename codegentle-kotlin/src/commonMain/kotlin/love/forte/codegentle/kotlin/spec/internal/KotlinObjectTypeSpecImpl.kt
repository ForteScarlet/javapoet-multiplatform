package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinObjectTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec

/**
 * Implementation of [KotlinObjectTypeSpec].
 *
 * @author ForteScarlet
 */
internal data class KotlinObjectTypeSpecImpl(
    override val name: String,
    override val kind: KotlinTypeSpec.Kind,
    override val kDoc: CodeValue,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val typeVariables: List<TypeRef<TypeVariableName>>,
    override val superinterfaces: List<TypeName>,
    override val properties: List<KotlinPropertySpec>,
    override val initializerBlock: CodeValue,
    override val functions: List<KotlinFunctionSpec>,
    override val subtypes: List<KotlinTypeSpec>
) : KotlinObjectTypeSpec {
    override val superclass: TypeName? = null

    override fun toString(): String {
        return "KotlinObjectTypeSpec(name='$name', kind=$kind)"
    }
}
