package love.forte.codepoet.java.naming.internal

import love.forte.codepoet.java.JavaCodeWriter
import love.forte.codepoet.java.emitToString
import love.forte.codepoet.java.naming.JavaArrayTypeName
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.JavaAnnotationSpec


/**
 *
 * @author ForteScarlet
 */
internal class JavaArrayTypeNameImpl(
    override val componentType: JavaTypeName,
    override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaArrayTypeName {
    override fun annotated(annotations: List<JavaAnnotationSpec>): JavaArrayTypeName {
        if (annotations.isEmpty()) return this

        return JavaArrayTypeNameImpl(componentType, this.annotations + annotations)
    }

    override fun withoutAnnotations(): JavaArrayTypeName {
        return if (annotations.isEmpty()) this else JavaArrayTypeNameImpl(componentType)
    }

    override fun emit(codeWriter: JavaCodeWriter, varargs: Boolean) {
        emitLeafType(codeWriter)
        emitBrackets(codeWriter, varargs)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaArrayTypeName) return false

        if (componentType != other.componentType) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = componentType.hashCode()
        result = 31 * result + annotations.hashCode()
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
    if (isAnnotated) {
        out.emit(" ")
        emitAnnotations(out)
    }

    val asArray = componentType as? JavaArrayTypeName

    if (asArray == null) {
        // Last bracket.
        out.emit(if (varargs) "..." else "[]")
        return
    }

    out.emit("[]")
    return asArray.emitBrackets(out, varargs)
}
