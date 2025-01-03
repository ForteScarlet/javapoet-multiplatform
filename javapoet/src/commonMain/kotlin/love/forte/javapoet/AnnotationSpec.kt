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

package love.forte.javapoet

import love.forte.javapoet.internal.AnnotationSpecImpl
import love.forte.javapoet.internal.isSourceName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic
import kotlin.reflect.KClass

/**
 * A generated annotation on a declaration.
 */
public interface AnnotationSpec {

    public val type: TypeName

    public val members: Map<String, List<CodeBlock>>

    public fun toBuilder(): Builder

    public class Builder internal constructor(private val type: TypeName) {
        public val members: MutableMap<String, MutableList<CodeBlock>> = linkedMapOf()

        public fun addMember(name: String, format: String, vararg args: Any): Builder =
            addMember(name, CodeBlock(format, *args))

        public fun addMember(name: String, codeBlock: CodeBlock): Builder = apply {
            val values = members.computeValueIfAbsent(name) { mutableListOf() }
            values.add(codeBlock)
        }

        // TODO addMemberForValue(memberName: String, value: Any)

        public fun build(): AnnotationSpec {
            for (name in members.keys) {
                check(name.isSourceName()) { "not a valid name: $name" }
            }

            return AnnotationSpecImpl(type, members.mapValues { it.value.toList() })
        }
    }

    public companion object {
        public const val VALUE: String = "value"

        @JvmStatic
        public fun builder(type: ClassName): Builder = Builder(type)

        // TODO
        @JvmStatic
        public fun builder(type: KClass<*>): Builder = builder(ClassName(type))

    }
}

public inline fun AnnotationSpec(annotation: ClassName, block: AnnotationSpec.Builder.() -> Unit = {}): AnnotationSpec =
    AnnotationSpec.builder(annotation).also(block).build()

// public fun AnnotationSpec.toBuilder(): AnnotationSpec.Builder = TODO()

// TODO Class

