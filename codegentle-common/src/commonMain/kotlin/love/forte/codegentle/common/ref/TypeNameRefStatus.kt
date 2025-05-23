package love.forte.codegentle.common.ref

/**
 * A reference status.
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeNameRefStatus

public object EmptyTypeNameRefStatus : TypeNameRefStatus

//region JavaTypeNameRefStatus
// @SubclassOptInRequired(CodeGentleRefImplementation::class)
// public interface JavaTypeNameRefStatus : TypeNameRefStatus {
//     public val annotations: List<AnnotationRef>
//
//     public companion object : TypeNameRefStatusBuilderFactory<JavaTypeNameRefStatus, JavaTypeNameRefStatusBuilder> {
//         override fun createBuilder(): JavaTypeNameRefStatusBuilder {
//             TODO("Not yet implemented")
//         }
//     }
// }
//
// /**
//  * A builder for [JavaTypeNameRefStatus].
//  */
// public class JavaTypeNameRefStatusBuilder @PublishedApi internal constructor() :
//     AnnotationRefCollectable<JavaTypeNameRefStatusBuilder>,
//     TypeNameRefStatusBuilder<JavaTypeNameRefStatus> {
//     private val annotations: MutableList<AnnotationRef> = mutableListOf()
//
//     override fun addAnnotationRef(ref: AnnotationRef): JavaTypeNameRefStatusBuilder = apply {
//         annotations.add(ref)
//     }
//
//     override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): JavaTypeNameRefStatusBuilder = apply {
//         annotations.addAll(refs)
//     }
//
//     override fun build(): JavaTypeNameRefStatus {
//         return JavaTypeNameRefStatusImpl(
//             annotations = annotations.toList()
//         )
//     }
// }
//endregion


// TODO Kotlin ref


// Builders

@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeNameRefStatusBuilder<out T : TypeNameRefStatus> {
    public fun build(): T
}


@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface TypeNameRefStatusBuilderFactory<out T : TypeNameRefStatus, out B : TypeNameRefStatusBuilder<T>> {
    public fun createBuilder(): B
}
