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

    override fun emit(codeWriter: CodeWriter, name: String?, implicitModifiers: Set<Modifier>) {
        javadocWithParameters().emit(codeWriter)
        codeWriter.emitAnnotations(annotations, false)
        codeWriter.emitModifiers(modifiers, implicitModifiers)

        if (typeVariables.isNotEmpty()) {
            codeWriter.emitTypeVariables(typeVariables)
            codeWriter.emit(" ")
        }

        if (isConstructor) {
            codeWriter.emit("$name(%V") {
                zeroWidthSpace()
            }
        } else {
            codeWriter.emit("%V ${this.name}(%V") {
                type(returnType ?: TypeName.Builtins.VOID)
                zeroWidthSpace()
            }
        }

        var firstParameter = true
        val i = parameters.iterator()
        while (i.hasNext()) {
            val parameter = i.next()
            if (!firstParameter) {
                codeWriter.emit(",")
                codeWriter.emitWrappingSpace()
            }

            parameter.emit(codeWriter, !i.hasNext() && isVarargs)
            firstParameter = false
        }

        codeWriter.emit(")")

        if (!defaultValue.isEmpty) {
            codeWriter.emit(" default ")
            defaultValue.emit(codeWriter)
        }

        if (!exceptions.isEmpty()) {
            codeWriter.emitWrappingSpace()
            codeWriter.emit("throws")
            var firstException = true
            for (exception in exceptions) {
                if (!firstException) codeWriter.emit(",")
                codeWriter.emitWrappingSpace()
                codeWriter.emit("%V") {
                    type(exception)
                }

                firstException = false
            }
        }

        when {
            hasModifier(Modifier.ABSTRACT) -> {
                codeWriter.emit(";\n")
            }

            hasModifier(Modifier.NATIVE) -> {
                // Code is allowed to support stuff like GWT JSNI.
                code.emit(codeWriter)
                codeWriter.emit(";\n")
            }

            else -> {
                codeWriter.emit(" {\n")

                codeWriter.indent()
                code.emit(codeWriter, true)
                codeWriter.unindent()

                codeWriter.emit("}\n")
            }
        }
        codeWriter.popTypeVariables(typeVariables)
    }

    private fun javadocWithParameters(): CodeBlock {
        if (parameters.isEmpty()) return javadoc

        val builder = javadoc.toBuilder()
        var emitTagNewline = true
        for (parameter in parameters) {
            val parameterDoc = parameter.javadoc
            if (parameterDoc.isEmpty) {
                continue
            }

            // Emit a new line before @param section only if the method javadoc is present.
            if (emitTagNewline && !javadoc.isEmpty) builder.add("\n")
            emitTagNewline = false
            builder.add("@param ${parameter.name} %V") {
                literal(parameterDoc)
            }
        }

        return builder.build()
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
