package love.forte.codegentle.common.writer

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.naming.ClassName

/**
 *
 * @author ForteScarlet
 */
public interface CodeWriter {
    public val strategy: Strategy

    public val indentValue: String

    // out?
    public val staticImportClassNames: Set<ClassName>
    public val staticImports: Set<String> // String?
    public val alwaysQualify: Set<String>

    public fun indent(levels: Int = 1)
    public fun unindent(levels: Int = 1)

    // TODO push/pop packageName?

    // comments and javadocs

    public fun emitComment(comment: CodeValue)

    public fun emitDoc(doc: CodeValue)

    public fun emit(code: CodeValue)

    public fun emit(s: String)

    public fun emitAndIndent(s: String)

    // TODO public fun emitTypeName(typeName: TypeName)
    // TODO public fun emitTypeRef(typeRef: TypeRef<*>)

    // TODO imports
    /**
     * Key: the identifier
     * Value: the import.
     */
    // public val imports: Map<String, Import>

    public companion object {
        public const val DEFAULT_COLUMN_LIMIT: Int = Int.MAX_VALUE
        public const val DEFAULT_INDENT: String = "    "
    }
}

public inline fun <C : CodeWriter> C.withIndent(
    levels: Int = 1,
    block: C.() -> Unit
) {
    indent(levels)
    block()
    unindent(levels)
}


public interface Import : CodeEmitter {
    // import a.b.c.d.$name1.$name2;
    // import static a.b.c.d.$name1.$name2;
    public val packageName: String?

    public val names: List<String>

    /**
     * The import's identifier.
     */
    public val identifier: String
}
