package love.forte.codepoet.java.internal

import love.forte.codepoet.java.InternalApi
import love.forte.codepoet.java.ParameterizedTypeName
import love.forte.codepoet.java.TypeVariableName
import love.forte.codepoet.java.toClassName
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

@InternalApi
public fun ParameterizedType.toParameterizedTypeName(
    map: MutableMap<Type, TypeVariableName>
): ParameterizedTypeName {
    val type = this

    val rawType = (type.rawType as Class<*>).toClassName()
    val ownerType = if (
        type.ownerType is ParameterizedType
        && !Modifier.isStatic((type.rawType as Class<*>).modifiers)
    ) {
        type.ownerType as ParameterizedType
    } else {
        null
    }

    val typeArguments = type.actualTypeArguments.map { it.toTypeName(map) }

    return ownerType?.toParameterizedTypeName(map)?.nestedClass(rawType.simpleName, typeArguments)
        ?: ParameterizedTypeNameImpl(null, rawType, typeArguments)
}
