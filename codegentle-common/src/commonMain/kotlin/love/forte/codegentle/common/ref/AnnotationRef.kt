package love.forte.codegentle.common.ref

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.computeValueIfAbsent
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.ref.internal.AnnotationRefImpl

/**
 * A reference to an annotation.
 *
 * @author ForteScarlet
 */
@SubclassOptInRequired(CodeGentleRefImplementation::class)
public interface AnnotationRef : TypeRef<ClassName> {
    override val typeName: ClassName
    public val members: Map<String, List<CodeValue>>

    override val status: TypeNameRefStatus
        get() = EmptyTypeNameRefStatus
}

public inline fun ClassName.annotationRef(block: AnnotationRefBuilder.() -> Unit = {}): AnnotationRef {
    return AnnotationRefBuilder(this@annotationRef).apply {
        block()
    }.build()
}

/**
 * Builder for [AnnotationRef].
 */
public class AnnotationRefBuilder(public val className: ClassName) :
    AnnotationRefBuildable<AnnotationRefBuilder> {
    private val members: MutableMap<String, MutableList<CodeValue>> = linkedMapOf()

    override fun addMember(name: String, codeValue: CodeValue): AnnotationRefBuilder = apply {
        val values = members.computeValueIfAbsent(name) { mutableListOf() }
        values.add(codeValue)
    }

    public fun build(): AnnotationRef {
        return AnnotationRefImpl(
            typeName = className,
            members = members.mapValues { it.value.toList() }
        )
    }
}
