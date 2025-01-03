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
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic
import kotlin.math.min


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

    public class Builder internal constructor() {
        internal val formatParts: MutableList<String> = mutableListOf()
        internal val args: MutableList<Any?> = mutableListOf()

        public val isEmpty: Boolean
            get() = formatParts.isEmpty()

        public fun addNamed(format: String, arguments: Map<String, *>): Builder = apply {
            var p = 0
            for (argument in arguments.keys) {
                check(CodeBlock.LOWERCASE.matches(argument)) {
                    "argument '$argument' must start with a lowercase character"
                }
            }

            while (p < format.length) {
                val nextP = format.indexOf("$", p)
                if (nextP == -1) {
                    formatParts.add(format.substring(p))
                    break
                }

                if (p != nextP) {
                    formatParts.add(format.substring(p, nextP))
                    p = nextP
                }

                var matcher: MatchResult? = null
                val colon = format.indexOf(':', p)
                if (colon != -1) {
                    val endIndex = min(colon + 2, format.length)
                    matcher = CodeBlock.NAMED_ARGUMENT.matchEntire(format.substring(p, endIndex))
                }
                if (matcher != null) { // TODO && matcher.lookingAt()
                    matcher.groups
                    val argumentName: String = matcher.groups["argumentName"]!!.value
                    check(arguments.containsKey(argumentName)) {
                        "Missing named argument for $${argumentName}"
                    }
                    val formatChar: Char = matcher.groups["typeChar"]!!.value[0]

                    addArgument(format, formatChar, arguments[argumentName])
                    formatParts.add("$$formatChar")
                    // p += matcher.regionEnd()
                    p += matcher.range.last
                } else {
                    check(p < format.length - 1) { "dangling $ at end" }
                    check(format[p + 1].isNoArgPlaceholder()) {
                        "unknown format $${format[p + 1]} at ${p + 1} in '$format'"
                    }
                    formatParts.add(format.substring(p, p + 2))
                    p += 2
                }
            }
        }

        public fun add(format: String, vararg args: Any?): Builder = apply {
            var hasRelative = false
            var hasIndexed = false

            var relativeParameterCount = 0
            val indexedParameterCount = IntArray(args.size)

            var p = 0
            while (p < format.length) {
                if (format[p] != '$') {
                    var nextP = format.indexOf('$', p + 1)
                    if (nextP == -1) {
                        nextP = format.length
                    }
                    formatParts.add(format.substring(p, nextP))
                    p = nextP
                    continue
                }

                // Is `$`
                p++

                val indexStart = p
                var c: Char
                do {
                    check(p < format.length) { "dangling format characters in '$format'" }
                    c = format[p++]
                } while (c in '0'..'9')
                val indexEnd = p - 1

                if (c.isNoArgPlaceholder()) {
                    check(indexStart == indexEnd) { "$$, $>, $<, $[, $], \$W, and \$Z may not have an index" }
                    formatParts.add("$$c")
                }

                // Find either the indexed argument, or the relative argument. (0-based).
                var index: Int
                if (indexStart < indexEnd) {
                    index = format.substring(indexStart, indexEnd).toInt() - 1
                    hasIndexed = true
                    if (args.isNotEmpty()) {
                        indexedParameterCount[index % args.size]++ // modulo is needed, checked below anyway
                    }
                } else {
                    index = relativeParameterCount
                    hasRelative = true
                    relativeParameterCount++
                }

                check(index >= 0 && index < args.size) {
                    "index ${index + 1} for '${format.substring(indexStart - 1, indexEnd + 1)}' " +
                        "not in range (received ${args.size} arguments)"
                }

                check(!hasIndexed || !hasRelative) { "cannot mix indexed and positional parameters" }

                addArgument(format, c, args[index])

                formatParts.add("$$c")
            }
        }

        private fun addArgument(format: String, c: Char, arg: Any?) {
            when (c) {
                'N' -> args.add(argToName(arg))
                'L' -> args.add(arg) // arg to literal
                'S' -> args.add(arg?.toString()) // arg to string
                'T' -> args.add(argToType(arg))
                else -> throw IllegalArgumentException("invalid format string: '$format'")
            }
        }

        private fun argToName(o: Any?): String {
            if (o is CharSequence) return o.toString()
            // TODO
            // if (o is ParameterSpec) return (o as ParameterSpec).name
            // if (o is FieldSpec) return (o as FieldSpec).name
            // if (o is MethodSpec) return (o as MethodSpec).name
            // if (o is TypeSpec) return (o as TypeSpec).name
            throw IllegalArgumentException("expected name but was $o")
        }

        private fun argToType(o: Any?): TypeName {
            if (o is TypeName) return o
            // TODO
            // if (o is TypeMirror) return com.squareup.javapoet.TypeName.get(o as TypeMirror)
            // if (o is javax.lang.model.element.Element) return com.squareup.javapoet.TypeName.get((o as javax.lang.model.element.Element).asType())
            // if (o is java.lang.reflect.Type) return com.squareup.javapoet.TypeName.get(o as java.lang.reflect.Type)
            throw IllegalArgumentException("expected type but was $o")
        }

        public fun beginControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            add("$controlFlow {\n", *args)
            indent()
        }

        public fun nextControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            unindent()
            add("} $controlFlow {\n", *args)
            indent()
        }

        public fun endControlFlow(): Builder = apply {
            unindent()
            add("}\n")
        }

        /**
         * @param controlFlow the optional control flow construct and its code, such as
         * `"while(foo == 20)"`. Only used for `"do/while"` control flows.
         */
        public fun endControlFlow(controlFlow: String, vararg args: Any?): Builder = apply {
            unindent()
            add("} $controlFlow;\n", *args)
        }

        public fun addStatement(format: String, vararg args: Any?): Builder = apply {
            add("$[")
            add(format, *args)
            add(";\n$]")
        }

        public fun addStatement(codeBlock: CodeBlock): Builder = apply {
            addStatement("\$L", codeBlock)
        }

        public fun add(codeBlock: CodeBlock): Builder = apply {
            codeBlock.addTo(this)
        }

        public fun indent(): Builder = apply {
            formatParts.add("$>")
        }

        public fun unindent(): Builder = apply {
            formatParts.add("$<")
        }

        public fun clear(): Builder = apply {
            formatParts.clear()
            args.clear()
        }


        public fun build(): CodeBlock {
            return CodeBlockImpl(formatParts.toList(), args.toList())
        }

        public companion object {
            private fun Char.isNoArgPlaceholder(): Boolean {
                val c = this
                return c == '$' || c == '>' || c == '<' || c == '[' || c == ']' || c == 'W' || c == 'Z'
            }
        }
    }

    public companion object {
        internal val NAMED_ARGUMENT = Regex("\\$(?<argumentName>[\\w_]+):(?<typeChar>[\\w]).*")
        internal val LOWERCASE = Regex("[a-z]+[\\w_]*")

        internal val EMPTY = CodeBlockImpl(emptyList(), emptyList())

        /**
         * Create a new [Builder].
         */
        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

@JvmName("of")
@JsName("emptyCodeBlock")
public fun CodeBlock(): CodeBlock = CodeBlock.EMPTY

@JvmName("of")
public fun CodeBlock(format: String, vararg args: Any?): CodeBlock {
    return CodeBlock.builder().add(format, *args).build()
}

// join?
// public fun Iterable<CodeBlock>.join(separator: String): CodeBlock = TODO()

// TODO Stream joining


