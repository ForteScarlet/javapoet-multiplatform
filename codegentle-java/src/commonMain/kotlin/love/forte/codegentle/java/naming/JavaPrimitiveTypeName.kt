package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.CodeGentleNamingImplementation
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.java.writer.JavaCodeWriter

@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface JavaPrimitiveTypeName : TypeName {
    public val keyword: String

    public fun box(): TypeName


    public companion object {
        internal const val VOID = "void"
        internal const val BOOLEAN = "boolean"
        internal const val BYTE = "byte"
        internal const val SHORT = "short"
        internal const val INT = "int"
        internal const val LONG = "long"
        internal const val CHAR = "char"
        internal const val FLOAT = "float"
        internal const val DOUBLE = "double"
    }
}

internal fun JavaPrimitiveTypeName.emitTo(codeWriter: JavaCodeWriter) {
    codeWriter.emit(keyword)
}
