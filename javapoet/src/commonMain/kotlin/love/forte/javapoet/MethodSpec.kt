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

package love.forte.javapoet

import love.forte.javapoet.internal.MethodSpecImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A generated constructor or method declaration.
 */
public interface MethodSpec {
    public val name: String
    public val javadoc: CodeBlock
    public val annotations: List<AnnotationSpec>
    public val modifiers: Set<Modifier>
    public val typeVariables: List<TypeVariableName>
    public val returnType: TypeName?
    public val parameters: List<ParameterSpec>
    public val isVarargs: Boolean
    public val exceptions: List<TypeName>
    public val code: CodeBlock
    public val defaultValue: CodeBlock

    public fun hasModifier(modifier: Modifier): Boolean = modifier in modifiers

    public val isConstructor: Boolean
        get() = name == CONSTRUCTOR

    public fun toBuilder(): Builder

    public class Builder internal constructor(
        public var name: String,
    ) {
        internal val javadoc = CodeBlock.builder()
        internal var returnType: TypeName? = null
        internal val code = CodeBlock.builder()
        internal var defaultValue: CodeBlock? = null
        internal val exceptions = linkedSetOf<TypeName>()

        public var isVarargs: Boolean = false

        public val typeVariables: MutableList<TypeVariableName> = mutableListOf()
        public val annotations: MutableList<AnnotationSpec> = mutableListOf()
        public val modifiers: MutableSet<Modifier> = linkedSetOf()
        public val parameters: MutableList<ParameterSpec> = mutableListOf()

        public fun addJavadoc(format: String?, vararg args: Any?): Builder = apply {
            javadoc.add(format!!, *args)
        }

        public fun addJavadoc(block: CodeBlock): Builder = apply {
            javadoc.add(block)
        }

        public fun addAnnotation(annotationSpec: AnnotationSpec): Builder = apply {
            annotations.add(annotationSpec)
        }

        public fun addAnnotations(vararg annotations: AnnotationSpec): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotations(annotations: Iterable<AnnotationSpec>): Builder = apply {
            this.annotations.addAll(annotations)
        }

        public fun addModifier(modifier: Modifier): Builder = apply {
            modifiers.add(modifier)
        }

        public fun addModifiers(modifiers: Iterable<Modifier>): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        public fun addModifiers(vararg modifiers: Modifier): Builder = apply {
            this.modifiers.addAll(modifiers)
        }

        public fun addTypeVariable(typeVariable: TypeVariableName): Builder = apply {
            typeVariables.add(typeVariable)
        }

        public fun addTypeVariables(vararg typeVariables: TypeVariableName): Builder = apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addTypeVariables(typeVariables: Iterable<TypeVariableName>): Builder = apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addParameter(parameter: ParameterSpec): Builder = apply {
            parameters.add(parameter)
        }

        public fun addParameters(parameters: Iterable<ParameterSpec>): Builder = apply {
            this.parameters.addAll(parameters)
        }

        public fun addParameters(vararg parameters: ParameterSpec): Builder = apply {
            this.parameters.addAll(parameters)
        }

        // TODO returns KType? KClass?

        public fun returns(typeName: TypeName): Builder = apply {
            check(name != CONSTRUCTOR) { "Constructor cannot have return type." }
            returnType = typeName
        }

        public fun varargs(): Builder = varargs(true)

        public fun varargs(varargs: Boolean): Builder = apply {
            this.isVarargs = varargs
        }

        public fun addException(exception: TypeName): Builder = apply {
            exceptions.add(exception)
        }

        public fun addExceptions(vararg exceptions: TypeName): Builder = apply {
            this.exceptions.addAll(exceptions)
        }

        public fun addExceptions(exceptions: Iterable<TypeName>): Builder = apply {
            this.exceptions.addAll(exceptions)
        }
        // TODO exceptions KType? KClass?

        public fun addCode(block: CodeBlock): Builder = apply {
            this.code.add(block)
        }

        public fun addCode(format: String, vararg args: Any?): Builder = apply {
            this.code.add(format, *args)
        }

        public fun addNamedCode(format: String, args: Map<String, *>): Builder = apply {
            this.code.addNamed(format, args)
        }

        public fun addComment(format: String, vararg args: Any?): Builder = apply {
            this.code.add("// $format\n", *args)
        }

        public fun defaultValue(format: String, vararg args: Any?): Builder =
            defaultValue(CodeBlock(format, *args))

        public fun defaultValue(codeBlock: CodeBlock): Builder = apply {
            check(defaultValue == null) { "`defaultValue` was already set" }
            this.defaultValue = codeBlock
        }


        /**
         * @param controlFlow the control flow construct and its code, such as `"if (foo == 5)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun beginControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            code.beginControlFlow(controlFlow, *args)
        }

        /**
         * @param codeBlock the control flow construct and its code, such as `"if (foo == 5)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun beginControlFlow(codeBlock: CodeBlock): Builder = apply {
            beginControlFlow("\$L", codeBlock)
        }

        /**
         * @param controlFlow the control flow construct and its code, such as `"else if (foo == 10)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun nextControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            code.nextControlFlow(controlFlow, *args)
        }

        /**
         * @param codeBlock the control flow construct and its code, such as `"else if (foo == 10)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun nextControlFlow(codeBlock: CodeBlock): Builder = apply {
            nextControlFlow("\$L", codeBlock)
        }

        public fun endControlFlow(): Builder = apply {
            code.endControlFlow()
        }


        public fun addStatement(format: String, vararg args: Any?): Builder = apply {
            code.addStatement(format, *args)
        }

        public fun addStatement(codeBlock: CodeBlock): Builder = apply {
            code.addStatement(codeBlock)
        }

        public fun build(): MethodSpec {
            return MethodSpecImpl(
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
                defaultValue = defaultValue ?: CodeBlock()
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
 * @see MethodSpec.methodBuilder
 */
public inline fun MethodSpec(name: String, block: MethodSpec.Builder.() -> Unit = {}): MethodSpec =
    MethodSpec.methodBuilder(name).apply(block).build()

/**
 * @see MethodSpec.constructorBuilder
 */
public inline fun MethodSpec(block: MethodSpec.Builder.() -> Unit = {}): MethodSpec =
    MethodSpec.constructorBuilder().apply(block).build()
