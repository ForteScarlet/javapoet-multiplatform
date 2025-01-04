package love.forte.javapoet.internal

import love.forte.javapoet.AnnotationSpec
import love.forte.javapoet.ArrayTypeName
import love.forte.javapoet.TypeName


/**
 *
 * @author ForteScarlet
 */
internal class ArrayTypeNameImpl(
    override val componentType: TypeName,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : ArrayTypeName {
    override fun annotated(annotations: List<AnnotationSpec>): ArrayTypeName {
        return ArrayTypeNameImpl(componentType, this.annotations + annotations)
    }

    override fun withoutAnnotations(): ArrayTypeName {
        return if (annotations.isEmpty()) this else ArrayTypeNameImpl(componentType)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArrayTypeName) return false

        if (componentType != other.componentType) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = componentType.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
    }

    override fun toString(): String {
        // TODO toString
        return super.toString()
    }
}
