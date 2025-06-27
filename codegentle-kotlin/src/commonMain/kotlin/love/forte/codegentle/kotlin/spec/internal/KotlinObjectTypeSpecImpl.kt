package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.MutableKotlinModifierSet
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinObjectTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec

/**
 * Implementation of [KotlinObjectTypeSpec].
 *
 * @author ForteScarlet
 */
internal data class KotlinObjectTypeSpecImpl(
    override val name: String,
    override val kind: KotlinTypeSpec.Kind,
    override val kDoc: CodeValue,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<KotlinModifier>,
    override val typeVariables: List<TypeRef<TypeVariableName>>,
    override val superinterfaces: List<TypeName>,
    override val properties: List<KotlinPropertySpec>,
    override val initializerBlock: CodeValue,
    override val functions: List<KotlinFunctionSpec>,
    override val subtypes: List<KotlinTypeSpec>
) : KotlinObjectTypeSpec {
    override val superclass: TypeName? = null

    override fun toString(): String {
        return "KotlinObjectTypeSpec(name='$name', kind=$kind)"
    }
}

/**
 * Builder implementation for [KotlinObjectTypeSpec].
 *
 * @author ForteScarlet
 */
internal class KotlinObjectTypeSpecBuilderImpl(
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
