package love.forte.codegentle.java.codepoint

import love.forte.codegentle.common.codepoint.CodePoint


internal actual fun CodePoint.isJavaIdentifierStart(): Boolean = isJavaIdentifierStartCommon()

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean = isJavaIdentifierPartCommon()
