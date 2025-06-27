package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.internal.KotlinObjectTypeSpecImpl

/**
 * A generated Kotlin object.
 *
 * ```kotlin
 * object MyObject {
 * }
 * ```
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinObjectTypeSpec : KotlinTypeSpec {
    override val name: String

    override val kind: KotlinTypeSpec.Kind
        get() = KotlinTypeSpec.Kind.OBJECT

    /**
     * Whether this is a companion object.
     */
    public val isCompanion: Boolean
        get() = kind == KotlinTypeSpec.Kind.COMPANION_OBJECT

    public companion object {
        public const val DEFAULT_COMPANION_NAME: String = "Companion"

        /**
         * Create a builder for an object.
         *
         * @param name the object name
         * @return a new builder
         */
        public fun builder(name: String): Builder {
            return KotlinObjectTypeSpecBuilder(name, false)
        }

        /**
         * Create a builder for a companion object.
         *
         * @return a new builder
         */
        public fun companionBuilder(name: String = DEFAULT_COMPANION_NAME): Builder {
            return KotlinObjectTypeSpecBuilder(name, true)
        }
    }

    /**
     * Builder for [KotlinObjectTypeSpec].
     */
    public interface Builder {
        /**
         * The object name.
         */
        public val name: String

        /**
         * Whether this is a companion object.
         */
        public val isCompanion: Boolean

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
        public fun addSuperinterfaces(vararg superinterfaces: TypeName): Builder = apply {
            addSuperinterfaces(superinterfaces.asList())
        }

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
        public fun addProperties(vararg properties: KotlinPropertySpec): Builder = apply {
            addProperties(properties.asList())
        }

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
        public fun addFunctions(vararg functions: KotlinFunctionSpec): Builder = apply {
            addFunctions(functions.asList())
        }

        /**
         * Add function.
         */
        public fun addFunction(function: KotlinFunctionSpec): Builder

        /**
         * Add subtype.
         */
        public fun addSubtype(subtype: KotlinTypeSpec): Builder

        /**
         * Add subtypes.
         */
        public fun addSubtypes(subtypes: Iterable<KotlinTypeSpec>): Builder

        /**
         * Add subtypes.
         */
        public fun addSubtypes(vararg subtypes: KotlinTypeSpec): Builder = apply {
            addSubtypes(subtypes.asList())
        }

        /**
         * Build [KotlinObjectTypeSpec] instance.
         */
        public fun build(): KotlinObjectTypeSpec
    }
}

/**
 * Builder implementation for [KotlinObjectTypeSpec].
 */
private class KotlinObjectTypeSpecBuilder(
    override val name: String,
    override val isCompanion: Boolean
) : KotlinObjectTypeSpec.Builder {
    private val kDoc = CodeValue.builder()
    private val initializerBlock = CodeValue.builder()

    private val annotationRefs: MutableList<AnnotationRef> = mutableListOf()
    private val modifierSet = MutableKotlinModifierSet.empty()
    private val typeVariableRefs: MutableList<TypeRef<TypeVariableName>> = mutableListOf()
    private val superinterfaces: MutableList<TypeName> = mutableListOf()
    private val properties: MutableList<KotlinPropertySpec> = mutableListOf()
    private val functions: MutableList<KotlinFunctionSpec> = mutableListOf()
    private val subtypes: MutableList<KotlinTypeSpec> = mutableListOf()

    override fun addKDoc(codeValue: CodeValue): KotlinObjectTypeSpec.Builder = apply {
        kDoc.add(codeValue)
    }

    override fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinObjectTypeSpec.Builder = apply {
        kDoc.add(format, *argumentParts)
    }

    override fun addInitializerBlock(codeValue: CodeValue): KotlinObjectTypeSpec.Builder = apply {
        this.initializerBlock.add(codeValue)
    }

    override fun addInitializerBlock(
        format: String,
        vararg argumentParts: CodeArgumentPart
    ): KotlinObjectTypeSpec.Builder = apply {
        this.initializerBlock.add(format, *argumentParts)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinObjectTypeSpec.Builder = apply {
        annotationRefs.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinObjectTypeSpec.Builder = apply {
        annotationRefs.addAll(refs)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinObjectTypeSpec.Builder = apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinObjectTypeSpec.Builder = apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifier(modifier: KotlinModifier): KotlinObjectTypeSpec.Builder = apply {
        this.modifierSet.add(modifier)
    }

    override fun addTypeVariableRefs(vararg typeVariables: TypeRef<TypeVariableName>): KotlinObjectTypeSpec.Builder =
        apply {
            this.typeVariableRefs.addAll(typeVariables)
        }

    override fun addTypeVariableRefs(typeVariables: Iterable<TypeRef<TypeVariableName>>): KotlinObjectTypeSpec.Builder =
        apply {
            this.typeVariableRefs.addAll(typeVariables)
        }

    override fun addTypeVariableRef(typeVariable: TypeRef<TypeVariableName>): KotlinObjectTypeSpec.Builder = apply {
        this.typeVariableRefs.add(typeVariable)
    }

    override fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): KotlinObjectTypeSpec.Builder = apply {
        this.superinterfaces.addAll(superinterfaces)
    }

    override fun addSuperinterface(superinterface: TypeName): KotlinObjectTypeSpec.Builder = apply {
        this.superinterfaces.add(superinterface)
    }

    override fun addProperties(properties: Iterable<KotlinPropertySpec>): KotlinObjectTypeSpec.Builder = apply {
        this.properties.addAll(properties)
    }

    override fun addProperty(property: KotlinPropertySpec): KotlinObjectTypeSpec.Builder = apply {
        this.properties.add(property)
    }

    override fun addFunctions(functions: Iterable<KotlinFunctionSpec>): KotlinObjectTypeSpec.Builder = apply {
        this.functions.addAll(functions)
    }

    override fun addFunction(function: KotlinFunctionSpec): KotlinObjectTypeSpec.Builder = apply {
        this.functions.add(function)
    }

    override fun addSubtype(subtype: KotlinTypeSpec): KotlinObjectTypeSpec.Builder = apply {
        subtypes.add(subtype)
    }

    override fun addSubtypes(subtypes: Iterable<KotlinTypeSpec>): KotlinObjectTypeSpec.Builder = apply {
        this.subtypes.addAll(subtypes)
    }

    override fun build(): KotlinObjectTypeSpec {
        val kind = if (isCompanion) KotlinTypeSpec.Kind.COMPANION_OBJECT else KotlinTypeSpec.Kind.OBJECT
        return KotlinObjectTypeSpecImpl(
            name = name,
            kind = kind,
            kDoc = kDoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifierSet.immutable(),
            typeVariables = typeVariableRefs.toList(),
            superinterfaces = superinterfaces.toList(),
            properties = properties.toList(),
            initializerBlock = initializerBlock.build(),
            functions = functions.toList(),
            subtypes = subtypes.toList()
        )
    }
}

/**
 * Create a [KotlinObjectTypeSpec] with the given name.
 *
 * @param name the object name
 * @param block the configuration block
 * @return a new [KotlinObjectTypeSpec] instance
 */
public inline fun KotlinObjectTypeSpec(
    name: String,
    isCompanion: Boolean = false,
    block: KotlinObjectTypeSpec.Builder.() -> Unit = {}
): KotlinObjectTypeSpec {
    return if (isCompanion) {
        KotlinObjectTypeSpec.companionBuilder(name).apply(block).build()
    } else {
        KotlinObjectTypeSpec.builder(name).apply(block).build()
    }
}

/**
 * Create a companion [KotlinObjectTypeSpec].
 *
 * @param block the configuration block
 * @return a new companion [KotlinObjectTypeSpec] instance
 */
public inline fun KotlinObjectTypeSpec(
    block: KotlinObjectTypeSpec.Builder.() -> Unit = {}
): KotlinObjectTypeSpec {
    return KotlinObjectTypeSpec.companionBuilder().apply(block).build()
}
