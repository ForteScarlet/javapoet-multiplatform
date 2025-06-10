package love.forte.codegentle.common.writer

/**
 *
 * @author ForteScarlet
 */
@InternalWriterApi
public abstract class AbstractCodeWriter : CodeWriter {
    protected var indentLevel: Int = 0
    protected var trailingNewline: Boolean = false

    override fun indent(levels: Int) {
        indentLevel += levels
    }

    override fun unindent(levels: Int) {
        check(indentLevel - levels >= 0) { "cannot unindent $levels from $indentLevel" }
        indentLevel -= levels
    }

}
