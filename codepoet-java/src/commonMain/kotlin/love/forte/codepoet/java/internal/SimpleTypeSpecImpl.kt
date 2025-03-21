package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*

internal class SimpleTypeSpecImpl(
    override val name: String,
    override val kind: TypeSpec.Kind,
    override val javadoc: CodeValue,
    override val annotations: List<AnnotationSpec>,
    override val modifiers: Set<Modifier>,
    override val typeVariables: List<TypeVariableName>,
    override val superclass: TypeName?,
    override val superinterfaces: List<TypeName>,
    override val fields: List<FieldSpec>,
    override val staticBlock: CodeValue,
    override val initializerBlock: CodeValue,
    override val methods: List<MethodSpec>,
    override val types: List<TypeSpec>
) : SimpleTypeSpec {

    override fun emit(codeWriter: CodeWriter, implicitModifiers: Set<Modifier>) {
        doEmit(codeWriter) {
            // Push an empty type (specifically without nested types) for type-resolution.
            codeWriter.pushType(this.toVirtualTypeSpec(name))
            codeWriter.emitJavadoc(javadoc)
            codeWriter.emitAnnotations(annotations, false)
            codeWriter.emitModifiers(modifiers, implicitModifiers + kind.asMemberModifiers)
            codeWriter.emit("${kind.name.lowercase()} $name")
            codeWriter.emitTypeVariables(typeVariables)

            emitSupers(codeWriter)

            codeWriter.popType()
            codeWriter.emit(" {\n")

            emitMembers(codeWriter)

            codeWriter.popTypeVariables(typeVariables)

            codeWriter.emit("}\n")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SimpleTypeSpec) return false

        if (name != other.name) return false
        if (kind != other.kind) return false
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
        var result = name.hashCode()
        result = 31 * result + kind.hashCode()
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
        return emitToString()
    }
}
