package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.internal.emit0
import love.forte.codegentle.java.literal
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.spec.JavaAnnotationSpec
import love.forte.codegentle.java.type
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit
import love.forte.codegentle.java.writer.emitToString


internal fun JavaCodeWriter.emitJavaAnnotation(
    inline: Boolean = true,
    type: TypeName,
    members: Map<String, List<CodeValue>>
) {
    val whitespace = if (inline) "" else "\n"
    val memberSeparator = ", " // member: inline only //if (inline) ", " else ",\n"

    if (members.isEmpty()) {
        // @Singleton
        emit("@%V") { type(type) }
    } else if (members.size == 1 && members.containsKey("value")) {
        // @Named("foo")
        emit("@%V(") { type(type) }
        // Always inline between values.
        emitAnnotationValues(memberSeparator, members["value"]!!)
        emit(")")
    } else {
        // @Column(name = "updated_at", nullable = false)
        emit("@%V(") { type(type) }
        withIndent(2) {
            val i = members.entries.iterator()
            while (i.hasNext()) {
                val entry = i.next()
                emit("%V = ") { literal(entry.key) }
                emitAnnotationValues(memberSeparator, entry.value)
                if (i.hasNext()) {
                    emit(memberSeparator)
                }
            }
        }
        emit(")")
    }
}

private fun JavaCodeWriter.emitAnnotationValues(
    memberSeparator: String,
    values: List<CodeValue>
) {
    if (values.size == 1) {
        indent(2)
        values[0].emit0(this)
        unindent(2)
        return
    }

    emit("{")
    var first = true
    for (codeBlock in values) {
        if (!first) emit(memberSeparator)
        codeBlock.emit0(this)
        first = false
    }
    emit("}")
}

internal class JavaAnnotationSpecImpl(
    override val type: JavaTypeName,
    override val members: Map<String, List<JavaCodeValue>>,
    override val annotations: List<JavaAnnotationRef>,
) : JavaAnnotationSpec {

    override fun emit(codeWriter: JavaCodeWriter, inline: Boolean) {
        codeWriter.emitJavaAnnotation(inline, type, members)
    }


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaAnnotationSpec) return false

        if (type != other.type) return false
        if (members != other.members) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + members.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
