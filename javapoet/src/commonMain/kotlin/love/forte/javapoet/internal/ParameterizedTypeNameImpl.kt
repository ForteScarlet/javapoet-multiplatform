package love.forte.javapoet.internal

import love.forte.javapoet.AnnotationSpec
import love.forte.javapoet.ClassName
import love.forte.javapoet.ParameterizedTypeName
import love.forte.javapoet.TypeName


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
        return ParameterizedTypeNameImpl(
            enclosingType, rawType, typeArguments, this.annotations + annotations
        )
    }

    override fun withoutAnnotations(): ParameterizedTypeName {
        return if (annotations.isEmpty()) this
        else ParameterizedTypeNameImpl(enclosingType, rawType, typeArguments)
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
        // TODO toString
        return super.toString()
    }
}
