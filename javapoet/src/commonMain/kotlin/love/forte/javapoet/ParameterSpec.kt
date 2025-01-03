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

@file:JvmName("ParameterSpecs")
@file:JvmMultifileClass

package love.forte.javapoet

import love.forte.javapoet.internal.ParameterSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated parameter declaration.
 */
public interface ParameterSpec {
    public val type: TypeName

    public val name: String

    public val annotations: List<AnnotationSpec>

    public val modifiers: Set<Modifier>

    public fun hasModifier(modifier: Modifier): Boolean = modifier in modifiers

    public val javadoc: CodeBlock

    public fun toBuilder(): Builder

    public class Builder internal constructor(
        public val type: TypeName,
        public val name: String
    ) {
        internal val javadoc = CodeBlock.builder()
        internal val annotations = mutableListOf<AnnotationSpec>()
        internal val modifiers = mutableSetOf<Modifier>()

        public fun addJavadoc(format: String, vararg args: Any?): Builder = apply {
            javadoc.add(format, *args)
        }

        public fun addJavadoc(block: CodeBlock): Builder = apply {
            javadoc.add(block)
        }

        public fun addAnnotations(vararg annotations: AnnotationSpec): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotations(annotations: Iterable<AnnotationSpec>): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotation(annotationSpec: AnnotationSpec): Builder = apply {
            annotations.add(annotationSpec)
        }

        public fun addAnnotation(annotation: ClassName): Builder =
            addAnnotation(AnnotationSpec(annotation))

        public fun addModifiers(vararg modifiers: Modifier): Builder = apply {
            modifiers.forEach { checkModifier(it) }
            this.modifiers.addAll(modifiers)
        }

        public fun addModifiers(modifiers: Iterable<Modifier>): Builder = apply {
            modifiers.forEach { checkModifier(it) }
            this.modifiers.addAll(modifiers)
        }

        public fun addModifier(modifier: Modifier): Builder = apply {
            checkModifier(modifier)
            modifiers.add(modifier)
        }

        /**
         * Only support [final][Modifier.FINAL]
         */
        private fun checkModifier(modifier: Modifier) {
            check(modifier == Modifier.FINAL) {
                "Unexpected parameter modifier: $modifier"
            }
        }

        public fun build(): ParameterSpec {
            return ParameterSpecImpl(
                type = type,
                name = name,
                annotations = annotations.toList(),
                modifiers = modifiers.toSet(),
                javadoc = javadoc.build()
            )
        }
    }

    public companion object {
        @JvmStatic
        public fun builder(type: TypeName, name: String, vararg modifiers: Modifier): Builder {
            return Builder(type, name).addModifiers(*modifiers)
        }
    }
}

public inline fun ParameterSpec(
    type: TypeName,
    name: String,
    block: ParameterSpec.Builder.() -> Unit = {}
): ParameterSpec = ParameterSpec.builder(type, name).apply(block).build()
