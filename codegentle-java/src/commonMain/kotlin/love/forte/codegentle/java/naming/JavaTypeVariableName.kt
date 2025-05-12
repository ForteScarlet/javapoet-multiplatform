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

import love.forte.codegentle.java.naming.internal.JavaTypeVariableNameImpl
import love.forte.codegentle.java.ref.JavaTypeRef
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public interface JavaTypeVariableName : JavaTypeName {
    public val name: String
    public val bounds: List<JavaTypeRef<*>>

    public fun withBounds(bounds: List<JavaTypeRef<*>>): JavaTypeVariableName

    public fun withBounds(vararg bounds: JavaTypeRef<*>): JavaTypeVariableName {
        return withBounds(bounds.asList())
    }
}

/**
 * Returns type variable named `name` without bounds.
 */
@JvmName("of")
public fun JavaTypeVariableName(name: String): JavaTypeVariableName =
    JavaTypeVariableNameImpl(name)

/**
 * Returns type variable named `name` with `bounds`.
 */
@JvmName("of")
public fun JavaTypeVariableName(name: String, vararg bounds: JavaTypeRef<*>): JavaTypeVariableName =
    JavaTypeVariableNameImpl(name, bounds.asList())

/**
 * Returns type variable named `name` with `bounds`.
 */
@JvmName("of")
public fun JavaTypeVariableName(name: String, bounds: Iterable<JavaTypeRef<*>>): JavaTypeVariableName =
    JavaTypeVariableNameImpl(name, bounds.toList())
