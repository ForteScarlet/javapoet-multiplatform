package love.forte.codegentle.common.code

import love.forte.codegentle.common.writer.CodeEmitter

/**
 *
 * @author ForteScarlet
 */
public interface CodeValue : CodeEmitter {
    public val parts: List<CodePart>
}

public val CodeValue.isEmpty: Boolean
    get() = parts.isEmpty()
