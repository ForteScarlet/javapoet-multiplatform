package love.forte.codegentle.common.codepoint

import kotlin.text.codePointAt as codePointAtKt

internal actual fun CodePoint.stringValue(): String {
    return (Character.toChars(code)).concatToString()
}

@InternalCodePointApi
public actual fun String.codePointAt(index: Int): CodePoint =
    CodePoint(codePointAtKt(index))

@InternalCodePointApi
public actual fun CodePoint.isLowerCase(): Boolean =
    Character.isLowerCase(code)

@InternalCodePointApi
public actual fun CodePoint.isUpperCase(): Boolean =
    Character.isUpperCase(code)

@InternalCodePointApi
public actual fun CodePoint.charCount(): Int {
    return Character.charCount(code)
}

@InternalCodePointApi
public actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder {
    return appendCodePoint(codePoint.code)
}
