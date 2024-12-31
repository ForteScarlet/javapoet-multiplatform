package love.forte.javapoet.internal

import kotlin.text.codePointAt as codePointAtKt

internal actual fun String.codePointAt(index: Int): CodePoint =
    CodePoint(codePointAtKt(index))

internal actual fun CodePoint.isLowerCase(): Boolean =
    Character.isLowerCase(code)

internal actual fun CodePoint.isUpperCase(): Boolean =
    Character.isUpperCase(code)

internal actual fun CodePoint.isJavaIdentifierStart(): Boolean =
    Character.isJavaIdentifierStart(code)

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean =
    Character.isJavaIdentifierPart(code)

internal actual fun CodePoint.charCount(): Int {
    return Character.charCount(code)
}

internal actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder {
    return appendCodePoint(codePoint.code)
}
