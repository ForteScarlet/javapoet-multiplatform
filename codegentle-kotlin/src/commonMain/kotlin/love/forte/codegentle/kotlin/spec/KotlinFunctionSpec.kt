package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueBuilder
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.spec.Spec
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierBuilderContainer
import love.forte.codegentle.kotlin.KotlinModifierContainer
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.internal.KotlinFunctionSpecImpl

/**
 * A Kotlin function。
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinFunctionSpec : Spec, KotlinModifierContainer {
    /**
     * Function name.
     */
    public val name: String
    public val returnType: TypeRef<*>

    override val modifiers: Set<KotlinModifier>
    public val annotations: List<AnnotationRef>
    public val typeVariables: List<TypeRef<TypeVariableName>>
    public val parameters: List<KotlinValueParameterSpec>
    public val receiver: TypeRef<*>?
    public val contextParameters: List<KotlinContextParameterSpec>
    public val kDoc: CodeValue
    public val code: CodeValue

    public companion object {
        /**
         * Create a function builder.
         *
         * @return new [KotlinFunctionSpecBuilder] instance.
         */
        public fun builder(name: String, type: TypeRef<*>): KotlinFunctionSpecBuilder {
            return KotlinFunctionSpecBuilder(name, type)
        }
    }
}

/**
 * Builder for [KotlinFunctionSpec].
 */
public class KotlinFunctionSpecBuilder @PublishedApi internal constructor(
    public val name: String,
    public val returnType: TypeRef<*>
) : BuilderDsl,
    KotlinModifierBuilderContainer,
    AnnotationRefCollectable<KotlinFunctionSpecBuilder> {

    private val kDoc: CodeValueBuilder = CodeValue.builder()
    private val code: CodeValueBuilder = CodeValue.builder()
    private val modifierSet = MutableKotlinModifierSet.empty()
    private val annotations = mutableListOf<AnnotationRef>()
    private val typeVariables = mutableListOf<TypeRef<TypeVariableName>>()
    private val parameters = mutableListOf<KotlinValueParameterSpec>()
    private var receiver: TypeRef<*>? = null
    private val contextParameters = mutableListOf<KotlinContextParameterSpec>()

    override fun addModifier(modifier: KotlinModifier): KotlinFunctionSpecBuilder = apply {
        modifierSet.add(modifier)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): KotlinFunctionSpecBuilder = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): KotlinFunctionSpecBuilder = apply {
        modifierSet.addAll(modifiers)
    }

    override fun addAnnotationRef(ref: AnnotationRef): KotlinFunctionSpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinFunctionSpecBuilder = apply {
        annotations.addAll(refs)
    }

    public fun addKDoc(codeValue: CodeValue): KotlinFunctionSpecBuilder = apply {
        kDoc.add(codeValue)
    }

    public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): KotlinFunctionSpecBuilder = apply {
        kDoc.add(format, *argumentParts)
    }

    public fun addTypeVariable(typeVariable: TypeRef<TypeVariableName>): KotlinFunctionSpecBuilder = apply {
        typeVariables.add(typeVariable)
    }

    public fun addTypeVariables(vararg typeVariables: TypeRef<TypeVariableName>): KotlinFunctionSpecBuilder = apply {
        this.typeVariables.addAll(typeVariables)
    }

    public fun addTypeVariables(typeVariables: Iterable<TypeRef<TypeVariableName>>): KotlinFunctionSpecBuilder = apply {
        this.typeVariables.addAll(typeVariables)
    }

    public fun addParameter(parameter: KotlinValueParameterSpec): KotlinFunctionSpecBuilder = apply {
        parameters.add(parameter)
    }

    public fun addParameters(parameters: Iterable<KotlinValueParameterSpec>): KotlinFunctionSpecBuilder = apply {
        this.parameters.addAll(parameters)
    }

    public fun addParameters(vararg parameters: KotlinValueParameterSpec): KotlinFunctionSpecBuilder = apply {
        this.parameters.addAll(parameters)
    }

    /**
     * Set the receiver type for this function.
     *
     * @param receiverType the receiver type
     * @return this builder
     */
    public fun receiver(receiverType: TypeRef<*>): KotlinFunctionSpecBuilder = apply {
        this.receiver = receiverType
    }

    /**
     * Add a context parameter to this function.
     *
     * @param contextParameter the context parameter
     * @return this builder
     */
    public fun addContextParameter(contextParameter: KotlinContextParameterSpec): KotlinFunctionSpecBuilder = apply {
        contextParameters.add(contextParameter)
    }

    /**
     * Add multiple context parameters to this function.
     *
     * @param contextParameters the context parameters
     * @return this builder
     */
    public fun addContextParameters(contextParameters: Iterable<KotlinContextParameterSpec>): KotlinFunctionSpecBuilder =
        apply {
            this.contextParameters.addAll(contextParameters)
        }

    /**
     * Add multiple context parameters to this function.
     *
     * @param contextParameters the context parameters
     * @return this builder
     */
    public fun addContextParameters(vararg contextParameters: KotlinContextParameterSpec): KotlinFunctionSpecBuilder =
        apply {
            this.contextParameters.addAll(contextParameters)
        }

    public fun addCode(codeValue: CodeValue): KotlinFunctionSpecBuilder = apply {
        code.add(codeValue)
    }

    public fun addCode(format: String, vararg argumentParts: CodeArgumentPart): KotlinFunctionSpecBuilder = apply {
        code.add(format, *argumentParts)
    }

    public fun addStatement(format: String, vararg argumentParts: CodeArgumentPart): KotlinFunctionSpecBuilder = apply {
        code.addStatement(format, *argumentParts)
    }

    public fun addStatement(codeValue: CodeValue): KotlinFunctionSpecBuilder = apply {
        code.addStatement(codeValue)
    }

    /**
     * 构建 [KotlinFunctionSpec] 实例。
     *
     * @return 新的 [KotlinFunctionSpec] 实例
     */
    public fun build(): KotlinFunctionSpec =
        KotlinFunctionSpecImpl(
            name = name,
            returnType = returnType,
            annotations = annotations.toList(),
            modifiers = modifierSet.immutable(),
            typeVariables = typeVariables.toList(),
            parameters = parameters.toList(),
            receiver = receiver,
            contextParameters = contextParameters.toList(),
            kDoc = kDoc.build(),
            code = code.build()
        )
}

/**
 * Create a [KotlinFunctionSpec] with the given name and return type.
 *
 * @param name the function name
 * @param type the return type
 * @param block the configuration block
 * @return a new [KotlinFunctionSpec] instance
 */
public inline fun KotlinFunctionSpec(
    name: String,
    type: TypeRef<*>,
    block: KotlinFunctionSpecBuilder.() -> Unit = {}
): KotlinFunctionSpec =
    KotlinFunctionSpec.builder(name, type).apply(block).build()

/**
 * Add KDoc with a format string and a configuration block.
 *
 * @param format the format string
 * @param block the configuration block
 * @return this builder
 */
public inline fun KotlinFunctionSpecBuilder.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinFunctionSpecBuilder = apply {
    addKDoc(CodeValue(format, block))
}

/**
 * Add code with a format string and a configuration block.
 *
 * @param format the format string
 * @param block the configuration block
 * @return this builder
 */
public inline fun KotlinFunctionSpecBuilder.addCode(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinFunctionSpecBuilder = apply {
    addCode(CodeValue(format, block))
}

/**
 * Add a statement with a format string and a configuration block.
 *
 * @param format the format string
 * @param block the configuration block
 * @return this builder
 */
public inline fun KotlinFunctionSpecBuilder.addStatement(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinFunctionSpecBuilder = apply {
    addStatement(CodeValue(format, block))
}
