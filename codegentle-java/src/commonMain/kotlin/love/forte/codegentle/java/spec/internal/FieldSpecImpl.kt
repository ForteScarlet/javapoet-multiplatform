package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.spec.FieldSpec
import love.forte.codegentle.java.spec.JavaAnnotationSpec


/**
 *
 * @author ForteScarlet
 */
internal class FieldSpecImpl internal constructor(
    override val type: JavaTypeName,
    override val name: String,
    override val javadoc: JavaCodeValue,
    override val annotations: List<JavaAnnotationSpec>,
    override val modifiers: Set<JavaModifier>,
    override val initializer: JavaCodeValue
) : FieldSpec {
    override fun toBuilder(): FieldSpec.Builder {
        return FieldSpec.Builder(type, name).also { builder ->
            builder.javadoc.add(javadoc)
            builder.annotations.addAll(annotations)
            builder.modifiers.addAll(modifiers)
            builder.initializer = initializer.takeUnless { it.isEmpty }
        }
    }

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        codeWriter.emitJavadoc(javadoc)
        codeWriter.emitAnnotations(annotations, false)
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
