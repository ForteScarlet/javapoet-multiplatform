package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.internal.KotlinAnonymousClassTypeSpecBuilderImpl

/**
 * A Kotlin anonymous class type specification.
 * Can be used as an implementation body for enum constants.
 *
 * ```kotlin
 * object : SuperType {
 *     // implementations
 * }
 * ```
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinAnonymousClassTypeSpec : KotlinTypeSpec {
    /**
     * Anonymous class has no name.
     */
    override val name: String
        get() = ""

    /**
     * Anonymous class has no specific kind.
     */
    override val kind: KotlinTypeSpec.Kind
        get() = KotlinTypeSpec.Kind.CLASS

    /**
     * Constructors for the anonymous class.
     */
    public val constructors: List<KotlinConstructorSpec>

    public companion object {
        /**
         * Create a builder for an anonymous class.
         *
         * @return a new builder
         */
        public fun builder(): Builder {
            return KotlinAnonymousClassTypeSpecBuilderImpl()
        }
    }

    /**
     * Builder for [KotlinAnonymousClassTypeSpec].
     */
    public interface Builder {
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
         * Set superclass.
         */
        public fun superclass(superclass: TypeName): Builder

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
         * Add constructor.
         */
        public fun addConstructor(constructor: KotlinConstructorSpec): Builder

        /**
         * Add constructors.
         */
        public fun addConstructors(constructors: Iterable<KotlinConstructorSpec>): Builder

        /**
         * Add constructors.
         */
        public fun addConstructors(vararg constructors: KotlinConstructorSpec): Builder

        /**
         * Build [KotlinAnonymousClassTypeSpec] instance.
         */
        public fun build(): KotlinAnonymousClassTypeSpec
    }
}

/**
 * Create a [KotlinAnonymousClassTypeSpec] with the given configuration.
 *
 * @param block the configuration block
 * @return a new [KotlinAnonymousClassTypeSpec] instance
 */
public inline fun KotlinAnonymousClassTypeSpec(
    block: KotlinAnonymousClassTypeSpec.Builder.() -> Unit = {}
): KotlinAnonymousClassTypeSpec {
    return KotlinAnonymousClassTypeSpec.builder().apply(block).build()
}

public inline fun KotlinAnonymousClassTypeSpec.Builder.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinAnonymousClassTypeSpec.Builder = addKDoc(CodeValue(format, block))

public inline fun KotlinAnonymousClassTypeSpec.Builder.addInitializerBlock(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinAnonymousClassTypeSpec.Builder = addInitializerBlock(CodeValue(format, block))

public inline fun KotlinAnonymousClassTypeSpec.Builder.addProperty(
    name: String,
    type: TypeRef<*>,
    block: KotlinPropertySpec.Builder.() -> Unit = {}
): KotlinAnonymousClassTypeSpec.Builder = addProperty(KotlinPropertySpec(name, type, block))

public inline fun KotlinAnonymousClassTypeSpec.Builder.addFunction(
    name: String,
    type: TypeRef<*>,
    block: KotlinFunctionSpec.Builder.() -> Unit = {}
): KotlinAnonymousClassTypeSpec.Builder = addFunction(KotlinFunctionSpec(name, type, block))

public inline fun KotlinAnonymousClassTypeSpec.Builder.addConstructor(
    block: KotlinConstructorSpec.Builder.() -> Unit = {}
): KotlinAnonymousClassTypeSpec.Builder = addConstructor(KotlinConstructorSpec.builder().apply(block).build())
