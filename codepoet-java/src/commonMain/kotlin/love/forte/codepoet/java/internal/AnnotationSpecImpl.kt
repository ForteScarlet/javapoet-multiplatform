package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


internal class AnnotationSpecImpl(
    override val type: TypeName,
    override val members: Map<String, List<CodeBlock>>,
) : AnnotationSpec {
    override fun toBuilder(): AnnotationSpec.Builder {
        val builder = AnnotationSpec.Builder(type)
        members.mapValuesTo(builder.members) { it.value.toMutableList() }
        return builder
    }

    override fun emit(codeWriter: CodeWriter, inline: Boolean) {
        val whitespace = if (inline) "" else "\n"
        val memberSeparator = if (inline) ", " else ",\n"

        if (members.isEmpty()) {
            // @Singleton
            codeWriter.emit("@%V") { type(type) }
        } else if (members.size == 1 && members.containsKey("value")) {
            // @Named("foo")
            codeWriter.emit("@%V(") { type(type) }
            emitAnnotationValues(codeWriter, whitespace, memberSeparator, members["value"]!!)
            codeWriter.emit(")")
        } else {
            // Inline:
            //   @Column(name = "updated_at", nullable = false)
            //
            // Not inline:
            //   @Column(
            //       name = "updated_at",
            //       nullable = false
            //   )
            codeWriter.emit("@%V($whitespace") { type(type) }
            codeWriter.indent(2)
            val i
                : Iterator<Map.Entry<String, List<CodeBlock>>> = members.entries.iterator()
            while (i.hasNext()) {
                val entry: Map.Entry<String, List<CodeBlock>> = i.next()
                codeWriter.emit("%V = ") { literal(entry.key) }
                emitAnnotationValues(codeWriter, whitespace, memberSeparator, entry.value)
                if (i.hasNext()) codeWriter.emit(memberSeparator)
            }
            codeWriter.unindent(2)
            codeWriter.emit("$whitespace)")
        }
    }

    private fun emitAnnotationValues(
        codeWriter: CodeWriter,
        whitespace: String,
        memberSeparator: String,
        values: List<CodeBlock>
    ) {
        if (values.size == 1) {
            codeWriter.indent(2)
            values[0].emit(codeWriter)
            codeWriter.unindent(2)
            return
        }

        codeWriter.emit("{$whitespace")
        codeWriter.indent(2)
        var first = true
        for (codeBlock in values) {
            if (!first) codeWriter.emit(memberSeparator)
            codeBlock.emit(codeWriter)
            first = false
        }
        codeWriter.unindent(2)
        codeWriter.emit("$whitespace}")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is AnnotationSpec) return false

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
