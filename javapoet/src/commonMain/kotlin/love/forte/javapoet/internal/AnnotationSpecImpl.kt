package love.forte.javapoet.internal

import love.forte.javapoet.AnnotationSpec
import love.forte.javapoet.CodeBlock
import love.forte.javapoet.CodeWriter
import love.forte.javapoet.TypeName


internal class AnnotationSpecImpl(
    override val type: TypeName,
    override val members: Map<String, List<CodeBlock>>,
) : AnnotationSpec {
    override fun toBuilder(): AnnotationSpec.Builder {
        val builder = AnnotationSpec.Builder(type)
        members.mapValuesTo(builder.members) { it.value.toMutableList() }
        return builder
    }

    override fun emit(codeWriter: CodeWriter) {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotationSpec) return false

        if (type != other.type) return false
        if (members != other.members) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + members.hashCode()
        return result
    }

    override fun toString(): String {
        // TODO CodeWriter
        return super.toString()
    }
}
