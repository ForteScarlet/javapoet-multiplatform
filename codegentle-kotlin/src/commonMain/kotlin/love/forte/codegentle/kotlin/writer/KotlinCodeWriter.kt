package love.forte.codegentle.kotlin.writer

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.common.writer.*

/**
 *
 * @author ForteScarlet
 */
public class KotlinCodeWriter : CodeWriter {
    override val alwaysQualify: Set<String>
        get() = TODO("Not yet implemented")
    override val strategy: Strategy
        get() = TODO("Not yet implemented")
    override val indentValue: String
        get() = TODO("Not yet implemented")
    override val staticImports: Set<String>
        get() = TODO("Not yet implemented")

    override fun indent(levels: Int) {
        TODO("Not yet implemented")
    }

    override fun unindent(levels: Int) {
        TODO("Not yet implemented")
    }

    override fun emitComment(
        comment: CodeValue,
        vararg options: CodeValueEmitOption
    ) {
        TODO("Not yet implemented")
    }

    override fun emitDoc(
        doc: CodeValue,
        vararg options: CodeValueEmitOption
    ) {
        TODO("Not yet implemented")
    }

    override fun emit(
        code: CodeValue,
        vararg options: CodeValueEmitOption
    ) {
        TODO("Not yet implemented")
    }

    override fun emit(
        typeName: TypeName,
        vararg options: TypeNameEmitOption
    ) {
        TODO("Not yet implemented")
    }

    override fun emit(
        typeRef: TypeRef<*>,
        vararg options: TypeRefEmitOption
    ) {
        TODO("Not yet implemented")
    }

    override fun emit(
        annotationRef: AnnotationRef,
        vararg options: AnnotationRefEmitOption
    ) {
        TODO("Not yet implemented")
    }

    override fun emit(s: String) {
        TODO("Not yet implemented")
    }
    // TODO
}
