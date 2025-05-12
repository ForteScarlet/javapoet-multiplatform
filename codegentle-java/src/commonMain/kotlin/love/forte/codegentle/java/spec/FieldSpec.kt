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

@file:JvmName("JavaFieldSpecs")
@file:JvmMultifileClass

package love.forte.codegentle.java.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaAnnotationRefCollectable
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.internal.FieldSpecImpl
import love.forte.codegentle.java.writer.JavaCodeWriter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 * A generated field declaration.
 */
public interface FieldSpec : JavaSpec, NamedSpec {
    override val name: String

    public val type: JavaTypeRef<*>

    public val javadoc: JavaCodeValue

    public val annotations: List<JavaAnnotationRef>

    public val modifiers: Set<JavaModifier>

    public val initializer: JavaCodeValue

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    @InternalJavaCodeGentleApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, emptySet())
    }

    @InternalJavaCodeGentleApi
    public fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier> = emptySet())
}

/**
 * @see FieldSpec.builder
 */
public inline fun FieldSpec(type: JavaTypeRef<*>, name: String, block: FieldSpecBuilder.() -> Unit = {}): FieldSpec =
    FieldSpecBuilder(type, name).also(block).build()


/**
 * @see FieldSpec.builder
 */
public inline fun FieldSpec(type: JavaTypeName, name: String, block: FieldSpecBuilder.() -> Unit = {}): FieldSpec =
    FieldSpec(type.javaRef(), name, block)

public inline fun FieldSpecBuilder.addJavadoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): FieldSpecBuilder = apply {
    addJavadoc(JavaCodeValue(format, block))
}

public inline fun FieldSpecBuilder.initializer(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): FieldSpecBuilder = apply {
    initializer(JavaCodeValue(format, block))
}

public class FieldSpecBuilder @PublishedApi internal constructor(
    public val type: JavaTypeRef<*>,
    public val name: String,
) : ModifierBuilderContainer,
    JavaAnnotationRefCollectable<FieldSpecBuilder> {
    internal val javadoc = JavaCodeValue.builder()
    internal val annotations = mutableListOf<JavaAnnotationRef>()
    internal val modifiers = ModifierSet()
    internal var initializer: JavaCodeValue? = null

    public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): FieldSpecBuilder = apply {
        addJavadoc(JavaCodeValue(format, *argumentParts))
    }

    public fun addJavadoc(codeValue: JavaCodeValue): FieldSpecBuilder = apply {
        javadoc.add(codeValue)
    }

    override fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): FieldSpecBuilder = apply {
        annotations.addAll(refs)
    }

    override fun addAnnotationRef(ref: JavaAnnotationRef): FieldSpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addModifiers(vararg modifiers: JavaModifier): FieldSpecBuilder = apply {
        this.modifiers.addAll(*modifiers)
    }

    override fun addModifiers(modifiers: Iterable<JavaModifier>): FieldSpecBuilder = apply {
        this.modifiers.addAll(modifiers)
    }

    override fun addModifier(modifier: JavaModifier): FieldSpecBuilder = apply {
        modifiers.add(modifier)
    }

    public fun initializer(format: String, vararg argumentParts: CodeArgumentPart): FieldSpecBuilder = apply {
        initializer(JavaCodeValue(format, *argumentParts))
    }

    public fun initializer(codeBlock: JavaCodeValue): FieldSpecBuilder = apply {
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
            initializer = initializer ?: JavaCodeValue.EMPTY
        )
    }
}
