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

@file:JvmName("JavaFieldSpecs")
@file:JvmMultifileClass

package love.forte.codegentle.java.spec

import love.forte.codegentle.java.naming.toJavaClassName
import love.forte.codegentle.java.naming.toJavaTypeName
import love.forte.codegentle.java.ref.JavaAnnotationRefBuilder
import love.forte.codegentle.java.ref.annotationRef
import java.lang.reflect.Type
import kotlin.reflect.KClass

/**
 * Create a [love.forte.codepoet.java.spec.FieldSpec] from [Type] with [Builder][block].
 */
public inline fun Type.toFieldSpec(name: String, block: JavaFieldSpecBuilder.() -> Unit = {}): JavaFieldSpec {
    return JavaFieldSpec(this.toJavaTypeName(), name, block)
}

public inline fun JavaFieldSpecBuilder.addAnnotationRef(
    cls: Class<*>,
    block: JavaAnnotationRefBuilder.() -> Unit = {}
): JavaFieldSpecBuilder = apply {
    addAnnotationRef(cls.toJavaClassName().annotationRef(block))
}

public inline fun JavaFieldSpecBuilder.addAnnotationRef(
    cls: KClass<*>,
    block: JavaAnnotationRefBuilder.() -> Unit = {}
): JavaFieldSpecBuilder = apply {
    addAnnotationRef(cls.toJavaClassName().annotationRef(block))
}
