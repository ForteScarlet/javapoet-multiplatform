package love.forte.codegentle.java.codepoint

import love.forte.codegentle.common.codepoint.CodePoint
import love.forte.codegentle.common.codepoint.charCount
import love.forte.codegentle.java.isJavaIdentifierPart
import love.forte.codegentle.java.isJavaIdentifierStart

internal expect fun CodePoint.isJavaIdentifierStart(): Boolean
internal expect fun CodePoint.isJavaIdentifierPart(): Boolean

internal fun CodePoint.isJavaIdentifierStartCommon(): Boolean {
    // TODO How check Java identifier start use code point?
    if (charCount() == 1) {
        return Char(code).isJavaIdentifierStart()
    }

    return true
}

internal fun CodePoint.isJavaIdentifierPartCommon(): Boolean {
    // TODO How check Java identifier part use code point?
    if (charCount() == 1) {
        return Char(code).isJavaIdentifierPart()
    }

    return true
}

