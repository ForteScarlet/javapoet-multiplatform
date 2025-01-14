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
public fun TypeName(type: Type, map: MutableMap<Type, TypeVariableName>): TypeName {
    return when (type) {
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
                ArrayTypeName(TypeName(type.componentType, map))
            } else {
                ClassName(type)
            }
        }

        is ParameterizedType -> ParameterizedTypeName(type, map)

        is WildcardType -> {
            val lowerBounds = type.lowerBounds

            if (lowerBounds.isNotEmpty()) {
                SupertypeWildcardTypeName(lowerBounds.map { TypeName(it) })
            } else {
                SubtypeWildcardTypeName(
                    type.upperBounds.map { TypeName(it) }.ifEmpty { listOf(ClassName.Builtins.OBJECT) }
                )
            }
        }

        is TypeVariable<*> -> TypeVariableName(type, map)

        is GenericArrayType -> ArrayTypeName(type, map)

        else -> throw IllegalArgumentException("Unexpected type $type")
    }
}
