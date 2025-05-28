package love.forte.codegentle.common.naming

import love.forte.codegentle.common.naming.internal.LowerWildcardTypeNameImpl
import love.forte.codegentle.common.naming.internal.UpperWildcardTypeNameImpl
import love.forte.codegentle.common.ref.TypeRef
import kotlin.js.JsName

/**
 * A Wildcard type name.
 *
 * @author ForteScarlet
 */
public sealed interface WildcardTypeName : TypeName {
    public val bounds: List<TypeRef<*>>

    // Java:
    //  uppers: `? extends T1 & T2`; Kotlin: `out A`, `out B`
    //  lowers: `? super T1 & T2`; Kotlin: `in A`, `in B`
    // Kotlin:
    //  `outs`, `ins`
}


/**
 * No bounds [WildcardTypeName].
 * - Java `?` or `? extends Object`
 * - Kotlin `*` or `out Any?`
 */
public data object EmptyWildcardTypeName : WildcardTypeName {
    override val bounds: List<TypeRef<*>> = emptyList()
}

public val WildcardTypeName.isEmpty: Boolean
    get() = bounds.isEmpty()

/**
 * Upper wildcard type name, subtype wildcard type name.
 * - Java: `? super T`.
 * - Kotlin `in T`.
 *
 * The bounds are *lower bounds*.
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface UpperWildcardTypeName : WildcardTypeName

/**
 * Lower wildcard type name, supertype wildcard type name.
 * - Java: `? extends T`.
 * - Kotlin: `T : ?`, `out T`.
 *
 * The bounds are *upper bounds*.
 */
@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface LowerWildcardTypeName : WildcardTypeName

@JsName("emptyWildcardTypeName")
public fun WildcardTypeName(): WildcardTypeName = EmptyWildcardTypeName

public fun LowerWildcardTypeName(upperBound: TypeRef<*>): LowerWildcardTypeName =
    LowerWildcardTypeNameImpl(listOf(upperBound))

public fun UpperWildcardTypeName(lowerBound: TypeRef<*>): UpperWildcardTypeName =
    UpperWildcardTypeNameImpl(listOf(lowerBound))

public fun LowerWildcardTypeName(upperBounds: List<TypeRef<*>>): LowerWildcardTypeName {
    require(upperBounds.isNotEmpty()) { "Upper bounds must not be empty." }
    return LowerWildcardTypeNameImpl(upperBounds)
}

public fun UpperWildcardTypeName(lowerBounds: List<TypeRef<*>>): UpperWildcardTypeName {
    require(lowerBounds.isNotEmpty()) { "Lower bounds must not be empty." }
    return UpperWildcardTypeNameImpl(lowerBounds)
}
