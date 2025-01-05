package love.forte.codepoet.java.internal

import love.forte.codepoet.java.AnnotationSpec
import love.forte.codepoet.java.CodeWriter
import love.forte.codepoet.java.TypeName
import love.forte.codepoet.java.TypeName.Builtins.VOID
import love.forte.codepoet.java.emitToString


internal class TypeNameImpl(
    private val keyword: String? = null,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : TypeName {

    override fun annotated(annotations: List<AnnotationSpec>): TypeName {
        return TypeNameImpl(keyword, this.annotations + annotations)
    }

    override fun withoutAnnotations(): TypeName {
        return if (annotations.isEmpty()) this else TypeNameImpl(keyword)
    }

    override val isPrimitive: Boolean
        get() = keyword != null && this != VOID

    override fun emit(codeWriter: CodeWriter) {
        if (keyword == null) throw AssertionError()

        if (isAnnotated) {
            codeWriter.emit("")
            emitAnnotations(codeWriter)
        }

        codeWriter.emitAndIndent(keyword)
    }

    override fun toString(): String {
        return emitToString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is TypeNameImpl) return false

        if (keyword != other.keyword) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyword?.hashCode() ?: 0
        result = 31 * result + annotations.hashCode()
        return result
    }
}

internal fun TypeName.emitAnnotations(codeWriter: CodeWriter) {
    for (annotation in annotations) {
        annotation.emit(codeWriter, true)
        codeWriter.emit(" ")
    }
}
