package love.forte.codegentle.java.strategy

import love.forte.codegentle.common.writer.Strategy

/**
 * The strategies for writing java files.
 *
 * @author ForteScarlet
 */
public interface JavaWriteStrategy : Strategy {


    public companion object Default : JavaWriteStrategy by DefaultJavaWriteStrategy()
}
