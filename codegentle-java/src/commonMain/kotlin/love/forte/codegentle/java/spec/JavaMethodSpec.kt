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

@file:JvmName("JavaMethodSpecs")
@file:JvmMultifileClass

package love.forte.codegentle.java.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.ref.TypeRefBuilder
import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.*
import love.forte.codegentle.java.ref.JavaTypeNameRefStatus
import love.forte.codegentle.java.ref.JavaTypeNameRefStatusBuilder
import love.forte.codegentle.java.ref.javaRef
import love.forte.codegentle.java.spec.internal.JavaMethodSpecImpl
import love.forte.codegentle.java.writer.JavaCodeWriter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated constructor or method declaration.
 */
public interface JavaMethodSpec : JavaSpec, NamedSpec {
    override val name: String
    public val javadoc: CodeValue
    public val annotations: List<AnnotationRef>
    public val modifiers: Set<JavaModifier>
    public val typeVariables: List<TypeRef<TypeVariableName>>
    public val returnType: TypeRef<*>?
    public val parameters: List<JavaParameterSpec>
    public val isVarargs: Boolean
    public val exceptions: List<TypeRef<*>>
    public val code: CodeValue
    public val defaultValue: CodeValue

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    public val isConstructor: Boolean
        get() = name == CONSTRUCTOR

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, null, emptySet())
    }

    public fun emit(codeWriter: JavaCodeWriter, name: String? = null, implicitModifiers: Set<JavaModifier> = emptySet())

    public companion object {
        internal const val CONSTRUCTOR: String = "<init>"

        @JvmStatic
        public fun methodBuilder(name: String): JavaMethodSpecBuilder = JavaMethodSpecBuilder(name)

        @JvmStatic
        public fun constructorBuilder(): JavaMethodSpecBuilder = JavaMethodSpecBuilder(CONSTRUCTOR)
    }

}

/**
 * @see JavaMethodSpec.methodBuilder
 */
public inline fun JavaMethodSpec(name: String, block: JavaMethodSpecBuilder.() -> Unit = {}): JavaMethodSpec =
    JavaMethodSpec.methodBuilder(name).apply(block).build()

/**
 * @see JavaMethodSpec.constructorBuilder
 */
public inline fun JavaMethodSpec(block: JavaMethodSpecBuilder.() -> Unit = {}): JavaMethodSpec =
    JavaMethodSpec.constructorBuilder().apply(block).build()

public class JavaMethodSpecBuilder internal constructor(
    public var name: String,
) : BuilderDsl,
    JavaModifierBuilderContainer,
    AnnotationRefCollectable<JavaMethodSpecBuilder> {
    internal val javadoc = JavaCodeValue.builder()
    internal var returnType: TypeRef<*>? = null

    @PublishedApi
    internal val code: JavaCodeValueBuilder = JavaCodeValue.builder()
    internal var defaultValue: JavaCodeValue? = null
    internal val exceptions = linkedSetOf<TypeRef<*>>()

    public var isVarargs: Boolean = false

    internal val typeVariables: MutableList<TypeRef<TypeVariableName>> = mutableListOf()
    internal val annotations: MutableList<AnnotationRef> = mutableListOf()
    internal val modifiers = JavaModifierSet()
    internal val parameters: MutableList<JavaParameterSpec> = mutableListOf()

    public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder = apply {
        addJavadoc(JavaCodeValue(format, *argumentParts))
    }

    public fun addJavadoc(codeValue: JavaCodeValue): JavaMethodSpecBuilder = apply {
        javadoc.add(codeValue)
    }

    override fun addAnnotationRef(ref: AnnotationRef): JavaMethodSpecBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): JavaMethodSpecBuilder = apply {
        this.annotations.addAll(refs)
    }

    override fun addModifier(modifier: JavaModifier): JavaMethodSpecBuilder = apply {
        modifiers.add(modifier)
    }

    override fun addModifiers(modifiers: Iterable<JavaModifier>): JavaMethodSpecBuilder = apply {
        this.modifiers.addAll(modifiers)
    }

    override fun addModifiers(vararg modifiers: JavaModifier): JavaMethodSpecBuilder = apply {
        this.modifiers.addAll(*modifiers)
    }

    public fun addTypeVariable(typeVariable: TypeRef<TypeVariableName>): JavaMethodSpecBuilder = apply {
        typeVariables.add(typeVariable)
    }

    public fun addTypeVariables(vararg typeVariables: TypeRef<TypeVariableName>): JavaMethodSpecBuilder =
        apply {
            this.typeVariables.addAll(typeVariables)
        }

    public fun addTypeVariables(typeVariables: Iterable<TypeRef<TypeVariableName>>): JavaMethodSpecBuilder =
        apply {
            this.typeVariables.addAll(typeVariables)
        }

    public fun addParameter(parameter: JavaParameterSpec): JavaMethodSpecBuilder = apply {
        parameters.add(parameter)
    }

    public fun addParameters(parameters: Iterable<JavaParameterSpec>): JavaMethodSpecBuilder = apply {
        this.parameters.addAll(parameters)
    }

    public fun addParameters(vararg parameters: JavaParameterSpec): JavaMethodSpecBuilder = apply {
        this.parameters.addAll(parameters)
    }

    // TODO returns KType? KClass?

    public fun returns(typeRef: TypeRef<*>): JavaMethodSpecBuilder = apply {
        check(name != JavaMethodSpec.CONSTRUCTOR) { "Constructor cannot have return type." }
        returnType = typeRef
    }

    public fun varargs(): JavaMethodSpecBuilder = varargs(true)

    public fun varargs(varargs: Boolean): JavaMethodSpecBuilder = apply {
        this.isVarargs = varargs
    }

    public fun addException(exception: TypeRef<*>): JavaMethodSpecBuilder = apply {
        exceptions.add(exception)
    }

    public fun addExceptions(vararg exceptions: TypeRef<*>): JavaMethodSpecBuilder = apply {
        this.exceptions.addAll(exceptions)
    }

    public fun addExceptions(exceptions: Iterable<TypeRef<*>>): JavaMethodSpecBuilder = apply {
        this.exceptions.addAll(exceptions)
    }
    // TODO exceptions KType? KClass?

    public fun addCode(codeValue: JavaCodeValue): JavaMethodSpecBuilder = apply {
        this.code.add(codeValue)
    }

    public fun addCode(format: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder = apply {
        addCode(JavaCodeValue(format, *argumentParts))
    }

    public fun addComment(format: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder = apply {
        addCode(JavaCodeValue("// $format\n", *argumentParts))
    }

    public fun defaultValue(format: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder =
        defaultValue(JavaCodeValue(format, *argumentParts))

    public fun defaultValue(codeBlock: JavaCodeValue): JavaMethodSpecBuilder = apply {
        check(defaultValue == null) { "`defaultValue` was already set" }
        this.defaultValue = codeBlock
    }


    /**
     * @param controlFlow the control flow construct and its code, such as `"if (foo == 5)"`.
     * Shouldn't contain braces or newline characters.
     */
    public fun beginControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder =
        apply {
            code.beginControlFlow(controlFlow, *argumentParts)
        }

    /**
     * @param codeBlock the control flow construct and its code, such as `"if (foo == 5)"`.
     * Shouldn't contain braces or newline characters.
     */
    public fun beginControlFlow(codeBlock: JavaCodeValue): JavaMethodSpecBuilder = apply {
        beginControlFlow("%V") { literal(codeBlock) }
    }

    /**
     * @param controlFlow the control flow construct and its code, such as `"else if (foo == 10)"`.
     * Shouldn't contain braces or newline characters.
     */
    public fun nextControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder =
        apply {
            code.nextControlFlow(controlFlow, *argumentParts)
        }

    /**
     * @param codeBlock the control flow construct and its code, such as `"else if (foo == 10)"`.
     * Shouldn't contain braces or newline characters.
     */
    public fun nextControlFlow(codeBlock: JavaCodeValue): JavaMethodSpecBuilder = apply {
        nextControlFlow("%V") { literal(codeBlock) }
    }

    public fun endControlFlow(): JavaMethodSpecBuilder = apply {
        code.endControlFlow()
    }

    public fun addStatement(format: String, vararg argumentParts: CodeArgumentPart): JavaMethodSpecBuilder = apply {
        addStatement(JavaCodeValue(format, *argumentParts))
    }

    public fun addStatement(codeValue: JavaCodeValue): JavaMethodSpecBuilder = apply {
        code.addStatement(codeValue)
    }

    public fun build(): JavaMethodSpec {
        return JavaMethodSpecImpl(
            name = name,
            javadoc = javadoc.build(),
            annotations = annotations.toList(),
            modifiers = modifiers.copy(),
            typeVariables = typeVariables.toList(),
            returnType = returnType,
            parameters = parameters.toList(),
            isVarargs = isVarargs,
            exceptions = exceptions.toList(),
            code = code.build(),
            defaultValue = defaultValue ?: JavaCodeValue.EMPTY
        )
    }
}

public inline fun <T : TypeVariableName> JavaMethodSpecBuilder.addTypeVariable(
    variableName: T,
    block: TypeRefBuilder<T, JavaTypeNameRefStatus, JavaTypeNameRefStatusBuilder>.() -> Unit = {}
): JavaMethodSpecBuilder =
    addException(variableName.javaRef(block))

public inline fun <T : TypeName> JavaMethodSpecBuilder.addException(
    type: T,
    block: TypeRefBuilder<T, JavaTypeNameRefStatus, JavaTypeNameRefStatusBuilder>.() -> Unit = {}
): JavaMethodSpecBuilder =
    addException(type.javaRef(block))

public inline fun <T : TypeName> JavaMethodSpecBuilder.returns(
    type: T,
    block: TypeRefBuilder<T, JavaTypeNameRefStatus, JavaTypeNameRefStatusBuilder>.() -> Unit = {}
): JavaMethodSpecBuilder =
    returns(type.javaRef(block))


public inline fun JavaMethodSpecBuilder.addJavadoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder = apply {
    addJavadoc(JavaCodeValue(format, block))
}

public inline fun JavaMethodSpecBuilder.addCode(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder = apply {
    addCode(JavaCodeValue(format, block))
}

public inline fun JavaMethodSpecBuilder.addComment(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder = apply {
    addCode(JavaCodeValue("// $format\n", block))
}

public inline fun JavaMethodSpecBuilder.defaultValue(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder =
    defaultValue(JavaCodeValue(format, block))

/**
 * @param controlFlow the control flow construct and its code, such as `"if (foo == 5)"`.
 * Shouldn't contain braces or newline characters.
 */
public inline fun JavaMethodSpecBuilder.beginControlFlow(
    controlFlow: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder =
    apply {
        code.beginControlFlow(controlFlow, block)
    }

/**
 * @param controlFlow the control flow construct and its code, such as `"else if (foo == 10)"`.
 * Shouldn't contain braces or newline characters.
 */
public inline fun JavaMethodSpecBuilder.nextControlFlow(
    controlFlow: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder =
    apply {
        code.nextControlFlow(controlFlow, block)
    }

public inline fun JavaMethodSpecBuilder.addStatement(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaMethodSpecBuilder = apply {
    addStatement(JavaCodeValue(format, block))
}
