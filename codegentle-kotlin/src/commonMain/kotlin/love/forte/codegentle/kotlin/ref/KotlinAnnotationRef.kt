package love.forte.codegentle.kotlin.ref

import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.kotlin.writer.KotlinCodeWriter

/**
 * Extension function to emit an [AnnotationRef] to a [KotlinCodeWriter].
 */
internal fun AnnotationRef.emitTo(codeWriter: KotlinCodeWriter) {
    codeWriter.emit("@")
    codeWriter.emit(typeName)
    
    // In a more complete implementation, we would handle annotation parameters
}
