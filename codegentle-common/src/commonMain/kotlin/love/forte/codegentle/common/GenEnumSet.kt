package love.forte.codegentle.common

/**
 * Mark an `enum class` to generate a `Set<TheEnum>` implementation
 */
@Retention(AnnotationRetention.SOURCE)
public annotation class GenEnumSet(
    val internal: Boolean = false,
    val mutableName: String = "",
    val immutableName: String = "",
    val containerName: String = "",
    val containerSingleAdder: String = "",
    val containerMultiAdder: String = "",
    val operatorsName: String = "",
)

// 标记一个enum class, 为它生成一个基于
