package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.naming.JavaArrayTypeName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.spec.JavaParameterSpec
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emitToString


internal class JavaParameterSpecImpl internal constructor(
    override val type: JavaTypeRef<*>,
    override val name: String,
    override val annotations: List<JavaAnnotationRef>,
    override val modifiers: Set<JavaModifier>,
    override val javadoc: JavaCodeValue,
) : JavaParameterSpec {
    override fun emit(codeWriter: JavaCodeWriter, vararg: Boolean) {
        codeWriter.emitAnnotationRefs(annotations, true)
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
