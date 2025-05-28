package love.forte.codegentle.common.codepoint

@Suppress("UNUSED_PARAMETER")
internal actual fun jsCodePointAt(str: String, index: Int): Int =
    js("str.codePointAt(index)")

@Suppress("UNUSED_PARAMETER")
internal actual fun jsFromCodePoint(code: Int): String =
    js("String.fromCodePoint(code)")
