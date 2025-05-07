package love.forte.codepoet.java

import love.forte.codepoet.common.emitter.CodeEmitter

@InternalJavaCodePoetApi
public interface JavaCodeEmitter : CodeEmitter {
    @InternalJavaCodePoetApi
    public fun emit(codeWriter: JavaCodeWriter)
}

