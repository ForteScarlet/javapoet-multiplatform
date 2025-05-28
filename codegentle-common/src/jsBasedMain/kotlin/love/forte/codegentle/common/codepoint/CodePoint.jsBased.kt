package love.forte.codegentle.common.codepoint

@Suppress("UNUSED_PARAMETER")
internal expect fun jsCodePointAt(str: String, index: Int): Int

@Suppress("UNUSED_PARAMETER")
internal expect fun jsFromCodePoint(code: Int): String

@InternalCodePointApi
public actual fun String.codePointAt(index: Int): CodePoint {
    val code = jsCodePointAt(this, index).toUInt()
    return CodePoint(code)
}

@InternalCodePointApi
public actual fun CodePoint.isLowerCase(): Boolean {
    val codeInt = code.toInt()

    // 直接使用Character.isLowerCase的行为逻辑
    // 这确保与Java标准库行为一致
    return if (codeInt <= Char.MAX_VALUE.code) {
        // 对于BMP范围内的码点，使用Kotlin的Char.isLowerCase()
        Char(codeInt).isLowerCase()
    } else {
        // 对于补充码点，遵循Java的Character.isLowerCase(int)行为
        // Java对补充码点的处理主要基于Unicode的一般类别属性
        when (category()) {
            CharCategory.LOWERCASE_LETTER -> true
            else -> false
        }
    }
}

@InternalCodePointApi
public actual fun CodePoint.isUpperCase(): Boolean {
    val codeInt = code.toInt()

    // 直接使用Character.isUpperCase的行为逻辑
    return if (codeInt <= Char.MAX_VALUE.code) {
        // 对于BMP范围内的码点，使用Kotlin的Char.isUpperCase()
        Char(codeInt).isUpperCase()
    } else {
        // 对于补充码点，遵循Java的Character.isUpperCase(int)行为
        when (category()) {
            CharCategory.UPPERCASE_LETTER -> true
            else -> false
        }
    }
}

@InternalCodePointApi
public actual fun CodePoint.charCount(): Int = charCountCommon()

@InternalCodePointApi
public actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder =
    appendCodePointCommon(codePoint)

internal actual fun CodePoint.stringValue(): String =
    jsFromCodePoint(code.toInt())
