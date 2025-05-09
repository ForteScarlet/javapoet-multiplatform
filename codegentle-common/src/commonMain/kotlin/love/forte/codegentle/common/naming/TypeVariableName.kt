package love.forte.codegentle.common.naming

import love.forte.codegentle.common.ref.TypeRef

/**
 * A JVM-based type variable name.
 *
 * @author ForteScarlet
 */
public interface TypeVariableName : TypeName, Named {
    override val name: String
    public val bounds: List<TypeRef>
}
