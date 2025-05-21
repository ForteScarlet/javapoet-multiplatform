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

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.PlatformTypeName
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.java.writer.JavaCodeEmitter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A type name represented in Java。
 */
public interface JavaTypeName : PlatformTypeName, JavaCodeEmitter


/**
 * Returns true if this is a primitive type like `int`.
 * Returns false for all other types
 *
 * Types including boxed primitives and `void`.
 */
public val TypeName.isPrimitive: Boolean
    get() = this is JavaPrimitiveTypeName && this != JavaPrimitiveTypeNames.VOID

/**
 * If this type is one of the primitive types ([JavaPrimitiveTypeNames.VOID],
 * [JavaPrimitiveTypeNames.INT], etc.),
 * return the boxed type ([JavaClassNames.BOXED_VOID], [JavaClassNames.BOXED_INT], etc.).
 * Otherwise, return itself.
 */
public fun TypeName.box(): TypeName {
    return (this as? JavaPrimitiveTypeName)?.box() ?: this
}


public fun TypeName.unbox(): TypeName {
    return when (this) {
        is JavaPrimitiveTypeName -> this
        is ClassName -> TODO()
        else -> throw UnsupportedOperationException("Can't unbox $this")
    }
    // return (this as? JavaPrimitiveTypeName)?.unbox()
    // // Only ClassNames and primitive TypeNames implement this fun.
    //     ?: throw UnsupportedOperationException("Can't unbox $this")
}
