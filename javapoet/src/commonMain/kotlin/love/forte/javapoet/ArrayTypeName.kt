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

package love.forte.javapoet

import love.forte.javapoet.internal.ArrayTypeNameImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 *
 *
 */
public interface ArrayTypeName : TypeName {
    public val componentType: TypeName

    override fun annotated(annotations: List<AnnotationSpec>): ArrayTypeName

    override fun annotated(vararg annotations: AnnotationSpec): ArrayTypeName {
        return annotated(annotations.asList())
    }

    override fun withoutAnnotations(): ArrayTypeName

    override val isPrimitive: Boolean
        get() = false

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, false)
    }

    public fun emit(codeWriter: CodeWriter, varargs: Boolean)
}

public fun ArrayTypeName(componentType: TypeName): ArrayTypeName =
    ArrayTypeNameImpl(componentType)
