package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueBuilder
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierBuilderContainer
import love.forte.codegentle.kotlin.KotlinModifierContainer
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.internal.KotlinPropertySpecImpl

/**
 * A Kotlin property.
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinPropertySpec : Spec, KotlinModifierContainer {
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

    public companion object {
        /**
         * Create a builder for a property.
         *
         * @param name the property name
         * @param type the property type
         * @return a new builder
         */
        public fun builder(name: String, type: TypeRef<*>): KotlinPropertySpecBuilder {
            return KotlinPropertySpecBuilder(name, type)
        }

        public operator fun invoke(
            name: String,
            type: TypeRef<*>,
            block: KotlinPropertySpecBuilder.() -> Unit = {}
        ): KotlinPropertySpec {
            return builder(name, type).apply(block).build()
        }
    }
}

public inline fun KotlinPropertySpec(
    name: String,
    type: TypeRef<*>,
    block: KotlinPropertySpecBuilder.() -> Unit = {}
): KotlinPropertySpec =
    KotlinPropertySpec.builder(name, type).also(block).build()

/**
 * Builder for [KotlinPropertySpec].
 */
public class KotlinPropertySpecBuilder @PublishedApi internal constructor(
    public val name: String,
    public val type: TypeRef<*>
) : BuilderDsl,
    KotlinModifierBuilderContainer,
    AnnotationRefCollectable<KotlinPropertySpecBuilder> {

    private val kDoc: CodeValueBuilder = CodeValue.builder()
    private val modifierSet = MutableKotlinModifierSet.empty()
    private val annotations = mutableListOf<AnnotationRef>()

    /**
     * Property's delegate.
     *
     * `val prop: $type by $codeValue`.
     *
     * Cannot exist at the same time as `initializer`.
     */
    private var delegate: CodeValue? = null

    /**
     * Initializer for property.
     * Cannot exist at the same time as `delegate`.
     */
    private var initializer: CodeValue? = null

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `delegate`.
     */
    public fun initializer(codeValue: CodeValue): KotlinPropertySpecBuilder = apply {
        require(delegate == null) { "Cannot exist at the same time as `delegate`" }
        initializer = codeValue
    }

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `initializer`.
     */
    public fun initializer(format: String, vararg arguments: CodeArgumentPart): KotlinPropertySpecBuilder = apply {
        initializer(CodeValue(format, *arguments))
    }

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `initializer`.
     */
    public fun delegate(codeValue: CodeValue): KotlinPropertySpecBuilder = apply {
        require(initializer == null) { "Cannot exist at the same time as `initializer`" }
        delegate = codeValue
    }

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `initializer`.
     */
    public fun delegate(format: String, vararg arguments: CodeArgumentPart): KotlinPropertySpecBuilder = apply {
        require(initializer == null) { "Cannot exist at the same time as `initializer`" }
        delegate(format, *arguments)
    }

    override fun addModifier(modifier: KotlinModifier): KotlinModifierBuilderContainer = apply {
        modifierSet.add(modifier)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinModifierBuilderContainer = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinModifierBuilderContainer = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinPropertySpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinPropertySpecBuilder = apply {
        annotations.addAll(refs)
    }

    public fun addKDoc(codeValue: CodeValue): KotlinPropertySpecBuilder = apply {
        kDoc.add(codeValue)
    }

    public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinPropertySpecBuilder = apply {
        kDoc.add(format, *argumentParts)
    }

    /**
     * Build [KotlinPropertySpec] instance.
     */
    public fun build(): KotlinPropertySpec =
        KotlinPropertySpecImpl(
            name = name,
            typeRef = type,
            annotations = annotations.toList(),
            modifiers = modifierSet.immutable(),
            kDoc = kDoc.build(),
            initializer = initializer,
            delegate = delegate
        )
}
