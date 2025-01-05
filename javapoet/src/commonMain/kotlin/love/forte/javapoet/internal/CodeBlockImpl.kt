package love.forte.javapoet.internal

import love.forte.javapoet.CodeBlock
import love.forte.javapoet.CodeValue
import love.forte.javapoet.CodeWriter
import love.forte.javapoet.emitToString


/**
 *
 */
internal class CodeBlockImpl(
    private val codeValue: CodeValue,
) : CodeBlock {

    override val isEmpty: Boolean
        get() = codeValue.parts.isEmpty()

    override fun addTo(builder: CodeBlock.Builder) {
        builder.codeParts.addAll(codeValue.parts)
    }

    override fun toBuilder(): CodeBlock.Builder {
        return CodeBlock.builder().also {
            it.add(this)
        }
    }

    override fun emit(codeWriter: CodeWriter, ensureTrailingNewline: Boolean) {
        codeValue.emit(codeWriter, ensureTrailingNewline)

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is CodeBlockImpl) return false

        if (codeValue != other.codeValue) return false

        return true
    }

    override fun hashCode(): Int {
        return codeValue.hashCode()
    }

    override fun toString(): String {
        return emitToString()
    }
}
