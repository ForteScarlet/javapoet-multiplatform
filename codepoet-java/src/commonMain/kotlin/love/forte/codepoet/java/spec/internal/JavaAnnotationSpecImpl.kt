package love.forte.codepoet.java.spec.internal

import love.forte.codepoet.common.code.CodeValue
import love.forte.codepoet.common.naming.TypeName
import love.forte.codepoet.java.*
import love.forte.codepoet.java.internal.emit0
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.spec.JavaAnnotationSpec


internal fun JavaCodeWriter.emitAnnotation(
    inline: Boolean = true,
    type: TypeName,
    members: Map<String, List<CodeValue>>
) {
    val whitespace = if (inline) "" else "\n"
    val memberSeparator = if (inline) ", " else ",\n"

    if (members.isEmpty()) {
        // @Singleton
        emit("@%V") { type(type) }
    } else if (members.size == 1 && members.containsKey("value")) {
        // @Named("foo")
        emit("@%V(") { type(type) }
        emitAnnotationValues(whitespace, memberSeparator, members["value"]!!)
        emit(")")
    } else {
        // Inline:
        //   @Column(name = "updated_at", nullable = false)
        //
        // Not inline:
        //   @Column(
        //       name = "updated_at",
        //       nullable = false
        //   )
        emit("@%V($whitespace") { type(type) }
        indent(2)
        val i = members.entries.iterator()
        while (i.hasNext()) {
            val entry = i.next()
            emit("%V = ") { literal(entry.key) }
            emitAnnotationValues(whitespace, memberSeparator, entry.value)
            if (i.hasNext()) {
                emit(memberSeparator)
            }
        }
        unindent(2)
        emit("$whitespace)")
    }
}

private fun JavaCodeWriter.emitAnnotationValues(
    whitespace: String,
    memberSeparator: String,
    values: List<CodeValue>
) {
    if (values.size == 1) {
        indent(2)
        values[0].emit0(this)
        unindent(2)
        return
    }

    emit("{$whitespace")
    indent(2)
    var first = true
    for (codeBlock in values) {
        if (!first) emit(memberSeparator)
        codeBlock.emit0(this)
        first = false
    }
    unindent(2)
    emit("$whitespace}")
}

internal class JavaAnnotationSpecImpl(
    override val type: JavaTypeName,
    override val members: Map<String, List<JavaCodeValue>>,
) : JavaAnnotationSpec {
    override fun toBuilder(): JavaAnnotationSpec.Builder {
        val builder = JavaAnnotationSpec.Builder(type)
        members.mapValuesTo(builder.members) { it.value.toMutableList() }
        return builder
    }

    override fun emit(codeWriter: JavaCodeWriter, inline: Boolean) {
        codeWriter.emitAnnotation(inline, type, members)
        // val whitespace = if (inline) "" else "\n"
        // val memberSeparator = if (inline) ", " else ",\n"
        //
        // if (members.isEmpty()) {
        //     // @Singleton
        //     codeWriter.emit("@%V") { type(type) }
        // } else if (members.size == 1 && members.containsKey("value")) {
        //     // @Named("foo")
        //     codeWriter.emit("@%V(") { type(type) }
        //     emitAnnotationValues(codeWriter, whitespace, memberSeparator, members["value"]!!)
        //     codeWriter.emit(")")
        // } else {
        //     // Inline:
        //     //   @Column(name = "updated_at", nullable = false)
        //     //
        //     // Not inline:
        //     //   @Column(
        //     //       name = "updated_at",
        //     //       nullable = false
        //     //   )
        //     codeWriter.emit("@%V($whitespace") { type(type) }
        //     codeWriter.indent(2)
        //     val i
        //         : Iterator<Map.Entry<String, List<JavaCodeValue>>> = members.entries.iterator()
        //     while (i.hasNext()) {
        //         val entry: Map.Entry<String, List<JavaCodeValue>> = i.next()
        //         codeWriter.emit("%V = ") { literal(entry.key) }
        //         emitAnnotationValues(codeWriter, whitespace, memberSeparator, entry.value)
        //         if (i.hasNext()) codeWriter.emit(memberSeparator)
        //     }
        //     codeWriter.unindent(2)
        //     codeWriter.emit("$whitespace)")
        // }
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
