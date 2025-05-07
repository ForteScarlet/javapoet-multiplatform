package love.forte.codepoet.common.naming

import love.forte.codepoet.common.ref.TypeRef

/**
 * A Wildcard type name.
 *
 * @author ForteScarlet
 */
public interface WildcardTypeName : TypeName {
    public val bounds: List<TypeRef>

    // Java:
    //  uppers: ? extends T1 & T2
    //  lowers: ? super T1 & T2
    // Kotlin:
    //  outs, ins
}
