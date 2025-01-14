package love.forte.codepoet.java.internal

import love.forte.codepoet.java.ArrayTypeName
import love.forte.codepoet.java.InternalApi
import love.forte.codepoet.java.TypeVariableName
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type

@InternalApi
public fun GenericArrayType.toArrayTypeName(map: MutableMap<Type, TypeVariableName>): ArrayTypeName {
    return ArrayTypeName(genericComponentType.toTypeName(map))
}
