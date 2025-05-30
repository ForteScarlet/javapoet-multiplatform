package love.forte.codegentle.java.codepoint

import love.forte.codegentle.common.codepoint.CodePoint
import love.forte.codegentle.common.codepoint.category
import love.forte.codegentle.common.codepoint.charCount
import love.forte.codegentle.java.isJavaIdentifierPart
import love.forte.codegentle.java.isJavaIdentifierStart

internal expect fun CodePoint.isJavaIdentifierStart(): Boolean
internal expect fun CodePoint.isJavaIdentifierPart(): Boolean


internal fun CodePoint.isJavaIdentifierStartCommon(): Boolean {
    if (charCount() == 1) {
        return Char(code).isJavaIdentifierStart()
    }

    // 获取字符类别
    val category = category()

    // 根据 Java 标识符规范判断
    return when (category) {
        // 字母类别
        CharCategory.UPPERCASE_LETTER,   // Lu
        CharCategory.LOWERCASE_LETTER,   // Ll
        CharCategory.TITLECASE_LETTER,   // Lt
        CharCategory.MODIFIER_LETTER,    // Lm
        CharCategory.OTHER_LETTER,       // Lo
        CharCategory.LETTER_NUMBER       // Nl
            -> true

        // 下划线特殊处理
        CharCategory.CONNECTOR_PUNCTUATION -> code == 0x005F // 只允许下划线

        // 其他所有类别都不允许作为标识符开始
        else -> false
    }
}

internal fun CodePoint.isJavaIdentifierPartCommon(): Boolean {
    if (charCount() == 1) {
        return Char(code).isJavaIdentifierPart()
    }

    // 获取字符类别
    val category = category()

    // 根据 Java 标识符规范判断
    return when (category) {
        // 数字类别
        CharCategory.DECIMAL_DIGIT_NUMBER,  // Nd

            // 标记类别
        CharCategory.NON_SPACING_MARK,      // Mn
        CharCategory.COMBINING_SPACING_MARK, // Mc

            // 连接符类别
        CharCategory.CONNECTOR_PUNCTUATION  // Pc
            -> true

        // 其他所有类别都不允许作为标识符部分
        else -> false
    }
}
