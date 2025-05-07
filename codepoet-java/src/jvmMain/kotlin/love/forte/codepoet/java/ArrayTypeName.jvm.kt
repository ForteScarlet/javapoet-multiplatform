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

@file:JvmName("ArrayTypeNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.java.naming.JavaArrayTypeName
import love.forte.codepoet.java.naming.JavaTypeVariableName
import java.lang.reflect.GenericArrayType
import java.lang.reflect.Type
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.ArrayType

public fun ArrayTypeName(componentType: Type): JavaArrayTypeName {
    return JavaArrayTypeName(componentType.toTypeName())
}

/**
 * Create an [JavaArrayTypeName] from [GenericArrayType].
 */
public fun GenericArrayType.toArrayTypeName(): JavaArrayTypeName {
    return toArrayTypeName(linkedMapOf())
}

internal fun GenericArrayType.toArrayTypeName(map: MutableMap<Type, JavaTypeVariableName>): JavaArrayTypeName {
    return JavaArrayTypeName(genericComponentType.toTypeName(map))
}

// javax.lang.model

public fun ArrayType.toArrayTypeName(): JavaArrayTypeName {
    return toArrayTypeName(mutableMapOf())
}

internal fun ArrayType.toArrayTypeName(map: MutableMap<TypeParameterElement, JavaTypeVariableName>): JavaArrayTypeName {
    return JavaArrayTypeName(componentType.toTypeName(map))
}
