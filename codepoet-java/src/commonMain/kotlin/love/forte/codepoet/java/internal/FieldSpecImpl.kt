package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


/**
 *
 * @author ForteScarlet
 */
internal class FieldSpecImpl internal constructor(
    override val type: TypeName,
    override val name: String,
    override val javadoc: CodeBlock,
    override val annotations: List<AnnotationSpec>,
    override val modifiers: Set<Modifier>,
    override val initializer: CodeBlock
) : FieldSpec {
    override fun toBuilder(): FieldSpec.Builder {
        return FieldSpec.Builder(type, name).also { builder ->
            builder.javadoc.add(javadoc)
            builder.annotations.addAll(annotations)
            builder.modifiers.addAll(modifiers)
            builder.initializer = initializer.takeUnless { it.isEmpty }
        }
    }

    override fun emit(codeWriter: CodeWriter) {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is FieldSpecImpl) return false

        if (type != other.type) return false
        if (name != other.name) return false
        if (javadoc != other.javadoc) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (initializer != other.initializer) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + javadoc.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + initializer.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
