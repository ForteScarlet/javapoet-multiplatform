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

@file:JvmName("JavaTypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.PlatformTypeName
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.java.naming.internal.JavaPrimitiveTypeNameImpl
import love.forte.codegentle.java.writer.JavaCodeEmitter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A type name represented in Java。
 */
public sealed interface JavaTypeName : PlatformTypeName, JavaCodeEmitter {
    public object Builtins {
        public val VOID: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.VOID)

        public val BOOLEAN: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.BOOLEAN)

        public val BYTE: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.BYTE)

        public val SHORT: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.SHORT)

        public val INT: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.INT)

        public val LONG: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.LONG)

        public val CHAR: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.CHAR)

        public val FLOAT: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.FLOAT)

        public val DOUBLE: JavaTypeName = JavaPrimitiveTypeNameImpl(JavaPrimitiveTypeName.DOUBLE)
    }
}

internal interface JavaPrimitiveTypeName : JavaTypeName {
    val keyword: String

    fun box(): JavaTypeName
    fun unbox(): JavaTypeName


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

/**
 * Returns true if this is a primitive type like `int`.
 * Returns false for all other types
 *
 * Types including boxed primitives and `void`.
 */
public val JavaTypeName.isPrimitive: Boolean
    get() = this is JavaPrimitiveTypeName && this != JavaPrimitiveTypeNames.VOID

/**
 * If this type is one of the primitive types ([JavaPrimitiveTypeNames.VOID],
 * [JavaPrimitiveTypeNames.INT], etc.),
 * return the boxed type ([JavaClassNames.BOXED_VOID], [JavaClassNames.BOXED_INT], etc.).
 * Otherwise, return itself.
 */
public fun JavaTypeName.box(): JavaTypeName {
    return (this as? JavaPrimitiveTypeName)?.box() ?: this
}

public fun JavaTypeName.unbox(): TypeName {
    return (this as? JavaPrimitiveTypeName)?.unbox()
    // Only ClassNames and primitive TypeNames implement this fun.
        ?: throw UnsupportedOperationException("Can't unbox $this")
}
