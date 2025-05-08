package love.forte.codepoet.java.naming.internal

import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.JavaTypeVariableName
import love.forte.codepoet.java.ref.JavaTypeRef


/**
 *
 * @author ForteScarlet
 */
internal class JavaTypeVariableNameImpl(
    override val name: String,
    override val bounds: List<JavaTypeRef> = emptyList(),
) : JavaTypeVariableName {
    override fun withBounds(bounds: List<JavaTypeRef>): JavaTypeVariableName {
        return JavaTypeVariableNameImpl(name, this.bounds + bounds)
    }

    override fun emit(codeWriter: JavaCodeWriter) {
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
