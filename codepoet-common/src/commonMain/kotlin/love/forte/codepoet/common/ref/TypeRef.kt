package love.forte.codepoet.common.ref

import love.forte.codepoet.common.naming.TypeName

/**
 * A reference to a [love.forte.codepoet.common.naming.TypeName].
 *
 * @author ForteScarlet
 */
public interface TypeRef {
    public val typeName: TypeName
    public val status: TypeNameRefStatus
}

/**
 * A reference status.
 *
 * @author ForteScarlet
 */
public interface TypeNameRefStatus

