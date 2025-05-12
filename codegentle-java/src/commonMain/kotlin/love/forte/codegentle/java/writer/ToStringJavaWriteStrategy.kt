package love.forte.codegentle.java.writer

import love.forte.codegentle.common.naming.TypeName

internal object ToStringJavaWriteStrategy : JavaWriteStrategy {
    override fun isValidName(name: TypeName): Boolean = true
}
