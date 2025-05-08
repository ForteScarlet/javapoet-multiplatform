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

package love.forte.codepoet.java.naming

import love.forte.codepoet.common.naming.WildcardTypeName
import love.forte.codepoet.java.naming.internal.JavaSubtypeWildcardTypeNameImpl
import love.forte.codepoet.java.naming.internal.JavaSupertypeWildcardTypeNameImpl
import love.forte.codepoet.java.ref.JavaTypeRef
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public sealed interface JavaWildcardTypeName : JavaTypeName, WildcardTypeName {
    override val bounds: List<JavaTypeRef>
    // public val upperBounds: List<JavaTypeName> // ? extends T1 & T2
    // public val lowerBounds: List<JavaTypeName> // ? super T1 & T2
}

public interface JavaSubtypeWildcardTypeName : JavaWildcardTypeName {
    // lowerBounds, ? super A
    public val lowerBounds: List<JavaTypeRef>
        get() = bounds
}

public interface JavaSupertypeWildcardTypeName : JavaWildcardTypeName {
    // upperBounds, ? extends A & B,
    // 接口在后，类在前
    public val upperBounds: List<JavaTypeRef>
        get() = bounds
}

@JvmName("of")
public fun JavaSubtypeWildcardTypeName(upperBound: JavaTypeRef): JavaSubtypeWildcardTypeName =
    JavaSubtypeWildcardTypeName(listOf(upperBound))

@JvmName("of")
public fun JavaSupertypeWildcardTypeName(lowerBound: JavaTypeRef): JavaSupertypeWildcardTypeName =
    JavaSupertypeWildcardTypeName(listOf(lowerBound))

@JvmName("of")
public fun JavaSubtypeWildcardTypeName(upperBounds: List<JavaTypeRef>): JavaSubtypeWildcardTypeName =
    JavaSubtypeWildcardTypeNameImpl(upperBounds)

@JvmName("of")
public fun JavaSupertypeWildcardTypeName(lowerBounds: List<JavaTypeRef>): JavaSupertypeWildcardTypeName =
    JavaSupertypeWildcardTypeNameImpl(lowerBounds)
