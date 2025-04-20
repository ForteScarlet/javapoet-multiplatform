@file:JvmName("CodeValues")

package love.forte.codepoet.java

import love.forte.codepoet.java.internal.JavaCodeValueImpl
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 * ```kotlin
 * CodeValue(format = "%V %V = %V;") {
 *     value(CodePart.type(ClassName("java.lang", "String")))
 *     value(CodePart.literal("value"))
 *     value(CodePart.string("Hello, World"))
 * }
 * ```
 * Will get:
 *
 * ```kotlin
 * java.lang.String value = "Hello, World";
 * ```
 *
 * @author ForteScarlet
 */
public interface CodeValue : JavaCodeEmitter {
    public val parts: List<CodePart>

    public val isEmpty: Boolean
        get() = parts.isEmpty()

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, ensureTrailingNewline = false)
    }

    public fun emit(codeWriter: CodeWriter, ensureTrailingNewline: Boolean)

    /**
     * ```Kotlin
     * builder {
     *  "%V, %V" {
     *    value(...) // for 1st `%V`
     *    value(...) // for 2nd `%V`
     *  }
     *  // Same as
     *  add("%V, %V") {
     *    value(...) // for 1st `%V`
     *    value(...) // for 2nd `%V`
     *  }
     * }
     * ```
     */
    public class Builder internal constructor() : BuilderDsl {
        private val parts = mutableListOf<CodePart>()

        @PublishedApi
        internal fun addParts(parts: List<CodePart>): Builder = apply {
            this.parts.addAll(parts)
        }

        public fun add(codeValue: CodeValue): Builder = apply {
            parts.addAll(codeValue.parts)
        }

        public fun add(format: String): Builder = apply {
            parts.add(CodeSimplePart(format))
        }

        public fun add(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addParts(builder(format).values(*argumentParts).parts())
        }

        public inline fun add(format: String, block: SingleFormatBuilder.() -> Unit): Builder = apply {
            addParts(builder(format).also(block).parts())
        }

        public inline operator fun String.invoke(block: SingleFormatBuilder.() -> Unit): Builder =
            add(this, block)

        public fun addStatement(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            parts.add(CodePart.statementBegin())
            add(CodeValue(format, *argumentParts))
            add(";\n")
            parts.add(CodePart.statementEnd())
        }

        public fun addStatement(codeValue: CodeValue): Builder = apply {
            parts.add(CodePart.statementBegin())
            add(codeValue)
            add(";\n")
            parts.add(CodePart.statementEnd())
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
            parts.add(CodePart.indent())
        }

        public fun unindent(): Builder = apply {
            parts.add(CodePart.unindent())
        }

        public fun clear(): Builder = apply {
            parts.clear()
        }

        public fun build(): CodeValue = JavaCodeValueImpl(parts)
    }

    /**
     * The Builder for [CodeValue] with single [format] target.
     *
     * ```Kotlin
     * builder("%V %V = %V;") {
     *   value(...) // For 1st `%V`
     *   value(...) // For 2nd `%V`
     *   value(...) // For 3rd `%V`
     * }.build()
     * ```
     *
     */
    public class SingleFormatBuilder internal constructor(public val format: String) : BuilderDsl {
        private val arguments = mutableListOf<CodeArgumentPart>()

        /**
         * Add a [CodeArgumentPart] for next argument placeholder.
         */
        public fun value(argument: CodeArgumentPart): SingleFormatBuilder = apply {
            arguments.add(argument)
        }

        /**
         * Add some [CodeArgumentPart]s for next argument placeholder.
         */
        public fun values(vararg arguments: CodeArgumentPart): SingleFormatBuilder = apply {
            this.arguments.addAll(arguments)
        }

        /**
         * Add some [CodeArgumentPart]s for next argument placeholder.
         */
        public fun values(arguments: Iterable<CodeArgumentPart>): SingleFormatBuilder = apply {
            this.arguments.addAll(arguments)
        }

        @PublishedApi
        internal fun parts(): List<CodePart> {
            if (arguments.isEmpty()) {
                return listOf(CodeSimplePart(format))
            }

            val argumentsStack = ArrayDeque(arguments)
            val parts = mutableListOf<CodePart>()

            var last = false

            var i = 0
            for (simplePart in format.splitToSequence(CodePart.PLACEHOLDER)) {
                if (simplePart.isNotEmpty()) {
                    parts.add(CodeSimplePart(simplePart))
                }
                // remove stack first
                val nextArg = argumentsStack.removeFirstOrNull()

                if (nextArg == null) {
                    check(!last) { "Miss argument in index $i" }
                    last = true
                } else {
                    parts.add(nextArg)
                }

                i++
            }

            check(argumentsStack.isEmpty()) { "${argumentsStack.size} redundant argument(s): $argumentsStack" }

            return parts
        }

        public fun build(): CodeValue = JavaCodeValueImpl(parts())
    }

    public companion object {
        internal val EMPTY = JavaCodeValueImpl(emptyList())

        @JvmStatic
        public fun builder(format: String): SingleFormatBuilder = SingleFormatBuilder(format)

        @JvmStatic
        public fun builder(): Builder = Builder()
    }
}

public operator fun CodeValue.plus(codeValue: CodeValue): CodeValue {
    return CodeValue(parts + codeValue.parts)
}

public typealias CodeValueSingleFormatBuilderDsl = CodeValue.SingleFormatBuilder.() -> Unit
public typealias CodeValueBuilderDsl = CodeValue.Builder.() -> Unit

// TODO auto-generated by KSP

public fun CodeValue.SingleFormatBuilder.skip(): CodeValue.SingleFormatBuilder = value(CodePart.skip())
public fun CodeValue.SingleFormatBuilder.literal(value: Any?): CodeValue.SingleFormatBuilder =
    value(CodePart.literal(value))

public fun CodeValue.SingleFormatBuilder.name(name: String?): CodeValue.SingleFormatBuilder =
    value(CodePart.name(name = name))

public fun CodeValue.SingleFormatBuilder.name(nameValue: Any): CodeValue.SingleFormatBuilder =
    value(CodePart.name(nameValue = nameValue))

public fun CodeValue.SingleFormatBuilder.string(value: String?): CodeValue.SingleFormatBuilder =
    value(CodePart.string(value))

public fun CodeValue.SingleFormatBuilder.type(type: TypeName): CodeValue.SingleFormatBuilder =
    value(CodePart.type(type))

// TODO JVM

public fun CodeValue.SingleFormatBuilder.indent(levels: Int = 1): CodeValue.SingleFormatBuilder =
    value(CodePart.indent(levels))

public fun CodeValue.SingleFormatBuilder.unindent(levels: Int = 1): CodeValue.SingleFormatBuilder =
    value(CodePart.unindent(levels))

public fun CodeValue.SingleFormatBuilder.statementBegin(): CodeValue.SingleFormatBuilder =
    value(CodePart.statementBegin())

public fun CodeValue.SingleFormatBuilder.statementEnd(): CodeValue.SingleFormatBuilder = value(CodePart.statementEnd())
public fun CodeValue.SingleFormatBuilder.wrappingSpace(): CodeValue.SingleFormatBuilder =
    value(CodePart.wrappingSpace())

public fun CodeValue.SingleFormatBuilder.zeroWidthSpace(): CodeValue.SingleFormatBuilder =
    value(CodePart.zeroWidthSpace())

public fun CodeValue.SingleFormatBuilder.otherCodeValue(value: CodeValue): CodeValue.SingleFormatBuilder =
    value(CodePart.otherCodeValue(value))

internal fun CodeValue(parts: List<CodePart>): CodeValue {
    return JavaCodeValueImpl(parts)
}

internal fun CodeValue(part: CodePart): CodeValue {
    return JavaCodeValueImpl(listOf(part))
}

public inline fun CodeValue(format: String, block: CodeValueSingleFormatBuilderDsl = {}): CodeValue {
    return CodeValue.builder(format).also(block).build()
}

@JvmName("of")
public fun CodeValue(format: String, argumentPart: CodeArgumentPart): CodeValue {
    return CodeValue(format) {
        value(argumentPart)
    }
}

@JvmName("of")
public fun CodeValue(format: String, vararg argumentParts: CodeArgumentPart): CodeValue {
    return CodeValue(format) {
        values(*argumentParts)
    }
}

@JvmName("of")
public fun CodeValue(format: String, argumentParts: Iterable<CodeArgumentPart>): CodeValue {
    return CodeValue(format) {
        values(argumentParts)
    }
}

public inline fun CodeValue(block: CodeValueBuilderDsl): CodeValue {
    return CodeValue.builder().also(block).build()
}

// Builders

public inline fun CodeValue.Builder.addStatement(format: String, block: CodeValueSingleFormatBuilderDsl = {}): CodeValue.Builder = apply {
    addStatement(CodeValue(format, block))
}


public inline fun CodeValue.Builder.beginControlFlow(controlFlow: String, block: CodeValueSingleFormatBuilderDsl = {}): CodeValue.Builder =
    apply {
        add("$controlFlow {\n") { block() }
        indent()
    }

public inline fun CodeValue.Builder.nextControlFlow(controlFlow: String, block: CodeValueSingleFormatBuilderDsl = {}): CodeValue.Builder =
    apply {
        unindent()
        add("} $controlFlow {\n") { block() }
        indent()
    }

/**
 * @param controlFlow the optional control flow construct and its code, such as
 * `"while(foo == 20)"`. Only used for `"do/while"` control flows.
 */
public inline fun CodeValue.Builder.endControlFlow(controlFlow: String, block: CodeValueSingleFormatBuilderDsl = {}): CodeValue.Builder =
    apply {
        unindent()
        add("} $controlFlow;\n") { block() }
    }
