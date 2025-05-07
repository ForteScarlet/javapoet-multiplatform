package love.forte.codepoet.java.naming.internal

import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaParameterizedTypeName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.JavaAnnotationSpec


/**
 *
 * @author ForteScarlet
 */
internal class JavaParameterizedTypeNameImpl(
    private val enclosingType: JavaParameterizedTypeName?,
    override val rawType: JavaClassName,
    override val typeArguments: List<JavaTypeName> = emptyList(),
    override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaParameterizedTypeName {
    override fun nestedClass(name: String): JavaParameterizedTypeName {
        return JavaParameterizedTypeNameImpl(
            this,
            rawType.nestedClass(name),
        )
    }

    override fun nestedClass(name: String, typeArguments: List<JavaTypeName>): JavaParameterizedTypeName {
        return JavaParameterizedTypeNameImpl(
            this,
            rawType.nestedClass(name),
            typeArguments.toList(),
        )
    }

    override fun annotated(annotations: List<JavaAnnotationSpec>): JavaParameterizedTypeName {
        if (annotations.isEmpty()) return this

        return JavaParameterizedTypeNameImpl(
            enclosingType, rawType, typeArguments, this.annotations + annotations
        )
    }

    override fun withoutAnnotations(): JavaParameterizedTypeName {
        return if (annotations.isEmpty()) this
        else JavaParameterizedTypeNameImpl(enclosingType, rawType, typeArguments)
    }

    override fun emit(codeWriter: JavaCodeWriter) {
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
        if (other !is JavaParameterizedTypeNameImpl) return false

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
