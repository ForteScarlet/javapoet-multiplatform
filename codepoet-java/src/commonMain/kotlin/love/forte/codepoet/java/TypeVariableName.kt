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
@file:JvmName("TypeVariableNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.java.internal.TypeVariableNameImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public interface TypeVariableName : TypeName {
    public val name: String
    public val bounds: List<TypeName>

    override fun annotated(annotations: List<AnnotationSpec>): TypeVariableName

    override fun annotated(vararg annotations: AnnotationSpec): TypeVariableName {
        if (annotations.isEmpty()) return this

        return annotated(annotations.asList())
    }

    override fun withoutAnnotations(): TypeVariableName

    public fun withBounds(bounds: List<TypeName>): TypeVariableName

    public fun withBounds(vararg bounds: TypeName): TypeVariableName {
        return withBounds(bounds.asList())
    }

    override val isPrimitive: Boolean
        get() = false
}

/**
 * Returns type variable named `name` without bounds.
 */
@JvmName("of")
public fun TypeVariableName(name: String): TypeVariableName =
    TypeVariableNameImpl(name)

/**
 * Returns type variable named `name` with `bounds`.
 */
@JvmName("of")
public fun TypeVariableName(name: String, vararg bounds: TypeName): TypeVariableName =
    TypeVariableNameImpl(name, bounds.asList())

/**
 * Returns type variable named `name` with `bounds`.
 */
@JvmName("of")
public fun TypeVariableName(name: String, bounds: Iterable<TypeName>): TypeVariableName =
    TypeVariableNameImpl(name, bounds.toList())
