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

import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


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


    public class Builder private constructor(
    ) {
        internal val formatParts: MutableList<String> = mutableListOf()
        internal val args: MutableList<Any?> = mutableListOf()

        public val isEmpty: Boolean
            get() = formatParts.isEmpty()

        public fun addNamed(format: String, arguments: Map<String, *>): Builder = apply {
            TODO()
        }

        public fun add(format: String, vararg args: Any?): Builder = apply {
            TODO()
        }

        /**
         * @param controlFlow the control flow construct and its code, such as `"if (foo == 5)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun beginControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            TODO()
        }

        /**
         * @param controlFlow the control flow construct and its code, such as `"else if (foo == 10)"`.
         * Shouldn't contain braces or newline characters.
         */
        public fun nextControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            TODO()
        }

        public fun endControlFlow(): Builder = apply {
            TODO()
        }

        /**
         * @param controlFlow the optional control flow construct and its code, such as
         * `"while(foo == 20)"`. Only used for `"do/while"` control flows.
         */
        public fun endControlFlow(controlFlow: String, vararg args: Any?): Builder = apply { }
    }

    public companion object {
        internal val NAMED_ARGUMENT = Regex("\\$(?<argumentName>[\\w_]+):(?<typeChar>[\\w]).*")
        internal val LOWERCASE = Regex("[a-z]+[\\w_]*")

    }
}


public fun CodeBlock(format: String, vararg args: Any?): CodeBlock = TODO()


public fun Iterable<CodeBlock>.join(separator: String): CodeBlock = TODO()

// TODO Stream joining


