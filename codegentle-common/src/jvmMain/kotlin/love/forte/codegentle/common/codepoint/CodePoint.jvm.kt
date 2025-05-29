package love.forte.codegentle.common.codepoint

import kotlin.text.codePointAt as codePointAtKt

internal actual fun CodePoint.stringValue(): String {
    return (Character.toChars(code.toInt())).concatToString()
    // val codeInt = code.toInt()
    // return if (codeInt <= Char.MAX_VALUE.code) {
    //     Char(codeInt).toString()
    // } else {
    //     // For supplementary code points, we need to use surrogate pairs
    //     val highSurrogate = Char.MIN_HIGH_SURROGATE + ((codeInt - 0x10000) shr 10)
    //     val lowSurrogate = Char.MIN_LOW_SURROGATE + (codeInt and 0x3ff)
    //     highSurrogate.toString() + lowSurrogate.toString()
    // }
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
