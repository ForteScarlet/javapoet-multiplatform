package love.forte.codepoet.java.naming.internal

import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emit
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.*
import love.forte.codepoet.java.spec.JavaAnnotationSpec
import love.forte.codepoet.java.type

internal class JavaSubtypeWildcardTypeNameImpl(
    override val lowerBounds: List<JavaTypeName>,
    override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaSubtypeWildcardTypeName {
    override fun withoutAnnotations(): JavaWildcardTypeName {
        return if (annotations.isEmpty()) this else JavaSubtypeWildcardTypeNameImpl(lowerBounds)
    }

    override fun annotated(annotations: List<JavaAnnotationSpec>): JavaWildcardTypeName {
        if (annotations.isEmpty()) return this

        return JavaSubtypeWildcardTypeNameImpl(lowerBounds, this.annotations + annotations)
    }

    override fun emit(codeWriter: JavaCodeWriter) {
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
        if (other !is JavaSubtypeWildcardTypeName) return false

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

internal class JavaSupertypeWildcardTypeNameImpl(
    override val upperBounds: List<JavaTypeName>,
    override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaSupertypeWildcardTypeName {
    override fun withoutAnnotations(): JavaWildcardTypeName {
        return if (annotations.isEmpty()) this else JavaSupertypeWildcardTypeNameImpl(upperBounds)
    }

    override fun annotated(annotations: List<JavaAnnotationSpec>): JavaWildcardTypeName {
        if (annotations.isEmpty()) return this

        return JavaSupertypeWildcardTypeNameImpl(upperBounds, this.annotations + annotations)
    }

    override fun emit(codeWriter: JavaCodeWriter) {
        if (upperBounds.isNotEmpty()) {
            var extends = false
            upperBounds.forEachIndexed { index, typeName ->
                if (index == 0) {
                    // first
                    codeWriter.emit("?")
                }

                if (typeName == JavaClassName.Builtins.OBJECT) {
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
        if (other !is JavaSupertypeWildcardTypeName) return false

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
