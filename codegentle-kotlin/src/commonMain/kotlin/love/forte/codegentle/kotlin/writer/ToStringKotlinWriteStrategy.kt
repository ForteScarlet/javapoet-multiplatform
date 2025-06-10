package love.forte.codegentle.kotlin.writer

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.kotlin.strategy.KotlinWriteStrategy

/**
 *
 * @author ForteScarlet
 */
public object ToStringKotlinWriteStrategy : KotlinWriteStrategy {
    override fun isIdentifier(value: String): Boolean = true

    override fun isValidSourceName(name: TypeName): Boolean = true

    override fun isValidSourceName(name: String): Boolean = true
}
