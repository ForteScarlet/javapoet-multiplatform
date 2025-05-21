package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.java.naming.internal.JavaPrimitiveTypeNameImpl

public object JavaPrimitiveTypeNames {
    public val VOID: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.VOID)
    public val BOOLEAN: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.BOOLEAN)
    public val BYTE: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.BYTE)
    public val SHORT: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.SHORT)
    public val INT: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.INT)
    public val LONG: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.LONG)
    public val CHAR: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.CHAR)
    public val FLOAT: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.FLOAT)
    public val DOUBLE: TypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.DOUBLE)
}
