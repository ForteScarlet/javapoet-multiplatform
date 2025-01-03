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

@file:JvmName("FieldSpecs")
@file:JvmMultifileClass

package love.forte.javapoet

import love.forte.javapoet.internal.FieldSpecImpl
import love.forte.javapoet.internal.isSourceName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated field declaration.
 */
public interface FieldSpec {

    public val type: TypeName

    public val name: String

    public val javadoc: CodeBlock

    public val annotations: List<AnnotationSpec>

    public val modifiers: Set<Modifier>

    public val initializer: CodeBlock

    public fun hasModifier(modifier: Modifier): Boolean = modifier in modifiers

    public fun toBuilder(): Builder

    public class Builder internal constructor(
        public val type: TypeName,
        public val name: String,
    ) {
        internal val javadoc = CodeBlock.builder()
        internal val annotations = mutableListOf<AnnotationSpec>()
        internal val modifiers = mutableSetOf<Modifier>()
        internal var initializer: CodeBlock? = null

        public fun addJavadoc(format: String, vararg args: Any): Builder = apply {
            javadoc.add(format, *args)
        }

        public fun addJavadoc(block: CodeBlock): Builder = apply {
            javadoc.add(block)
        }

        public fun addAnnotations(annotationSpecs: Iterable<AnnotationSpec>): Builder = apply {
            annotations.addAll(annotationSpecs)
        }

        public fun addAnnotations(vararg annotationSpecs: AnnotationSpec): Builder = apply {
            annotations.addAll(annotationSpecs)
        }

        public fun addAnnotation(annotationSpec: AnnotationSpec): Builder = apply {
            annotations.add(annotationSpec)
        }

        public fun addAnnotation(annotationSpec: ClassName): Builder = apply {
            addAnnotation(AnnotationSpec.builder(annotationSpec).build())
        }

        // TODO addAnnotation(Class)

        public fun addModifiers(vararg modifiers: Modifier): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        public fun addModifiers(modifiers: Iterable<Modifier>): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        public fun addModifier(modifier: Modifier): Builder = apply {
            modifiers.add(modifier)
        }

        public fun initializer(format: String, vararg args: Any): Builder = apply {
            initializer(CodeBlock(format, *args))
        }

        public fun initializer(codeBlock: CodeBlock): Builder = apply {
            check(initializer == null) { "initializer was already set" }
            initializer = codeBlock
        }

        public fun build(): FieldSpec {
            return FieldSpecImpl(
                type = type,
                name = name,
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = modifiers.toSet(),
                initializer = initializer ?: CodeBlock()
            )
        }
    }

    public companion object {
        @JvmStatic
        public fun builder(type: TypeName, name: String, vararg modifiers: Modifier): Builder {
            check(name.isSourceName()) { "not a valid name: $name" }
            return Builder(type, name).also {
                it.addModifiers(*modifiers)
            }
        }
    }
}
