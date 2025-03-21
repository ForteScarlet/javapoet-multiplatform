package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


internal class EnumTypeSpecImpl(
    override val name: String,
    override val kind: TypeSpec.Kind,
    override val enumConstants: Map<String, AnonymousClassTypeSpec>,
    override val javadoc: CodeValue,
    override val annotations: List<AnnotationSpec>,
    override val modifiers: Set<Modifier>,
    override val typeVariables: List<TypeVariableName>,
    override val superinterfaces: List<TypeName>,
    override val fields: List<FieldSpec>,
    override val staticBlock: CodeValue,
    override val initializerBlock: CodeValue,
    override val methods: List<MethodSpec>,
    override val types: List<TypeSpec>
) : EnumTypeSpec {

    override fun emit(codeWriter: CodeWriter, implicitModifiers: Set<Modifier>) {
        doEmit(codeWriter) {
            codeWriter.pushType(this.toVirtualTypeSpec(name))

            javadoc.emit(codeWriter)
            codeWriter.emitAnnotations(annotations, false)
            codeWriter.emitModifiers(modifiers, implicitModifiers + kind.asMemberModifiers)
            codeWriter.emit("enum %V") {
                literal(name)
            }
            codeWriter.emitTypeVariables(typeVariables)

            // implements
            codeWriter.emitImplements(superinterfaces)

            codeWriter.popType()
            codeWriter.emit(" {\n")

            emitMembers(codeWriter) { firstMember, notFirst ->
                val needsSeparator = fields.isNotEmpty() || methods.isNotEmpty() || types.isNotEmpty()
                val i = enumConstants.entries.iterator()
                while (i.hasNext()) {
                    val enumConstant = i.next()
                    if (!firstMember) {
                        codeWriter.emit("\n")
                    }

                    enumConstant.value.emit(codeWriter, enumConstant.key)
                    notFirst()
                    if (i.hasNext()) {
                        codeWriter.emit(",\n")
                    } else if (!needsSeparator) {
                        codeWriter.emit("\n")
                    }
                }

                if (needsSeparator) {
                    codeWriter.emit(";\n")
                }
            }

            codeWriter.popTypeVariables(typeVariables)
            codeWriter.emit("}\n")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EnumTypeSpec) return false

        if (name != other.name) return false
        if (kind != other.kind) return false
        if (enumConstants != other.enumConstants) return false
        if (javadoc != other.javadoc) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (typeVariables != other.typeVariables) return false
        if (superinterfaces != other.superinterfaces) return false
        if (fields != other.fields) return false
        if (staticBlock != other.staticBlock) return false
        if (initializerBlock != other.initializerBlock) return false
        if (methods != other.methods) return false
        if (types != other.types) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + kind.hashCode()
        result = 31 * result + enumConstants.hashCode()
        result = 31 * result + javadoc.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + typeVariables.hashCode()
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
