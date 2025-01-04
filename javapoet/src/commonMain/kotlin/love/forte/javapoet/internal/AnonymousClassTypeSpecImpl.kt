package love.forte.javapoet.internal

import love.forte.javapoet.*


internal class AnonymousClassTypeSpecImpl(
    override val kind: TypeSpec.Kind,
    override val anonymousTypeArguments: CodeBlock,
    override val javadoc: CodeBlock,
    override val annotations: List<AnnotationSpec>,
    override val modifiers: Set<Modifier>,
    override val typeVariables: List<TypeVariableName>,
    override val superclass: TypeName?,
    override val superinterfaces: List<TypeName>,
    override val fields: List<FieldSpec>,
    override val staticBlock: CodeBlock,
    override val initializerBlock: CodeBlock,
    override val methods: List<MethodSpec>,
    override val types: List<TypeSpec>
) : AnonymousClassTypeSpec {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnonymousClassTypeSpec) return false

        if (kind != other.kind) return false
        if (anonymousTypeArguments != other.anonymousTypeArguments) return false
        if (javadoc != other.javadoc) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (typeVariables != other.typeVariables) return false
        if (superclass != other.superclass) return false
        if (superinterfaces != other.superinterfaces) return false
        if (fields != other.fields) return false
        if (staticBlock != other.staticBlock) return false
        if (initializerBlock != other.initializerBlock) return false
        if (methods != other.methods) return false
        if (types != other.types) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kind.hashCode()
        result = 31 * result + anonymousTypeArguments.hashCode()
        result = 31 * result + javadoc.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + typeVariables.hashCode()
        result = 31 * result + (superclass?.hashCode() ?: 0)
        result = 31 * result + superinterfaces.hashCode()
        result = 31 * result + fields.hashCode()
        result = 31 * result + staticBlock.hashCode()
        result = 31 * result + initializerBlock.hashCode()
        result = 31 * result + methods.hashCode()
        result = 31 * result + types.hashCode()
        return result
    }

    override fun toString(): String {
        // TODO toString
        return super.toString()
    }
}
