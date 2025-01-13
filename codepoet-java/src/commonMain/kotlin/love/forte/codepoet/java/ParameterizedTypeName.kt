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
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


public interface ParameterizedTypeName : TypeName {
    // private val enclosingType: ParameterizedTypeName

    public val rawType: ClassName

    public val typeArguments: List<TypeName>

    /**
     * Returns a new [ParameterizedTypeName] instance for the specified [name] as nested inside this class.
     */
    public fun nestedClass(name: String): ParameterizedTypeName

    /**
     * Returns a new [ParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    public fun nestedClass(name: String, typeArguments: List<TypeName>): ParameterizedTypeName

    /**
     * Returns a new [ParameterizedTypeName] instance for the specified [name] as nested
     * inside this class, with the specified [typeArguments].
     */
    public fun nestedClass(name: String, vararg typeArguments: TypeName): ParameterizedTypeName =
        nestedClass(name, typeArguments.asList())

    override fun annotated(annotations: List<AnnotationSpec>): ParameterizedTypeName

    override fun annotated(vararg annotations: AnnotationSpec): ParameterizedTypeName {
        if (annotations.isEmpty()) return this
        return annotated(annotations.asList())
    }

    override fun withoutAnnotations(): ParameterizedTypeName

    override val isPrimitive: Boolean
        get() = false

    public companion object
}

/*
  TODO
  /** Returns a parameterized type, applying {@code typeArguments} to {@code rawType}. */
  public static ParameterizedTypeName get(Class<?> rawType, Type... typeArguments) {
    return new ParameterizedTypeName(null, ClassName.get(rawType), list(typeArguments));
  }

  /** Returns a parameterized type equivalent to {@code type}. */
  public static ParameterizedTypeName get(ParameterizedType type) {
    return get(type, new LinkedHashMap<>());
  }
 */

/** Returns a parameterized type, applying [typeArguments] to [rawType]. */
@JvmName("of")
public fun ParameterizedTypeName(rawType: ClassName, vararg typeArguments: TypeName): ParameterizedTypeName {
    return ParameterizedTypeNameImpl(null, rawType, typeArguments.asList())
}
