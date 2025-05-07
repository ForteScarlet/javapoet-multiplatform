package love.forte.codepoet.java.internal

import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.common.code.CodePart
import love.forte.codepoet.common.code.CodeSimplePart
import love.forte.codepoet.common.code.CodeValue
import love.forte.codepoet.common.naming.canonicalName
import love.forte.codepoet.java.JavaCodeValue
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.literalWithDoubleQuotes
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaTypeName


internal fun CodeValue.emit0(codeWriter: JavaCodeWriter, ensureTrailingNewline: Boolean = false) {
    var deferredTypeName: JavaClassName? = null
    val iterator = parts.listIterator()

    while (iterator.hasNext()) {
        when (val part = iterator.next()) {
            is CodeSimplePart -> {
                val value = part.value
                // handle deferred type
                if (deferredTypeName != null) {
                    if (value.startsWith(".")) {
                        if (codeWriter.emitStaticImportMember(deferredTypeName.canonicalName, value)) {
                            // okay, static import hit and all was emitted, so clean-up and jump to next part
                            deferredTypeName = null
                            continue
                        }
                    }
                    deferredTypeName.emit(codeWriter)
                    deferredTypeName = null
                }
                codeWriter.emitAndIndent(value)
            }

            is CodeArgumentPart.Skip -> {
                codeWriter.emit(CodePart.PLACEHOLDER)
            }

            is CodeArgumentPart.Literal -> {
                codeWriter.emitLiteral(part.value)
            }

            is CodeArgumentPart.Name -> {
                codeWriter.emitLiteral(part.name)
            }

            is CodeArgumentPart.Str -> {
                codeWriter.emitAndIndent(
                    part.value?.literalWithDoubleQuotes(codeWriter.indentValue)
                        ?: "null"
                )
            }

            is CodeArgumentPart.Type -> {
                // TODO cast
                val typeName = part.type as JavaTypeName
                if (typeName is JavaClassName && iterator.hasNext()) {
                    val next = parts[iterator.nextIndex()]
                    // !next.start('$')
                    if (next !is CodeArgumentPart) {
                        val candidate: JavaClassName = typeName
                        if (codeWriter.staticImportClassNames.contains(candidate.canonicalName)) {
                            check(deferredTypeName == null) { "pending type for static import?!" }
                            deferredTypeName = candidate
                            continue
                        }
                    }
                }

                typeName.emit(codeWriter)
            }

            is CodeArgumentPart.Indent -> {
                codeWriter.indent(part.levels)
            }

            is CodeArgumentPart.Unindent -> {
                codeWriter.unindent(part.levels)
            }

            is CodeArgumentPart.StatementBegin -> {
                check(codeWriter.statementLine == -1) {
                    "statement enter $[ followed by statement enter $["
                }
                codeWriter.statementLine = 0
            }

            is CodeArgumentPart.StatementEnd -> {
                check(codeWriter.statementLine != -1) {
                    "statement exit $] has no matching statement enter $["
                }
                if (codeWriter.statementLine > 0) {
                    codeWriter.unindent(2) // End a multi-line statement. Decrease the indentation level.
                }
                codeWriter.statementLine = -1
            }

            is CodeArgumentPart.WrappingSpace -> {
                codeWriter.out.wrappingSpace(codeWriter.indentLevel + 2)
            }

            is CodeArgumentPart.ZeroWidthSpace -> {
                codeWriter.out.zeroWidthSpace(codeWriter.indentLevel + 2)
            }

            is CodeArgumentPart.OtherCodeValue -> {
                // TODO type cast
                codeWriter.emit(part.value as JavaCodeValue)
            }
        }
    }

    if (ensureTrailingNewline && codeWriter.out.lastChar != '\n') {
        codeWriter.emit("\n")
    }
}


internal data class JavaJavaCodeValueImpl(override val parts: List<CodePart>) : JavaCodeValue {
    override fun toString(): String {
        return emitToString()
    }
}
