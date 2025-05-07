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

@file:JvmName("ArrayTypeNames")
@file:JvmMultifileClass

package love.forte.codepoet.java.naming

import love.forte.codepoet.java.InternalJavaCodePoetApi
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.naming.internal.JavaArrayTypeNameImpl
import love.forte.codepoet.java.spec.JavaAnnotationSpec
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 *
 *
 */
public interface JavaArrayTypeName : JavaTypeName {
    public val componentType: JavaTypeName

    override fun annotated(annotations: List<JavaAnnotationSpec>): JavaArrayTypeName

    override fun annotated(vararg annotations: JavaAnnotationSpec): JavaArrayTypeName {
        if (annotations.isEmpty()) return this
        return annotated(annotations.asList())
    }

    override fun withoutAnnotations(): JavaArrayTypeName

    override val isPrimitive: Boolean
        get() = false

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, false)
    }

    @InternalJavaCodePoetApi
    public fun emit(codeWriter: JavaCodeWriter, varargs: Boolean)
}

@JvmName("of")
public fun JavaArrayTypeName(componentType: JavaTypeName): JavaArrayTypeName =
    JavaArrayTypeNameImpl(componentType)
