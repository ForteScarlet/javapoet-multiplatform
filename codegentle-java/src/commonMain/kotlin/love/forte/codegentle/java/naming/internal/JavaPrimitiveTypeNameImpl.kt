package love.forte.codegentle.java.naming.internal

import love.forte.codegentle.java.JavaCodeWriter
import love.forte.codegentle.java.emitToString
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaPrimitiveTypeName
import love.forte.codegentle.java.naming.JavaTypeName


internal class JavaPrimitiveTypeNameImpl(
    override val keyword: String,
    // override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaPrimitiveTypeName {

    override fun box(): JavaTypeName {
        return when (keyword) {
            JavaPrimitiveTypeName.VOID -> {
                JavaClassName.Builtins.BOXED_VOID //.annotated(annotations)
            }

            JavaPrimitiveTypeName.BOOLEAN -> {
                JavaClassName.Builtins.BOXED_BOOLEAN // .annotated(annotations)
            }

            JavaPrimitiveTypeName.BYTE -> {
                JavaClassName.Builtins.BOXED_BYTE // .annotated(annotations)
            }

            JavaPrimitiveTypeName.SHORT -> {
                JavaClassName.Builtins.BOXED_SHORT // .annotated(annotations)
            }

            JavaPrimitiveTypeName.INT -> {
                JavaClassName.Builtins.BOXED_INT // .annotated(annotations)
            }

            JavaPrimitiveTypeName.LONG -> {
                JavaClassName.Builtins.BOXED_LONG // .annotated(annotations)
            }

            JavaPrimitiveTypeName.CHAR -> {
                JavaClassName.Builtins.BOXED_CHAR // .annotated(annotations)
            }

            JavaPrimitiveTypeName.FLOAT -> {
                JavaClassName.Builtins.BOXED_FLOAT // .annotated(annotations)
            }

            JavaPrimitiveTypeName.DOUBLE -> {
                JavaClassName.Builtins.BOXED_DOUBLE // .annotated(annotations)
            }

            else -> throw IllegalArgumentException("Unknown primitive type's keyword $keyword")
        }
    }

    override fun unbox(): JavaTypeName {
        return this
    }

    override fun emit(codeWriter: JavaCodeWriter) {
        // if (isAnnotated) {
        //     codeWriter.emit("")
        //     emitAnnotations(codeWriter)
        // }

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
