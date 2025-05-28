package love.forte.codegentle.common.codepoint

import kotlin.text.codePointAt as codePointAtKt

internal actual fun CodePoint.stringValue(): String {
    return code.toInt().toChar().toString()
}

@InternalCodePointApi
public actual fun String.codePointAt(index: Int): CodePoint =
    CodePoint(codePointAtKt(index).toUInt())

@InternalCodePointApi
public actual fun CodePoint.isLowerCase(): Boolean =
    Character.isLowerCase(code.toInt())

@InternalCodePointApi
public actual fun CodePoint.isUpperCase(): Boolean =
    Character.isUpperCase(code.toInt())

@InternalCodePointApi
public actual fun CodePoint.charCount(): Int {
    return Character.charCount(code.toInt())
}

@InternalCodePointApi
public actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder {
    return appendCodePoint(codePoint.code.toInt())
}
