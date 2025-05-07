/*
 * Copyright (C) 2015 Square, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("ParameterizedTypeNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.java.naming.JavaParameterizedTypeName
import love.forte.codepoet.java.naming.JavaTypeVariableName
import love.forte.codepoet.java.naming.internal.JavaParameterizedTypeNameImpl
import java.lang.reflect.Modifier
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

public fun Class<*>.toParameterizedTypeName(vararg typeArguments: Type): JavaParameterizedTypeName {
    return JavaParameterizedTypeNameImpl(null, this.toClassName(), typeArguments.map { it.toTypeName() })
}

public fun ParameterizedType.toParameterizedTypeName(): JavaParameterizedTypeName =
    toParameterizedTypeName(linkedMapOf())

internal fun ParameterizedType.toParameterizedTypeName(map: MutableMap<Type, JavaTypeVariableName>): JavaParameterizedTypeName {
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
        ?: JavaParameterizedTypeNameImpl(null, rawType, typeArguments)
}
