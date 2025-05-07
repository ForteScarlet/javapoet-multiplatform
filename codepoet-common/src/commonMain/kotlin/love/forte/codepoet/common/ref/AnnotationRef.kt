package love.forte.codepoet.common.ref

import love.forte.codepoet.common.code.CodeValue
import love.forte.codepoet.common.naming.ClassName

/**
 * A reference to an annotation.
 *
 * @author ForteScarlet
 */
public interface AnnotationRef {
    public val className: ClassName
    public val members: Map<String, List<CodeValue>>
}
