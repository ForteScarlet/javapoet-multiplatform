package love.forte.codepoet.common.naming

import love.forte.codepoet.common.ref.TypeRef

/**
 * A JVM-based type variable name.
 *
 * @author ForteScarlet
 */
public interface TypeVariableName : TypeName, Named {
    override val name: String
    public val bounds: List<TypeRef>
}
