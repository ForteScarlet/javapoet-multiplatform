package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


/**
 *
 * @author ForteScarlet
 */
internal class TypeVariableNameImpl(
    override val name: String,
    override val bounds: List<TypeName> = emptyList(),
    override val annotations: List<AnnotationSpec> = emptyList()
) : TypeVariableName {
    override fun annotated(annotations: List<AnnotationSpec>): TypeVariableName {
        if (annotations.isEmpty()) return this

        return TypeVariableNameImpl(name, bounds, this.annotations + annotations)
    }

    override fun withoutAnnotations(): TypeVariableName {
        return if (annotations.isEmpty()) this else TypeVariableNameImpl(name, bounds)
    }

    override fun withBounds(bounds: List<TypeName>): TypeVariableName {
        return TypeVariableNameImpl(name, this.bounds + bounds, annotations)
    }

    override fun emit(codeWriter: CodeWriter) {
        emitAnnotations(codeWriter)
        codeWriter.emitAndIndent(name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TypeVariableName) return false

        if (name != other.name) return false
        if (bounds != other.bounds) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bounds.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
