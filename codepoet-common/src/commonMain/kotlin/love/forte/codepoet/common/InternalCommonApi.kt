package love.forte.codepoet.common

@RequiresOptIn(
    message = "This is an internal common api. " +
        "For internal use only, no compatibility or stability is guaranteed externally.",
    level = RequiresOptIn.Level.WARNING
)
public annotation class InternalCommonApi
