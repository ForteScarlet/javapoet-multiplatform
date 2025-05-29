package love.forte.codegentle.kotlin

import love.forte.codegentle.common.naming.PackageName
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec

/**
 * 表示一个 Kotlin 源文件。
 *
 * 一个 Kotlin 源文件可以包含一个或多个顶级类、接口、对象等。
 *
 * @property packageName 包名
 * @property typeSpec 类型规范
 *
 * TODO: 实现 KotlinFile 类，参考 JavaFile 的实现
 */
public class KotlinFile private constructor(
    public val packageName: PackageName,
    public val typeSpec: KotlinTypeSpec,
    private var fileComment: String?,
    private val importedTypes: Set<TypeName>,
    private val staticImportedTypes: Map<TypeName, Set<String>>
) {
    // 用于存储导入语句的可变集合
    private val mutableImports: MutableSet<TypeName> = importedTypes.toMutableSet()
    
    // 用于存储静态导入语句的可变映射
    private val mutableStaticImports: MutableMap<TypeName, Set<String>> = staticImportedTypes.toMutableMap()

    /**
     * 获取导入的类型。
     */
    public val imports: Set<TypeName>
        get() = mutableImports.toSet()

    /**
     * 获取静态导入的类型和成员。
     */
    public val staticImports: Map<TypeName, Set<String>>
        get() = mutableStaticImports.toMap()

    /**
     * 构建器类，用于构建 [KotlinFile] 实例。
     */
    public class Builder(
        private val packageName: PackageName,
        private val typeSpec: KotlinTypeSpec
    ) {
        private var fileComment: String? = null
        private val imports: MutableSet<TypeName> = mutableSetOf()
        private val staticImports: MutableMap<TypeName, MutableSet<String>> = mutableMapOf()

        /**
         * 添加文件注释。
         *
         * @param comment 注释内容
         * @return 当前构建器实例
         */
        public fun addFileComment(comment: String): Builder = apply {
            this.fileComment = comment
        }

        /**
         * 添加导入语句。
         *
         * @param typeName 要导入的类型
         * @return 当前构建器实例
         */
        public fun addImport(typeName: TypeName): Builder = apply {
            imports.add(typeName)
        }

        /**
         * 添加静态导入语句。
         *
         * @param typeName 要导入的类型
         * @param names 要导入的静态成员名称
         * @return 当前构建器实例
         */
        public fun addStaticImport(typeName: TypeName, vararg names: String): Builder = apply {
            val set = staticImports.getOrPut(typeName) { mutableSetOf() }
            names.forEach { set.add(it) }
        }

        /**
         * 构建 [KotlinFile] 实例。
         *
         * @return 新的 [KotlinFile] 实例
         */
        public fun build(): KotlinFile {
            return KotlinFile(
                packageName = packageName,
                typeSpec = typeSpec,
                fileComment = fileComment,
                importedTypes = imports.toSet(),
                staticImportedTypes = staticImports.mapValues { it.value.toSet() }
            )
        }
    }

    public companion object {
        /**
         * 创建一个 [KotlinFile.Builder] 实例。
         *
         * @param packageName 包名
         * @param typeSpec 类型规范
         * @return 新的 [KotlinFile.Builder] 实例
         */
        public fun builder(packageName: PackageName, typeSpec: KotlinTypeSpec): Builder {
            return Builder(packageName, typeSpec)
        }
    }

    /**
     * 创建一个 [KotlinFile] 实例。
     *
     * @param packageName 包名
     * @param typeSpec 类型规范
     * @param block 配置 [KotlinFile.Builder] 的代码块
     * @return 新的 [KotlinFile] 实例
     */
    public constructor(
        packageName: PackageName,
        typeSpec: KotlinTypeSpec,
        block: Builder.() -> Unit = {}
    ) : this(
        packageName = packageName,
        typeSpec = typeSpec,
        fileComment = null,
        importedTypes = emptySet(),
        staticImportedTypes = emptyMap()
    ) {
        val builder = Builder(packageName, typeSpec).apply(block)
        val file = builder.build()
        this.fileComment = file.fileComment
        this.mutableImports.addAll(file.imports)
        this.mutableStaticImports.putAll(file.staticImports)
    }

    /**
     * 将 Kotlin 文件写入字符串。
     *
     * @return 包含 Kotlin 代码的字符串
     *
     * TODO: 实现 writeToKotlinString 方法
     */
    public fun writeToKotlinString(): String {
        // 这里需要实现将 Kotlin 文件写入字符串的逻辑
        return "// TODO: 实现 writeToKotlinString 方法"
    }
}
