package love.forte.codepoet.common.code

import love.forte.codepoet.common.emitter.CodeEmitter

/**
 *
 * @author ForteScarlet
 */
public interface CodeValue : CodeEmitter {
    public val parts: List<CodePart>
}

public val CodeValue.isEmpty: Boolean
    get() = parts.isEmpty()
