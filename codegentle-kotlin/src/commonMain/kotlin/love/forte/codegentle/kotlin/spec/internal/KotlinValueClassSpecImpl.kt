package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.*

/**
 * Implementation of [KotlinValueClassSpec].
 *
 * @author ForteScarlet
 */
internal data class KotlinValueClassSpecImpl(
    override val name: String,
    override val primaryParameter: KotlinValueParameterSpec,
    override val kDoc: CodeValue,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val typeVariables: List<TypeRef<TypeVariableName>>,
    override val superinterfaces: List<TypeName>,
    override val properties: List<KotlinPropertySpec>,
    override val initializerBlock: CodeValue,
    override val functions: List<KotlinFunctionSpec>,
    override val subtypes: List<KotlinTypeSpec>
) : KotlinValueClassSpec {
    override val superclass: TypeName? = null

    override fun toString(): String {
        return "KotlinValueClassSpec(name='$name', kind=$kind)"
    }
}
