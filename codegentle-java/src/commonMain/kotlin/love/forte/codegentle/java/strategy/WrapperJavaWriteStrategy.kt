package love.forte.codegentle.java.strategy

public abstract class WrapperJavaWriteStrategy(protected val target: JavaWriteStrategy): JavaWriteStrategy by target
