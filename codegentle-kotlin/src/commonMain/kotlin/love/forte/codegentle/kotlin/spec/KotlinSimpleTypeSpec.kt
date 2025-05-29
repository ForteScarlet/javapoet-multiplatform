package love.forte.codegentle.kotlin.spec

/**
 * 表示一个简单的 Kotlin 类型规范，如类、接口等。
 *
 * @property kind 类型的种类
 * @property name 类型的名称
 *
 * TODO: 实现 KotlinSimpleTypeSpec 类，参考 JavaSimpleTypeSpec 的实现
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinSimpleTypeSpec : KotlinTypeSpec {
    /**
     * 创建一个简单的 Kotlin 类型规范的构建器。
     *
     * @param kind 类型的种类
     * @param name 类型的名称
     * @return 新的构建器实例
     */
    public companion object {
        /**
         * 创建一个简单的 Kotlin 类型规范。
         *
         * @param kind 类型的种类
         * @param name 类型的名称
         * @param block 配置构建器的代码块
         * @return 新的 [KotlinSimpleTypeSpec] 实例
         */
        public operator fun invoke(
            kind: KotlinTypeSpec.Kind,
            name: String,
            block: Builder.() -> Unit = {}
        ): KotlinSimpleTypeSpec {
            // TODO: 实现 invoke 方法
            throw NotImplementedError("KotlinSimpleTypeSpec.invoke 方法尚未实现")
        }
    }

    /**
     * 简单 Kotlin 类型规范的构建器。
     */
    public interface Builder {
        /**
         * 类型的种类。
         */
        public val kind: KotlinTypeSpec.Kind

        /**
         * 类型的名称。
         */
        public val name: String

        /**
         * 构建 [KotlinSimpleTypeSpec] 实例。
         *
         * @return 新的 [KotlinSimpleTypeSpec] 实例
         */
        public fun build(): KotlinSimpleTypeSpec
    }
}
