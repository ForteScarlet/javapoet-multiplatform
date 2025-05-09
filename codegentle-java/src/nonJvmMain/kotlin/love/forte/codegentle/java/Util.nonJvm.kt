package love.forte.codegentle.java

internal actual fun Char.isJavaIdentifierStart(): Boolean = isJavaIdentifierStartCommon()

internal actual fun Char.isJavaIdentifierPart(): Boolean = isJavaIdentifierPartCommon()
