package love.forte.javapoet.internal

import love.forte.javapoet.AnnotationSpec
import love.forte.javapoet.CodeWriter
import love.forte.javapoet.TypeName
import love.forte.javapoet.TypeVariableName


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
        return TypeVariableNameImpl(name, bounds, this.annotations + annotations)
    }

    override fun withoutAnnotations(): TypeVariableName {
        return if (annotations.isEmpty()) this else TypeVariableNameImpl(name, bounds)
    }

    override fun withBounds(bounds: List<TypeName>): TypeVariableName {
        return TypeVariableNameImpl(name, this.bounds + bounds, annotations)
    }

    override fun emit(codeWriter: CodeWriter) {
        TODO("Not yet implemented")
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
        // TODO toString
        return super.toString()
    }
}
