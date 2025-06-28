package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierBuilderContainer
import love.forte.codegentle.kotlin.KotlinModifierContainer
import love.forte.codegentle.kotlin.spec.internal.KotlinPropertySpecBuilderImpl

/**
 * A Kotlin property.
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinPropertySpec : KotlinSpec, KotlinModifierContainer {
    /**
     * Property's name.
     */
    public val name: String

    /**
     * Property's type ref.
     */
    public val typeRef: TypeRef<*>

    public val annotations: List<AnnotationRef>
    override val modifiers: Set<KotlinModifier>
    public val kDoc: CodeValue
    public val initializer: CodeValue?
    public val delegate: CodeValue?

    public val getter: KotlinGetterSpec?
    public val setter: KotlinSetterSpec?

    /**
     * Builder for [KotlinPropertySpec].
     */
    public interface Builder : BuilderDsl,
        KotlinModifierBuilderContainer,
        AnnotationRefCollectable<Builder> {

        /**
         * Property's name.
         */
        public val name: String

        /**
         * Property's type.
         */
        public val type: TypeRef<*>

        /**
         * Set the initializer for the property.
         *
         * @param codeValue the initializer code
         * @return this builder
         * @throws IllegalArgumentException if the property already has a delegate
         */
        public fun initializer(codeValue: CodeValue): Builder

        /**
         * Set the initializer for the property.
         *
         * @param format the initializer format string
         * @param arguments the initializer arguments
         * @return this builder
         * @throws IllegalArgumentException if the property already has a delegate
         */
        public fun initializer(format: String, vararg arguments: CodeArgumentPart): Builder

        /**
         * Set the delegate for the property.
         *
         * @param codeValue the delegate code
         * @return this builder
         * @throws IllegalArgumentException if the property already has an initializer
         */
        public fun delegate(codeValue: CodeValue): Builder

        /**
         * Set the delegate for the property.
         *
         * @param format the delegate format string
         * @param arguments the delegate arguments
         * @return this builder
         * @throws IllegalArgumentException if the property already has an initializer
         */
        public fun delegate(format: String, vararg arguments: CodeArgumentPart): Builder

        /**
         * Add KDoc to the property.
         *
         * @param codeValue the KDoc code
         * @return this builder
         */
        public fun addKDoc(codeValue: CodeValue): Builder

        /**
         * Add KDoc to the property.
         *
         * @param format the KDoc format string
         * @param argumentParts the KDoc arguments
         * @return this builder
         */
        public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): Builder

        override fun addModifier(modifier: KotlinModifier): Builder

        override fun addModifiers(modifiers: Iterable<KotlinModifier>): Builder

        override fun addModifiers(vararg modifiers: KotlinModifier): Builder

        /**
         * Build a [KotlinPropertySpec] instance.
         *
         * @return a new [KotlinPropertySpec] instance
         */
        public fun build(): KotlinPropertySpec
    }

    public companion object {
        /**
         * Create a builder for a property.
         *
         * @param name the property name
         * @param type the property type
         * @return a new builder
         */
        public fun builder(name: String, type: TypeRef<*>): Builder {
            return KotlinPropertySpecBuilderImpl(name, type)
        }

        public operator fun invoke(
            name: String,
            type: TypeRef<*>,
            block: Builder.() -> Unit = {}
        ): KotlinPropertySpec {
            return builder(name, type).apply(block).build()
        }
    }
}

/**
 * Create a [KotlinPropertySpec] with the given name and type.
 *
 * @param name the property name
 * @param type the property type
 * @param block the configuration block
 * @return a new [KotlinPropertySpec] instance
 */
public inline fun KotlinPropertySpec(
    name: String,
    type: TypeRef<*>,
    block: KotlinPropertySpec.Builder.() -> Unit = {}
): KotlinPropertySpec =
    KotlinPropertySpec.builder(name, type).also(block).build()

public inline fun KotlinPropertySpec.Builder.initializer(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinPropertySpec.Builder = initializer(CodeValue(format, block))

public inline fun KotlinPropertySpec.Builder.delegate(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinPropertySpec.Builder = delegate(CodeValue(format, block))

public inline fun KotlinPropertySpec.Builder.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinPropertySpec.Builder = addKDoc(CodeValue(format, block))
