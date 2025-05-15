/*
 * Copyright (C) 2014 Google, Inc.
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

@file:JvmName("JavaClassNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.toClassName
import javax.lang.model.element.TypeElement
import kotlin.reflect.KClass

/**
 * Create a [love.forte.codegentle.java.naming.JavaClassName] from [KClass].
 *
 * @see Class.toJavaClassName
 */
public fun KClass<*>.toJavaClassName(): JavaClassName = java.toJavaClassName()

/**
 * Create a [JavaClassName] from [Class].
 */
public fun Class<*>.toJavaClassName(): JavaClassName {
    when (this) {
        VOID_BOXED_CLASS -> return JavaClassNames.BOXED_VOID
        BOOLEAN_BOXED_CLASS -> return JavaClassNames.BOXED_BOOLEAN
        BYTE_BOXED_CLASS -> return JavaClassNames.BOXED_BYTE
        SHORT_BOXED_CLASS -> return JavaClassNames.BOXED_SHORT
        INT_BOXED_CLASS -> return JavaClassNames.BOXED_INT
        LONG_BOXED_CLASS -> return JavaClassNames.BOXED_LONG
        CHAR_BOXED_CLASS -> return JavaClassNames.BOXED_CHAR
        FLOAT_BOXED_CLASS -> return JavaClassNames.BOXED_FLOAT
        OBJECT_CLASS -> return JavaClassNames.OBJECT
        STRING_CLASS -> return JavaClassNames.STRING
    }

    return toClassName().java()
}

// javax.lang.model

public fun TypeElement.toJavaClassName(): JavaClassName = toClassName().java()
