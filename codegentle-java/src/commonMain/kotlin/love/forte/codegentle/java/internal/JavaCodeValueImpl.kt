package love.forte.codegentle.java.internal

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodePart
import love.forte.codegentle.common.code.CodeSimplePart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.canonicalName
import love.forte.codegentle.common.writer.InternalWriterApi
import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.literalWithDoubleQuotes
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.naming.internal.emitTo
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emitToString


@OptIn(InternalWriterApi::class)
internal fun CodeValue.emit0(codeWriter: JavaCodeWriter, ensureTrailingNewline: Boolean = false) {
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
                // TODO cast
                val typeName = part.type as? JavaTypeName
                    ?: error("type ${part.type} is not a JavaTypeName, but ${part.type::class}")
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

                typeName.emit(codeWriter)
            }

            is CodeArgumentPart.TypeRef -> {
                val typeRef = part.type as? JavaTypeRef<*>
                    ?: error("type ref ${part.type} is not a JavaTypeRef, but ${part.type::class}")

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

                typeRef.emit(codeWriter)
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
