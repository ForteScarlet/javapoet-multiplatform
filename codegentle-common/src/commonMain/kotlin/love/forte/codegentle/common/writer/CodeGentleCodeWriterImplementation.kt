package love.forte.codegentle.common.writer

/**
 * A [CodeWriter]-related type that should be implemented internally only.
 * The stability and compatibility of third-party implementations are not guaranteed.
 */
@RequiresOptIn(
    message = "A CodeWriter-related type that should be implemented internally only. " +
        "The stability and compatibility of third-party implementations are not guaranteed.",
    level = RequiresOptIn.Level.ERROR
)
public annotation class CodeGentleCodeWriterImplementation
