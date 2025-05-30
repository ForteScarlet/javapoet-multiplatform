package love.forte.codegentle.common.codepoint

@Suppress("UNUSED_PARAMETER")
internal expect fun jsCodePointAt(str: String, index: Int): Int

@Suppress("UNUSED_PARAMETER")
internal expect fun jsFromCodePoint(code: Int): String

@InternalCodePointApi
public actual fun String.codePointAt(index: Int): CodePoint {
    val code = jsCodePointAt(this, index)
    return CodePoint(code)
}

internal actual fun CodePoint.stringValue(): String =
    jsFromCodePoint(code)
