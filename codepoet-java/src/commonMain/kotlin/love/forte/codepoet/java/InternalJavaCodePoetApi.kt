package love.forte.codepoet.java

import kotlin.annotation.AnnotationTarget.*

@RequiresOptIn(
    message = "An internal API",
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
public annotation class InternalJavaCodePoetApi
