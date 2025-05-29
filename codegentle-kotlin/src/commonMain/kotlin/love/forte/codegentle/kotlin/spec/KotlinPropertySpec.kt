package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.spec.Spec

/**
 * 表示一个 Kotlin 属性规范。
 *
 * @property name 属性名称
 *
 * TODO: 实现 KotlinPropertySpec 类，参考 JavaFieldSpec 的实现
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinPropertySpec : Spec {
    /**
     * 属性名称。
     */
    public val name: String

    /**
     * 创建一个 Kotlin 属性规范的构建器。
     *
     * @param name 属性名称
     * @return 新的构建器实例
     */
    public companion object {
        /**
         * 创建一个 Kotlin 属性规范。
         *
         * @param name 属性名称
         * @param block 配置构建器的代码块
         * @return 新的 [KotlinPropertySpec] 实例
         */
        public operator fun invoke(
            name: String,
            block: Builder.() -> Unit = {}
        ): KotlinPropertySpec {
            // TODO: 实现 invoke 方法
            throw NotImplementedError("KotlinPropertySpec.invoke 方法尚未实现")
        }
    }

    /**
     * Kotlin 属性规范的构建器。
     */
    public interface Builder {
        /**
         * 属性名称。
         */
        public val name: String

        /**
         * 设置属性的初始化表达式。
         *
         * @param format 格式字符串
         * @param args 参数
         * @return 当前构建器实例
         */
        public fun initializer(format: String, vararg args: Any?): Builder

        /**
         * 设置属性的初始化表达式。
         *
         * @param format 格式字符串
         * @param block 配置初始化表达式的代码块
         * @return 当前构建器实例
         */
        public fun initializer(format: String, block: InitializerBuilder.() -> Unit): Builder

        /**
         * 设置属性为可变（var）。
         *
         * @return 当前构建器实例
         */
        public fun mutable(): Builder

        /**
         * 设置属性为只读（val）。
         *
         * @return 当前构建器实例
         */
        public fun readonly(): Builder

        /**
         * 设置属性的委托表达式。
         *
         * @param format 格式字符串
         * @param args 参数
         * @return 当前构建器实例
         */
        public fun delegate(format: String, vararg args: Any?): Builder

        /**
         * 设置属性的委托表达式。
         *
         * @param format 格式字符串
         * @param block 配置委托表达式的代码块
         * @return 当前构建器实例
         */
        public fun delegate(format: String, block: InitializerBuilder.() -> Unit): Builder

        /**
         * 构建 [KotlinPropertySpec] 实例。
         *
         * @return 新的 [KotlinPropertySpec] 实例
         */
        public fun build(): KotlinPropertySpec
    }

    /**
     * 初始化表达式构建器。
     */
    public interface InitializerBuilder {
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
