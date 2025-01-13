package love.forte.codepoet.java.internal

import love.forte.codepoet.java.isJavaIdentifierPart
import love.forte.codepoet.java.isJavaIdentifierStart
import kotlin.jvm.JvmInline

@JvmInline
internal value class CodePoint(internal val code: Int)


internal expect fun String.codePointAt(index: Int): CodePoint

internal expect fun CodePoint.isLowerCase(): Boolean
internal expect fun CodePoint.isUpperCase(): Boolean

internal expect fun CodePoint.isJavaIdentifierStart(): Boolean
internal expect fun CodePoint.isJavaIdentifierPart(): Boolean

internal expect fun CodePoint.charCount(): Int

internal expect fun StringBuilder.appendCodePoint(codePoint: CodePoint): StringBuilder


/*
 * Copyright 2010-2023 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */
internal fun StringBuilder.appendCodePointCommon(codePoint: CodePoint): StringBuilder {
    // Copied from StringBuilder.kt,
    // TODO Is this correct?
    val code = codePoint.code
    if (code <= Char.MAX_VALUE.code) {
        append(code.toChar())
    } else {
        append(Char.MIN_HIGH_SURROGATE + ((code - 0x10000) ushr 10))
        append(Char.MIN_LOW_SURROGATE + (code and 0x3ff))
    }
    return this
}

internal fun CodePoint.isJavaIdentifierStartCommon(): Boolean {
    // TODO How check Java identifier start use code point?
    if (charCount() == 1) {
        return Char(code).isJavaIdentifierStart()
    }

    return true
}

internal fun CodePoint.isJavaIdentifierPartCommon(): Boolean {
    // TODO How check Java identifier part use code point?
    if (charCount() == 1) {
        return Char(code).isJavaIdentifierPart()
    }

    return true
}

internal fun CodePoint.charCountCommon(): Int {
    return if (code >= 0x10000) 2 else 1
}
