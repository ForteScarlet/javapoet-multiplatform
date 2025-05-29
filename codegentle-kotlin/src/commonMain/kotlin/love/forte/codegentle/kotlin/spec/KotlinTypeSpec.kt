package love.forte.codegentle.kotlin.spec

/**
 * Kotlin 类型规范接口，表示一个 Kotlin 类型（类、接口、对象等）。
 *
 * TODO: 实现 KotlinTypeSpec 接口，参考 JavaTypeSpec 的实现
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinTypeSpec : KotlinSpec {
    /**
     * 类型的种类（类、接口、对象等）。
     */
    public enum class Kind {
        /**
         * 类
         */
        CLASS,

        /**
         * 接口
         */
        INTERFACE,

        /**
         * 对象
         */
        OBJECT,

        /**
         * 伴生对象
         */
        COMPANION_OBJECT,

        /**
         * 枚举类
         */
        ENUM,

        /**
         * 注解类
         */
        ANNOTATION,

        /**
         * 密封类
         */
        SEALED,

        /**
         * 数据类
         */
        DATA,

        /**
         * 值类
         */
        VALUE,

        /**
         * 函数式接口
         */
        FUN_INTERFACE,

        /**
         * 类型别名
         */
        TYPE_ALIAS
    }

    /**
     * 获取类型的种类。
     *
     * @return 类型的种类
     */
    public val kind: Kind

    /**
     * 获取类型的名称。
     *
     * @return 类型的名称
     */
    public val name: String
}
