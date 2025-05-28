package love.forte.codegentle.java.codepoint

import love.forte.codegentle.common.codepoint.CodePoint


internal actual fun CodePoint.isJavaIdentifierStart(): Boolean =
    Character.isJavaIdentifierStart(code.toInt())

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean =
    Character.isJavaIdentifierPart(code.toInt())
