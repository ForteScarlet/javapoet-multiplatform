package love.forte.codegentle.kotlin.writer

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.writer.*
import love.forte.codegentle.kotlin.emitTo
import love.forte.codegentle.kotlin.ref.kotlinOrNull
import love.forte.codegentle.kotlin.strategy.KotlinWriteStrategy

/**
 *
 * @author ForteScarlet
 */
@OptIn(InternalWriterApi::class)
public class KotlinCodeWriter private  constructor(
    override val strategy: KotlinWriteStrategy,
    override val indentValue: String,
    internal val out: LineWrapper,

    // public val staticImportClassNames: Set<ClassName> = emptySet(),
    override val staticImports: Set<String> = emptySet(),
    override val alwaysQualify: Set<String> = emptySet(),
    internal val importedTypes: Map<String, ClassName> = emptyMap()
): AbstractCodeWriter() {

    override fun emitComment(
        comment: CodeValue,
        vararg options: CodeValueEmitOption
    ) {
        trailingNewline = true
        commentType = CommentType.COMMENT
        try {
            comment.emitTo(this)
            emit(strategy.newline())
        } finally {
            commentType = null
        }
    }

    override fun emitDoc(
        doc: CodeValue,
        vararg options: CodeValueEmitOption
    ) {
        if (doc.isEmpty) return

        emit("/**${strategy.newline()}")
        this.commentType = CommentType.DOC
        try {
            doc.emitTo(this, true)
        } finally {
            this.commentType = null
        }

        emit(" */${strategy.newline()}")
    }

    override fun emit(
        code: CodeValue,
        vararg options: CodeValueEmitOption
    ) {
        val ensureTrailingNewline = options.contains(KotlinCodeValueEmitOption.EnsureTrailingNewline)
        code.emitTo(this, ensureTrailingNewline)
    }

    internal fun emitStaticImportMember(canonical: String, part: String): Boolean {
        val partWithoutLeadingDot = part.substring(1)
        if (partWithoutLeadingDot.isEmpty()) return false
        val first = partWithoutLeadingDot[0]
        if (!strategy.isIdentifier(first.toString())) return false
        val explicit = canonical + "." + extractMemberName(partWithoutLeadingDot)
        val wildcard = "$canonical.*"
        if (staticImports.contains(explicit) || staticImports.contains(wildcard)) {
            emitAndIndent(partWithoutLeadingDot)
            return true
        }
        return false
    }

    internal fun extractMemberName(part: String): String {
        require(strategy.isIdentifier(part[0].toString())) { "not an identifier: $part" }
        for (i in 1..part.length) {
            if (!strategy.isIdentifier(part.substring(0, i))) {
                return part.substring(0, i - 1)
            }
        }
        return part
    }

    internal fun emitLiteral(value: Any?) {
        when (value) {
            is AnnotationRef -> {
                emit(value, CommonAnnotationRefEmitOption.Inline)
            }
            else -> emitAndIndent(value.toString())
        }
    }

    override fun emit(
        typeName: TypeName,
        vararg options: TypeNameEmitOption
    ) {
        when (typeName) {
            is ClassName -> {
                // For now, just emit the simple name
                // In a more complete implementation, we would handle imports and qualified names
                emit(typeName.simpleName)
            }
            else -> emit(typeName.toString())
        }
    }

    override fun emit(
        typeRef: TypeRef<*>,
        vararg options: TypeRefEmitOption
    ) {
        val typeNameOptions = mutableListOf<TypeNameEmitOption>()
        val annotationOptions = mutableListOf<AnnotationRefEmitOption>()

        // TODO KotlinTypeRefEmitOption?
        // for (option in options) {
        //     when (option) {
        //         is KotlinTypeRefEmitOption.TypeNameOptions -> typeNameOptions.addAll(option.options)
        //         is KotlinTypeRefEmitOption.AnnotationOptions -> annotationOptions.addAll(option.options)
        //         else -> {} // Ignore unknown options
        //     }
        // }

        // Emit annotations if any
        typeRef.status.kotlinOrNull?.annotations?.forEach { annotation ->
            emit(annotation, *annotationOptions.toTypedArray())
            emit(" ")
        }

        // Emit the type name
        emit(typeRef.typeName, *typeNameOptions.toTypedArray())
    }

    override fun emit(
        annotationRef: AnnotationRef,
        vararg options: AnnotationRefEmitOption
    ) {
        val inline = options.contains(CommonAnnotationRefEmitOption.Inline)

        emit("@")
        emit(annotationRef.typeName)

        // In a more complete implementation, we would handle annotation parameters
    }

    override fun emit(s: String) {
        emitAndIndent(s)
    }

    internal fun emitAndIndent(s: String) {
        var first = true
        for (line in s.lineSequence()) {
            // Emit a newline character. Make sure blank lines in comments look good.
            if (!first) {
                if (commentType != null && trailingNewline) {
                    emitIndentation()
                    out.append(if (commentType == CommentType.DOC) " *" else "//")
                }
                out.append(strategy.newline())
                trailingNewline = true
                if (statementLine != -1) {
                    if (statementLine == 0) {
                        indent(2) // Begin multiple-line statement. Increase the indentation level.
                    }
                    statementLine++
                }
            }

            first = false
            if (line.isEmpty()) continue  // Don't indent empty lines.

            // Emit indentation and comment prefix if necessary.
            if (trailingNewline) {
                emitIndentation()
                when (commentType) {
                    CommentType.DOC -> {
                        out.append(" * ")
                    }

                    CommentType.COMMENT -> {
                        out.append("// ")
                    }

                    null -> {}
                }
            }

            out.append(line)
            trailingNewline = false
        }
    }

    internal fun emitIndentation() {
        repeat(indentLevel) {
            out.append(indentValue)
        }
    }

    internal enum class CommentType {
        DOC,
        COMMENT;
    }

    internal var commentType: CommentType? = null
    internal var statementLine: Int = -1
}
