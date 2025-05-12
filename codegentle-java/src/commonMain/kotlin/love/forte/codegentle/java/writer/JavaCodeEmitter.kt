package love.forte.codegentle.java.writer

import love.forte.codegentle.common.writer.CodeEmitter
import love.forte.codegentle.java.InternalJavaCodeGentleApi

@InternalJavaCodeGentleApi
public interface JavaCodeEmitter : CodeEmitter {
    @InternalJavaCodeGentleApi
    public fun emit(codeWriter: JavaCodeWriter)
}
