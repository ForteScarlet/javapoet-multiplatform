module love.forte.codegentle.common {
    requires java.compiler;
    requires kotlin.stdlib;

    exports love.forte.codegentle.common;
    exports love.forte.codegentle.common.code;
    exports love.forte.codegentle.common.writer;
    exports love.forte.codegentle.common.naming;
    exports love.forte.codegentle.common.ref;
    exports love.forte.codegentle.common.spec;
    // TODO exports .. to .. Only
    exports love.forte.codegentle.common.codepoint;
}
