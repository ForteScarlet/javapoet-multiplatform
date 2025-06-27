package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.KotlinValueParameterSpec

/**
 *
 * @author ForteScarlet
 */
internal data class KotlinValueParameterSpecImpl(
    override val name: String,
    override val typeRef: TypeRef<*>,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val kDoc: CodeValue,
    override val defaultValue: CodeValue?
) : KotlinValueParameterSpec {
    override fun toString(): String {
        return "KotlinValueParameterSpec(name='$name', type=${typeRef.typeName})"
    }
}
