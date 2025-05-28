package love.forte.codegentle.java.writer

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.MemberName

/**
 * Java import for classes and static members (static fields, static methods, etc.).
 *
 * @author ForteScarlet
 */
public sealed class JavaImport {
    internal data class ClassImport(val className: ClassName) : JavaImport()
    internal data class MemberImport(val memberName: MemberName) : JavaImport()
}
