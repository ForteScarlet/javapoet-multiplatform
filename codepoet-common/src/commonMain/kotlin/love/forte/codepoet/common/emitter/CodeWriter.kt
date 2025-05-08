package love.forte.codepoet.common.emitter

import love.forte.codepoet.common.naming.ClassName

/**
 *
 * @author ForteScarlet
 */
public interface CodeWriter {
    public val indentValue: String
    // out?
    public val staticImportClassNames: Set<ClassName>

    public val staticImports: Set<String> // String?
    public val alwaysQualify: Set<String>

}
