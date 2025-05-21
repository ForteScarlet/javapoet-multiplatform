package love.forte.codegentle.common.ref

/**
 * A ref type that should only be implemented internally.
 * The stability and compatibility of third-party implementations are not guaranteed.
 */
@RequiresOptIn(
    message = "A ref type that should only be implemented internally. " +
        "The stability and compatibility of third-party implementations are not guaranteed.",
    level = RequiresOptIn.Level.ERROR
)
public annotation class CodeGentleRefImplementation
