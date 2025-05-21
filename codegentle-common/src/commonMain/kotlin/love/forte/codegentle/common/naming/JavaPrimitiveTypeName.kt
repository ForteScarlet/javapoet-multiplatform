package love.forte.codegentle.common.naming

/**
 * A Java primitive type name.
 */
@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface JavaPrimitiveTypeName : TypeName {
    public val keyword: String
    public fun box(): TypeName

    public companion object {
        internal const val VOID: String = "void"
        internal const val BOOLEAN: String = "boolean"
        internal const val BYTE: String = "byte"
        internal const val SHORT: String = "short"
        internal const val INT: String = "int"
        internal const val LONG: String = "long"
        internal const val CHAR: String = "char"
        internal const val FLOAT: String = "float"
        internal const val DOUBLE: String = "double"
    }
}

public fun TypeName.unbox(): JavaPrimitiveTypeName {
    return when (this) {
        is JavaPrimitiveTypeName -> this
        // TODO Integer -> int
        is ClassName -> {


            TODO()
        }
        else -> throw UnsupportedOperationException("Can't unbox $this of type ${this::class}")
    }
}
