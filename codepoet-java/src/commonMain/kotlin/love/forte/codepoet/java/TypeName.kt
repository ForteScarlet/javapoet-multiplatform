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

package love.forte.codepoet.java

import love.forte.codepoet.java.internal.PrimitiveTypeNameImpl
import kotlin.jvm.JvmField
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A type name.
 */
public sealed interface TypeName : CodeEmitter {
    public val annotations: List<AnnotationSpec>

    public fun annotated(annotations: List<AnnotationSpec>): TypeName

    public fun annotated(vararg annotations: AnnotationSpec): TypeName {
        if (annotations.isEmpty()) return this
        return annotated(annotations.asList())
    }

    public fun withoutAnnotations(): TypeName

    public val isAnnotated: Boolean
        get() = annotations.isNotEmpty()

    /**
     * Returns true if this is a primitive type like `int`.
     * Returns false for all other types
     *
     * types including boxed primitives and `void`.
     */
    public val isPrimitive: Boolean

    /**
     * If this type is one of the primitive types ([Builtins.VOID], [Builtins.INT], etc.),
     * return the boxed type ([ClassName.Builtins.BOXED_VOID], [ClassName.Builtins.BOXED_INT], etc.).
     * Otherwise, return itself.
     */
    public fun box(): TypeName {
        // Only primitive TypeNames implement this fun.
        return this
    }

    public fun unbox(): TypeName {
        // Only ClassNames and primitive TypeNames implement this fun.
        throw UnsupportedOperationException("Can't unbox $this")
    }

    public object Builtins {
        @JvmField
        public val VOID: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.VOID)

        @JvmField
        public val BOOLEAN: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.BOOLEAN)

        @JvmField
        public val BYTE: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.BYTE)

        @JvmField
        public val SHORT: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.SHORT)

        @JvmField
        public val INT: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.INT)

        @JvmField
        public val LONG: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.LONG)

        @JvmField
        public val CHAR: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.CHAR)

        @JvmField
        public val FLOAT: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.FLOAT)

        @JvmField
        public val DOUBLE: TypeName = PrimitiveTypeNameImpl(PrimitiveTypeName.DOUBLE)
    }
}

internal interface PrimitiveTypeName : TypeName {
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
