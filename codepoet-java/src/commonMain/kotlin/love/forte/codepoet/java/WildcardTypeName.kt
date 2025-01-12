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
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public sealed interface WildcardTypeName : TypeName {
    public val upperBounds: List<TypeName> // ? extends T1 & T2
    public val lowerBounds: List<TypeName> // ? super T1 & T2

    override fun withoutAnnotations(): WildcardTypeName

    override fun annotated(annotations: List<AnnotationSpec>): WildcardTypeName

    override fun annotated(vararg annotations: AnnotationSpec): WildcardTypeName {
        if (annotations.isEmpty()) return this
        return annotated(annotations.asList())
    }

    override val isPrimitive: Boolean
        get() = false
}

public interface SubtypeWildcardTypeName : WildcardTypeName {
    // lowerBounds, ? super A
    override val upperBounds: List<TypeName>
        get() = emptyList()
}

public interface SupertypeWildcardTypeName : WildcardTypeName {
    // upperBounds, ? extends A & B,
    // 接口在后，类在前
    override val lowerBounds: List<TypeName>
        get() = emptyList()
}

public fun SubtypeWildcardTypeName(upperBound: TypeName): SubtypeWildcardTypeName =
    SubtypeWildcardTypeName(listOf(upperBound))

public fun SupertypeWildcardTypeName(lowerBound: TypeName): SupertypeWildcardTypeName =
    SupertypeWildcardTypeName(listOf(lowerBound))

internal fun SubtypeWildcardTypeName(upperBounds: List<TypeName>): SubtypeWildcardTypeName =
    SubtypeWildcardTypeNameImpl(upperBounds)

internal fun SupertypeWildcardTypeName(lowerBounds: List<TypeName>): SupertypeWildcardTypeName =
    SupertypeWildcardTypeNameImpl(lowerBounds)
