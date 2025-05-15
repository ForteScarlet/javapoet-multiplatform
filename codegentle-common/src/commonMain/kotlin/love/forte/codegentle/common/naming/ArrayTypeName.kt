package love.forte.codegentle.common.naming

import love.forte.codegentle.common.ref.TypeRef

/**
 *
 * @author ForteScarlet
 */
public interface ArrayTypeName : TypeName {
    public val componentType: TypeRef<*>
}

// TODO array type
