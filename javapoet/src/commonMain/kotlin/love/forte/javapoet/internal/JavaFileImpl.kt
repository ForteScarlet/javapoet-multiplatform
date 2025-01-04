package love.forte.javapoet.internal

import love.forte.javapoet.CodeBlock
import love.forte.javapoet.JavaFile
import love.forte.javapoet.TypeSpec


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
        TODO("Not yet implemented")
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
