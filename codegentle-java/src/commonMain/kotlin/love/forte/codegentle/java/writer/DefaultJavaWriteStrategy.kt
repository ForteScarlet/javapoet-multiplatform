package love.forte.codegentle.java.writer

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.simpleNames
import love.forte.codegentle.java.internal.isSourceName

/**
 *
 * @author ForteScarlet
 */
public open class DefaultJavaWriteStrategy : JavaWriteStrategy {
    override fun isValidName(name: TypeName): Boolean {
        return when (name) {
            is ClassName -> name.simpleNames.all { it.isSourceName() }
            // TODO
            else -> true
        }
    }
}
