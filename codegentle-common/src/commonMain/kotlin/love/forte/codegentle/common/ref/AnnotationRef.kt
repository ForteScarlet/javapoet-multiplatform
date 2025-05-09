package love.forte.codegentle.common.ref

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName

/**
 * A reference to an annotation.
 *
 * @author ForteScarlet
 */
public interface AnnotationRef {
    public val className: ClassName
    public val members: Map<String, List<CodeValue>>
}
