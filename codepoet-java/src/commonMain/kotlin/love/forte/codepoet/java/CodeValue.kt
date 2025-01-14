@file:JvmName("CodeValueKt")

package love.forte.codepoet.java

import love.forte.codepoet.java.internal.CodeValueImpl
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
public interface CodeValue : CodeEmitter {
    public val parts: List<CodePart>

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, ensureTrailingNewline = false)
    }

    public fun emit(codeWriter: CodeWriter, ensureTrailingNewline: Boolean)

    public class Builder internal constructor(public val format: String) : BuilderDsl {
        private val arguments = mutableListOf<CodeArgumentPart>()

        /**
         * Add a [CodeArgumentPart] for next argument placeholder.
         */
        public fun value(argument: CodeArgumentPart): Builder = apply {
            arguments.add(argument)
        }

        /**
         * Add some [CodeArgumentPart]s for next argument placeholder.
         */
        public fun values(vararg arguments: CodeArgumentPart): Builder = apply {
            this.arguments.addAll(arguments)
        }

        /**
         * Add some [CodeArgumentPart]s for next argument placeholder.
         */
        public fun values(arguments: Iterable<CodeArgumentPart>): Builder = apply {
            this.arguments.addAll(arguments)
        }

        public fun build(): CodeValue {
            if (arguments.isEmpty()) {
                return CodeValue(listOf(CodeSimplePart(format)))
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

            return CodeValueImpl(parts)
        }
    }

    public companion object {
        internal val EMPTY = CodeValueImpl(emptyList())

        @JvmStatic
        public fun builder(format: String): Builder = Builder(format)
    }
}

public typealias CodeValueBuilderDsl = CodeValue.Builder.() -> Unit

public fun CodeValue.Builder.skip(): CodeValue.Builder = value(CodePart.skip())
public fun CodeValue.Builder.literal(value: Any?): CodeValue.Builder = value(CodePart.literal(value))
public fun CodeValue.Builder.name(name: String?): CodeValue.Builder = value(CodePart.name(name = name))
public fun CodeValue.Builder.name(nameValue: Any): CodeValue.Builder = value(CodePart.name(nameValue = nameValue))
public fun CodeValue.Builder.string(value: String?): CodeValue.Builder = value(CodePart.string(value))
public fun CodeValue.Builder.type(type: TypeName): CodeValue.Builder = value(CodePart.type(type))
public fun CodeValue.Builder.indent(levels: Int = 1): CodeValue.Builder = value(CodePart.indent(levels))
public fun CodeValue.Builder.unindent(levels: Int = 1): CodeValue.Builder = value(CodePart.unindent(levels))
public fun CodeValue.Builder.statementBegin(): CodeValue.Builder = value(CodePart.statementBegin())
public fun CodeValue.Builder.statementEnd(): CodeValue.Builder = value(CodePart.statementEnd())
public fun CodeValue.Builder.wrappingSpace(): CodeValue.Builder = value(CodePart.wrappingSpace())
public fun CodeValue.Builder.zeroWidthSpace(): CodeValue.Builder = value(CodePart.zeroWidthSpace())

internal fun CodeValue(parts: List<CodePart>): CodeValue {
    return CodeValueImpl(parts)
}

public inline fun CodeValue(format: String, block: CodeValueBuilderDsl = {}): CodeValue {
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

