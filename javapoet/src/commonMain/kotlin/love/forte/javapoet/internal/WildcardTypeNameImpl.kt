package love.forte.javapoet.internal

import love.forte.javapoet.*

internal class SubtypeWildcardTypeNameImpl(
    override val lowerBounds: List<TypeName>,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : SubtypeWildcardTypeName {
    override fun withoutAnnotations(): WildcardTypeName {
        return if (annotations.isEmpty()) this else SubtypeWildcardTypeNameImpl(lowerBounds)
    }

    override fun annotated(annotations: List<AnnotationSpec>): WildcardTypeName {
        return SubtypeWildcardTypeNameImpl(lowerBounds, this.annotations + annotations)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SubtypeWildcardTypeName) return false

        if (lowerBounds != other.lowerBounds) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = lowerBounds.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
    }

    override fun toString(): String {
        // TODO toString
        return super.toString()
    }
}

internal class SupertypeWildcardTypeNameImpl(
    override val upperBounds: List<TypeName>,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : SupertypeWildcardTypeName {
    override fun withoutAnnotations(): WildcardTypeName {
        return if (annotations.isEmpty()) this else SupertypeWildcardTypeNameImpl(upperBounds)
    }

    override fun annotated(annotations: List<AnnotationSpec>): WildcardTypeName {
        return SupertypeWildcardTypeNameImpl(upperBounds, this.annotations + annotations)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is SupertypeWildcardTypeName) return false

        if (upperBounds != other.upperBounds) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = upperBounds.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
    }

    override fun toString(): String {
        // TODO toString
        return super.toString()
    }
}
