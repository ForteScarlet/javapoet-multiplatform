package love.forte.codegentle.java.writer

import love.forte.codegentle.common.writer.CodeValueEmitOption

public sealed class JavaCodeValueEmitOption : CodeValueEmitOption {
    public data object EnsureTrailingNewline : JavaCodeValueEmitOption()
}
