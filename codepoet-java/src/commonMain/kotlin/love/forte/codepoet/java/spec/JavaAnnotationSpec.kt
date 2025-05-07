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
@file:JvmName("AnnotationSpecs")
@file:JvmMultifileClass

package love.forte.codepoet.java.spec

import love.forte.codepoet.common.BuilderDsl
import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.common.computeValueIfAbsent
import love.forte.codepoet.java.CodeValueSingleFormatBuilderDsl
import love.forte.codepoet.java.InternalJavaCodePoetApi
import love.forte.codepoet.java.JavaCodeValue
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.internal.isSourceName
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.internal.JavaAnnotationSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * A generated annotation on a declaration.
 */
public interface JavaAnnotationSpec : JavaSpec {

    public val type: JavaTypeName

    public val members: Map<String, List<JavaCodeValue>>

    public fun toBuilder(): Builder

    @InternalJavaCodePoetApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, true)
    }

    @InternalJavaCodePoetApi
    public fun emit(codeWriter: JavaCodeWriter, inline: Boolean = true)

    public class Builder internal constructor(private val type: JavaTypeName) : BuilderDsl {
        public val members: MutableMap<String, MutableList<JavaCodeValue>> = linkedMapOf()

        public fun addMember(name: String, codeValue: JavaCodeValue): Builder = apply {
            val values = members.computeValueIfAbsent(name) { mutableListOf() }
            values.add(codeValue)
        }

        public fun addMember(name: String, format: String, vararg argumentParts: CodeArgumentPart): Builder =
            addMember(name, JavaCodeValue(format, *argumentParts))


        // TODO addMemberForValue(memberName: String, value: Any)

        public fun build(): JavaAnnotationSpec {
            for (name in members.keys) {
                check(name.isSourceName()) { "not a valid name: $name" }
            }

            return JavaAnnotationSpecImpl(type, members.mapValues { it.value.toList() })
        }
    }

    public companion object {
        public const val VALUE: String = "value"

        @JvmStatic
        public fun builder(type: JavaClassName): Builder = Builder(type)
    }
}

public inline fun JavaAnnotationSpec(annotation: JavaClassName, block: JavaAnnotationSpec.Builder.() -> Unit = {}): JavaAnnotationSpec =
    JavaAnnotationSpec.builder(annotation).also(block).build()


public inline fun JavaAnnotationSpec.Builder.addMember(
    name: String,
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaAnnotationSpec.Builder =
    addMember(name, JavaCodeValue(format, block))
