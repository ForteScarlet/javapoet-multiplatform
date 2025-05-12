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
@file:JvmName("JavaTypeVariableNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.ref.javaRef
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable

public fun TypeVariable<*>.toTypeVariableName(): JavaTypeVariableName =
    toTypeVariableName(linkedMapOf())

internal fun TypeVariable<*>.toTypeVariableName(map: MutableMap<Type, JavaTypeVariableName>): JavaTypeVariableName {
    val type = this
    return map[type] ?: run {
        val bounds = mutableListOf<JavaTypeRef<*>>()

        for (bound in type.bounds) {
            if (bound != Object::class.java) {
                bounds.add(bound.toTypeName(map).javaRef())
            }
        }

        JavaTypeVariableName(type.name, bounds).also {
            map[type] = it
        }
    }
}
