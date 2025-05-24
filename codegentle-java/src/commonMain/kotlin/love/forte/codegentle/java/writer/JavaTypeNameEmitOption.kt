package love.forte.codegentle.java.writer

import love.forte.codegentle.common.writer.TypeNameEmitOption

public sealed class JavaTypeNameEmitOption : TypeNameEmitOption {
    public data object Vararg : JavaTypeNameEmitOption()
}
