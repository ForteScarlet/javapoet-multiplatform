package love.forte.codegentle.common.writer

import love.forte.codegentle.common.naming.TypeName

/**
 * Dialect for code writer.
 *
 * @author ForteScarlet
 */
public interface Strategy {
    /**
     * Checks if a [TypeName] is a valid source name.
     */
    public fun isValidSourceName(name: TypeName): Boolean

    /**
     * Checks if a [String] is a valid source name.
     */
    public fun isValidSourceName(name: String): Boolean

    /**
     * The new line string.
     */
    public fun newline(): String = "\n"


    public fun isIdentifier(value: String): Boolean
}
