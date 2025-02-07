package love.forte.codepoet.java.internal

import love.forte.codepoet.java.InternalApi
import love.forte.codepoet.java.TypeName
import love.forte.codepoet.java.TypeVariableName
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

@InternalApi
public fun TypeVariable<*>.toTypeVariableName(map: MutableMap<Type, TypeVariableName>): TypeVariableName {
    val type = this
    return map[type] ?: run {
        val bounds = mutableListOf<TypeName>()

        for (bound in type.bounds) {
            if (bound != Object::class.java) {
                bounds.add(bound.toTypeName(map))
            }
        }

        TypeVariableName(type.name, bounds).also {
            map[type] = it
        }
    }
}
