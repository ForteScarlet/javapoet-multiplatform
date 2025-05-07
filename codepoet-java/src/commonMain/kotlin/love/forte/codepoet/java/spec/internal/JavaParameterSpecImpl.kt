package love.forte.codepoet.java.spec.internal

import love.forte.codepoet.java.JavaCodeValue
import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.JavaModifier
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.JavaArrayTypeName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.JavaAnnotationSpec
import love.forte.codepoet.java.spec.JavaParameterSpec


internal class JavaParameterSpecImpl internal constructor(
    override val type: JavaTypeName,
    override val name: String,
    override val annotations: List<JavaAnnotationSpec>,
    override val modifiers: Set<JavaModifier>,
    override val javadoc: JavaCodeValue,
) : JavaParameterSpec {
    override fun toBuilder(): JavaParameterSpec.Builder {
        return JavaParameterSpec.builder(type, name).also { builder ->
            builder.annotations.addAll(annotations)
            builder.modifiers.addAll(modifiers)
            builder.javadoc.add(javadoc)
        }
    }

    override fun emit(codeWriter: JavaCodeWriter, vararg: Boolean) {
        codeWriter.emitAnnotations(annotations, true)
        codeWriter.emitModifiers(modifiers)

        if (vararg) {
            (type as JavaArrayTypeName).emit(codeWriter, true)
        } else {
            type.emit(codeWriter)
        }

        codeWriter.emit(" $name")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaParameterSpec) return false

        if (type != other.type) return false
        if (name != other.name) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (javadoc != other.javadoc) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + javadoc.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
