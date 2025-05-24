package love.forte.codegentle.java.writer

import love.forte.codegentle.common.writer.AnnotationRefEmitOption
import love.forte.codegentle.common.writer.TypeNameEmitOption
import love.forte.codegentle.common.writer.TypeRefEmitOption

public sealed class JavaTypeRefEmitOption : TypeRefEmitOption {
    public class AnnotationOptions(public vararg val options: AnnotationRefEmitOption) : JavaTypeRefEmitOption()
    public class TypeNameOptions(public vararg val options: TypeNameEmitOption) : JavaTypeRefEmitOption()
}
