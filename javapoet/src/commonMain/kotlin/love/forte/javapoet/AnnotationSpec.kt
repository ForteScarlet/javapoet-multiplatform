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

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

/**
 * A generated annotation on a declaration.
 */
public interface AnnotationSpec {

    public val type: TypeName

    public val members: Map<String, List<Any/* TODO CodeBlock */>>


    public class Builder internal constructor(
        private val type: TypeName,
        public val members: LinkedHashMap<String, List<Any/* TODO CodeBlock */>> = linkedMapOf(),
    ) {

        public fun addMember(name: String, format: String, vararg args: Any): Builder = apply {
            TODO()
        }

        public fun addMember(name: String, codeBlock: Any /* TODO */): Builder = apply {
            TODO()
        }

        public fun build(): AnnotationSpec {
            TODO()
        }
    }

    public companion object {
        public const val VALUE: String = "value"

    }
}

public fun AnnotationSpec.toBuilder(): AnnotationSpec.Builder = TODO()

public inline fun buildAnnotationSpec(type: ClassName, block: AnnotationSpec.Builder.() -> Unit): AnnotationSpec {
    return annotationSpecBuilder(type).also(block).build()
}

public fun annotationSpecBuilder(type: ClassName): AnnotationSpec.Builder =
    AnnotationSpec.Builder(type)

// TODO Class

