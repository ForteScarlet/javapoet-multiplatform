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

package love.forte.codepoet.java

import love.forte.codepoet.java.FieldSpec.Builder
import love.forte.codepoet.java.internal.FieldSpecImpl
import love.forte.codepoet.java.internal.isSourceName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated field declaration.
 */
public interface FieldSpec : JavaCodeEmitter {

    public val type: TypeName

    public val name: String

    public val javadoc: CodeValue

    public val annotations: List<AnnotationSpec>

    public val modifiers: Set<Modifier>

    public val initializer: CodeValue

    public fun hasModifier(modifier: Modifier): Boolean = modifier in modifiers

    public fun toBuilder(): Builder

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, emptySet())
    }

    public fun emit(codeWriter: CodeWriter, implicitModifiers: Set<Modifier> = emptySet())

    public class Builder internal constructor(
        public val type: TypeName,
        public val name: String,
    ) : ModifierBuilderContainer {
        internal val javadoc = CodeValue.builder()
        internal val annotations = mutableListOf<AnnotationSpec>()
        internal val modifiers = ModifierSet()
        internal var initializer: CodeValue? = null

        public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addJavadoc(CodeValue(format, *argumentParts))
        }

        public fun addJavadoc(codeValue: CodeValue): Builder = apply {
            javadoc.add(codeValue)
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

        override fun addModifiers(vararg modifiers: Modifier): Builder = apply {
            this.modifiers.addAll(*modifiers)
        }

        override fun addModifiers(modifiers: Iterable<Modifier>): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifier(modifier: Modifier): Builder = apply {
            modifiers.add(modifier)
        }

        public fun initializer(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            initializer(CodeValue(format, *argumentParts))
        }

        public fun initializer(codeBlock: CodeValue): Builder = apply {
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
                initializer = initializer ?: CodeValue.EMPTY
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

/**
 * @see FieldSpec.builder
 */
public inline fun FieldSpec(type: TypeName, name: String, block: Builder.() -> Unit = {}): FieldSpec =
    FieldSpec.builder(type, name).also(block).build()


public inline fun Builder.addJavadoc(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    addJavadoc(CodeValue(format, block))
}

public inline fun Builder.initializer(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    initializer(CodeValue(format, block))
}
