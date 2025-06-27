package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierBuilderContainer
import love.forte.codegentle.kotlin.KotlinModifierContainer
import love.forte.codegentle.kotlin.spec.internal.KotlinValueParameterSpecBuilderImpl

/**
 * A Kotlin value parameter.
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinValueParameterSpec : Spec, KotlinModifierContainer {
    /**
     * Parameter name.
     */
    public val name: String
    public val typeRef: TypeRef<*>

    public val annotations: List<AnnotationRef>
    override val modifiers: Set<KotlinModifier>
    public val kDoc: CodeValue
    public val defaultValue: CodeValue?

    /**
     * Builder for [KotlinValueParameterSpec].
     */
    public interface Builder : BuilderDsl,
        KotlinModifierBuilderContainer,
        AnnotationRefCollectable<Builder> {
        /**
         * Parameter name.
         */
        public val name: String

        /**
         * Parameter type.
         */
        public val type: TypeRef<*>

        /**
         * Set the default value for the parameter.
         *
         * @param codeValue the default value code
         * @return this builder
         */
        public fun defaultValue(codeValue: CodeValue): Builder

        /**
         * Set the default value for the parameter.
         *
         * @param format the default value format string
         * @param argumentParts the default value arguments
         * @return this builder
         */
        public fun defaultValue(format: String, vararg argumentParts: CodeArgumentPart): Builder

        /**
         * Add KDoc to the parameter.
         *
         * @param codeValue the KDoc code
         * @return this builder
         */
        public fun addKDoc(codeValue: CodeValue): Builder

        /**
         * Add KDoc to the parameter.
         *
         * @param format the KDoc format string
         * @param argumentParts the KDoc arguments
         * @return this builder
         */
        public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): Builder

        /**
         * Build a [KotlinValueParameterSpec] instance.
         *
         * @return a new [KotlinValueParameterSpec] instance
         */
        public fun build(): KotlinValueParameterSpec
    }

    public companion object {
        /**
         * Create a builder for a value parameter.
         *
         * @param name the parameter name
         * @param type the parameter type
         * @return a new builder
         */
        public fun builder(name: String, type: TypeRef<*>): Builder =
            KotlinValueParameterSpecBuilderImpl(name, type)
    }
}

public inline fun KotlinValueParameterSpec.Builder.defaultValue(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinValueParameterSpec.Builder = apply {
    defaultValue(CodeValue(format, block))
}

public inline fun KotlinValueParameterSpec.Builder.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinValueParameterSpec.Builder = apply {
    addKDoc(CodeValue(format, block))
}
