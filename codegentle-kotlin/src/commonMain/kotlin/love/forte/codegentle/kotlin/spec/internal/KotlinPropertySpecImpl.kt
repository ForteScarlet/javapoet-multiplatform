package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueBuilder
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec

/**
 *
 * @author ForteScarlet
 */
internal data class KotlinPropertySpecImpl(
    override val name: String,
    override val typeRef: TypeRef<*>,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val kDoc: CodeValue,
    override val initializer: CodeValue?,
    override val delegate: CodeValue?
) : KotlinPropertySpec {
    override fun toString(): String {
        return "KotlinPropertySpec(name='$name', type=${typeRef.typeName})"
    }
}

/**
 * Implementation of [KotlinPropertySpec.Builder].
 *
 * @author ForteScarlet
 */
internal class KotlinPropertySpecBuilderImpl(
    override val name: String,
    override val type: TypeRef<*>
) : KotlinPropertySpec.Builder {

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
    override fun initializer(codeValue: CodeValue): KotlinPropertySpec.Builder = apply {
        require(delegate == null) { "Cannot exist at the same time as `delegate`" }
        initializer = codeValue
    }

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `initializer`.
     */
    override fun initializer(format: String, vararg arguments: CodeArgumentPart): KotlinPropertySpec.Builder = apply {
        initializer(CodeValue(format, *arguments))
    }

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `initializer`.
     */
    override fun delegate(codeValue: CodeValue): KotlinPropertySpec.Builder = apply {
        require(initializer == null) { "Cannot exist at the same time as `initializer`" }
        delegate = codeValue
    }

    /**
     * @throws IllegalArgumentException Cannot exist at the same time as `initializer`.
     */
    override fun delegate(format: String, vararg arguments: CodeArgumentPart): KotlinPropertySpec.Builder = apply {
        require(initializer == null) { "Cannot exist at the same time as `initializer`" }
        delegate(CodeValue(format, *arguments))
    }

    override fun addModifier(modifier: KotlinModifier): KotlinPropertySpec.Builder = apply {
        modifierSet.add(modifier)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinPropertySpec.Builder = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinPropertySpec.Builder = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinPropertySpec.Builder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinPropertySpec.Builder = apply {
        annotations.addAll(refs)
    }

    override fun addKDoc(codeValue: CodeValue): KotlinPropertySpec.Builder = apply {
        kDoc.add(codeValue)
    }

    override fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinPropertySpec.Builder = apply {
        kDoc.add(format, *argumentParts)
    }

    /**
     * Build [KotlinPropertySpec] instance.
     */
    override fun build(): KotlinPropertySpec =
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
