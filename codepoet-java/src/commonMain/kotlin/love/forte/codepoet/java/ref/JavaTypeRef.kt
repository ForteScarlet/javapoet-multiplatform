package love.forte.codepoet.java.ref

import love.forte.codepoet.common.BuilderDsl
import love.forte.codepoet.common.ref.TypeNameRefStatus
import love.forte.codepoet.common.ref.TypeRef
import love.forte.codepoet.java.JavaCodeEmitter
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.ref.internal.JavaTypeNameRefStatusImpl
import love.forte.codepoet.java.ref.internal.JavaTypeRefImpl

/**
 * Java's [TypeRef].
 */
public interface JavaTypeRef<out T : JavaTypeName> : TypeRef, JavaCodeEmitter {
    override val typeName: T
    override val status: JavaTypeNameRefStatus
}

/**
 * Java's [TypeNameRefStatus].
 */
public interface JavaTypeNameRefStatus : TypeNameRefStatus {
    public val annotations: List<JavaAnnotationRef>
}

/**
 * @see TypeRef
 */
public inline fun <T : JavaTypeName> T.javaRef(block: JavaTypeRefBuilder<T>.() -> Unit = {}): JavaTypeRef<T> =
    JavaTypeRefBuilder(this).also(block).build()

/**
 * Builder for [JavaTypeRef].
 */
public class JavaTypeRefBuilder<T : JavaTypeName> @PublishedApi internal constructor(public val typeName: T) :
    JavaAnnotationRefCollectable<JavaTypeRefBuilder<T>>,
    BuilderDsl {
    public val status: JavaTypeNameRefStatusBuilder = JavaTypeNameRefStatusBuilder()

    override fun addAnnotationRef(ref: JavaAnnotationRef): JavaTypeRefBuilder<T> = apply {
        status.addAnnotationRef(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): JavaTypeRefBuilder<T> = apply {
        status.addAnnotationRefs(refs)
    }

    public fun build(): JavaTypeRef<T> {
        return JavaTypeRefImpl(
            typeName = typeName,
            status = status.build()
        )
    }
}

/**
 * ```Kotlin
 * val ref = JavaTypeNameRef {
 *     status {
 *         // ...
 *     }
 * }
 */
public inline fun <T : JavaTypeName> JavaTypeRefBuilder<T>.status(
    block: JavaTypeNameRefStatusBuilder.() -> Unit = {}
): JavaTypeRefBuilder<T> = apply { status.block() }

/**
 * ```Kotlin
 * val status = JavaTypeNameRefStatus {
 *     // ...
 * }
 * ```
 */
public inline fun JavaTypeNameRefStatus(block: JavaTypeNameRefStatusBuilder.() -> Unit = {}): JavaTypeNameRefStatus =
    JavaTypeNameRefStatusBuilder().apply(block).build()

/**
 * Builder for [JavaTypeNameRefStatus].
 */
public class JavaTypeNameRefStatusBuilder @PublishedApi internal constructor() :
    JavaAnnotationRefCollectable<JavaTypeNameRefStatusBuilder> {
    private val annotations: MutableList<JavaAnnotationRef> = mutableListOf()

    override fun addAnnotationRef(ref: JavaAnnotationRef): JavaTypeNameRefStatusBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): JavaTypeNameRefStatusBuilder = apply {
        annotations.addAll(refs)
    }

    public fun build(): JavaTypeNameRefStatus {
        return JavaTypeNameRefStatusImpl(
            annotations = annotations.toList()
        )
    }
}

public inline fun JavaTypeNameRefStatusBuilder.addAnnotationRef(
    className: JavaClassName,
    block: JavaAnnotationRefBuilder.() -> Unit = {}
): JavaTypeNameRefStatusBuilder =
    addAnnotationRef(JavaAnnotationRefBuilder(className).apply(block).build())
