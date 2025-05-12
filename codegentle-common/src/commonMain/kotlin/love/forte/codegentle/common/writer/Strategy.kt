package love.forte.codegentle.common.writer

import love.forte.codegentle.common.naming.TypeName

/**
 * Dialect for code writer.
 *
 * @author ForteScarlet
 */
public interface Strategy {
    // TODO
    //  判断名称是否合法
    //  ?

    /**
     * Checks if a [TypeName] is a valid name.
     */
    public fun isValidName(name: TypeName): Boolean

}
