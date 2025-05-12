package love.forte.codegentle.java.internal

import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.writer.JavaWriteStrategy
import love.forte.codegentle.java.writer.ToStringJavaWriteStrategy

private object NullAppendable : Appendable {
    override fun append(value: Char): Appendable = this
    override fun append(value: CharSequence?): Appendable = this
    override fun append(value: CharSequence?, startIndex: Int, endIndex: Int): Appendable = this
}

internal class JavaFileImpl(
    override val fileComment: JavaCodeValue,
    override val packageName: String,
    override val type: JavaTypeSpec,
    override val skipJavaLangImports: Boolean,
    override val staticImports: Set<String>,
    override val alwaysQualify: Set<String>,
    override val indent: String
) : JavaFile {
    override fun writeTo(out: Appendable, strategy: JavaWriteStrategy) {
        // First pass: emit the entire class, just to collect the types we'll need to import.
        // TODO support dynamics imports?
        val importsCollector = JavaCodeWriter.create(
            dialect = strategy,
            out = NullAppendable,
            indent = indent,
            staticImports = staticImports,
            alwaysQualify = alwaysQualify
        )
        emit(importsCollector)
        val suggestedImports: Map<String, JavaClassName> = importsCollector.suggestedImports()

        val codeWriter = JavaCodeWriter.create(
            dialect = strategy,
            out = out,
            indent = indent,
            importedTypes = suggestedImports,
            staticImports = staticImports,
            alwaysQualify = alwaysQualify
        )

        emit(codeWriter)
    }


    override fun emit(codeWriter: JavaCodeWriter) {
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
                    literal(className)
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
            writeTo(this, ToStringJavaWriteStrategy)
        }
    }
}
