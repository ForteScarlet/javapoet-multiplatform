package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.internal.KotlinValueClassSpecImpl

/**
 * A generated Kotlin value class.
 *
 * ```kotlin
 * value class ValueClass(val value: String)
 * ```
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinValueClassSpec : KotlinTypeSpec {
    override val name: String

    override val kind: KotlinTypeSpec.Kind
        get() = KotlinTypeSpec.Kind.VALUE

    /**
     * The primary constructor parameter of the value class.
     */
    public val primaryParameter: KotlinValueParameterSpec

    public companion object {
        /**
         * Create a builder for a value class.
         *
         * @param name the value class name
         * @param primaryParameter the primary constructor parameter
         * @return a new builder
         */
        public fun builder(name: String, primaryParameter: KotlinValueParameterSpec): Builder {
            return KotlinValueClassSpecBuilder(name, primaryParameter)
        }
    }

    /**
     * Builder for [KotlinValueClassSpec].
     */
    public interface Builder {
        /**
         * The value class name.
         */
        public val name: String

        /**
         * The primary constructor parameter of the value class.
         */
        public val primaryParameter: KotlinValueParameterSpec

        /**
         * Add KDoc.
         */
        public fun addKDoc(codeValue: CodeValue): Builder

        /**
         * Add KDoc.
         */
        public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): Builder

        /**
         * Add initializer block.
         */
        public fun addInitializerBlock(codeValue: CodeValue): Builder

        /**
         * Add initializer block.
         */
        public fun addInitializerBlock(format: String, vararg argumentParts: CodeArgumentPart): Builder

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
         * Add superinterfaces.
         */
        public fun addSuperinterfaces(vararg superinterfaces: TypeName): Builder

        /**
         * Add superinterfaces.
         */
        public fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): Builder

        /**
         * Add superinterface.
         */
        public fun addSuperinterface(superinterface: TypeName): Builder

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
         * Add functions.
         */
        public fun addFunctions(functions: Iterable<KotlinFunctionSpec>): Builder

        /**
         * Add functions.
         */
        public fun addFunctions(vararg functions: KotlinFunctionSpec): Builder

        /**
         * Add function.
         */
        public fun addFunction(function: KotlinFunctionSpec): Builder

        /**
         * Build [KotlinValueClassSpec] instance.
         */
        public fun build(): KotlinValueClassSpec
    }
}

/**
 * Builder implementation for [KotlinValueClassSpec].
 */
private class KotlinValueClassSpecBuilder(
    override val name: String,
    override val primaryParameter: KotlinValueParameterSpec
) : KotlinValueClassSpec.Builder {
    private val kDoc = CodeValue.builder()
    private val initializerBlock = CodeValue.builder()

    private val annotationRefs: MutableList<AnnotationRef> = mutableListOf()
    private val modifierSet = MutableKotlinModifierSet.empty()
    private val typeVariableRefs: MutableList<TypeRef<TypeVariableName>> = mutableListOf()
    private val superinterfaces: MutableList<TypeName> = mutableListOf()
    private val properties: MutableList<KotlinPropertySpec> = mutableListOf()
    private val functions: MutableList<KotlinFunctionSpec> = mutableListOf()

    override fun addKDoc(codeValue: CodeValue): KotlinValueClassSpec.Builder = apply {
        kDoc.add(codeValue)
    }

    override fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinValueClassSpec.Builder = apply {
        kDoc.add(format, *argumentParts)
    }

    override fun addInitializerBlock(codeValue: CodeValue): KotlinValueClassSpec.Builder = apply {
        this.initializerBlock.add(codeValue)
    }

    override fun addInitializerBlock(format: String, vararg argumentParts: CodeArgumentPart): KotlinValueClassSpec.Builder = apply {
        this.initializerBlock.add(format, *argumentParts)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinValueClassSpec.Builder = apply {
        annotationRefs.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinValueClassSpec.Builder = apply {
        annotationRefs.addAll(refs)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinValueClassSpec.Builder = apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinValueClassSpec.Builder = apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifier(modifier: KotlinModifier): KotlinValueClassSpec.Builder = apply {
        this.modifierSet.add(modifier)
    }

    override fun addTypeVariableRefs(vararg typeVariables: TypeRef<TypeVariableName>): KotlinValueClassSpec.Builder = apply {
        this.typeVariableRefs.addAll(typeVariables)
    }

    override fun addTypeVariableRefs(typeVariables: Iterable<TypeRef<TypeVariableName>>): KotlinValueClassSpec.Builder = apply {
        this.typeVariableRefs.addAll(typeVariables)
    }

    override fun addTypeVariableRef(typeVariable: TypeRef<TypeVariableName>): KotlinValueClassSpec.Builder = apply {
        this.typeVariableRefs.add(typeVariable)
    }

    override fun addSuperinterfaces(vararg superinterfaces: TypeName): KotlinValueClassSpec.Builder = apply {
        this.superinterfaces.addAll(superinterfaces)
    }

    override fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): KotlinValueClassSpec.Builder = apply {
        this.superinterfaces.addAll(superinterfaces)
    }

    override fun addSuperinterface(superinterface: TypeName): KotlinValueClassSpec.Builder = apply {
        this.superinterfaces.add(superinterface)
    }

    override fun addProperties(vararg properties: KotlinPropertySpec): KotlinValueClassSpec.Builder = apply {
        this.properties.addAll(properties)
    }

    override fun addProperties(properties: Iterable<KotlinPropertySpec>): KotlinValueClassSpec.Builder = apply {
        this.properties.addAll(properties)
    }

    override fun addProperty(property: KotlinPropertySpec): KotlinValueClassSpec.Builder = apply {
        this.properties.add(property)
    }

    override fun addFunctions(functions: Iterable<KotlinFunctionSpec>): KotlinValueClassSpec.Builder = apply {
        this.functions.addAll(functions)
    }

    override fun addFunctions(vararg functions: KotlinFunctionSpec): KotlinValueClassSpec.Builder = apply {
        this.functions.addAll(functions)
    }

    override fun addFunction(function: KotlinFunctionSpec): KotlinValueClassSpec.Builder = apply {
        this.functions.add(function)
    }

    override fun build(): KotlinValueClassSpec {
        return KotlinValueClassSpecImpl(
            name = name,
            primaryParameter = primaryParameter,
            kDoc = kDoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifierSet.immutable(),
            typeVariables = typeVariableRefs.toList(),
            superinterfaces = superinterfaces.toList(),
            properties = properties.toList(),
            initializerBlock = initializerBlock.build(),
            functions = functions.toList(),
            subtypes = emptyList<KotlinTypeSpec>()
        )
    }
}

/**
 * Create a [KotlinValueClassSpec] with the given name and primary parameter.
 *
 * @param name the value class name
 * @param primaryParameter the primary constructor parameter
 * @param block the configuration block
 * @return a new [KotlinValueClassSpec] instance
 */
public inline fun KotlinValueClassSpec(
    name: String,
    primaryParameter: KotlinValueParameterSpec,
    block: KotlinValueClassSpec.Builder.() -> Unit = {}
): KotlinValueClassSpec {
    return KotlinValueClassSpec.builder(name, primaryParameter).apply(block).build()
}
