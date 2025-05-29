package love.forte.codegentle.kotlin.spec

import kotlin.annotation.AnnotationTarget.*

/**
 * 标记一个类是 CodeGentle Kotlin 规范的实现。
 *
 * 这个注解用于标记那些实现了 CodeGentle Kotlin 规范接口的类。
 * 使用这个注解的类应该是内部实现，不应该被外部代码直接使用。
 */
@RequiresOptIn(
    message = "这是一个内部实现 API，不应该被直接使用",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    CLASS,
    FUNCTION,
    PROPERTY,
    CONSTRUCTOR,
    TYPEALIAS
)
@MustBeDocumented
public annotation class CodeGentleKotlinSpecImplementation
