package love.forte.codegentle.java.naming

import love.forte.codegentle.java.naming.internal.JavaPrimitiveTypeNameImpl

public object JavaPrimitiveTypeNames {
    public val VOID: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.VOID)
    public val BOOLEAN: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.BOOLEAN)
    public val BYTE: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.BYTE)
    public val SHORT: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.SHORT)
    public val INT: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.INT)
    public val LONG: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.LONG)
    public val CHAR: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.CHAR)
    public val FLOAT: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.FLOAT)
    public val DOUBLE: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.DOUBLE)
}
