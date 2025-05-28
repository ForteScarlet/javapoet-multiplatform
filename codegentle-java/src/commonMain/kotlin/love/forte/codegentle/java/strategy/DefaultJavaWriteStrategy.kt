package love.forte.codegentle.java.strategy

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.Named
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.simpleNames
import love.forte.codegentle.java.internal.isSourceIdentifier
import love.forte.codegentle.java.internal.isSourceName

/**
 *
 * @author ForteScarlet
 */
public open class DefaultJavaWriteStrategy : JavaWriteStrategy {
    override fun isValidSourceName(name: TypeName): Boolean {
        return when (name) {
            is ClassName -> {
                // TODO packageName ?
                name.simpleNames.all { it.isSourceName() }
            }

            is Named -> {
                name.name.isSourceName()
            }

            else -> true
        }
    }

    override fun isValidSourceName(name: String): Boolean =
        name.isSourceName()

    override fun isIdentifier(value: String): Boolean {
        return value.isSourceIdentifier()
    }
}
