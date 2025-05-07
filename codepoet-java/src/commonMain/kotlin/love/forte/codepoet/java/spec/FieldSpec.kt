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

package love.forte.codepoet.java.spec

import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.common.spec.NamedSpec
import love.forte.codepoet.java.*
import love.forte.codepoet.java.internal.isSourceName
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.internal.FieldSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated field declaration.
 */
public interface FieldSpec : JavaSpec, NamedSpec {
    override val name: String

    public val type: JavaTypeName

    public val javadoc: JavaCodeValue

    public val annotations: List<JavaAnnotationSpec>

    public val modifiers: Set<JavaModifier>

    public val initializer: JavaCodeValue

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    public fun toBuilder(): Builder

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, emptySet())
    }

    public fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier> = emptySet())

    public class Builder internal constructor(
        public val type: JavaTypeName,
        public val name: String,
    ) : ModifierBuilderContainer {
        internal val javadoc = JavaCodeValue.Companion.builder()
        internal val annotations = mutableListOf<JavaAnnotationSpec>()
        internal val modifiers = ModifierSet()
        internal var initializer: JavaCodeValue? = null

        public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addJavadoc(JavaCodeValue(format, *argumentParts))
        }

        public fun addJavadoc(codeValue: JavaCodeValue): Builder = apply {
            javadoc.add(codeValue)
        }

        public fun addAnnotations(annotationSpecs: Iterable<JavaAnnotationSpec>): Builder = apply {
            annotations.addAll(annotationSpecs)
        }

        public fun addAnnotations(vararg annotationSpecs: JavaAnnotationSpec): Builder = apply {
            annotations.addAll(annotationSpecs)
        }

        public fun addAnnotation(annotationSpec: JavaAnnotationSpec): Builder = apply {
            annotations.add(annotationSpec)
        }

        public fun addAnnotation(annotationSpec: JavaClassName): Builder = apply {
            addAnnotation(JavaAnnotationSpec.builder(annotationSpec).build())
        }

        override fun addModifiers(vararg modifiers: JavaModifier): Builder = apply {
            this.modifiers.addAll(*modifiers)
        }

        override fun addModifiers(modifiers: Iterable<JavaModifier>): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifier(modifier: JavaModifier): Builder = apply {
            modifiers.add(modifier)
        }

        public fun initializer(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            initializer(JavaCodeValue(format, *argumentParts))
        }

        public fun initializer(codeBlock: JavaCodeValue): Builder = apply {
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
                initializer = initializer ?: JavaCodeValue.Companion.EMPTY
            )
        }
    }

    public companion object {
        @JvmStatic
        public fun builder(type: JavaTypeName, name: String, vararg modifiers: JavaModifier): Builder {
            check(name.isSourceName()) { "not a valid name: $name" }
            return Builder(type, name).also {
                it.addModifiers(*modifiers)
            }
        }
    }
}

/**
 * @see FieldSpec.builder
 */
public inline fun FieldSpec(type: JavaTypeName, name: String, block: FieldSpec.Builder.() -> Unit = {}): FieldSpec =
    FieldSpec.builder(type, name).also(block).build()


public inline fun FieldSpec.Builder.addJavadoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): FieldSpec.Builder = apply {
    addJavadoc(JavaCodeValue(format, block))
}

public inline fun FieldSpec.Builder.initializer(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): FieldSpec.Builder = apply {
    initializer(JavaCodeValue(format, block))
}
