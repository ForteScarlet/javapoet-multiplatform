package love.forte.javapoet.internal

import love.forte.javapoet.CodeBlock


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
}
