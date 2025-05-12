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

@file:JvmName("JavaArrayTypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.java.ref.JavaTypeRefBuilder
import love.forte.codegentle.java.ref.javaRef
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.ArrayType

public inline fun JavaArrayTypeName(
    componentType: Type,
    block: JavaTypeRefBuilder<*>.() -> Unit = {}
): JavaArrayTypeName {
    return JavaArrayTypeName(componentType.toTypeName().javaRef(block))
}

/**
 * Create an [JavaArrayTypeName] from [GenericArrayType].
 */
public inline fun GenericArrayType.toJavaArrayTypeName(
    block: JavaTypeRefBuilder<*>.() -> Unit = {}
): JavaArrayTypeName {
    return toJavaArrayTypeName(linkedMapOf(), block)
}

@PublishedApi
internal inline fun GenericArrayType.toJavaArrayTypeName(
    map: MutableMap<Type, JavaTypeVariableName>,
    block: JavaTypeRefBuilder<*>.() -> Unit
): JavaArrayTypeName {
    return JavaArrayTypeName(genericComponentType.toTypeName(map).javaRef(block))
}

// javax.lang.model

public fun ArrayType.toJavaArrayTypeName(
    block: JavaTypeRefBuilder<*>.() -> Unit = {}
): JavaArrayTypeName {
    return toJavaArrayTypeName(mutableMapOf(), block)
}

internal fun ArrayType.toJavaArrayTypeName(
    map: MutableMap<TypeParameterElement, JavaTypeVariableName>,
    block: JavaTypeRefBuilder<*>.() -> Unit = {}
): JavaArrayTypeName {
    return JavaArrayTypeName(componentType.toTypeName(map).javaRef(block))
}
