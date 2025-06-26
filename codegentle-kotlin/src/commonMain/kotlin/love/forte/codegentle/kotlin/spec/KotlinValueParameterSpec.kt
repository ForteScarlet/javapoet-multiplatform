package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueBuilder
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierBuilderContainer
import love.forte.codegentle.kotlin.KotlinModifierContainer
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.internal.KotlinValueParameterSpecImpl

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


    public companion object {
        /**
         * Create a [KotlinValueParameterSpecBuilder].
         *
         * @return new [KotlinValueParameterSpec] instance
         */
        public fun builder(name: String, type: TypeRef<*>): KotlinValueParameterSpecBuilder =
            KotlinValueParameterSpecBuilder(name, type)
    }
}

/**
 * Builder for [KotlinValueParameterSpec].
 */
public class KotlinValueParameterSpecBuilder @PublishedApi internal constructor(
    public val name: String,
    public val type: TypeRef<*>
) : BuilderDsl,
    KotlinModifierBuilderContainer,
    AnnotationRefCollectable<KotlinValueParameterSpecBuilder> {
    private val modifierSet = MutableKotlinModifierSet.empty()
    private var defaultValue: CodeValue? = null
    private val kDoc: CodeValueBuilder = CodeValue.builder()
    private val annotations = mutableListOf<AnnotationRef>()

    override fun addModifier(modifier: KotlinModifier): KotlinValueParameterSpecBuilder = apply {
        modifierSet.add(modifier)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinValueParameterSpecBuilder = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinValueParameterSpecBuilder = apply {
        modifierSet.addAll(modifiers)
    }

    public fun defaultValue(codeValue: CodeValue): KotlinValueParameterSpecBuilder = apply {
        defaultValue = codeValue
    }

    public fun defaultValue(format: String, vararg argumentParts: CodeArgumentPart): KotlinValueParameterSpecBuilder =
        apply {
            defaultValue = CodeValue(format, *argumentParts)
        }

    public fun addKDoc(codeValue: CodeValue): KotlinValueParameterSpecBuilder = apply {
        kDoc.add(codeValue)
    }

    public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinValueParameterSpecBuilder = apply {
        kDoc.add(format, *argumentParts)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinValueParameterSpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinValueParameterSpecBuilder = apply {
        annotations.addAll(refs)
    }

    /**
     * Build [KotlinValueParameterSpec].
     */
    public fun build(): KotlinValueParameterSpec =
        KotlinValueParameterSpecImpl(
            name = name,
            typeRef = type,
            annotations = annotations.toList(),
            modifiers = modifierSet.immutable(),
            kDoc = kDoc.build(),
            defaultValue = defaultValue
        )
}

public inline fun KotlinValueParameterSpecBuilder.defaultValue(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinValueParameterSpecBuilder = apply {
    defaultValue(CodeValue(format, block))
}

public inline fun KotlinValueParameterSpecBuilder.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinValueParameterSpecBuilder = apply {
    addKDoc(CodeValue(format, block))
}
