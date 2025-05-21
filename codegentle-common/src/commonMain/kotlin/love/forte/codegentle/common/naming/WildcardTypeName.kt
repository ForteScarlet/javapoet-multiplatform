package love.forte.codegentle.common.naming

import love.forte.codegentle.common.naming.internal.LowerWildcardTypeNameImpl
import love.forte.codegentle.common.naming.internal.UpperWildcardTypeNameImpl
import love.forte.codegentle.common.ref.TypeRef

/**
 * A Wildcard type name.
 *
 * @author ForteScarlet
 */
public sealed interface WildcardTypeName : TypeName {
    public val bounds: List<TypeRef<*>>

    // Java:
    //  uppers: ? extends T1 & T2; Kotlin: out A, out B
    //  lowers: ? super T1 & T2; Kotlin: in A, in B
    // Kotlin:
    //  outs, ins
}

@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface UpperWildcardTypeName : WildcardTypeName

@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface LowerWildcardTypeName : WildcardTypeName


public fun LowerWildcardTypeName(upperBound: TypeRef<*>): LowerWildcardTypeName =
    LowerWildcardTypeNameImpl(listOf(upperBound))

public fun UpperWildcardTypeName(lowerBound: TypeRef<*>): UpperWildcardTypeName =
    UpperWildcardTypeNameImpl(listOf(lowerBound))

public fun LowerWildcardTypeName(upperBounds: List<TypeRef<*>>): LowerWildcardTypeName =
    LowerWildcardTypeNameImpl(upperBounds)

public fun UpperWildcardTypeName(lowerBounds: List<TypeRef<*>>): UpperWildcardTypeName =
    UpperWildcardTypeNameImpl(lowerBounds)
