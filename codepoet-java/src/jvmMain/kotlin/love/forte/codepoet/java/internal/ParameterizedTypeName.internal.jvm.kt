package love.forte.codepoet.java.internal

import love.forte.codepoet.java.ClassName
import love.forte.codepoet.java.InternalApi
import love.forte.codepoet.java.ParameterizedTypeName
import love.forte.codepoet.java.TypeVariableName
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@InternalApi
public fun ParameterizedTypeName(
    type: ParameterizedType,
    map: MutableMap<Type, TypeVariableName>
): ParameterizedTypeName {
    val rawType = ClassName(type.rawType as Class<*>)
    val ownerType = if (
        type.ownerType is ParameterizedType
        && !Modifier.isStatic((type.rawType as Class<*>).modifiers)
    ) {
        type.ownerType as ParameterizedType
    } else {
        null
    }

    val typeArguments = type.actualTypeArguments.map { TypeName(it, map) }

    return if (ownerType != null) {
        ParameterizedTypeName(ownerType, map).nestedClass(rawType.simpleName, typeArguments)
    } else {
        ParameterizedTypeNameImpl(null, rawType, typeArguments)
    }
}
