package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.spec.JavaFieldSpec
import love.forte.codegentle.java.type
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit
import love.forte.codegentle.java.writer.emitToString


/**
 *
 * @author ForteScarlet
 */
internal class JavaFieldSpecImpl internal constructor(
    override val type: JavaTypeRef<*>,
    override val name: String,
    override val javadoc: JavaCodeValue,
    override val annotations: List<JavaAnnotationRef>,
    override val modifiers: Set<JavaModifier>,
    override val initializer: JavaCodeValue
) : JavaFieldSpec {

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        codeWriter.emitJavadoc(javadoc)
        codeWriter.emitAnnotationRefs(annotations, false)
        codeWriter.emitModifiers(modifiers, implicitModifiers)
        codeWriter.emit("%V $name") {
            type(type)
        }
        if (!initializer.isEmpty) {
            codeWriter.emit(" = ")
            initializer.emit(codeWriter)
        }
        codeWriter.emit(";\n")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaFieldSpecImpl) return false

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
