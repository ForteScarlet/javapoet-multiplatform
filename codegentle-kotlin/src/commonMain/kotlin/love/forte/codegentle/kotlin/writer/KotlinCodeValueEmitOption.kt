package love.forte.codegentle.kotlin.writer

import love.forte.codegentle.common.writer.CodeValueEmitOption

/**
 * Options for emitting Kotlin code values.
 *
 * @author ForteScarlet
 */
public enum class KotlinCodeValueEmitOption : CodeValueEmitOption {
    /**
     * Ensures that the emitted code ends with a newline.
     */
    EnsureTrailingNewline
}
