package love.forte.codegentle.common.naming

/**
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleNamingImplementation::class)
public interface Named {
    public val name: String
}
