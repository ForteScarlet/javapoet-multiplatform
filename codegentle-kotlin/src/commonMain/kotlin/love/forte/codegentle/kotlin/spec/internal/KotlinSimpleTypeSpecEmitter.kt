package love.forte.codegentle.kotlin.spec.internal

import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.KotlinSimpleTypeSpec
import love.forte.codegentle.kotlin.writer.KotlinCodeWriter

/**
 * Extension function to emit a [KotlinSimpleTypeSpec] to a [KotlinCodeWriter].
 */
internal fun KotlinSimpleTypeSpec.emitTo(codeWriter: KotlinCodeWriter, implicitModifiers: Set<KotlinModifier> = emptySet()) {
    // Emit KDoc
    if (!kDoc.isEmpty) {
        codeWriter.emitDoc(kDoc)
    }

    // Emit annotations
    codeWriter.emitAnnotationRefs(annotations, false)

    // Emit modifiers
    codeWriter.emitModifiers(modifiers, implicitModifiers)

    // Emit the type keyword based on the kind
    when (kind) {
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.CLASS -> codeWriter.emit("class ")
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.INTERFACE -> codeWriter.emit("interface ")
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.OBJECT -> codeWriter.emit("object ")
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.COMPANION_OBJECT -> codeWriter.emit("companion object ")
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.ENUM -> codeWriter.emit("enum class ")
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.ANNOTATION -> codeWriter.emit("annotation class ")
        love.forte.codegentle.kotlin.spec.KotlinTypeSpec.Kind.VALUE -> codeWriter.emit("value class ")
        else -> codeWriter.emit("class ") // Default to class
    }

    // Emit the name
    codeWriter.emit(name)

    // Emit type variables
    if (typeVariables.isNotEmpty()) {
        codeWriter.emitTypeVariableRefs(typeVariables)
    }

    // Emit superclass and superinterfaces
    val hasExtends = superclass != null
    val hasImplements = superinterfaces.isNotEmpty()

    if (hasExtends || hasImplements) {
        codeWriter.emit(" : ")

        if (hasExtends) {
            codeWriter.emit(superclass!!)
            if (hasImplements) {
                codeWriter.emit(", ")
            }
        }

        if (hasImplements) {
            superinterfaces.forEachIndexed { index, typeName ->
                if (index > 0) codeWriter.emit(", ")
                codeWriter.emit(typeName)
            }
        }
    }

    // Emit the body
    codeWriter.emit(" {\n")
    codeWriter.indent()

    // Emit initializer block
    if (!initializerBlock.isEmpty) {
        codeWriter.emit("init {\n")
        codeWriter.indent()
        codeWriter.emit(initializerBlock)
        codeWriter.unindent()
        codeWriter.emit("}\n\n")
    }

    // Emit properties
    for (property in properties) {
        // TODO: Implement property.emitTo(codeWriter)
        codeWriter.emit("// Property: ${property.name}\n")
    }

    // Emit functions
    for (function in functions) {
        // TODO: Implement function.emitTo(codeWriter)
        codeWriter.emit("// Function: ${function.name}\n")
    }

    // Emit subtypes
    for (subtype in subtypes) {
        // TODO: Implement subtype.emitTo(codeWriter)
        codeWriter.emit("// Subtype\n")
    }

    codeWriter.unindent()
    codeWriter.emit("}")
}

/**
 * Extension function to emit a [KotlinSimpleTypeSpec] to a [KotlinCodeWriter].
 */
internal fun KotlinSimpleTypeSpec.emitTo(codeWriter: KotlinCodeWriter) {
    emitTo(codeWriter, emptySet())
}
