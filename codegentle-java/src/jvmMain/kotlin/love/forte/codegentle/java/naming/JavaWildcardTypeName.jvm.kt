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
@file:JvmName("JavaWildcardTypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.java.naming.internal.JavaSubtypeWildcardTypeNameImpl
import love.forte.codegentle.java.naming.internal.JavaSupertypeWildcardTypeNameImpl
import love.forte.codegentle.java.ref.javaRef
import java.lang.reflect.Type

public fun Type.toJavaSubtypeWildcardTypeName(): JavaSubtypeWildcardTypeName =
    listOf(this).toJavaSubtypeWildcardTypeName()

public fun Type.toJavaSupertypeWildcardTypeName(): JavaSupertypeWildcardTypeName =
    listOf(this).toJavaSupertypeWildcardTypeName()

public fun Iterable<Type>.toJavaSubtypeWildcardTypeName(): JavaSubtypeWildcardTypeName =
    JavaSubtypeWildcardTypeNameImpl(this.map { it.toJavaTypeName().javaRef() })

public fun Iterable<Type>.toJavaSupertypeWildcardTypeName(): JavaSupertypeWildcardTypeName =
    JavaSupertypeWildcardTypeNameImpl(this.map { it.toJavaTypeName().javaRef() })
