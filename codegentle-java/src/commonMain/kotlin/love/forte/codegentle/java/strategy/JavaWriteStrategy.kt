package love.forte.codegentle.java.strategy

import love.forte.codegentle.common.writer.Strategy

/**
 * The strategies for writing Java files.
 *
 * @author ForteScarlet
 */
public interface JavaWriteStrategy : Strategy {
    /**
     * Whether to omit reference to `java.lang`.
     */
    public fun omitJavaLangPackage(): Boolean

    public companion object Default : JavaWriteStrategy by DefaultJavaWriteStrategy()
}
