package love.forte.codegentle.kotlin.ref

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.*

@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface KotlinTypeNameRefStatus : TypeNameRefStatus {
    public val annotations: List<AnnotationRef>

    public companion object : TypeNameRefStatusBuilderFactory<KotlinTypeNameRefStatus, KotlinTypeNameRefStatusBuilder> {
        override fun createBuilder(): KotlinTypeNameRefStatusBuilder = KotlinTypeNameRefStatusBuilder()
    }
}

/**
 * A builder for [KotlinTypeNameRefStatus].
 */
public class KotlinTypeNameRefStatusBuilder @PublishedApi internal constructor() :
    AnnotationRefCollectable<KotlinTypeNameRefStatusBuilder>,
    TypeNameRefStatusBuilder<KotlinTypeNameRefStatus> {
    public var nullable: Boolean = false
    private val annotations: MutableList<AnnotationRef> = mutableListOf()

    override fun addAnnotationRef(ref: AnnotationRef): KotlinTypeNameRefStatusBuilder = apply {
        annotations.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): KotlinTypeNameRefStatusBuilder = apply {
        annotations.addAll(refs)
    }

    override fun build(): KotlinTypeNameRefStatus {
        TODO("impl")
        // return JavaTypeNameRefStatusImpl(
        //     annotations = annotations.toList()
        // )
    }
}

public typealias KotlinTypeRefBuilderDsl<T> = TypeRefBuilder<T, KotlinTypeNameRefStatus, KotlinTypeNameRefStatusBuilder>.() -> Unit

/**
 * Create a [TypeRef] with [T] and [KotlinTypeNameRefStatus].
 *
 * @see TypeRef
 */
public inline fun <T : TypeName> T.kotlinRef(
    block: KotlinTypeRefBuilderDsl<T> = {}
): TypeRef<T> = ref(KotlinTypeNameRefStatus, block)

/**
 * ```Kotlin
 * status as? JavaTypeNameRefStatus
 * ```
 */
public val TypeNameRefStatus.kotlinOrNull: KotlinTypeNameRefStatus?
    get() = this as? KotlinTypeNameRefStatus?
