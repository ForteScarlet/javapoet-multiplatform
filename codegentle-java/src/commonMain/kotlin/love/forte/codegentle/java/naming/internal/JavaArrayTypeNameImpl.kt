package love.forte.codegentle.java.naming.internal

import love.forte.codegentle.java.JavaCodeWriter
import love.forte.codegentle.java.emitToString
import love.forte.codegentle.java.naming.JavaArrayTypeName
import love.forte.codegentle.java.ref.JavaTypeRef


/**
 *
 * @author ForteScarlet
 */
internal class JavaArrayTypeNameImpl(
    override val componentType: JavaTypeRef<*>,
) : JavaArrayTypeName {
    override fun emit(codeWriter: JavaCodeWriter, varargs: Boolean) {
        emitLeafType(codeWriter)
        emitBrackets(codeWriter, varargs)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaArrayTypeName) return false

        if (componentType != other.componentType) return false

        return true
    }

    override fun hashCode(): Int {
        val result = componentType.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}

private fun JavaArrayTypeName.emitLeafType(out: JavaCodeWriter) {
    val asArray = componentType as? JavaArrayTypeName
    if (asArray != null) {
        return asArray.emitLeafType(out)
    }
    return componentType.emit(out)
}

private fun JavaArrayTypeName.emitBrackets(out: JavaCodeWriter, varargs: Boolean) {
    // if (isAnnotated) {
    //     out.emit(" ")
    //     emitAnnotations(out)
    // }

    val asArray = componentType as? JavaArrayTypeName

    if (asArray == null) {
        // Last bracket.
        out.emit(if (varargs) "..." else "[]")
        return
    }

    out.emit("[]")
    return asArray.emitBrackets(out, varargs)
}
