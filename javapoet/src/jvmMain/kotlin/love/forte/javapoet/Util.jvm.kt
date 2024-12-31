package love.forte.javapoet

internal actual fun Char.isJavaIdentifierStart(): Boolean =
    Character.isJavaIdentifierStart(this)

internal actual fun Char.isJavaIdentifierPart(): Boolean =
    Character.isJavaIdentifierPart(this)
