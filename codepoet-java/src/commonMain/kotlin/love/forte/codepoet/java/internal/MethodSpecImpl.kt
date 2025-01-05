package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


internal class MethodSpecImpl(
    override val name: String,
    override val javadoc: CodeBlock,
    override val annotations: List<AnnotationSpec>,
    override val modifiers: Set<Modifier>,
    override val typeVariables: List<TypeVariableName>,
    override val returnType: TypeName?,
    override val parameters: List<ParameterSpec>,
    override val isVarargs: Boolean,
    override val exceptions: List<TypeName>,
    override val code: CodeBlock,
    override val defaultValue: CodeBlock,
) : MethodSpec {
    override fun toBuilder(): MethodSpec.Builder {
        return MethodSpec.Builder(name).also { builder ->
            builder.javadoc.add(javadoc)
            builder.annotations.addAll(annotations)
            builder.modifiers.addAll(modifiers)
            builder.typeVariables.addAll(typeVariables)
            builder.returnType = returnType
            builder.parameters.addAll(parameters)
            builder.isVarargs = isVarargs
            builder.exceptions.addAll(exceptions)
            builder.code.add(code)
            builder.defaultValue = defaultValue.takeUnless { it.isEmpty }
        }
    }

    override fun emit(codeWriter: CodeWriter) {
        TODO("Not yet implemented")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is MethodSpecImpl) return false

        if (isVarargs != other.isVarargs) return false
        if (name != other.name) return false
        if (javadoc != other.javadoc) return false
        if (annotations != other.annotations) return false
        if (modifiers != other.modifiers) return false
        if (typeVariables != other.typeVariables) return false
        if (returnType != other.returnType) return false
        if (parameters != other.parameters) return false
        if (exceptions != other.exceptions) return false
        if (code != other.code) return false
        if (defaultValue != other.defaultValue) return false

        return true
    }

    override fun hashCode(): Int {
        var result = isVarargs.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + javadoc.hashCode()
        result = 31 * result + annotations.hashCode()
        result = 31 * result + modifiers.hashCode()
        result = 31 * result + typeVariables.hashCode()
        result = 31 * result + (returnType?.hashCode() ?: 0)
        result = 31 * result + parameters.hashCode()
        result = 31 * result + exceptions.hashCode()
        result = 31 * result + code.hashCode()
        result = 31 * result + defaultValue.hashCode()
        return result
    }

    override fun toString(): String {
        return emitToString()
    }

}
