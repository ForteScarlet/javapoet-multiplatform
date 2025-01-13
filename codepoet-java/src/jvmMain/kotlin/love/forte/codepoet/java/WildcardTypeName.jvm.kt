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
@file:JvmName("WildcardTypeNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.java.internal.SubtypeWildcardTypeNameImpl
import love.forte.codepoet.java.internal.SupertypeWildcardTypeNameImpl
import java.lang.reflect.Type

@JvmName("of")
public fun SubtypeWildcardTypeName(upperBound: Type): SubtypeWildcardTypeName =
    SubtypeWildcardTypeName(listOf(upperBound))

@JvmName("of")
public fun SupertypeWildcardTypeName(lowerBound: Type): SupertypeWildcardTypeName =
    SupertypeWildcardTypeName(listOf(lowerBound))

@JvmName("of")
public fun SubtypeWildcardTypeName(upperBounds: List<Type>): SubtypeWildcardTypeName =
    SubtypeWildcardTypeNameImpl(upperBounds.map { TypeName(it) })

@JvmName("of")
public fun SupertypeWildcardTypeName(lowerBounds: List<Type>): SupertypeWildcardTypeName =
    SupertypeWildcardTypeNameImpl(lowerBounds.map { TypeName(it) })
