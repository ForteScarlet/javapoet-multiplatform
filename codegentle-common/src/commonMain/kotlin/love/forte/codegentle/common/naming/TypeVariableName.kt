package love.forte.codegentle.common.naming

import love.forte.codegentle.common.naming.internal.TypeVariableNameImpl
import love.forte.codegentle.common.ref.TypeRef

/**
 * A JVM-based type variable name.
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface TypeVariableName : TypeName, Named {
    override val name: String
    public val bounds: List<TypeRef<*>>
}

/**
 * Returns type variable named `name` without bounds.
 */
public fun TypeVariableName(name: String): TypeVariableName =
    TypeVariableNameImpl(name, emptyList())

/**
 * Returns type variable named `name` with `bounds`.
 */
public fun TypeVariableName(name: String, vararg bounds: TypeRef<*>): TypeVariableName =
    TypeVariableNameImpl(name, bounds.asList())

/**
 * Returns type variable named `name` with `bounds`.
 */
public fun TypeVariableName(name: String, bounds: Iterable<TypeRef<*>>): TypeVariableName =
    TypeVariableNameImpl(name, bounds.toList())
