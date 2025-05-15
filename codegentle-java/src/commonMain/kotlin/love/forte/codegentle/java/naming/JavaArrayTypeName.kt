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

@file:JvmName("JavaArrayTypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.ArrayTypeName
import love.forte.codegentle.java.InternalJavaCodeGentleApi
import love.forte.codegentle.java.naming.internal.JavaArrayTypeNameImpl
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.ref.JavaTypeRefBuilder
import love.forte.codegentle.java.ref.ref
import love.forte.codegentle.java.writer.JavaCodeWriter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 *
 *
 */
public interface JavaArrayTypeName : JavaTypeName, ArrayTypeName {
    override val componentType: JavaTypeRef<*>

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, false)
    }

    @InternalJavaCodeGentleApi
    public fun emit(codeWriter: JavaCodeWriter, varargs: Boolean)
}

@JvmName("of")
public fun JavaArrayTypeName(componentType: JavaTypeRef<*>): JavaArrayTypeName =
    JavaArrayTypeNameImpl(componentType)


public inline fun <T : JavaTypeName> JavaArrayTypeName(
    componentType: T,
    block: JavaTypeRefBuilder<T>.() -> Unit = {}
): JavaArrayTypeName =
    JavaArrayTypeName(componentType.ref(block))
