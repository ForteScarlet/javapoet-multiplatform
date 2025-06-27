package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.writer.KotlinCodeWriter

/**
 * Extension function to emit a [KotlinFunctionSpec] to a [KotlinCodeWriter].
 */
internal fun KotlinFunctionSpec.emitTo(codeWriter: KotlinCodeWriter) {
    // Emit KDoc
    if (!kDoc.isEmpty) {
        codeWriter.emitDoc(kDoc)
    }

    // Emit annotations
    codeWriter.emitAnnotationRefs(annotations, false)

    // Emit context parameters if any
    if (contextParameters.isNotEmpty()) {
        codeWriter.emit("context(")
        contextParameters.forEachIndexed { index, param ->
            if (index > 0) codeWriter.emit(", ")
            // TODO: Implement contextParameter.emitTo(codeWriter)
            codeWriter.emit(param.typeRef.toString())
        }
        codeWriter.emit(") ")
    }

    // Emit modifiers
    codeWriter.emitModifiers(modifiers)

    // Emit the function keyword
    codeWriter.emit("fun ")

    // Emit type variables if any
    if (typeVariables.isNotEmpty()) {
        codeWriter.emitTypeVariableRefs(typeVariables)
        codeWriter.emit(" ")
    }

    // Emit receiver if any
    receiver?.let { recv ->
        codeWriter.emit(recv)
        codeWriter.emit(".")
    }

    // Emit the name
    codeWriter.emit(name)

    // Emit parameters
    codeWriter.emit("(")
    parameters.forEachIndexed { index, param ->
        if (index > 0) codeWriter.emit(", ")
        // TODO: Implement parameter.emitTo(codeWriter)
        codeWriter.emit("${param.name}: ${param.typeRef}")
    }
    codeWriter.emit(")")

    // Emit return type
    codeWriter.emit(": ")
    codeWriter.emit(returnType)

    // Emit body
    if (!code.isEmpty) {
        codeWriter.emit(" {\n")
        codeWriter.indent()
        codeWriter.emit(code)
        codeWriter.unindent()
        codeWriter.emit("\n}")
    } else {
        // Empty body
        codeWriter.emit(" {}")
    }
}
