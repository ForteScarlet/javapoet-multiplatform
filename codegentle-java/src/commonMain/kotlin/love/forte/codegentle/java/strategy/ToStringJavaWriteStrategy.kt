package love.forte.codegentle.java.strategy

import love.forte.codegentle.common.naming.TypeName

internal object ToStringJavaWriteStrategy : JavaWriteStrategy {
    override fun isValidName(name: TypeName): Boolean = true

    override fun isIdentifier(value: String): Boolean = true
}
