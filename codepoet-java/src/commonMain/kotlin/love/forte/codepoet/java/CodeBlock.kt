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

package love.forte.codepoet.java

import love.forte.codepoet.java.CodeBlock.Builder
import love.forte.codepoet.java.internal.CodeBlockImpl
import kotlin.js.JsName
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * A fragment of a .java file, potentially containing declarations, statements, and documentation.
 * Code blocks are not necessarily well-formed Java code, and are not validated. This class assumes
 * javac will check correctness later!
 *
 * CodeBlock support arguments, see [CodeValue], [CodePart].
 */
public interface CodeBlock : CodeEmitter {

    public val isEmpty: Boolean

    public fun addTo(builder: Builder)

    public fun toBuilder(): Builder

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, false)
    }

    public fun emit(codeWriter: CodeWriter, ensureTrailingNewline: Boolean = false)

    public class Builder internal constructor() {
        internal val codeParts: MutableList<CodePart> = mutableListOf()

        public val isEmpty: Boolean
            get() = codeParts.isEmpty()

        public fun add(codeValue: CodeValue): Builder = apply {
            codeParts.addAll(codeValue.parts)
        }

        public fun add(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            add(CodeValue(format, *argumentParts))
        }

        public fun add(codeBlock: CodeBlock): Builder = apply {
            codeBlock.addTo(this)
        }

        public fun addStatement(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            codeParts.add(CodePart.statementBegin())
            add(CodeValue(format, *argumentParts))
            add(";\n")
            codeParts.add(CodePart.statementEnd())
        }

        public fun addStatement(codeValue: CodeValue): Builder = apply {
            codeParts.add(CodePart.statementBegin())
            add(codeValue)
            add(";\n")
            codeParts.add(CodePart.statementEnd())
        }

        public fun addStatement(codeBlock: CodeBlock): Builder = apply {
            codeParts.add(CodePart.statementBegin())
            add(codeBlock)
            add(";\n")
            codeParts.add(CodePart.statementEnd())
        }

        public fun beginControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): Builder =
            apply {
                add("$controlFlow {\n", *argumentParts)
                indent()
            }

        public fun nextControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): Builder =
            apply {
                unindent()
                add("} $controlFlow {\n", *argumentParts)
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
        public fun endControlFlow(controlFlow: String, vararg argumentParts: CodeArgumentPart): Builder =
            apply {
                unindent()
                add("} $controlFlow;\n", *argumentParts)
            }

        public fun indent(): Builder = apply {
            codeParts.add(CodePart.indent())
        }

        public fun unindent(): Builder = apply {
            codeParts.add(CodePart.unindent())
        }

        public fun clear(): Builder = apply {
            codeParts.clear()
        }


        public fun build(): CodeBlock {
            return CodeBlockImpl(CodeValue(codeParts.toList()))
        }
    }

    public companion object {
        internal val EMPTY = CodeBlockImpl(CodeValue.EMPTY)

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
public fun CodeBlock(codeValue: CodeValue): CodeBlock {
    return CodeBlock.builder().add(codeValue).build()
}

@JvmName("of")
public fun CodeBlock(format: String, argumentPart: CodeArgumentPart): CodeBlock {
    return CodeBlock(CodeValue(format, argumentPart))
}

@JvmName("of")
public fun CodeBlock(format: String, vararg argumentParts: CodeArgumentPart): CodeBlock {
    return CodeBlock(CodeValue(format, *argumentParts))
}

@JvmName("of")
public fun CodeBlock(format: String, argumentParts: Iterable<CodeArgumentPart>): CodeBlock {
    return CodeBlock(CodeValue(format, argumentParts))
}

public inline fun CodeBlock(format: String, block: CodeValueBuilderDsl = {}): CodeBlock {
    return CodeBlock.builder().add(CodeValue(format, block)).build()
}

public inline fun CodeBlock(block: Builder.() -> Unit): CodeBlock =
    CodeBlock.builder().also(block).build()

// Builders

public inline fun Builder.add(format: String, block: CodeValueBuilderDsl = {}): Builder = apply {
    add(CodeValue(format, block))
}

public inline fun Builder.addStatement(format: String, block: CodeValueBuilderDsl = {}): Builder = apply {
    addStatement(CodeValue(format, block))
}


public inline fun Builder.beginControlFlow(controlFlow: String, block: CodeValueBuilderDsl = {}): Builder =
    apply {
        add("$controlFlow {\n") { block() }
        indent()
    }

public inline fun Builder.nextControlFlow(controlFlow: String, block: CodeValueBuilderDsl = {}): Builder =
    apply {
        unindent()
        add("} $controlFlow {\n") { block() }
        indent()
    }

/**
 * @param controlFlow the optional control flow construct and its code, such as
 * `"while(foo == 20)"`. Only used for `"do/while"` control flows.
 */
public inline fun Builder.endControlFlow(controlFlow: String, block: CodeValueBuilderDsl = {}): Builder =
    apply {
        unindent()
        add("} $controlFlow;\n") { block() }
    }
