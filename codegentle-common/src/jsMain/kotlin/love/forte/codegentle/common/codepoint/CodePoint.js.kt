package love.forte.codegentle.common.codepoint

@InternalCodePointApi
public actual fun String.codePointAt(index: Int): CodePoint {
    val code = jsCodePointAt(this, index)
    return CodePoint(code)
}

@InternalCodePointApi
public actual fun CodePoint.isLowerCase(): Boolean {
    // TODO Will there be a problem? Is there a better way?
    val str = jsFromCodePoint(this.code)

    if (str.length != 1) {
        return false
    }

    return str.first().isLowerCase()
}

@InternalCodePointApi
public actual fun CodePoint.isUpperCase(): Boolean {
    // TODO Will there be a problem? Is there a better way?
    val str = jsFromCodePoint(this.code)

    if (str.length != 1) {
        return false
    }

    return str.first().isUpperCase()
}

@Suppress("UNUSED_PARAMETER")
private fun jsCodePointAt(str: String, index: Int): Int =
    js("str.codePointAt(index)").unsafeCast<Int>()

@Suppress("UNUSED_PARAMETER")
private fun jsFromCodePoint(code: Int): String =
    js("String.fromCodePoint(code)").toString()

@InternalCodePointApi
public actual fun CodePoint.charCount(): Int = charCountCommon()

@InternalCodePointApi
public actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder =
    appendCodePointCommon(codePoint)
