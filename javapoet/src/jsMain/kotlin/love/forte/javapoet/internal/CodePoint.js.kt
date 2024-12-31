package love.forte.javapoet.internal

internal actual fun String.codePointAt(index: Int): CodePoint {
    val code = jsCodePointAt(this, index)
    return CodePoint(code)
}

internal actual fun CodePoint.isLowerCase(): Boolean {
    // TODO Will there be a problem? Is there a better way?
    val str = jsFromCodePoint(this.code)

    if (str.length != 1) {
        return false
    }

    return str.first().isLowerCase()
}

internal actual fun CodePoint.isUpperCase(): Boolean {
    // TODO Will there be a problem? Is there a better way?
    val str = jsFromCodePoint(this.code)

    if (str.length != 1) {
        return false
    }

    return str.first().isUpperCase()
}

@Suppress("unused")
private fun jsCodePointAt(str: String, index: Int): Int =
    js("str.codePointAt(index)").unsafeCast<Int>()

@Suppress("unused")
private fun jsFromCodePoint(code: Int): String =
    js("String.fromCodePoint(code)").toString()

internal actual fun CodePoint.isJavaIdentifierStart(): Boolean = isJavaIdentifierStartCommon()

internal actual fun CodePoint.isJavaIdentifierPart(): Boolean = isJavaIdentifierPartCommon()

internal actual fun CodePoint.charCount(): Int = charCountCommon()

internal actual fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder = appendCodePointCommon(codePoint)
