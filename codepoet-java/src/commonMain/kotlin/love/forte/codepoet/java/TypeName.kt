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

import love.forte.codepoet.java.internal.TypeNameImpl
import kotlin.jvm.JvmField
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

public interface TypeName : CodeEmitter {

    public val annotations: List<AnnotationSpec>

    public fun annotated(annotations: List<AnnotationSpec>): TypeName

    public fun annotated(vararg annotations: AnnotationSpec): TypeName {
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

    public object Builtins {
        @JvmField
        public val VOID: TypeName = TypeName("void")

        @JvmField
        public val BOOLEAN: TypeName = TypeName("boolean")

        @JvmField
        public val BYTE: TypeName = TypeName("byte")

        @JvmField
        public val SHORT: TypeName = TypeName("short")

        @JvmField
        public val INT: TypeName = TypeName("int")

        @JvmField
        public val LONG: TypeName = TypeName("long")

        @JvmField
        public val CHAR: TypeName = TypeName("char")

        @JvmField
        public val FLOAT: TypeName = TypeName("float")

        @JvmField
        public val DOUBLE: TypeName = TypeName("double")

        @JvmField
        public val OBJECT: TypeName = ClassName.Builtins.OBJECT
    }

    public companion object {
        // private val BOXED_VOID: ClassName = ClassName("java.lang", "Void")
        // private val BOXED_BOOLEAN: ClassName = ClassName("java.lang", "Boolean")
        // private val BOXED_BYTE: ClassName = ClassName("java.lang", "Byte")
        // private val BOXED_SHORT: ClassName = ClassName("java.lang", "Short")
        // private val BOXED_INT: ClassName = ClassName("java.lang", "Integer")
        // private val BOXED_LONG: ClassName = ClassName("java.lang", "Long")
        // private val BOXED_CHAR: ClassName = ClassName("java.lang", "Character")
        // private val BOXED_FLOAT: ClassName = ClassName("java.lang", "Float")
        // private val BOXED_DOUBLE: ClassName = ClassName("java.lang", "Double")
    }
}


private fun TypeName(keyword: String): TypeName = when (keyword) {
    "void" -> TypeName.Builtins.VOID
    "boolean" -> TypeName.Builtins.BOOLEAN
    "byte" -> TypeName.Builtins.BYTE
    "short" -> TypeName.Builtins.SHORT
    "int" -> TypeName.Builtins.INT
    "long" -> TypeName.Builtins.LONG
    "char" -> TypeName.Builtins.CHAR
    "float" -> TypeName.Builtins.FLOAT
    "double" -> TypeName.Builtins.DOUBLE
    else -> TypeNameImpl(keyword)
}

private fun TypeName(keyword: String, annotations: List<AnnotationSpec>): TypeName {
    if (annotations.isEmpty()) {
        return TypeName(keyword)
    }

    return TypeNameImpl(keyword, annotations)
}
