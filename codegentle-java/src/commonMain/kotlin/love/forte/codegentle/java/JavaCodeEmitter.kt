package love.forte.codegentle.java

import love.forte.codegentle.common.writer.CodeEmitter

@InternalJavaCodeGentleApi
public interface JavaCodeEmitter : CodeEmitter {
    @InternalJavaCodeGentleApi
    public fun emit(codeWriter: JavaCodeWriter)
}

