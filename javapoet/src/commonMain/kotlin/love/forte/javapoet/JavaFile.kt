package love.forte.javapoet

import love.forte.javapoet.JavaFile.Builder
import love.forte.javapoet.internal.JavaFileImpl


/**
 *
 * @author ForteScarlet
 */
public interface JavaFile {

    public val fileComment: CodeBlock
    public val packageName: String

    // TODO types?

    public val type: TypeSpec

    /**
     * Call this to omit imports for classes in `java.lang`, such as `java.lang.String`.
     *
     * By default, JavaPoet explicitly imports types in `java.lang` to defend against
     * naming conflicts. Suppose an (ill-advised) class is named `com.example.String`. When
     * `java.lang` imports are skipped, generated code in `com.example` that references
     * `java.lang.String` will get `com.example.String` instead.
     */
    public val skipJavaLangImports: Boolean

    public val staticImports: Set<String>
    public val alwaysQualify: Set<String>

    public val indent: String

    public fun writeTo(out: Appendable)

    // TODO emit

    public class Builder internal constructor(
        public val packageName: String,
        public val type: TypeSpec,
    ) {
        private val fileComment = CodeBlock.builder()
        private var skipJavaLangImports: Boolean = false
        private var indent: String = "    "
        private val staticImports = linkedSetOf<String>()

        public fun addFileComment(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addFileComment(CodeValue(format, *argumentParts))
        }

        public fun addFileComment(codeValue: CodeValue): Builder = apply {
            fileComment.add(codeValue)
        }

        public fun addFileComment(codeBlock: CodeBlock): Builder = apply {
            fileComment.add(codeBlock)
        }

        public fun addStaticImport(import: String): Builder = apply {
            staticImports.add(import)
        }

        public fun addStaticImport(className: ClassName, vararg names: String): Builder = apply {
            require(names.isNotEmpty()) { "`names` is empty" }
            for (name in names) {
                staticImports.add(className.canonicalName + "." + name)
            }
        }

        public fun addStaticImport(className: ClassName, names: Iterable<String>): Builder = apply {
            val iter = names.iterator()
            require(!iter.hasNext()) { "`names` is empty" }

            for (name in iter) {
                staticImports.add(className.canonicalName + "." + name)
            }
        }

        public fun skipJavaLangImports(): Builder =
            skipJavaLangImports(true)

        public fun skipJavaLangImports(skipJavaLangImports: Boolean): Builder = apply {
            this.skipJavaLangImports = skipJavaLangImports
        }

        public fun indent(indent: String): Builder = apply {
            this.indent = indent
        }

        public fun build(): JavaFile {
            val alwaysQualify = linkedSetOf<String>()

            // TODO alwaysQualify.addAll(type.alwaysQualifiedNames)
            //  for (nested in spec.typeSpecs) {
            //    fillAlwaysQualifiedNames(nested, alwaysQualifiedNames)
            //  }


            return JavaFileImpl(
                fileComment = fileComment.build(),
                packageName = packageName,
                type = type,
                skipJavaLangImports = skipJavaLangImports,
                staticImports = LinkedHashSet(staticImports),
                alwaysQualify = alwaysQualify,
                indent = indent
            )
        }
    }
}

public inline fun Builder.addFileComment(format: String, block: CodeValueBuilderDsl = {}): Builder = apply {
    addFileComment(CodeValue(format, block))
}
