package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


/**
 *
 * @author ForteScarlet
 */
internal class ParameterizedTypeNameImpl(
    private val enclosingType: ParameterizedTypeName?,
    override val rawType: ClassName,
    override val typeArguments: List<TypeName> = emptyList(),
    override val annotations: List<AnnotationSpec> = emptyList(),
) : ParameterizedTypeName {
    override fun nestedClass(name: String): ParameterizedTypeName {
        return ParameterizedTypeNameImpl(
            this,
            rawType.nestedClass(name),
        )
    }

    override fun nestedClass(name: String, typeArguments: List<TypeName>): ParameterizedTypeName {
        return ParameterizedTypeNameImpl(
            this,
            rawType.nestedClass(name),
            typeArguments.toList(),
        )
    }

    override fun annotated(annotations: List<AnnotationSpec>): ParameterizedTypeName {
        if (annotations.isEmpty()) return this

        return ParameterizedTypeNameImpl(
            enclosingType, rawType, typeArguments, this.annotations + annotations
        )
    }

    override fun withoutAnnotations(): ParameterizedTypeName {
        return if (annotations.isEmpty()) this
        else ParameterizedTypeNameImpl(enclosingType, rawType, typeArguments)
    }

    override fun emit(codeWriter: CodeWriter) {
        if (enclosingType != null) {
            enclosingType.emit(codeWriter)
            codeWriter.emit(".")
            if (isAnnotated) {
                codeWriter.emit(" ")
                emitAnnotations(codeWriter)
            }
            codeWriter.emit(rawType.simpleName)
        } else {
            rawType.emit(codeWriter)
        }

        if (typeArguments.isNotEmpty()) {
            codeWriter.emitAndIndent("<")
            var firstParameter = true
            for (parameter in typeArguments) {
                if (!firstParameter) codeWriter.emitAndIndent(", ")
                parameter.emit(codeWriter)
                firstParameter = false
            }
            codeWriter.emitAndIndent(">")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ParameterizedTypeNameImpl) return false

        if (enclosingType != other.enclosingType) return false
        if (rawType != other.rawType) return false
        if (typeArguments != other.typeArguments) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = enclosingType?.hashCode() ?: 0
        result = 31 * result + rawType.hashCode()
        result = 31 * result + typeArguments.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
