package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.spec.Spec

/**
 * 表示一个 Kotlin 函数规范。
 *
 * @property name 函数名称
 *
 * TODO: 实现 KotlinFunctionSpec 类，参考 JavaMethodSpec 的实现
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinFunctionSpec : Spec {
    /**
     * 函数名称。
     */
    public val name: String

    /**
     * 创建一个 Kotlin 函数规范的构建器。
     *
     * @param name 函数名称
     * @return 新的构建器实例
     */
    public companion object {
        /**
         * 创建一个 Kotlin 函数规范。
         *
         * @param name 函数名称
         * @param block 配置构建器的代码块
         * @return 新的 [KotlinFunctionSpec] 实例
         */
        public operator fun invoke(
            name: String,
            block: KotlinFunctionSpecBuilder.() -> Unit = {}
        ): KotlinFunctionSpec {
            // TODO: 实现 invoke 方法
            TODO("KotlinFunctionSpec.invoke 方法尚未实现")
        }
    }

}

/**
 * Kotlin 函数规范的构建器。
 */
public class KotlinFunctionSpecBuilder {
    /**
     * 函数名称。
     */
    public val name: String = TODO()

    // TODO addStatement

    /**
     * 构建 [KotlinFunctionSpec] 实例。
     *
     * @return 新的 [KotlinFunctionSpec] 实例
     */
    public fun build(): KotlinFunctionSpec = TODO()
}

/**
 * 语句构建器。
 */
public interface KFSStatementBuilder {
    /**
     * 添加字符串参数。
     *
     * @param value 字符串值
     */
    public fun emitString(value: String)

    /**
     * 添加数字参数。
     *
     * @param value 数字值
     */
    public fun emitNumber(value: Number)

    /**
     * 添加布尔参数。
     *
     * @param value 布尔值
     */
    public fun emitBoolean(value: Boolean)
}
