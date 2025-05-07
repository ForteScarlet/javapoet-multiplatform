package love.forte.codepoet.java.naming.internal

import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.naming.JavaTypeVariableName


/**
 *
 * @author ForteScarlet
 */
internal class JavaTypeVariableNameImpl(
    override val name: String,
    override val bounds: List<JavaTypeName> = emptyList(),
    // override val annotations: List<JavaAnnotationSpec> = emptyList()
) : JavaTypeVariableName {
    // override fun annotated(annotations: List<JavaAnnotationSpec>): JavaTypeVariableName {
    //     if (annotations.isEmpty()) return this
    //
    //     return JavaTypeVariableNameImpl(name, bounds, this.annotations + annotations)
    // }
    //
    // override fun withoutAnnotations(): JavaTypeVariableName {
    //     return if (annotations.isEmpty()) this else JavaTypeVariableNameImpl(name, bounds)
    // }

    override fun withBounds(bounds: List<JavaTypeName>): JavaTypeVariableName {
        return JavaTypeVariableNameImpl(name, this.bounds + bounds)
    }

    override fun emit(codeWriter: JavaCodeWriter) {
        emitAnnotations(codeWriter)
        codeWriter.emitAndIndent(name)
    }

    override fun toString(): String {
        return emitToString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaTypeVariableNameImpl) return false

        if (name != other.name) return false
        if (bounds != other.bounds) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + bounds.hashCode()
        return result
    }
}
