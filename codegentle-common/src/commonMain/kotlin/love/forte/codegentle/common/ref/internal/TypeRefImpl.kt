package love.forte.codegentle.common.ref.internal

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.TypeNameRefStatus
import love.forte.codegentle.common.ref.TypeRef

/**
 *
 * @author ForteScarlet
 */
internal data class TypeRefImpl<out T : TypeName>(
    override val typeName: T,
    override val status: TypeNameRefStatus
) : TypeRef<T>
