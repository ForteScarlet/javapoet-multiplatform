// package love.forte.codegentle.java.spec.internal
//
// import love.forte.codegentle.common.code.CodeValue
// import love.forte.codegentle.common.code.literal
// import love.forte.codegentle.common.code.type
// import love.forte.codegentle.common.naming.TypeName
// import love.forte.codegentle.common.ref.AnnotationRef
// import love.forte.codegentle.common.writer.withIndent
// import love.forte.codegentle.java.emitTo
// import love.forte.codegentle.java.spec.JavaAnnotationSpec
// import love.forte.codegentle.java.writer.JavaCodeWriter
// import love.forte.codegentle.java.writer.emit
// import love.forte.codegentle.java.writer.emitToString
//
//
// internal fun JavaCodeWriter.emitJavaAnnotation(
//     inline: Boolean = true,
//     type: TypeName,
//     members: Map<String, List<CodeValue>>
// ) {
//     val whitespace = if (inline) "" else "\n"
//     val memberSeparator = ", " // member: inline only //if (inline) ", " else ",\n"
//
//     if (members.isEmpty()) {
//         // @Singleton
//         emit("@%V") { type(type) }
//     } else if (members.size == 1 && members.containsKey("value")) {
//         // @Named("foo")
//         emit("@%V(") { type(type) }
//         // Always inline between values.
//         emitAnnotationValues(memberSeparator, members["value"]!!)
//         emit(")")
//     } else {
//         // @Column(name = "updated_at", nullable = false)
//         emit("@%V(") { type(type) }
//         withIndent(2) {
//             val i = members.entries.iterator()
//             while (i.hasNext()) {
//                 val entry = i.next()
//                 emit("%V = ") { literal(entry.key) }
//                 emitAnnotationValues(memberSeparator, entry.value)
//                 if (i.hasNext()) {
//                     emit(memberSeparator)
//                 }
//             }
//         }
//         emit(")")
//     }
// }
//
// private fun JavaCodeWriter.emitAnnotationValues(
//     memberSeparator: String,
//     values: List<CodeValue>
// ) {
//     if (values.size == 1) {
//         indent(2)
//         values[0].emitTo(this)
//         unindent(2)
//         return
//     }
//
//     emit("{")
//     var first = true
//     for (codeBlock in values) {
//         if (!first) emit(memberSeparator)
//         codeBlock.emitTo(this)
//         first = false
//     }
//     emit("}")
// }
//
// // internal class JavaAnnotationSpecImpl(
// //     override val type: TypeName,
// //     override val members: Map<String, List<CodeValue>>,
// //     override val annotations: List<AnnotationRef>,
// // ) : JavaAnnotationSpec {
// //
// //     override fun emit(codeWriter: JavaCodeWriter, inline: Boolean) {
// //         codeWriter.emitJavaAnnotation(inline, type, members)
// //     }
// //
// //
// //     override fun equals(other: Any?): Boolean {
// //         if (this === other) return true
// //         if (other !is JavaAnnotationSpec) return false
// //
// //         if (type != other.type) return false
// //         if (members != other.members) return false
// //
// //         return true
// //     }
// //
// //     override fun hashCode(): Int {
// //         var result = type.hashCode()
// //         result = 31 * result + members.hashCode()
// //         return result
// //     }
// //
// //     override fun toString(): String {
// //         return emitToString()
// //     }
// // }
