package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.internal.KotlinAnnotationTypeSpecImpl

/**
 * A generated Kotlin annotation class.
 *
 * ```kotlin
 * annotation class AnnotationType {
 * }
 * ```
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinAnnotationTypeSpec : KotlinTypeSpec {
    override val name: String

    override val kind: KotlinTypeSpec.Kind
        get() = KotlinTypeSpec.Kind.ANNOTATION

    override val superclass: TypeName?
        get() = null

    override val superinterfaces: List<TypeName>
        get() = emptyList()

    public companion object {
        /**
         * Create a builder for an annotation class.
         *
         * @param name the annotation class name
         * @return a new builder
         */
        public fun builder(name: String): Builder {
            return KotlinAnnotationTypeSpecBuilder(name)
        }
    }

    /**
     * Builder for [KotlinAnnotationTypeSpec].
     */
    public interface Builder {
        /**
         * The annotation class name.
         */
        public val name: String

        /**
         * Add KDoc.
         */
        public fun addKDoc(codeValue: CodeValue): Builder

        /**
         * Add KDoc.
         */
        public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): Builder

        /**
         * Add annotation reference.
         */
        public fun addAnnotationRef(ref: AnnotationRef): Builder

        /**
         * Add annotation references.
         */
        public fun addAnnotationRefs(refs: Iterable<AnnotationRef>): Builder

        /**
         * Add modifiers.
         */
        public fun addModifiers(vararg modifiers: KotlinModifier): Builder

        /**
         * Add modifiers.
         */
        public fun addModifiers(modifiers: Iterable<KotlinModifier>): Builder

        /**
         * Add modifier.
         */
        public fun addModifier(modifier: KotlinModifier): Builder

        /**
         * Add type variable references.
         */
        public fun addTypeVariableRefs(vararg typeVariables: TypeRef<TypeVariableName>): Builder

        /**
         * Add type variable references.
         */
        public fun addTypeVariableRefs(typeVariables: Iterable<TypeRef<TypeVariableName>>): Builder

        /**
         * Add type variable reference.
         */
        public fun addTypeVariableRef(typeVariable: TypeRef<TypeVariableName>): Builder

        /**
         * Add properties.
         */
        public fun addProperties(vararg properties: KotlinPropertySpec): Builder

        /**
         * Add properties.
         */
        public fun addProperties(properties: Iterable<KotlinPropertySpec>): Builder

        /**
         * Add property.
         */
        public fun addProperty(property: KotlinPropertySpec): Builder

        /**
         * Build [KotlinAnnotationTypeSpec] instance.
         */
        public fun build(): KotlinAnnotationTypeSpec
    }
}

/**
 * Builder implementation for [KotlinAnnotationTypeSpec].
 */
private class KotlinAnnotationTypeSpecBuilder(
    override val name: String
) : KotlinAnnotationTypeSpec.Builder {
    private val kDoc = CodeValue.builder()

    private val annotationRefs: MutableList<AnnotationRef> = mutableListOf()
    private val modifierSet = MutableKotlinModifierSet.empty()
    private val typeVariableRefs: MutableList<TypeRef<TypeVariableName>> = mutableListOf()
    private val properties: MutableList<KotlinPropertySpec> = mutableListOf()

    override fun addKDoc(codeValue: CodeValue): KotlinAnnotationTypeSpec.Builder = apply {
        kDoc.add(codeValue)
    }

    override fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinAnnotationTypeSpec.Builder = apply {
        kDoc.add(format, *argumentParts)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinAnnotationTypeSpec.Builder = apply {
        annotationRefs.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinAnnotationTypeSpec.Builder = apply {
        annotationRefs.addAll(refs)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinAnnotationTypeSpec.Builder = apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinAnnotationTypeSpec.Builder = apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifier(modifier: KotlinModifier): KotlinAnnotationTypeSpec.Builder = apply {
        this.modifierSet.add(modifier)
    }

    override fun addTypeVariableRefs(vararg typeVariables: TypeRef<TypeVariableName>): KotlinAnnotationTypeSpec.Builder = apply {
        this.typeVariableRefs.addAll(typeVariables)
    }

    override fun addTypeVariableRefs(typeVariables: Iterable<TypeRef<TypeVariableName>>): KotlinAnnotationTypeSpec.Builder = apply {
        this.typeVariableRefs.addAll(typeVariables)
    }

    override fun addTypeVariableRef(typeVariable: TypeRef<TypeVariableName>): KotlinAnnotationTypeSpec.Builder = apply {
        this.typeVariableRefs.add(typeVariable)
    }

    override fun addProperties(vararg properties: KotlinPropertySpec): KotlinAnnotationTypeSpec.Builder = apply {
        this.properties.addAll(properties)
    }

    override fun addProperties(properties: Iterable<KotlinPropertySpec>): KotlinAnnotationTypeSpec.Builder = apply {
        this.properties.addAll(properties)
    }

    override fun addProperty(property: KotlinPropertySpec): KotlinAnnotationTypeSpec.Builder = apply {
        this.properties.add(property)
    }

    override fun build(): KotlinAnnotationTypeSpec {
        return KotlinAnnotationTypeSpecImpl(
            name = name,
            kDoc = kDoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifierSet.immutable(),
            typeVariables = typeVariableRefs.toList(),
            properties = properties.toList(),
            functions = emptyList<KotlinFunctionSpec>(),
            subtypes = emptyList<KotlinTypeSpec>()
        )
    }
}

/**
 * Create a [KotlinAnnotationTypeSpec] with the given name.
 *
 * @param name the annotation class name
 * @param block the configuration block
 * @return a new [KotlinAnnotationTypeSpec] instance
 */
public inline fun KotlinAnnotationTypeSpec(
    name: String,
    block: KotlinAnnotationTypeSpec.Builder.() -> Unit = {}
): KotlinAnnotationTypeSpec {
    return KotlinAnnotationTypeSpec.builder(name).apply(block).build()
}
