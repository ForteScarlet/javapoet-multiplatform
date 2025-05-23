package love.forte.codegentle.java.spec

/**
 * A [JavaSpec]-related type that should be implemented internally only.
 * The stability and compatibility of third-party implementations are not guaranteed.
 */
@RequiresOptIn(
    message = "A JavaSpec-related type that should be implemented internally only. " +
        "The stability and compatibility of third-party implementations are not guaranteed.",
    level = RequiresOptIn.Level.ERROR
)
public annotation class CodeGentleJavaSpecImplementation
