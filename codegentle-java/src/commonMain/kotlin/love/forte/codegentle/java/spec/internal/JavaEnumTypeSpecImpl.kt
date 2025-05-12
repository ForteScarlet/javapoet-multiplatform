package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.internal.doEmit
import love.forte.codegentle.java.internal.emitImplements
import love.forte.codegentle.java.internal.emitMembers
import love.forte.codegentle.java.internal.toVirtualTypeSpec
import love.forte.codegentle.java.literal
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.naming.JavaTypeVariableName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.spec.*
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit
import love.forte.codegentle.java.writer.emitToString


internal class JavaEnumTypeSpecImpl(
    override val name: String,
    override val kind: JavaTypeSpec.Kind,
    override val enumConstants: Map<String, JavaAnonymousClassTypeSpec>,
    override val javadoc: JavaCodeValue,
    override val annotations: List<JavaAnnotationRef>,
    override val modifiers: Set<JavaModifier>,
    override val typeVariables: List<JavaTypeRef<JavaTypeVariableName>>,
    override val superinterfaces: List<JavaTypeName>,
    override val fields: List<FieldSpec>,
    override val staticBlock: JavaCodeValue,
    override val initializerBlock: JavaCodeValue,
    override val methods: List<JavaMethodSpec>,
    override val subtypes: List<JavaTypeSpec>
) : JavaEnumTypeSpec {

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        doEmit(codeWriter) {
            codeWriter.pushType(this.toVirtualTypeSpec(name))

            javadoc.emit(codeWriter)
            codeWriter.emitAnnotationRefs(annotations, false)
            codeWriter.emitModifiers(modifiers, implicitModifiers + kind.asMemberModifiers)
            codeWriter.emit("enum %V") {
                literal(name)
            }
            codeWriter.emitTypeVariableRefs(typeVariables)

            // implements
            codeWriter.emitImplements(superinterfaces)

            codeWriter.popType()
            codeWriter.emit(" {\n")

            emitMembers(codeWriter) { firstMember, notFirst ->
                val needsSeparator = fields.isNotEmpty() || methods.isNotEmpty() || subtypes.isNotEmpty()
                val i = enumConstants.entries.iterator()
                while (i.hasNext()) {
                    val enumConstant = i.next()
                    if (!firstMember) {
                        codeWriter.emit("\n")
                    }

                    enumConstant.value.emit(codeWriter, enumConstant.key)
                    notFirst()
                    if (i.hasNext()) {
                        codeWriter.emit(",\n")
                    } else if (!needsSeparator) {
                        codeWriter.emit("\n")
                    }
                }

                if (needsSeparator) {
                    codeWriter.emit(";\n")
                }
            }

            codeWriter.popTypeVariableRefs(typeVariables)
            codeWriter.emit("}\n")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaEnumTypeSpec) return false

        if (name != other.name) return false
        if (kind != other.kind) return false
        if (enumConstants != other.enumConstants) return false
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
        result = 31 * result + enumConstants.hashCode()
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
