package love.forte.javapoet.internal

import love.forte.javapoet.CodeBlock
import love.forte.javapoet.CodeBlock.Builder
import love.forte.javapoet.TypeName
import kotlin.math.min


/**
 *
 */
internal class CodeBlockImpl private constructor(
    private val formatParts: List<String>,
    private val args: List<Any?>,
) : CodeBlock {

    override val isEmpty: Boolean
        get() = formatParts.isEmpty()

    override fun addTo(builder: CodeBlock.Builder) {
        builder.formatParts.addAll(formatParts)
        builder.args.addAll(args)
    }

    override fun toBuilder(): CodeBlock.Builder {
        return CodeBlock.builder().also {
            it.add(this)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CodeBlockImpl) return false

        if (formatParts != other.formatParts) return false
        if (args != other.args) return false

        return true
    }

    override fun hashCode(): Int {
        var result = formatParts.hashCode()
        result = 31 * result + args.hashCode()
        return result
    }

    override fun toString(): String {
        // TODO
        return super.toString()
    }

    class Builder : CodeBlock.Builder() {
        override fun addNamed(format: String, arguments: Map<String, *>): Builder = apply {
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

        override fun add(format: String, vararg args: Any?): Builder = apply {
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

        override fun build(): CodeBlock {
            return CodeBlockImpl(formatParts.toList(), args.toList())
        }

        companion object {
            private fun Char.isNoArgPlaceholder(): Boolean {
                val c = this
                return c == '$' || c == '>' || c == '<' || c == '[' || c == ']' || c == 'W' || c == 'Z'
            }
        }
    }
}
