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
import love.forte.codepoet.java.ref.JavaAnnotationRef
import love.forte.codepoet.java.ref.JavaAnnotationRefCollectable
import love.forte.codepoet.java.ref.JavaTypeRef
import love.forte.codepoet.java.spec.internal.JavaParameterSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated parameter declaration.
 */
public interface JavaParameterSpec : JavaSpec, NamedSpec {
    override val name: String
    public val type: JavaTypeRef<*>

    public val annotations: List<JavaAnnotationRef>

    public val modifiers: Set<JavaModifier>

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    public val javadoc: JavaCodeValue

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, false)
    }

    public fun emit(codeWriter: JavaCodeWriter, vararg: Boolean = false)


    public companion object {
        @JvmStatic
        public fun builder(type: JavaTypeRef<*>, name: String, vararg modifiers: JavaModifier): JavaParameterSpecBuilder {
            return JavaParameterSpecBuilder(type, name).addModifiers(*modifiers)
        }
    }
}

public class JavaParameterSpecBuilder internal constructor(
    public val type: JavaTypeRef<*>,
    public val name: String
) : ModifierBuilderContainer, JavaAnnotationRefCollectable<JavaParameterSpecBuilder> {
    internal val javadoc = JavaCodeValue.Companion.builder()
    internal val annotations = mutableListOf<JavaAnnotationRef>()
    internal val modifiers = mutableSetOf<JavaModifier>()

    public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): JavaParameterSpecBuilder = apply {
        addJavadoc(JavaCodeValue(format, *argumentParts))
    }

    public fun addJavadoc(codeValue: JavaCodeValue): JavaParameterSpecBuilder = apply {
        javadoc.add(codeValue)
    }

    override fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): JavaParameterSpecBuilder = apply {
        this.annotations.addAll(refs)
    }

    override fun addAnnotationRef(ref: JavaAnnotationRef): JavaParameterSpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addModifiers(vararg modifiers: JavaModifier): JavaParameterSpecBuilder = apply {
        modifiers.forEach { checkModifier(it) }
        this.modifiers.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<JavaModifier>): JavaParameterSpecBuilder = apply {
        modifiers.forEach { checkModifier(it) }
        this.modifiers.addAll(modifiers)
    }

    override fun addModifier(modifier: JavaModifier): JavaParameterSpecBuilder = apply {
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


public inline fun JavaParameterSpec(
    type: JavaTypeRef<*>,
    name: String,
    block: JavaParameterSpecBuilder.() -> Unit = {}
): JavaParameterSpec = JavaParameterSpec.builder(type, name).apply(block).build()

public inline fun JavaParameterSpecBuilder.addJavadoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaParameterSpecBuilder = apply {
    addJavadoc(JavaCodeValue(format, block))
}
