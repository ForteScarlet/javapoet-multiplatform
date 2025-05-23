package love.forte.codegentle.java.naming.internal

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.java.naming.JavaClassNames
import love.forte.codegentle.java.naming.JavaPrimitiveTypeName


internal class JavaPrimitiveTypeNameImpl(
    override val keyword: String,
) : JavaPrimitiveTypeName {

    override fun box(): TypeName {
        return when (keyword) {
            JavaPrimitiveTypeName.VOID -> {
                JavaClassNames.BOXED_VOID //.annotated(annotations)
            }

            JavaPrimitiveTypeName.BOOLEAN -> {
                JavaClassNames.BOXED_BOOLEAN
            }

            JavaPrimitiveTypeName.BYTE -> {
                JavaClassNames.BOXED_BYTE
            }

            JavaPrimitiveTypeName.SHORT -> {
                JavaClassNames.BOXED_SHORT
            }

            JavaPrimitiveTypeName.INT -> {
                JavaClassNames.BOXED_INT
            }

            JavaPrimitiveTypeName.LONG -> {
                JavaClassNames.BOXED_LONG
            }

            JavaPrimitiveTypeName.CHAR -> {
                JavaClassNames.BOXED_CHAR
            }

            JavaPrimitiveTypeName.FLOAT -> {
                JavaClassNames.BOXED_FLOAT
            }

            JavaPrimitiveTypeName.DOUBLE -> {
                JavaClassNames.BOXED_DOUBLE
            }

            else -> throw IllegalArgumentException("Unknown primitive type's keyword $keyword")
        }
    }

    override fun toString(): String {
        return keyword
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
