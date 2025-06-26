package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.kotlin.spec.internal.KotlinContextParameterSpecImpl

/**
 * A Kotlin context parameter.
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinContextParameterSpec : Spec {
    /**
     * Parameter name.
     * `null` if it's `_`, e.g., `context(_: ParameterType)`.
     */
    public val name: String?
    public val typeRef: TypeRef<*>

    public companion object {
        /**
         * Create a [KotlinContextParameterSpecBuilder].
         *
         * @return new [KotlinContextParameterSpec] instance
         */
        public fun builder(name: String?, type: TypeRef<*>): KotlinContextParameterSpecBuilder =
            KotlinContextParameterSpecBuilder(name, type)
    }
}

/**
 * Builder for [KotlinContextParameterSpec].
 */
public class KotlinContextParameterSpecBuilder @PublishedApi internal constructor(
    public val name: String?,
    public val type: TypeRef<*>
) : BuilderDsl {

    /**
     * Build [KotlinContextParameterSpec].
     */
    public fun build(): KotlinContextParameterSpec =
        KotlinContextParameterSpecImpl(name, type)
}
