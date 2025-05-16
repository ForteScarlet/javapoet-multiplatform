package love.forte.codegentle.common.naming.internal

import love.forte.codegentle.common.naming.LowerWildcardTypeName
import love.forte.codegentle.common.naming.UpperWildcardTypeName
import love.forte.codegentle.common.ref.TypeRef

internal class UpperWildcardTypeNameImpl(
    override val bounds: List<TypeRef<*>>
) : UpperWildcardTypeName

internal class LowerWildcardTypeNameImpl(
    override val bounds: List<TypeRef<*>>
) : LowerWildcardTypeName
