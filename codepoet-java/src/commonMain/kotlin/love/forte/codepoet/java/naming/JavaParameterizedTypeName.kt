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

package love.forte.codepoet.java.naming

import love.forte.codepoet.java.naming.internal.JavaParameterizedTypeNameImpl
import love.forte.codepoet.java.ref.JavaTypeRef
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public interface JavaParameterizedTypeName : JavaTypeName {
    public val rawType: JavaClassName

    public val typeArguments: List<JavaTypeRef>

    /**
     * Returns a new [JavaParameterizedTypeName] instance for the specified [name] as nested inside this class.
     */
    public fun nestedClass(name: String): JavaParameterizedTypeName

    /**
     * Returns a new [JavaParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    public fun nestedClass(name: String, typeArguments: List<JavaTypeRef>): JavaParameterizedTypeName

    /**
     * Returns a new [JavaParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    public fun nestedClass(name: String, vararg typeArguments: JavaTypeRef): JavaParameterizedTypeName =
        nestedClass(name, typeArguments.asList())

    public companion object
}

/*
  TODO ParameterizedTypeName from Class, Type
  /** Returns a parameterized type, applying {@code typeArguments} to {@code rawType}. */
  public static ParameterizedTypeName get(Class<?> rawType, Type... typeArguments) {
    return new ParameterizedTypeName(null, ClassName.get(rawType), list(typeArguments));
  }

  TODO ParameterizedTypeName from ParameterizedType
  /** Returns a parameterized type equivalent to {@code type}. */
  public static ParameterizedTypeName get(ParameterizedType type) {
    return get(type, new LinkedHashMap<>());
  }
 */

/** Returns a parameterized type, applying [typeArguments] to [rawType]. */
@JvmName("of")
public fun JavaParameterizedTypeName(rawType: JavaClassName, vararg typeArguments: JavaTypeRef): JavaParameterizedTypeName {
    return JavaParameterizedTypeNameImpl(null, rawType, typeArguments.asList())
}
/** Returns a parameterized type, applying [typeArguments] to [rawType]. */
@JvmName("of")
public fun JavaParameterizedTypeName(rawType: JavaClassName, typeArguments: Iterable<JavaTypeRef>): JavaParameterizedTypeName {
    return JavaParameterizedTypeNameImpl(null, rawType, typeArguments.toList())
}
