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

@file:JvmName("JavaAnnotationSpecs")
@file:JvmMultifileClass

package love.forte.codegentle.java.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.computeValueIfAbsent
import love.forte.codegentle.java.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.java.InternalJavaCodeGentleApi
import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.internal.isSourceName
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaAnnotationRefCollectable
import love.forte.codegentle.java.spec.internal.JavaAnnotationSpecImpl
import love.forte.codegentle.java.writer.JavaCodeWriter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * A generated annotation on a declaration.
 */
public interface JavaAnnotationSpec : JavaSpec {

    public val type: JavaTypeName

    public val members: Map<String, List<JavaCodeValue>>

    public val annotations: List<JavaAnnotationRef>

    @InternalJavaCodeGentleApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, true)
    }

    @InternalJavaCodeGentleApi
    public fun emit(codeWriter: JavaCodeWriter, inline: Boolean = true)

    public companion object {
        public const val VALUE: String = "value"

        @JvmStatic
        public fun builder(type: JavaClassName): JavaAnnotationSpecBuilder = JavaAnnotationSpecBuilder(type)
    }
}

public inline fun JavaAnnotationSpec(
    annotation: JavaClassName,
    block: JavaAnnotationSpecBuilder.() -> Unit = {}
): JavaAnnotationSpec =
    JavaAnnotationSpec.builder(annotation).also(block).build()


public inline fun JavaAnnotationSpecBuilder.addMember(
    name: String,
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaAnnotationSpecBuilder =
    addMember(name, JavaCodeValue(format, block))

public class JavaAnnotationSpecBuilder internal constructor(private val type: JavaTypeName) :
    JavaAnnotationRefCollectable<JavaAnnotationSpecBuilder>,
    BuilderDsl {
    private val members: MutableMap<String, MutableList<JavaCodeValue>> = linkedMapOf()
    private val annotations: MutableList<JavaAnnotationRef> = mutableListOf()

    public fun addMember(name: String, codeValue: JavaCodeValue): JavaAnnotationSpecBuilder = apply {
        val values = members.computeValueIfAbsent(name) { mutableListOf() }
        values.add(codeValue)
    }

    public fun addMember(
        name: String,
        format: String,
        vararg argumentParts: CodeArgumentPart
    ): JavaAnnotationSpecBuilder =
        addMember(name, JavaCodeValue(format, *argumentParts))

    override fun addAnnotationRef(ref: JavaAnnotationRef): JavaAnnotationSpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): JavaAnnotationSpecBuilder = apply {
        annotations.addAll(refs)
    }

    // TODO addMemberForValue(memberName: String, value: Any)

    public fun build(): JavaAnnotationSpec {
        for (name in members.keys) {
            check(name.isSourceName()) { "not a valid name: $name" }
        }

        return JavaAnnotationSpecImpl(
            type,
            members.mapValues { it.value.toList() },
            annotations.toList()
        )
    }
}
