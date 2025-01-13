module love.forte.codepoet.java {
    requires kotlin.stdlib;
    requires love.forte.codepoet.common;
    requires java.compiler;

    exports love.forte.codepoet.java;
    exports love.forte.codepoet.java.internal to love.forte.codepoet.java.apt;
}
