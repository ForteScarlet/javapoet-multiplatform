package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.internal.doEmit
import love.forte.codegentle.java.internal.emitMembers
import love.forte.codegentle.java.internal.emitSupers
import love.forte.codegentle.java.internal.toVirtualTypeSpec
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emitToString


/**
 *
 * @author ForteScarlet
 */
internal class JavaRecordTypeSpecImpl(
    override val name: String,
    override val kind: JavaTypeSpec.Kind,
    override val mainConstructorParameters: List<JavaParameterSpec>,
    override val javadoc: CodeValue,
    override val annotations: List<AnnotationRef>,
    override val modifiers: Set<JavaModifier>,
    override val typeVariables: List<TypeRef<TypeVariableName>>,
    override val superinterfaces: List<TypeName>,
    override val fields: List<JavaFieldSpec>,
    override val staticBlock: CodeValue,
    override val initializerBlock: CodeValue,
    override val methods: List<JavaMethodSpec>,
    override val subtypes: List<JavaTypeSpec>
) : JavaRecordTypeSpec {

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        // TODO
        doEmit(codeWriter) {
            // Push an empty type (specifically without nested types) for type-resolution.
            codeWriter.pushType(this.toVirtualTypeSpec(name))
            codeWriter.emitDoc(javadoc)
            codeWriter.emitAnnotationRefs(annotations, false)
            codeWriter.emitModifiers(modifiers, implicitModifiers + kind.asMemberModifiers)
            codeWriter.emit("record $name")
            codeWriter.emitTypeVariableRefs(typeVariables)

            emitSupers(codeWriter)

            codeWriter.popType()

            // main constructor
            codeWriter.emit("(")

            var firstMainConstructorParameter = true
            for (mainConstructorParameter in mainConstructorParameters) {
                if (!firstMainConstructorParameter) {
                    codeWriter.emit(", ")
                }
                mainConstructorParameter.emit(codeWriter)
                firstMainConstructorParameter = false
            }

            codeWriter.emit(")")

            codeWriter.emit(" {\n")

            emitMembers(codeWriter)

            codeWriter.popTypeVariableRefs(typeVariables)

            codeWriter.emit("}\n")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaRecordTypeSpec) return false

        if (name != other.name) return false
        if (kind != other.kind) return false
        if (mainConstructorParameters != other.mainConstructorParameters) return false
        if (javadoc != other.javadoc) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (typeVariables != other.typeVariables) return false
        if (superinterfaces != other.superinterfaces) return false
        if (fields != other.fields) return false
        if (staticBlock != other.staticBlock) return false
        if (initializerBlock != other.initializerBlock) return false
        if (methods != other.methods) return false
        if (subtypes != other.subtypes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + kind.hashCode()
        result = 31 * result + mainConstructorParameters.hashCode()
        result = 31 * result + javadoc.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + typeVariables.hashCode()
        result = 31 * result + superinterfaces.hashCode()
        result = 31 * result + fields.hashCode()
        result = 31 * result + staticBlock.hashCode()
        result = 31 * result + initializerBlock.hashCode()
        result = 31 * result + methods.hashCode()
        result = 31 * result + subtypes.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
