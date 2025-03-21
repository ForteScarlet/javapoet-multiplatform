package love.forte.codepoet.java.internal

import love.forte.codepoet.common.codepoint.CodePoint


internal actual fun CodePoint.isJavaIdentifierStart(): Boolean = isJavaIdentifierStartCommon()

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean = isJavaIdentifierPartCommon()
