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

@file:JvmName("CodeBlocks")
@file:JvmMultifileClass

package love.forte.javapoet

import love.forte.javapoet.internal.CodeBlockImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A fragment of a .java file, potentially containing declarations, statements, and documentation.
 * Code blocks are not necessarily well-formed Java code, and are not validated. This class assumes
 * javac will check correctness later!
 *
 * Code blocks support placeholders like `java.text.Format`.
 * Where `String.format`
 * uses percent `%` to reference target values, this class uses dollar sign `$` and has
 * its own set of permitted placeholders:
 *
 * - `$L` emits a `literal` value with no escaping. Arguments for literals may be
 *   strings, primitives, [type declarations][TypeSpec],
 *   [annotations][AnnotationSpec] and even other code blocks.
 * - `$N` emits a `name`, using name collision avoidance where necessary. Arguments
 *   for names may be strings (actually any [character sequence][CharSequence]),
 *   [parameters][ParameterSpec], [fields][FieldSpec],
 *   [methods][MethodSpec], and [types][TypeSpec].
 * - `$S` escapes the value as a `string`, wraps it with double quotes, and emits
 *   that. For example, `6" sandwich` is emitted `"6\" sandwich"`.
 * - `$T` emits a `type` reference. Types will be imported if possible. Arguments
 *   for types may be `Class` classes, `TypeMirror` type mirrors,
 *   and `Element` elements.
 * - `$$` emits a dollar sign.
 * - `$W` emits a space or a newline, depending on its position on the line. This prefers
 *   to wrap lines before 100 columns.
 * - `$Z` acts as a zero-width space. This prefers to wrap lines before 100 columns.
 * - `$>` increases the indentation level.
 * - `$<` decreases the indentation level.
 * - `$[` begins a statement. For multiline statements, every line after the first line
 *   is double-indented.
 * - `$]` ends a statement.
 */
public interface CodeBlock {

    public val isEmpty: Boolean

    public fun addTo(builder: Builder)

    public fun toBuilder(): Builder

    public abstract class Builder {
        internal val formatParts: MutableList<String> = mutableListOf()
        internal val args: MutableList<Any?> = mutableListOf()

        public open val isEmpty: Boolean
            get() = formatParts.isEmpty()

        public abstract fun addNamed(format: String, arguments: Map<String, *>): Builder

        public abstract fun add(format: String, vararg args: Any?): Builder

        public open fun beginControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            add("$controlFlow {\n", *args)
            indent()
        }

        public open fun nextControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            unindent()
            add("} $controlFlow {\n", *args)
            indent()
        }

        public open fun endControlFlow(): Builder = apply {
            unindent()
            add("}\n")
        }

        /**
         * @param controlFlow the optional control flow construct and its code, such as
         * `"while(foo == 20)"`. Only used for `"do/while"` control flows.
         */
        public open fun endControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            unindent()
            add("} $controlFlow;\n", *args)
        }

        public open fun addStatement(format: String, vararg args: Any?): Builder = apply {
            add("$[")
            add(format, *args)
            add(";\n$]")
        }

        public open fun addStatement(codeBlock: CodeBlock): Builder = apply {
            addStatement("\$L", codeBlock)
        }

        public open fun add(codeBlock: CodeBlock): Builder = apply {
            codeBlock.addTo(this)
        }

        public open fun indent(): Builder = apply {
            formatParts.add("$>")
        }

        public open fun unindent(): Builder = apply {
            formatParts.add("$<")
        }

        public open fun clear(): Builder = apply {
            formatParts.clear()
            args.clear()
        }

        public abstract fun build(): CodeBlock
    }

    public companion object {
        internal val NAMED_ARGUMENT = Regex("\\$(?<argumentName>[\\w_]+):(?<typeChar>[\\w]).*")
        internal val LOWERCASE = Regex("[a-z]+[\\w_]*")

        /**
         * Create a new [Builder].
         */
        @JvmStatic
        public fun builder(): Builder = CodeBlockImpl.Builder()
    }
}


public fun CodeBlock(format: String, vararg args: Any?): CodeBlock {
    return CodeBlock.builder().add(format, *args).build()
}


// join?
// public fun Iterable<CodeBlock>.join(separator: String): CodeBlock = TODO()

// TODO Stream joining


