package love.forte.codepoet.common.codepoint

@RequiresOptIn(
    message = "This is an internal code point api. " +
        "For internal use only, no compatibility or stability is guaranteed externally.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class InternalCodePointApi
