package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


internal class PrimitiveTypeNameImpl(
    override val keyword: String,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : PrimitiveTypeName {

    override fun annotated(annotations: List<AnnotationSpec>): TypeName {
        if (annotations.isEmpty()) return this

        return PrimitiveTypeNameImpl(keyword, this.annotations + annotations)
    }

    override fun withoutAnnotations(): TypeName {
        return if (annotations.isEmpty()) this else PrimitiveTypeNameImpl(keyword)
    }

    override val isPrimitive: Boolean
        get() = this != TypeName.Builtins.VOID

    override fun box(): TypeName {
        return when (keyword) {
            PrimitiveTypeName.VOID -> {
                ClassName.Builtins.BOXED_VOID.annotated(annotations)
            }

            PrimitiveTypeName.BOOLEAN -> {
                ClassName.Builtins.BOXED_BOOLEAN.annotated(annotations)
            }

            PrimitiveTypeName.BYTE -> {
                ClassName.Builtins.BOXED_BYTE.annotated(annotations)
            }

            PrimitiveTypeName.SHORT -> {
                ClassName.Builtins.BOXED_SHORT.annotated(annotations)
            }

            PrimitiveTypeName.INT -> {
                ClassName.Builtins.BOXED_INT.annotated(annotations)
            }

            PrimitiveTypeName.LONG -> {
                ClassName.Builtins.BOXED_LONG.annotated(annotations)
            }

            PrimitiveTypeName.CHAR -> {
                ClassName.Builtins.BOXED_CHAR.annotated(annotations)
            }

            PrimitiveTypeName.FLOAT -> {
                ClassName.Builtins.BOXED_FLOAT.annotated(annotations)
            }

            PrimitiveTypeName.DOUBLE -> {
                ClassName.Builtins.BOXED_DOUBLE.annotated(annotations)
            }

            else -> throw IllegalArgumentException("Unknown primitive type's keyword $keyword")
        }
    }

    override fun unbox(): TypeName {
        return this
    }

    override fun emit(codeWriter: CodeWriter) {
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

internal fun TypeName.emitAnnotations(codeWriter: CodeWriter) {
    for (annotation in annotations) {
        annotation.emit(codeWriter, true)
        codeWriter.emit(" ")
    }
}
