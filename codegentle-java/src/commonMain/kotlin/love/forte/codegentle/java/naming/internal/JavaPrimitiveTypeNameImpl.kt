package love.forte.codegentle.java.naming.internal

import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.naming.JavaPrimitiveTypeName
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emitToString


internal class JavaPrimitiveTypeNameImpl(
    override val keyword: String,
    // override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaPrimitiveTypeName {

    override fun box(): JavaTypeName {
        return when (keyword) {
            JavaPrimitiveTypeName.VOID -> {
                JavaClassNames.BOXED_VOID //.annotated(annotations)
            }

            JavaPrimitiveTypeName.BOOLEAN -> {
                JavaClassNames.BOXED_BOOLEAN // .annotated(annotations)
            }

            JavaPrimitiveTypeName.BYTE -> {
                JavaClassNames.BOXED_BYTE // .annotated(annotations)
            }

            JavaPrimitiveTypeName.SHORT -> {
                JavaClassNames.BOXED_SHORT // .annotated(annotations)
            }

            JavaPrimitiveTypeName.INT -> {
                JavaClassNames.BOXED_INT // .annotated(annotations)
            }

            JavaPrimitiveTypeName.LONG -> {
                JavaClassNames.BOXED_LONG // .annotated(annotations)
            }

            JavaPrimitiveTypeName.CHAR -> {
                JavaClassNames.BOXED_CHAR // .annotated(annotations)
            }

            JavaPrimitiveTypeName.FLOAT -> {
                JavaClassNames.BOXED_FLOAT // .annotated(annotations)
            }

            JavaPrimitiveTypeName.DOUBLE -> {
                JavaClassNames.BOXED_DOUBLE // .annotated(annotations)
            }

            else -> throw IllegalArgumentException("Unknown primitive type's keyword $keyword")
        }
    }

    override fun unbox(): JavaTypeName {
        return this
    }

    override fun emit(codeWriter: JavaCodeWriter) {
        codeWriter.emitAndIndent(keyword)
    }

    override fun toString(): String {
        return emitToString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaPrimitiveTypeNameImpl) return false

        if (keyword != other.keyword) return false

        return true
    }

    override fun hashCode(): Int {
        return keyword.hashCode()
    }
}
