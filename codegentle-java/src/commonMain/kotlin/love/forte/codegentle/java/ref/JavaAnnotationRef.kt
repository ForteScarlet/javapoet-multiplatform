package love.forte.codegentle.java.ref

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.literal
import love.forte.codegentle.common.code.type
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.writer.withIndent
import love.forte.codegentle.java.emitTo
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit

//
// import love.forte.codegentle.common.computeValueIfAbsent
// import love.forte.codegentle.common.naming.ClassName
// import love.forte.codegentle.common.ref.AnnotationRef
// import love.forte.codegentle.java.JavaCodeValue
// import love.forte.codegentle.java.ref.internal.JavaAnnotationRefImpl
// import love.forte.codegentle.java.writer.JavaCodeEmitter

// /**
//  * A Java annotation ref.
//  *
//  * @author ForteScarlet
//  */
// public interface JavaAnnotationRef : AnnotationRef, JavaCodeEmitter {
//     override val className: ClassName
//     override val members: Map<String, List<JavaCodeValue>>
// }
//
// public inline fun ClassName.annotationRef(block: JavaAnnotationRefBuilder.() -> Unit = {}): JavaAnnotationRef {
//     return JavaAnnotationRefBuilder(this@annotationRef).apply {
//         block()
//     }.build()
// }
//
// /**
//  * Builder for [JavaAnnotationRef].
//  */
// public class JavaAnnotationRefBuilder(public val className: ClassName) :
//     JavaAnnotationRefBuildable<JavaAnnotationRefBuilder> {
//     private val members: MutableMap<String, MutableList<JavaCodeValue>> = linkedMapOf()
//
//     override fun addMember(name: String, codeValue: JavaCodeValue): JavaAnnotationRefBuilder = apply {
//         val values = members.computeValueIfAbsent(name) { mutableListOf() }
//         values.add(codeValue)
//     }
//
//     public fun build(): JavaAnnotationRef {
//         return JavaAnnotationRefImpl(
//             className = className,
//             members = members.mapValues { it.value.toList() }
//         )
//     }
// }

internal fun AnnotationRef.emitTo(codeWriter: JavaCodeWriter) {
    codeWriter.emitJavaAnnotation(typeName, members)
}


internal fun JavaCodeWriter.emitJavaAnnotation(
    type: TypeName,
    members: Map<String, List<CodeValue>>
) {
    // val whitespace = if (inline) "" else "\n"
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
        values[0].emitTo(this)
        unindent(2)
        return
    }

    emit("{")
    var first = true
    for (codeBlock in values) {
        if (!first) emit(memberSeparator)
        codeBlock.emitTo(this)
        first = false
    }
    emit("}")
}
