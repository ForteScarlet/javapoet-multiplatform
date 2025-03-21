package love.forte.codepoet.common.codepoint

import kotlin.text.codePointAt as codePointAtKt

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
