package love.forte.javapoet.internal

import love.forte.javapoet.*


/**
 *
 * @author ForteScarlet
 */
internal class ArrayTypeNameImpl(
    override val componentType: TypeName,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : ArrayTypeName {
    override fun annotated(annotations: List<AnnotationSpec>): ArrayTypeName {
        return ArrayTypeNameImpl(componentType, this.annotations + annotations)
    }

    override fun withoutAnnotations(): ArrayTypeName {
        return if (annotations.isEmpty()) this else ArrayTypeNameImpl(componentType)
    }

    override fun emit(codeWriter: CodeWriter, varargs: Boolean) {
        emitLeafType(codeWriter)
        emitBrackets(codeWriter, varargs)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ArrayTypeName) return false

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

private fun ArrayTypeName.emitLeafType(out: CodeWriter) {
    val asArray = componentType as? ArrayTypeName
    if (asArray != null) {
        return asArray.emitLeafType(out)
    }
    return componentType.emit(out)
}

private fun ArrayTypeName.emitBrackets(out: CodeWriter, varargs: Boolean) {
    if (isAnnotated) {
        out.emit(" ")
        emitAnnotations(out)
    }

    val asArray = componentType as? ArrayTypeName

    if (asArray == null) {
        // Last bracket.
        out.emit(if (varargs) "..." else "[]")
        return
    }

    out.emit("[]")
    return asArray.emitBrackets(out, varargs)
}
