package love.forte.codepoet.java.ref

import love.forte.codepoet.common.BuilderDsl
import love.forte.codepoet.java.naming.JavaClassName

/**
 *
 *
 * ```Kotlin
 * builder {
 *    addAnnotationRef(...)
 * }
 * ```
 *
 *
 * @author ForteScarlet
 */
public interface JavaAnnotationRefCollectable<B : JavaAnnotationRefCollectable<B>> : BuilderDsl {
    // private val annotations: MutableList<JavaAnnotationRef> = mutableListOf()

    public fun addAnnotationRef(ref: JavaAnnotationRef): B

    public fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): B

    public fun addAnnotationRefs(vararg refs: JavaAnnotationRef): B =
        addAnnotationRefs(refs.asList())
}

public inline fun <B : JavaAnnotationRefCollectable<B>> B.addAnnotationRef(
    className: JavaClassName,
    block: JavaAnnotationRefBuilder.() -> Unit = {}
): B = addAnnotationRef(JavaAnnotationRefBuilder(className).apply(block).build())
