package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


internal class ParameterSpecImpl internal constructor(
    override val type: TypeName,
    override val name: String,
    override val annotations: List<AnnotationSpec>,
    override val modifiers: Set<Modifier>,
    override val javadoc: CodeValue,
) : ParameterSpec {
    override fun toBuilder(): ParameterSpec.Builder {
        return ParameterSpec.builder(type, name).also { builder ->
            builder.annotations.addAll(annotations)
            builder.modifiers.addAll(modifiers)
            builder.javadoc.add(javadoc)
        }
    }

    override fun emit(codeWriter: CodeWriter, vararg: Boolean) {
        codeWriter.emitAnnotations(annotations, true)
        codeWriter.emitModifiers(modifiers)

        if (vararg) {
            (type as ArrayTypeName).emit(codeWriter, true)
        } else {
            type.emit(codeWriter)
        }

        codeWriter.emit(" $name")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParameterSpec) return false

        if (type != other.type) return false
        if (name != other.name) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (javadoc != other.javadoc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + javadoc.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
