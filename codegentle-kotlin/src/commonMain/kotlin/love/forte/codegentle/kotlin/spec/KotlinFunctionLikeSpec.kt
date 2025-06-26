package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierContainer

/**
 * A Kotlin function or A Kotlin constructor.
 *
 * @author ForteScarlet
 */
public interface KotlinFunctionLikeSpec : Spec, KotlinModifierContainer {
    /**
     * Function name if it exists.
     * `null` if it's a constructor.
     */
    public val name: String?

    /**
     * Function return type if it exists.
     * `null` if it's a constructor.
     */
    public val returnType: TypeRef<*>?

    override val modifiers: Set<KotlinModifier>
    public val annotations: List<AnnotationRef>
    public val parameters: List<KotlinValueParameterSpec>
    public val kDoc: CodeValue
    public val code: CodeValue
}
