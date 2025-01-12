package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*

internal class SubtypeWildcardTypeNameImpl(
    override val lowerBounds: List<TypeName>,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : SubtypeWildcardTypeName {
    override fun withoutAnnotations(): WildcardTypeName {
        return if (annotations.isEmpty()) this else SubtypeWildcardTypeNameImpl(lowerBounds)
    }

    override fun annotated(annotations: List<AnnotationSpec>): WildcardTypeName {
        if (annotations.isEmpty()) return this

        return SubtypeWildcardTypeNameImpl(lowerBounds, this.annotations + annotations)
    }

    override fun emit(codeWriter: CodeWriter) {
        if (lowerBounds.isNotEmpty()) {
            lowerBounds.forEachIndexed { index, typeName ->
                if (index == 0) {
                    // first
                    codeWriter.emit("? super %V") {
                        type(typeName)
                    }
                } else {
                    codeWriter.emit(" & %V") {
                        type(typeName)
                    }
                }
            }
        }
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
        return emitToString()
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
        if (annotations.isEmpty()) return this

        return SupertypeWildcardTypeNameImpl(upperBounds, this.annotations + annotations)
    }

    override fun emit(codeWriter: CodeWriter) {
        if (upperBounds.isNotEmpty()) {
            var extends = false
            upperBounds.forEachIndexed { index, typeName ->
                if (index == 0) {
                    // first
                    codeWriter.emit("?")
                }

                if (typeName == ClassName.Builtins.OBJECT) {
                    // continue
                    return@forEachIndexed
                }

                if (!extends) {
                    codeWriter.emit(" extends %V") { type(typeName) }
                    extends = true
                } else {
                    codeWriter.emit(" & %V") { type(typeName) }
                }
            }
        }

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
        return emitToString()
    }
}
