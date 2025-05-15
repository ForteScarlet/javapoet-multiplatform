package love.forte.codegentle.java.spec.internal

import love.forte.codegentle.java.JavaCodeValue
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.internal.doEmit
import love.forte.codegentle.java.internal.emitMembers
import love.forte.codegentle.java.internal.emitSupers
import love.forte.codegentle.java.internal.toVirtualTypeSpec
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.naming.JavaTypeVariableName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.spec.JavaFieldSpec
import love.forte.codegentle.java.spec.JavaMethodSpec
import love.forte.codegentle.java.spec.JavaSealedTypeSpec
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.type
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit
import love.forte.codegentle.java.writer.emitToString


/**
 *
 * @author ForteScarlet
 */
internal class JavaSealedTypeSpecImpl(
    override val name: String,
    override val kind: JavaTypeSpec.Kind,
    override val permits: List<JavaTypeName>,
    override val javadoc: JavaCodeValue,
    override val annotations: List<JavaAnnotationRef>,
    override val modifiers: Set<JavaModifier>,
    override val typeVariables: List<JavaTypeRef<JavaTypeVariableName>>,
    override val superclass: JavaTypeName?,
    override val superinterfaces: List<JavaTypeName>,
    override val fields: List<JavaFieldSpec>,
    override val staticBlock: JavaCodeValue,
    override val initializerBlock: JavaCodeValue,
    override val methods: List<JavaMethodSpec>,
    override val subtypes: List<JavaTypeSpec>
) : JavaSealedTypeSpec {

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        doEmit(codeWriter) {
            // Push an empty type (specifically without nested types) for type-resolution.
            codeWriter.pushType(this.toVirtualTypeSpec(name))
            codeWriter.emitDoc(javadoc)
            codeWriter.emitAnnotationRefs(annotations, false)
            codeWriter.emitModifiers(modifiers, implicitModifiers + kind.asMemberModifiers)
            val kindName = when (kind) {
                JavaTypeSpec.Kind.SEALED_CLASS -> "sealed class"
                JavaTypeSpec.Kind.SEALED_INTERFACE -> "sealed interface"
                else -> error("unexpected kind for sealed type: $kind")
            }
            codeWriter.emit("$kindName $name")
            codeWriter.emitTypeVariableRefs(typeVariables)

            emitSupers(codeWriter)

            if (permits.isNotEmpty()) {
                codeWriter.emit(" permits")
                var firstType = true
                for (permit in permits) {
                    if (!firstType) {
                        codeWriter.emit(",")
                    }
                    codeWriter.emit(" %V") { type(permit) }
                    firstType = false
                }
            }

            codeWriter.popType()
            codeWriter.emit(" {\n")

            emitMembers(codeWriter)

            codeWriter.popTypeVariableRefs(typeVariables)

            codeWriter.emit("}\n")
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaSealedTypeSpec) return false

        if (name != other.name) return false
        if (kind != other.kind) return false
        if (permits != other.permits) return false
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
        if (subtypes != other.subtypes) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + kind.hashCode()
        result = 31 * result + permits.hashCode()
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
        result = 31 * result + subtypes.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }
}
