package love.forte.codepoet.java.ref

import love.forte.codepoet.common.BuilderDsl
import love.forte.codepoet.common.naming.ClassName
import love.forte.codepoet.common.naming.TypeName
import love.forte.codepoet.common.ref.AnnotationRef
import love.forte.codepoet.common.ref.TypeNameRefStatus
import love.forte.codepoet.common.ref.TypeRef
import love.forte.codepoet.java.ref.internal.JavaTypeNameRefStatusImpl
import love.forte.codepoet.java.ref.internal.JavaTypeRefImpl

/**
 * Java's [TypeRef].
 */
public interface JavaTypeRef : TypeRef {
    override val typeName: TypeName
    override val status: JavaTypeNameRefStatus
}

/**
 * Java's [TypeNameRefStatus].
 */
public interface JavaTypeNameRefStatus : TypeNameRefStatus {
    public val annotations: List<AnnotationRef>
}

/**
 * @see TypeRef
 */
public inline fun TypeName.javaRef(block: JavaTypeNameRefBuilder.() -> Unit): JavaTypeRef =
    JavaTypeNameRefBuilder(this).also(block).build()

/**
 * Builder for [JavaTypeRef].
 */
public class JavaTypeNameRefBuilder @PublishedApi internal constructor(public val typeName: TypeName) : BuilderDsl {
    public val status: JavaTypeNameRefStatusBuilder = JavaTypeNameRefStatusBuilder()

    public fun addAnnotationRef(ref: JavaAnnotationRef): JavaTypeNameRefBuilder = apply {
        status.addAnnotationRef(ref)
    }

    public fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): JavaTypeNameRefBuilder = apply {
        status.addAnnotationRefs(refs)
    }

    public fun addAnnotationRefs(vararg refs: JavaAnnotationRef): JavaTypeNameRefBuilder = apply {
        status.addAnnotationRefs(*refs)
    }

    public fun build(): JavaTypeRef {
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
public inline fun JavaTypeNameRefBuilder.status(block: JavaTypeNameRefStatusBuilder.() -> Unit = {}): JavaTypeNameRefBuilder =
    apply { status.block() }

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
public class JavaTypeNameRefStatusBuilder @PublishedApi internal constructor() : BuilderDsl {
    private val annotations: MutableList<JavaAnnotationRef> = mutableListOf()

    public fun addAnnotationRef(ref: JavaAnnotationRef): JavaTypeNameRefStatusBuilder = apply {
        annotations.add(ref)
    }

    public fun addAnnotationRefs(refs: Iterable<JavaAnnotationRef>): JavaTypeNameRefStatusBuilder = apply {
        annotations.addAll(refs)
    }

    public fun addAnnotationRefs(vararg refs: JavaAnnotationRef): JavaTypeNameRefStatusBuilder =
        addAnnotationRefs(refs.asList())

    public fun build(): JavaTypeNameRefStatus {
        return JavaTypeNameRefStatusImpl(
            annotations = annotations.toList()
        )
    }
}

public inline fun JavaTypeNameRefStatusBuilder.addAnnotationRef(
    className: ClassName,
    block: JavaAnnotationRefBuilder.() -> Unit = {}
): JavaTypeNameRefStatusBuilder =
    addAnnotationRef(JavaAnnotationRefBuilder(className).apply(block).build())
