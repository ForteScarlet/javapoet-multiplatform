package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*
import java.lang.reflect.*

private val VOID_CLASS = Void::class.javaPrimitiveType!!
private val BOOLEAN_CLASS = Boolean::class.javaPrimitiveType!!
private val BYTE_CLASS = Byte::class.javaPrimitiveType!!
private val SHORT_CLASS = Short::class.javaPrimitiveType!!
private val INT_CLASS = Int::class.javaPrimitiveType!!
private val LONG_CLASS = Long::class.javaPrimitiveType!!
private val CHAR_CLASS = Char::class.javaPrimitiveType!!
private val FLOAT_CLASS = Float::class.javaPrimitiveType!!

@InternalApi
public fun Type.toTypeName(map: MutableMap<Type, TypeVariableName>): TypeName {
    return when (val type = this) {
        is Class<*> -> {
            when (type) {
                VOID_CLASS -> return TypeName.Builtins.VOID
                BOOLEAN_CLASS -> return TypeName.Builtins.BOOLEAN
                BYTE_CLASS -> return TypeName.Builtins.BYTE
                SHORT_CLASS -> return TypeName.Builtins.SHORT
                INT_CLASS -> return TypeName.Builtins.INT
                LONG_CLASS -> return TypeName.Builtins.LONG
                CHAR_CLASS -> return TypeName.Builtins.CHAR
                FLOAT_CLASS -> return TypeName.Builtins.FLOAT
            }

            if (type.isArray) {
                ArrayTypeName(type.componentType.toTypeName(map))
            } else {
                type.toClassName()
            }
        }

        is ParameterizedType -> type.toParameterizedTypeName(map)

        is WildcardType -> {
            val lowerBounds = type.lowerBounds

            if (lowerBounds.isNotEmpty()) {
                SupertypeWildcardTypeName(lowerBounds.map { it.toTypeName() })
            } else {
                SubtypeWildcardTypeName(
                    type.upperBounds.map { it.toTypeName() }.ifEmpty { listOf(ClassName.Builtins.OBJECT) }
                )
            }
        }

        is TypeVariable<*> -> type.toTypeVariableName(map)

        is GenericArrayType -> type.toArrayTypeName(map)

        else -> throw IllegalArgumentException("Unexpected type $type")
    }
}
