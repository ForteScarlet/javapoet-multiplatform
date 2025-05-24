package love.forte.codegentle.java

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodePart
import love.forte.codegentle.common.code.CodeSimplePart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.canonicalName
import love.forte.codegentle.common.writer.InternalWriterApi
import love.forte.codegentle.java.naming.emitTo
import love.forte.codegentle.java.writer.JavaCodeWriter

@OptIn(InternalWriterApi::class)
internal fun CodeValue.emitTo(codeWriter: JavaCodeWriter, ensureTrailingNewline: Boolean = false) {
    var deferredTypeName: ClassName? = null
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
                    deferredTypeName.emitTo(codeWriter)
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
                val typeName = part.type
                // TODO 下面这逻辑干啥的？
                if (typeName is ClassName && iterator.hasNext()) {
                    val next = parts[iterator.nextIndex()]
                    // !next.start('$')
                    if (next !is CodeArgumentPart) {
                        val candidate: ClassName = typeName
                        if (codeWriter.staticImportClassNames.contains(candidate)) {
                            check(deferredTypeName == null) { "pending type for static import?!" }
                            deferredTypeName = candidate
                            continue
                        }
                    }
                }

                codeWriter.emit(typeName)
            }

            is CodeArgumentPart.TypeRef -> {
                val typeRef = part.type
                val typeName = typeRef.typeName
                // TODO 下面这逻辑干啥的？
                if (typeName is ClassName && iterator.hasNext()) {
                    val next = parts[iterator.nextIndex()]
                    // !next.start('$')
                    if (next !is CodeArgumentPart) {
                        val candidate: ClassName = typeName
                        if (codeWriter.staticImportClassNames.contains(candidate)) {
                            check(deferredTypeName == null) { "pending type for static import?!" }
                            deferredTypeName = candidate
                            continue
                        }
                    }
                }

                codeWriter.emit(typeRef)
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
                codeWriter.emit(part.value)
            }

        }
    }

    if (ensureTrailingNewline && codeWriter.out.lastChar != '\n') {
        codeWriter.emit("\n")
    }
}
