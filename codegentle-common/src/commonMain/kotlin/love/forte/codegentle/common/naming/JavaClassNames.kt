package love.forte.codegentle.common.naming

public object JavaClassNames {

    internal const val JAVA_LANG_PACKAGE = "java.lang"

    public val OBJECT: ClassName = ClassName(JAVA_LANG_PACKAGE, "Object")

    public val STRING: ClassName = ClassName(JAVA_LANG_PACKAGE, "String")

    // primitives

    internal const val BOXED_VOID_SIMPLE_NAME = "Void"

    public val BOXED_VOID: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_VOID_SIMPLE_NAME)

    internal const val BOXED_BOOLEAN_SIMPLE_NAME = "Boolean"

    public val BOXED_BOOLEAN: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_BOOLEAN_SIMPLE_NAME)

    internal const val BOXED_BYTE_SIMPLE_NAME = "Byte"

    public val BOXED_BYTE: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_BYTE_SIMPLE_NAME)

    internal const val BOXED_SHORT_SIMPLE_NAME = "Short"

    public val BOXED_SHORT: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_SHORT_SIMPLE_NAME)

    internal const val BOXED_INT_SIMPLE_NAME = "Integer"

    public val BOXED_INT: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_INT_SIMPLE_NAME)

    internal const val BOXED_LONG_SIMPLE_NAME = "Long"

    public val BOXED_LONG: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_LONG_SIMPLE_NAME)

    internal const val BOXED_CHAR_SIMPLE_NAME = "Character"

    public val BOXED_CHAR: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_CHAR_SIMPLE_NAME)

    internal const val BOXED_FLOAT_SIMPLE_NAME = "Float"

    public val BOXED_FLOAT: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_FLOAT_SIMPLE_NAME)

    internal const val BOXED_DOUBLE_SIMPLE_NAME = "Double"

    public val BOXED_DOUBLE: ClassName = ClassName(JAVA_LANG_PACKAGE, BOXED_DOUBLE_SIMPLE_NAME)


}
