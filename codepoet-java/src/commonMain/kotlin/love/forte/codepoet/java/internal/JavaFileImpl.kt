package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*

private object NullAppendable : Appendable {
    override fun append(value: Char): Appendable = this
    override fun append(value: CharSequence?): Appendable = this
    override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Appendable = this
}

internal class JavaFileImpl(
    override val fileComment: CodeBlock,
    override val packageName: String,
    override val type: TypeSpec,
    override val skipJavaLangImports: Boolean,
    override val staticImports: Set<String>,
    override val alwaysQualify: Set<String>,
    override val indent: String
) : JavaFile {
    override fun writeTo(out: Appendable) {
        // First pass: emit the entire class, just to collect the types we'll need to import.
        val importsCollector = CodeWriter.create(
            NullAppendable,
            indent,
            staticImports,
            alwaysQualify
        )
        emit(importsCollector)
        val suggestedImports: Map<String, ClassName> = importsCollector.suggestedImports()

        val codeWriter = CodeWriter.create(
            out,
            indent,
            suggestedImports,
            staticImports,
            alwaysQualify
        )

        emit(codeWriter)
    }


    override fun emit(codeWriter: CodeWriter) {
        codeWriter.inPackage(packageName) {
            if (!fileComment.isEmpty) {
                codeWriter.emitComment(fileComment)
            }

            if (packageName.isNotEmpty()) {
                codeWriter.emit("package $packageName;\n")
                codeWriter.emit("\n")
            }

            if (staticImports.isNotEmpty()) {
                for (signature in staticImports) {
                    codeWriter.emit("import static $signature;\n")
                }
                codeWriter.emit("\n")
            }

            var importedTypesCount = 0
            val importedTypeSet = LinkedHashSet(codeWriter.importedTypes.values)

            for (className in importedTypeSet) {
                // TODO what about nested types like java.util.Map.Entry?
                if (skipJavaLangImports
                    && className.packageName == "java.lang"
                    && !alwaysQualify.contains(className.simpleName)
                ) {
                    continue
                }
                codeWriter.emit("import %V;\n") {
                    literal(className.withoutAnnotations())
                }
                importedTypesCount++
            }

            if (importedTypesCount > 0) {
                codeWriter.emit("\n")
            }

            type.emit(codeWriter, emptySet())
        }


    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is JavaFileImpl) return false

        if (skipJavaLangImports != other.skipJavaLangImports) return false
        if (fileComment != other.fileComment) return false
        if (packageName != other.packageName) return false
        if (type != other.type) return false
        if (staticImports != other.staticImports) return false
        if (alwaysQualify != other.alwaysQualify) return false
        if (indent != other.indent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = skipJavaLangImports.hashCode()
        result = 31 * result + fileComment.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + staticImports.hashCode()
        result = 31 * result + alwaysQualify.hashCode()
        result = 31 * result + indent.hashCode()
        return result
    }

    override fun toString(): String {
        return buildString {
            writeTo(this)
        }
    }
}
