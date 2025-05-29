package love.forte.codegentle.common.codepoint

internal actual fun CodePoint.stringValue(): String {
    val codeInt = code.toInt()
    return if (codeInt <= Char.MAX_VALUE.code) {
        Char(codeInt).toString()
    } else {
        // For supplementary code points, we need to use surrogate pairs
        val highSurrogate = Char.MIN_HIGH_SURROGATE + ((codeInt - 0x10000) shr 10)
        val lowSurrogate = Char.MIN_LOW_SURROGATE + (codeInt and 0x3ff)
        highSurrogate.toString() + lowSurrogate.toString()
    }
}

@InternalCodePointApi
public actual fun String.codePointAt(index: Int): CodePoint {
    if (index < 0 || index >= length) {
        throw IndexOutOfBoundsException("String index out of range: $index")
    }

    val char = this[index]
    if (!char.isHighSurrogate() || index + 1 >= length) {
        // If it's not a high surrogate or it's the last character,
        // return the code point of the character
        return CodePoint(char.code.toUInt())
    }

    val nextChar = this[index + 1]
    if (!nextChar.isLowSurrogate()) {
        // If the next character is not a low surrogate,
        // return the code point of the current character
        return CodePoint(char.code.toUInt())
    }

    // Calculate the code point from the surrogate pair
    val highSurrogate = char.code - Char.MIN_HIGH_SURROGATE.code
    val lowSurrogate = nextChar.code - Char.MIN_LOW_SURROGATE.code
    val codePoint = 0x10000 + (highSurrogate shl 10) + lowSurrogate

    return CodePoint(codePoint.toUInt())
}
