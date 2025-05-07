package love.forte.codepoet.java.spec.internal

import love.forte.codepoet.common.code.isEmpty
import love.forte.codepoet.java.*
import love.forte.codepoet.java.internal.doEmit
import love.forte.codepoet.java.internal.emitMembers
import love.forte.codepoet.java.naming.JavaTypeName
import love.forte.codepoet.java.naming.JavaTypeVariableName
import love.forte.codepoet.java.spec.*


internal class JavaAnonymousClassTypeSpecImpl(
    override val kind: JavaTypeSpec.Kind,
    override val anonymousTypeArguments: JavaCodeValue,
    override val javadoc: JavaCodeValue,
    override val annotations: List<JavaAnnotationSpec>,
    override val modifiers: Set<JavaModifier>,
    override val typeVariables: List<JavaTypeVariableName>,
    override val superclass: JavaTypeName?,
    override val superinterfaces: List<JavaTypeName>,
    override val fields: List<FieldSpec>,
    override val staticBlock: JavaCodeValue,
    override val initializerBlock: JavaCodeValue,
    override val methods: List<JavaMethodSpec>,
    override val types: List<JavaTypeSpec>
) : JavaAnonymousClassTypeSpec {

    override fun emit(codeWriter: JavaCodeWriter, enumName: String?, implicitModifiers: Set<JavaModifier>) {
        doEmit(codeWriter) {
            if (enumName != null) {
                javadoc.emit(codeWriter)
                codeWriter.emitAnnotations(annotations, false)
                codeWriter.emit(enumName)
                if (!anonymousTypeArguments.isEmpty) {
                    codeWriter.emit("(")
                    anonymousTypeArguments.emit(codeWriter)
                    codeWriter.emit(")")
                }
                if (fields.isEmpty() && methods.isEmpty() && types.isEmpty()) {
                    return
                }
                codeWriter.emit(" {\n")
            } else {
                val supertype = superinterfaces.firstOrNull()
                    ?: superclass

                codeWriter.emit("new %V(") { type(supertype!!) }
                anonymousTypeArguments.emit(codeWriter)
                codeWriter.emit(") {\n")
            }

            emitMembers(codeWriter)
            codeWriter.popTypeVariables(typeVariables)
            codeWriter.emit("}")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaAnonymousClassTypeSpec) return false

        if (kind != other.kind) return false
        if (anonymousTypeArguments != other.anonymousTypeArguments) return false
        if (javadoc != other.javadoc) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (typeVariables != other.typeVariables) return false
        if (superclass != other.superclass) return false
        if (superinterfaces != other.superinterfaces) return false
        if (fields != other.fields) return false
        if (staticBlock != other.staticBlock) return false
        if (initializerBlock != other.initializerBlock) return false
        if (methods != other.methods) return false
        if (types != other.types) return false

        return true
    }

    override fun hashCode(): Int {
        var result = kind.hashCode()
        result = 31 * result + anonymousTypeArguments.hashCode()
        result = 31 * result + javadoc.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + typeVariables.hashCode()
        result = 31 * result + (superclass?.hashCode() ?: 0)
        result = 31 * result + superinterfaces.hashCode()
        result = 31 * result + fields.hashCode()
        result = 31 * result + staticBlock.hashCode()
        result = 31 * result + initializerBlock.hashCode()
        result = 31 * result + methods.hashCode()
        result = 31 * result + types.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
