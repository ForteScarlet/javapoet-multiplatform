package love.forte.codegentle.java

import love.forte.codegentle.common.emitter.CodeEmitter

@InternalJavaCodePoetApi
public interface JavaCodeEmitter : CodeEmitter {
    @InternalJavaCodePoetApi
    public fun emit(codeWriter: JavaCodeWriter)
}

