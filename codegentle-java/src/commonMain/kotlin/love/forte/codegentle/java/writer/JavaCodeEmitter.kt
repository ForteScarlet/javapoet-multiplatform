package love.forte.codegentle.java.writer

import love.forte.codegentle.java.InternalJavaCodeGentleApi

@InternalJavaCodeGentleApi
public interface JavaCodeEmitter {
    @InternalJavaCodeGentleApi
    public fun emit(codeWriter: JavaCodeWriter)
}
