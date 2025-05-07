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

package love.forte.codepoet.java.spec

import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.common.spec.NamedSpec
import love.forte.codepoet.java.*
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.internal.JavaParameterSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated parameter declaration.
 */
public interface JavaParameterSpec : JavaSpec, NamedSpec {
    override val name: String
    public val type: JavaTypeName

    public val annotations: List<JavaAnnotationSpec>

    public val modifiers: Set<JavaModifier>

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    public val javadoc: JavaCodeValue

    public fun toBuilder(): Builder

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, false)
    }

    public fun emit(codeWriter: JavaCodeWriter, vararg: Boolean = false)


    public class Builder internal constructor(
        public val type: JavaTypeName,
        public val name: String
    ) : ModifierBuilderContainer {
        internal val javadoc = JavaCodeValue.Companion.builder()
        internal val annotations = mutableListOf<JavaAnnotationSpec>()
        internal val modifiers = mutableSetOf<JavaModifier>()

        public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addJavadoc(JavaCodeValue(format, *argumentParts))
        }

        public fun addJavadoc(codeValue: JavaCodeValue): Builder = apply {
            javadoc.add(codeValue)
        }

        public fun addAnnotations(vararg annotations: JavaAnnotationSpec): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotations(annotations: Iterable<JavaAnnotationSpec>): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotation(annotationSpec: JavaAnnotationSpec): Builder = apply {
            annotations.add(annotationSpec)
        }

        public fun addAnnotation(annotation: JavaClassName): Builder =
            addAnnotation(JavaAnnotationSpec(annotation))

        override fun addModifiers(vararg modifiers: JavaModifier): Builder = apply {
            modifiers.forEach { checkModifier(it) }
            this.modifiers.addAll(modifiers)
        }

        override fun addModifiers(modifiers: Iterable<JavaModifier>): Builder = apply {
            modifiers.forEach { checkModifier(it) }
            this.modifiers.addAll(modifiers)
        }

        override fun addModifier(modifier: JavaModifier): Builder = apply {
            checkModifier(modifier)
            modifiers.add(modifier)
        }

        /**
         * Only support [final][JavaModifier.FINAL]
         */
        private fun checkModifier(modifier: JavaModifier) {
            check(modifier == JavaModifier.FINAL) {
                "Unexpected parameter modifier: $modifier"
            }
        }

        public fun build(): JavaParameterSpec {
            return JavaParameterSpecImpl(
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
        public fun builder(type: JavaTypeName, name: String, vararg modifiers: JavaModifier): Builder {
            return Builder(type, name).addModifiers(*modifiers)
        }
    }
}

public inline fun JavaParameterSpec(
    type: JavaTypeName,
    name: String,
    block: JavaParameterSpec.Builder.() -> Unit = {}
): JavaParameterSpec = JavaParameterSpec.builder(type, name).apply(block).build()

public inline fun JavaParameterSpec.Builder.addJavadoc(format: String, block: CodeValueSingleFormatBuilderDsl = {}): JavaParameterSpec.Builder = apply {
    addJavadoc(JavaCodeValue(format, block))
}
