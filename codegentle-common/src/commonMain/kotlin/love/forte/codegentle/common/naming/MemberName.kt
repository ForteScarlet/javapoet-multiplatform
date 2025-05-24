package love.forte.codegentle.common.naming


/**
 * A Member name.
 *
 * - Java: static methods, static fields, Enum's elements, enclosed classes, etc.
 * - Kotlin: top-level functions, Object's properties, Enum's elements, enclosed classes, etc.
 *
 * @author ForteScarlet
 */
public interface MemberName : Naming, Named {
    /**
     * Member's name.
     */
    override val name: String

    /**
     * Member's package name.
     */
    public val packageName: PackageName

    /**
     * Enclosing [ClassName] if it exists.
     *
     */
    public val enclosingClassName: ClassName?
}
// TODO MemberName? or Kotlin Only?
