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

import love.forte.codepoet.java.internal.ParameterizedTypeNameImpl
import love.forte.codepoet.java.internal.toParameterizedTypeName
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

public fun Class<*>.toParameterizedTypeName(vararg typeArguments: Type): ParameterizedTypeName {
    return ParameterizedTypeNameImpl(null, this.toClassName(), typeArguments.map { it.toTypeName() })
}

public fun ParameterizedType.toParameterizedTypeName(): ParameterizedTypeName =
    toParameterizedTypeName(linkedMapOf())
