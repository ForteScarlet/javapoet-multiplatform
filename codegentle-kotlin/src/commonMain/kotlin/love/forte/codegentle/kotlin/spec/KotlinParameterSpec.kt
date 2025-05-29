package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.spec.Spec

/**
 * 表示一个 Kotlin 参数规范。
 *
 * @property name 参数名称
 *
 * TODO: 实现 KotlinParameterSpec 类，参考 JavaParameterSpec 的实现
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinParameterSpec : Spec {
    /**
     * 参数名称。
     */
    public val name: String

    /**
     * 创建一个 Kotlin 参数规范的构建器。
     *
     * @param name 参数名称
     * @return 新的构建器实例
     */
    public companion object {
        /**
         * 创建一个 Kotlin 参数规范。
         *
         * @param name 参数名称
         * @param block 配置构建器的代码块
         * @return 新的 [KotlinParameterSpec] 实例
         */
        public operator fun invoke(
            name: String,
            block: Builder.() -> Unit = {}
        ): KotlinParameterSpec {
            // TODO: 实现 invoke 方法
            throw NotImplementedError("KotlinParameterSpec.invoke 方法尚未实现")
        }
    }

    /**
     * Kotlin 参数规范的构建器。
     */
    public interface Builder {
        /**
         * 参数名称。
         */
        public val name: String

        /**
         * 设置参数为可变参数（vararg）。
         *
         * @return 当前构建器实例
         */
        public fun vararg(): Builder

        /**
         * 设置参数为 noinline。
         *
         * @return 当前构建器实例
         */
        public fun noinline(): Builder

        /**
         * 设置参数为 crossinline。
         *
         * @return 当前构建器实例
         */
        public fun crossinline(): Builder

        /**
         * 设置参数的默认值。
         *
         * @param format 格式字符串
         * @param args 参数
         * @return 当前构建器实例
         */
        public fun defaultValue(format: String, vararg args: Any?): Builder

        /**
         * 设置参数的默认值。
         *
         * @param format 格式字符串
         * @param block 配置默认值的代码块
         * @return 当前构建器实例
         */
        public fun defaultValue(format: String, block: DefaultValueBuilder.() -> Unit): Builder

        /**
         * 构建 [KotlinParameterSpec] 实例。
         *
         * @return 新的 [KotlinParameterSpec] 实例
         */
        public fun build(): KotlinParameterSpec
    }

    /**
     * 默认值构建器。
     */
    public interface DefaultValueBuilder {
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
}
