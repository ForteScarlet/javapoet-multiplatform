package love.forte.javapoet.internal

import love.forte.javapoet.*


/**
 *
 */
internal class CodeBlockImpl(
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

    override fun emit(codeWriter: CodeWriter, ensureTrailingNewline: Boolean) {
        var a = 0
        var deferredTypeName: ClassName? = null
        val partIterator = formatParts.listIterator()
        while (partIterator.hasNext()) {
            when (val part = partIterator.next()) {
                "\$L" -> codeWriter.emitLiteral(args[a++])
                "\$N" -> codeWriter.emitAndIndent(args[a++] as String)
                "\$S" -> {
                    val string = args[a++]
                    // Emit null as a literal null: no quotes.
                    codeWriter.emitAndIndent(
                        string?.toString()?.literalWithDoubleQuotes(codeWriter.indent)
                            ?: "null"
                    )
                }

                "\$T" -> {
                    val typeName: TypeName = args[a++] as TypeName
                    // defer "typeName.emit(this)" if next format part will be handled by the default case
                    if (typeName is ClassName && partIterator.hasNext()) {
                        if (!formatParts[partIterator.nextIndex()].startsWith("$")) {
                            val candidate: ClassName = typeName
                            if (codeWriter.staticImportClassNames.contains(candidate.canonicalName)) {
                                check(deferredTypeName == null) { "pending type for static import?!" }
                                deferredTypeName = candidate
                                continue
                            }
                        }
                    }
                    typeName.emit(codeWriter)
                }

                "$$" -> codeWriter.emitAndIndent("$")
                "$>" -> codeWriter.indent()
                "$<" -> codeWriter.unindent()
                "$[" -> {
                    check(codeWriter.statementLine == -1) {
                        "statement enter $[ followed by statement enter $["
                    }
                    codeWriter.statementLine = 0
                }

                "$]" -> {
                    check(codeWriter.statementLine != -1) {
                        "statement exit $] has no matching statement enter $["
                    }
                    if (codeWriter.statementLine > 0) {
                        codeWriter.unindent(2) // End a multi-line statement. Decrease the indentation level.
                    }
                    codeWriter.statementLine = -1
                }

                "\$W" -> codeWriter.out.wrappingSpace(codeWriter.indentLevel + 2)
                "\$Z" -> codeWriter.out.zeroWidthSpace(codeWriter.indentLevel + 2)
                else -> {
                    // handle deferred type
                    if (deferredTypeName != null) {
                        if (part.startsWith(".")) {
                            if (codeWriter.emitStaticImportMember(deferredTypeName.canonicalName, part)) {
                                // okay, static import hit and all was emitted, so clean-up and jump to next part
                                deferredTypeName = null
                                continue
                            }
                        }
                        deferredTypeName.emit(codeWriter)
                        deferredTypeName = null
                    }
                    codeWriter.emitAndIndent(part)
                }
            }
        }

        if (ensureTrailingNewline && codeWriter.out.lastChar != '\n') {
            codeWriter.emit("\n")
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
        return buildString {
            emit(CodeWriter.create(this))
        }
    }
}
