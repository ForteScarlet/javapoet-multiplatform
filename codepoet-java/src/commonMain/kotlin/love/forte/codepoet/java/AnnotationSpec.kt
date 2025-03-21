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

package love.forte.codepoet.java

import love.forte.codepoet.common.computeValueIfAbsent
import love.forte.codepoet.java.AnnotationSpec.Builder
import love.forte.codepoet.java.internal.AnnotationSpecImpl
import love.forte.codepoet.java.internal.isSourceName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic

/**
 * A generated annotation on a declaration.
 */
public interface AnnotationSpec : CodeEmitter {

    public val type: TypeName

    public val members: Map<String, List<CodeValue>>

    public fun toBuilder(): Builder

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, true)
    }

    @InternalApi
    public fun emit(codeWriter: CodeWriter, inline: Boolean = true)

    public class Builder internal constructor(private val type: TypeName) : BuilderDsl {
        public val members: MutableMap<String, MutableList<CodeValue>> = linkedMapOf()

        public fun addMember(name: String, codeValue: CodeValue): Builder = apply {
            val values = members.computeValueIfAbsent(name) { mutableListOf() }
            values.add(codeValue)
        }

        public fun addMember(name: String, format: String, vararg argumentParts: CodeArgumentPart): Builder =
            addMember(name, CodeValue(format, *argumentParts))


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
    }
}

public inline fun AnnotationSpec(annotation: ClassName, block: Builder.() -> Unit = {}): AnnotationSpec =
    AnnotationSpec.builder(annotation).also(block).build()


public inline fun Builder.addMember(name: String, format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder =
    addMember(name, CodeValue(format, block))
