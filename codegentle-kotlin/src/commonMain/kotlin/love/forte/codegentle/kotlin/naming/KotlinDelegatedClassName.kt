package love.forte.codegentle.kotlin.naming

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.CodeGentleNamingImplementation
import love.forte.codegentle.common.naming.TypeName

/**
 * A delegated Kotlin [TypeName]
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface KotlinDelegatedClassName : TypeName {
    public val typeName: ClassName
    public val delegate: CodeValue
}

