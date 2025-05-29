package love.forte.codegentle.kotlin

import kotlin.annotation.AnnotationTarget.*

@RequiresOptIn(
    message = "这是一个内部 API",
    level = RequiresOptIn.Level.ERROR
)
@Retention(AnnotationRetention.BINARY)
@Target(
    CLASS,
    ANNOTATION_CLASS,
    PROPERTY,
    FIELD,
    CONSTRUCTOR,
    FUNCTION,
    PROPERTY_GETTER,
    PROPERTY_SETTER,
    TYPEALIAS
)
@MustBeDocumented
public annotation class InternalKotlinCodeGentleApi
