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

@file:JvmName("TypeNames")
@file:JvmMultifileClass

package love.forte.codepoet.java.naming

import love.forte.codepoet.common.naming.TypeName
import love.forte.codepoet.java.JavaCodeEmitter
import love.forte.codepoet.java.naming.internal.PrimitiveTypeNameImpl
import kotlin.jvm.JvmField
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A type name represented in Java。
 */
public sealed interface JavaTypeName : TypeName, JavaCodeEmitter {
    // public val annotations: List<JavaAnnotationSpec>
    //
    // public fun annotated(annotations: List<JavaAnnotationSpec>): JavaTypeName
    //
    // public fun annotated(vararg annotations: JavaAnnotationSpec): JavaTypeName {
    //     if (annotations.isEmpty()) return this
    //     return annotated(annotations.asList())
    // }
    //
    // public fun withoutAnnotations(): JavaTypeName
    //
    // public val isAnnotated: Boolean
    //     get() = annotations.isNotEmpty()

    /**
     * Returns true if this is a primitive type like `int`.
     * Returns false for all other types
     *
     * types including boxed primitives and `void`.
     */
    public val isPrimitive: Boolean

    /**
     * If this type is one of the primitive types ([Builtins.VOID], [Builtins.INT], etc.),
     * return the boxed type ([JavaClassName.Builtins.BOXED_VOID], [JavaClassName.Builtins.BOXED_INT], etc.).
     * Otherwise, return itself.
     */
    public fun box(): JavaTypeName {
        // Only primitive TypeNames implement this fun.
        return this
    }

    public fun unbox(): JavaTypeName {
        // Only ClassNames and primitive TypeNames implement this fun.
        throw UnsupportedOperationException("Can't unbox $this")
    }

    public object Builtins {
        @JvmField
        public val VOID: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.VOID)

        @JvmField
        public val BOOLEAN: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.BOOLEAN)

        @JvmField
        public val BYTE: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.BYTE)

        @JvmField
        public val SHORT: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.SHORT)

        @JvmField
        public val INT: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.INT)

        @JvmField
        public val LONG: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.LONG)

        @JvmField
        public val CHAR: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.CHAR)

        @JvmField
        public val FLOAT: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.FLOAT)

        @JvmField
        public val DOUBLE: JavaTypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.DOUBLE)
    }
}

internal interface PrimitiveTypeName : JavaTypeName {
    val keyword: String

    companion object {
        const val VOID = "void"
        const val BOOLEAN = "boolean"
        const val BYTE = "byte"
        const val SHORT = "short"
        const val INT = "int"
        const val LONG = "long"
        const val CHAR = "char"
        const val FLOAT = "float"
        const val DOUBLE = "double"
    }
}
