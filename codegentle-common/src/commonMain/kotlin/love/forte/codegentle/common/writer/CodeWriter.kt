package love.forte.codegentle.common.writer

import love.forte.codegentle.common.naming.ClassName

/**
 *
 * @author ForteScarlet
 */
public interface CodeWriter {
    public val strategy: Strategy

    public val indentValue: String

    // out?
    public val staticImportClassNames: Set<ClassName>
    public val staticImports: Set<String> // String?
    public val alwaysQualify: Set<String>

}
