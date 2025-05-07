package love.forte.codepoet.java.naming.internal

import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.naming.PrimitiveTypeName
import love.forte.codepoet.java.spec.JavaAnnotationSpec


internal class PrimitiveTypeNameImpl(
    override val keyword: String,
    override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : PrimitiveTypeName {

    override fun annotated(annotations: List<JavaAnnotationSpec>): JavaTypeName {
        if (annotations.isEmpty()) return this

        return PrimitiveTypeNameImpl(keyword, this.annotations + annotations)
    }

    override fun withoutAnnotations(): JavaTypeName {
        return if (annotations.isEmpty()) this else PrimitiveTypeNameImpl(keyword)
    }

    override val isPrimitive: Boolean
        get() = this != JavaTypeName.Builtins.VOID

    override fun box(): JavaTypeName {
        return when (keyword) {
            PrimitiveTypeName.VOID -> {
                JavaClassName.Builtins.BOXED_VOID.annotated(annotations)
            }

            PrimitiveTypeName.BOOLEAN -> {
                JavaClassName.Builtins.BOXED_BOOLEAN.annotated(annotations)
            }

            PrimitiveTypeName.BYTE -> {
                JavaClassName.Builtins.BOXED_BYTE.annotated(annotations)
            }

            PrimitiveTypeName.SHORT -> {
                JavaClassName.Builtins.BOXED_SHORT.annotated(annotations)
            }

            PrimitiveTypeName.INT -> {
                JavaClassName.Builtins.BOXED_INT.annotated(annotations)
            }

            PrimitiveTypeName.LONG -> {
                JavaClassName.Builtins.BOXED_LONG.annotated(annotations)
            }

            PrimitiveTypeName.CHAR -> {
                JavaClassName.Builtins.BOXED_CHAR.annotated(annotations)
            }

            PrimitiveTypeName.FLOAT -> {
                JavaClassName.Builtins.BOXED_FLOAT.annotated(annotations)
            }

            PrimitiveTypeName.DOUBLE -> {
                JavaClassName.Builtins.BOXED_DOUBLE.annotated(annotations)
            }

            else -> throw IllegalArgumentException("Unknown primitive type's keyword $keyword")
        }
    }

    override fun unbox(): JavaTypeName {
        return this
    }

    override fun emit(codeWriter: JavaCodeWriter) {
        if (isAnnotated) {
            codeWriter.emit("")
            emitAnnotations(codeWriter)
        }

        codeWriter.emitAndIndent(keyword)
    }

    override fun toString(): String {
        return emitToString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is PrimitiveTypeName) return false

        if (keyword != other.keyword) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyword.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
    }
}

internal fun JavaTypeName.emitAnnotations(codeWriter: JavaCodeWriter) {
    for (annotation in annotations) {
        annotation.emit(codeWriter, true)
        codeWriter.emit(" ")
    }
}
