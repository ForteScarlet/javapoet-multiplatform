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

@file:JvmName("JavaParameterizedTypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.ParameterizedTypeName
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.java.naming.internal.JavaParameterizedTypeNameImpl
import love.forte.codegentle.java.ref.JavaTypeRef
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public interface JavaParameterizedTypeName : JavaTypeName, ParameterizedTypeName {
    override val enclosingType: JavaParameterizedTypeName?
    override val rawType: ClassName

    override val typeArguments: List<TypeRef<*>>

    /**
     * Returns a new [JavaParameterizedTypeName] instance for the specified [name] as nested inside this class.
     */
    override fun nestedClass(name: String): JavaParameterizedTypeName

    /**
     * Returns a new [JavaParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    override fun nestedClass(name: String, typeArguments: List<TypeRef<*>>): JavaParameterizedTypeName

    public companion object
}

/** Returns a parameterized type, applying [typeArguments] to [rawType]. */
@JvmName("of")
public fun JavaParameterizedTypeName(rawType: ClassName, vararg typeArguments: JavaTypeRef<*>): JavaParameterizedTypeName {
    return JavaParameterizedTypeNameImpl(null, rawType, typeArguments.asList())
}
/** Returns a parameterized type, applying [typeArguments] to [rawType]. */
@JvmName("of")
public fun JavaParameterizedTypeName(rawType: ClassName, typeArguments: Iterable<JavaTypeRef<*>>): JavaParameterizedTypeName {
    return JavaParameterizedTypeNameImpl(null, rawType, typeArguments.toList())
}
