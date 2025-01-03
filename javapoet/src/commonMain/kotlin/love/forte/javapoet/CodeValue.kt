package love.forte.javapoet


/**
 *
 */
public sealed class CodeValue {
}

/**
 * A literal code text.
 */
public data class LiteralCode(public val value: String) : CodeValue()

/**
 * A placeholder
 */
public open class Placeholder(
    public val format: String,
    public val args: List<Any?>
) : CodeValue() {

}

