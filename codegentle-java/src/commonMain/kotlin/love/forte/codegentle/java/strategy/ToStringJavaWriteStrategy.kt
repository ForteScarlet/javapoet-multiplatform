package love.forte.codegentle.java.strategy

import love.forte.codegentle.common.naming.TypeName

internal object ToStringJavaWriteStrategy : JavaWriteStrategy {
    override fun isValidSourceName(name: TypeName): Boolean = true

    override fun isValidSourceName(name: String): Boolean = true

    override fun isIdentifier(value: String): Boolean = true

    override fun omitJavaLangPackage(): Boolean = true
}



