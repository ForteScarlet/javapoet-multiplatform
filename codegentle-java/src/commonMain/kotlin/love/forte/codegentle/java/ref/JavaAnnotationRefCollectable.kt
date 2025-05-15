package love.forte.codegentle.java.ref

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.literal
import love.forte.codegentle.java.naming.JavaAnnotationNames
import love.forte.codegentle.java.string
import love.forte.codegentle.java.type
import kotlin.jvm.JvmInline

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
    className: ClassName,
    block: JavaAnnotationRefBuilder.() -> Unit = {}
): B = addAnnotationRef(JavaAnnotationRefBuilder(className).apply(block).build())

/**
 * Some operations for [JavaAnnotationRefCollectable].
 *
 * @see JavaAnnotationNames
 */
@JvmInline
public value class AnnotationRefCollectableOps<B : JavaAnnotationRefCollectable<B>>
@PublishedApi
internal constructor(public val collectable: B) {
    // TODO auto-generate?

    public fun add(ref: JavaAnnotationRef): B = collectable.addAnnotationRef(ref)
    public fun addAll(refs: Iterable<JavaAnnotationRef>): B = collectable.addAnnotationRefs(refs)
    public fun addAll(vararg refs: JavaAnnotationRef): B = collectable.addAnnotationRefs(refs.asList())

    /**
     * Add annotation ref: [JavaAnnotationNames.Override]
     */
    public fun addOverride(): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Override)
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Deprecated]
     *
     * @param since `since` of `java.lang.Deprecated` since Java 9.
     * @param forRemoval `forRemoval` of `java.lang.Deprecated` since Java 9.
     */
    public fun addDeprecated(
        since: String? = null,
        forRemoval: Boolean? = null,
    ): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Deprecated) {
            since?.also { since ->
                addMember("since", "%V") {
                    string(since)
                }
            }
            forRemoval?.also { forRemoval ->
                addMember("forRemoval", forRemoval.toString())
            }
        }
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.SuppressWarnings]
     *
     * @param values `value` of `java.lang.SuppressWarnings`.
     */
    public fun addSuppressWarnings(vararg values: String): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.SuppressWarnings) {
            if (values.isNotEmpty()) {
                for (value in values) {
                    addMember("value", "%V") { string(value) }
                }
            }
        }
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.SafeVarargs]
     */
    public fun addSafeVarargs(): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.SafeVarargs)
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Documented]
     */
    public fun addDocumented(): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Documented)
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Retention]
     *
     * @param value `value` of `java.lang.annotation.Retention`
     */
    public fun addRetention(value: AnnotationRetention? = null): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Retention) {
            val javaRetentionName = when (value) {
                AnnotationRetention.SOURCE -> "SOURCE"
                AnnotationRetention.BINARY -> "CLASS"
                AnnotationRetention.RUNTIME -> "RUNTIME"
                null -> null
            }

            javaRetentionName?.also { retentionName ->
                addMember("value", "%V.%V") {
                    type(ClassName("java.lang.annotation", "Retention"))
                    literal(retentionName)
                }
            }
        }
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Target].
     *
     * Note that [values] are not validated.
     *
     * @param values Elements of `java.lang.annotation.ElementType`.
     * All chars must meet `it in 'A'..'Z' || it == '_'`.
     *
     * @throws IllegalArgumentException if any chars not meet `it in 'A'..'Z' || it == '_'`
     */
    public fun addTarget(vararg values: String): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Target) {
            if (values.isNotEmpty()) {
                for (value in values) {
                    val elementName = value.uppercase()
                    require(elementName.all { it in 'A'..'Z' || it == '_' })
                    addMember("value", "%V.%V") {
                        type(ClassName("java.lang.annotation", "ElementType"))
                        literal(elementName)
                    }
                }
            }
        }
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Inherited].
     */
    public fun addInherited(): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Inherited)
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Repeatable].
     *
     * @param value `value` of `java.lang.annotation.Repeatable`
     */
    public fun addRepeatable(value: ClassName): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Repeatable) {
            addMember("value", "%V.class") {
                type(value)
            }
        }
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Native].
     */
    public fun addNative(): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Native)
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.FunctionalInterface].
     */
    public fun addFunctionalInterface(): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.FunctionalInterface)
    }

    /**
     * Add annotation ref: [JavaAnnotationNames.Generated] since Java 9.
     *
     * @param value `value` of `javax.annotation.processing.Generated`.
     * @param date `date` of `javax.annotation.processing.Generated`.
     * @param comments `comments` of `javax.annotation.processing.Generated`.
     */
    public fun addGenerated(
        values: Array<String>? = null,
        date: String? = null,
        comments: String? = null
    ): B {
        return collectable.addAnnotationRef(JavaAnnotationNames.Generated) {
            values?.takeIf { it.isNotEmpty() }?.also { values ->
                for (value in values) {
                    addMember("value", "%V") { string(value) }
                }
            }
            date?.also { d -> addMember("date", "%V") { string(d) } }
            comments?.also { c -> addMember("comments", "%V") { string(c) } }
        }
    }

}

public inline val <B : JavaAnnotationRefCollectable<B>> B.annotationRefs
    get() = AnnotationRefCollectableOps(this)

/**
 * DSL block with [AnnotationRefCollectableOps].
 */
public inline fun <B : JavaAnnotationRefCollectable<B>> B.annotationRefs(
    block: AnnotationRefCollectableOps<B>.() -> Unit
) {
    annotationRefs.block()
}
