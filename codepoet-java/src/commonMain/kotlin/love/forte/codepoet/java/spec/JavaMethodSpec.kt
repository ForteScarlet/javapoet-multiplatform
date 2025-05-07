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

@file:JvmName("MethodSpecs")
@file:JvmMultifileClass

package love.forte.codepoet.java.spec

import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.common.spec.NamedSpec
import love.forte.codepoet.java.*
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.naming.JavaTypeVariableName
import love.forte.codepoet.java.spec.JavaMethodSpec.Builder
import love.forte.codepoet.java.spec.internal.JavaMethodSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated constructor or method declaration.
 */
public interface JavaMethodSpec : JavaSpec, NamedSpec {
    override val name: String
    public val javadoc: JavaCodeValue
    public val annotations: List<JavaAnnotationSpec>
    public val modifiers: Set<JavaModifier>
    public val typeVariables: List<JavaTypeVariableName>
    public val returnType: JavaTypeName?
    public val parameters: List<JavaParameterSpec>
    public val isVarargs: Boolean
    public val exceptions: List<JavaTypeName>
    public val code: JavaCodeValue
    public val defaultValue: JavaCodeValue

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    public val isConstructor: Boolean
        get() = name == CONSTRUCTOR

    public fun toBuilder(): Builder

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, null, emptySet())
    }

    public fun emit(codeWriter: JavaCodeWriter, name: String? = null, implicitModifiers: Set<JavaModifier> = emptySet())

    public class Builder internal constructor(
        public var name: String,
    ) : ModifierBuilderContainer {
        internal val javadoc = JavaCodeValue.Companion.builder()
        internal var returnType: JavaTypeName? = null

        @PublishedApi
        internal val code: JavaCodeValueBuilder = JavaCodeValue.Companion.builder()
        internal var defaultValue: JavaCodeValue? = null
        internal val exceptions = linkedSetOf<JavaTypeName>()

        public var isVarargs: Boolean = false

        public val typeVariables: MutableList<JavaTypeVariableName> = mutableListOf()
        public val annotations: MutableList<JavaAnnotationSpec> = mutableListOf()
        public val modifiers: MutableSet<JavaModifier> = linkedSetOf()
        public val parameters: MutableList<JavaParameterSpec> = mutableListOf()

        public fun addJavadoc(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addJavadoc(JavaCodeValue(format, *argumentParts))
        }

        public fun addJavadoc(codeValue: JavaCodeValue): Builder = apply {
            javadoc.add(codeValue)
        }

        public fun addAnnotation(annotationSpec: JavaAnnotationSpec): Builder = apply {
            annotations.add(annotationSpec)
        }

        public fun addAnnotations(vararg annotations: JavaAnnotationSpec): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotations(annotations: Iterable<JavaAnnotationSpec>): Builder = apply {
            this.annotations.addAll(annotations)
        }

        override fun addModifier(modifier: JavaModifier): Builder = apply {
            modifiers.add(modifier)
        }

        override fun addModifiers(modifiers: Iterable<JavaModifier>): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifiers(vararg modifiers: JavaModifier): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        public fun addTypeVariable(typeVariable: JavaTypeVariableName): Builder = apply {
            typeVariables.add(typeVariable)
        }

        public fun addTypeVariables(vararg typeVariables: JavaTypeVariableName): Builder = apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addTypeVariables(typeVariables: Iterable<JavaTypeVariableName>): Builder = apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addParameter(parameter: JavaParameterSpec): Builder = apply {
            parameters.add(parameter)
        }

        public fun addParameters(parameters: Iterable<JavaParameterSpec>): Builder = apply {
            this.parameters.addAll(parameters)
        }

        public fun addParameters(vararg parameters: JavaParameterSpec): Builder = apply {
            this.parameters.addAll(parameters)
        }

        // TODO returns KType? KClass?

        public fun returns(typeName: JavaTypeName): Builder = apply {
            check(name != CONSTRUCTOR) { "Constructor cannot have return type." }
            returnType = typeName
        }

        public fun varargs(): Builder = varargs(true)

        public fun varargs(varargs: Boolean): Builder = apply {
            this.isVarargs = varargs
        }

        public fun addException(exception: JavaTypeName): Builder = apply {
            exceptions.add(exception)
        }

        public fun addExceptions(vararg exceptions: JavaTypeName): Builder = apply {
            this.exceptions.addAll(exceptions)
        }

        public fun addExceptions(exceptions: Iterable<JavaTypeName>): Builder = apply {
            this.exceptions.addAll(exceptions)
        }
        // TODO exceptions KType? KClass?

        public fun addCode(codeValue: JavaCodeValue): Builder = apply {
            this.code.add(codeValue)
        }

        public fun addCode(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addCode(JavaCodeValue(format, *argumentParts))
        }

        public fun addComment(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addCode(JavaCodeValue("// $format\n", *argumentParts))
        }

        public fun defaultValue(format: String, vararg argumentParts: CodeArgumentPart): Builder =
            defaultValue(JavaCodeValue(format, *argumentParts))

        public fun defaultValue(codeBlock: JavaCodeValue): Builder = apply {
            check(defaultValue == null) { "`defaultValue` was already set" }
            this.defaultValue = codeBlock
        }


        /**
         * @param controlFlow the control flow construct and its code, such as `"if (foo == 5)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun beginControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            code.beginControlFlow(controlFlow, *argumentParts)
        }

        /**
         * @param codeBlock the control flow construct and its code, such as `"if (foo == 5)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun beginControlFlow(codeBlock: JavaCodeValue): Builder = apply {
            beginControlFlow("%V") { literal(codeBlock) }
        }

        /**
         * @param controlFlow the control flow construct and its code, such as `"else if (foo == 10)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun nextControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            code.nextControlFlow(controlFlow, *argumentParts)
        }

        /**
         * @param codeBlock the control flow construct and its code, such as `"else if (foo == 10)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun nextControlFlow(codeBlock: JavaCodeValue): Builder = apply {
            nextControlFlow("%V") { literal(codeBlock) }
        }

        public fun endControlFlow(): Builder = apply {
            code.endControlFlow()
        }

        public fun addStatement(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addStatement(JavaCodeValue(format, *argumentParts))
        }

        public fun addStatement(codeValue: JavaCodeValue): Builder = apply {
            code.addStatement(codeValue)
        }

        public fun build(): JavaMethodSpec {
            return JavaMethodSpecImpl(
                name = name,
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                returnType = returnType,
                parameters = parameters.toList(),
                isVarargs = isVarargs,
                exceptions = exceptions.toList(),
                code = code.build(),
                defaultValue = defaultValue ?: JavaCodeValue.Companion.EMPTY
            )
        }
    }

    public companion object {
        private const val CONSTRUCTOR: String = "<init>"

        @JvmStatic
        public fun methodBuilder(name: String): Builder = Builder(name)

        @JvmStatic
        public fun constructorBuilder(): Builder = Builder(CONSTRUCTOR)
    }

}

/**
 * @see JavaMethodSpec.methodBuilder
 */
public inline fun JavaMethodSpec(name: String, block: Builder.() -> Unit = {}): JavaMethodSpec =
    JavaMethodSpec.methodBuilder(name).apply(block).build()

/**
 * @see JavaMethodSpec.constructorBuilder
 */
public inline fun JavaMethodSpec(block: Builder.() -> Unit = {}): JavaMethodSpec =
    JavaMethodSpec.constructorBuilder().apply(block).build()

public inline fun Builder.addJavadoc(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    addJavadoc(JavaCodeValue(format, block))
}

public inline fun Builder.addCode(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    addCode(JavaCodeValue(format, block))
}

public inline fun Builder.addComment(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    addCode(JavaCodeValue("// $format\n", block))
}

public inline fun Builder.defaultValue(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder =
    defaultValue(JavaCodeValue(format, block))


/**
 * @param controlFlow the control flow construct and its code, such as `"if (foo == 5)"`.
 * Shouldn't contain braces or newline characters.
 */
public inline fun Builder.beginControlFlow(controlFlow: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder =
    apply {
        code.beginControlFlow(controlFlow, block)
    }

/**
 * @param controlFlow the control flow construct and its code, such as `"else if (foo == 10)"`.
 * Shouldn't contain braces or newline characters.
 */
public inline fun Builder.nextControlFlow(controlFlow: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder =
    apply {
        code.nextControlFlow(controlFlow, block)
    }

public inline fun Builder.addStatement(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    addStatement(JavaCodeValue(format, block))
}
