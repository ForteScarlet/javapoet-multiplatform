package love.forte.codepoet.java

import kotlin.jvm.JvmOverloads
import kotlin.jvm.JvmStatic

/**
 * A part of [CodeValue].
 */
public sealed class CodePart {
    public companion object {
        public const val PLACEHOLDER: String = "%V"

        /**
         * Skip this `%V`, Just write `%V` itself.
         */
        @JvmStatic
        public fun skip(): CodeArgumentPart = CodeArgumentPart.Skip

        /**
         * Emits a `literal` value with no escaping.
         * Arguments for literals may be
         * strings, primitives, [type declarations][TypeSpec],
         * [annotations][AnnotationSpec] and even other [code blocks][CodeBlock]
         * or [code emitters][CodeEmitter].
         */
        @JvmStatic
        public fun literal(value: Any?): CodeArgumentPart = CodeArgumentPart.Literal(value)

        /**
         * Emits a `name`, using name collision avoidance where necessary.
         * Arguments for names may be strings (actually any [character sequence][CharSequence]),
         * [parameters][ParameterSpec], [fields][FieldSpec],
         * [methods][MethodSpec], and [types][TypeSpec].
         */
        @JvmStatic
        public fun name(name: String?): CodeArgumentPart = CodeArgumentPart.Name(name)

        /**
         * Emits a `name`, using name collision avoidance where necessary.
         * Arguments for names may be strings (actually any [character sequence][CharSequence]),
         * [parameters][ParameterSpec], [fields][FieldSpec],
         * [methods][MethodSpec], and [types][TypeSpec].
         */
        @JvmStatic
        public fun name(nameValue: Any): CodeArgumentPart = CodeArgumentPart.Name(nameValue)

        /**
         * Escapes the value as a `string`, wraps it with double quotes, and emits
         *  that. For example, `6" sandwich` is emitted `"6\" sandwich"`.
         */
        @JvmStatic
        public fun string(value: String?): CodeArgumentPart = CodeArgumentPart.Str(value)

        /**
         * Emits a `type` reference. Types will be imported if possible. Arguments
         * for types may be `Class` classes, `TypeMirror` type mirrors,
         * and `Element` elements.
         */
        @JvmStatic
        public fun type(type: TypeName): CodeArgumentPart = CodeArgumentPart.Type(type)

        // TODO type(Any)

        /**
         * Increases the indentation level.
         */
        @JvmStatic
        @JvmOverloads
        public fun indent(levels: Int = 1): CodeArgumentPart = CodeArgumentPart.Indent(levels)

        /**
         * Decreases the indentation level.
         */
        @JvmStatic
        @JvmOverloads
        public fun unindent(levels: Int = 1): CodeArgumentPart = CodeArgumentPart.Unindent(levels)


        /**
         * Begins a statement.
         * For multiline statements, every line after the first line
         * is double-indented.
         */
        @JvmStatic
        public fun statementBegin(): CodeArgumentPart = CodeArgumentPart.StatementBegin

        /**
         * Ends a statement.
         */
        @JvmStatic
        public fun statementEnd(): CodeArgumentPart = CodeArgumentPart.StatementEnd

        /**
         * Emits a space or a newline, depending on its position on the line. This prefers
         * to wrap lines before 100 columns.
         */
        @JvmStatic
        public fun wrappingSpace(): CodeArgumentPart = CodeArgumentPart.WrappingSpace

        /**
         * Acts as a zero-width space. This prefers to wrap lines before 100 columns.
         */
        @JvmStatic
        public fun zeroWidthSpace(): CodeArgumentPart = CodeArgumentPart.ZeroWidthSpace
    }


}

public sealed class CodeArgumentPart : CodePart() {
    /**
     * Skip this `%V`, Just write `%V` itself.
     */
    internal data object Skip : CodeArgumentPart()

    /**
     * `%L` emits a `literal` value with no escaping.
     * Arguments for literals may be
     * strings, primitives, [type declarations][TypeSpec],
     * [annotations][AnnotationSpec] and even other [code blocks][CodeBlock]
     * or [code emitters][CodeEmitter].
     */
    internal data class Literal(val value: Any?) : CodeArgumentPart()

    /**
     * `%N` emits a `name`, using name collision avoidance where necessary.
     * Arguments for names may be strings (actually any [character sequence][CharSequence]),
     * [parameters][ParameterSpec], [fields][FieldSpec],
     * [methods][MethodSpec], and [types][TypeSpec].
     */
    internal data class Name(val name: String?) : CodeArgumentPart() {
        internal constructor(name: Any) : this(argToName(name))

        companion object {
            private fun argToName(o: Any): String? {
                return when (o) {
                    is CharSequence -> o.toString()
                    is ParameterSpec -> o.name
                    is FieldSpec -> o.name
                    is MethodSpec -> o.name
                    is TypeSpec -> o.name
                    else -> throw IllegalArgumentException("expected name but was $o")
                }
            }
        }
    }

    /**
     * `%S` escapes the value as a `string`, wraps it with double quotes, and emits
     *  that. For example, `6" sandwich` is emitted `"6\" sandwich"`.
     */
    internal data class Str(val value: String?) : CodeArgumentPart()

    /**
     * `%T` emits a `type` reference. Types will be imported if possible. Arguments
     * for types may be `Class` classes, `TypeMirror` type mirrors,
     * and `Element` elements.
     */
    internal data class Type(val type: TypeName) : CodeArgumentPart() {
        companion object {
            internal fun argToType(o: Any?): TypeName {
                if (o is TypeName) return o
                // TODO
                // if (o is TypeMirror) return com.squareup.javapoet.TypeName.get(o as TypeMirror)
                // if (o is javax.lang.model.element.Element) return com.squareup.javapoet.TypeName.get((o as javax.lang.model.element.Element).asType())
                // if (o is java.lang.reflect.Type) return com.squareup.javapoet.TypeName.get(o as java.lang.reflect.Type)
                throw IllegalArgumentException("expected type but was $o")
            }
        }
    }

    /**
     * `%>` increases the indentation level.
     */
    internal data class Indent(val levels: Int = 1) : CodeArgumentPart()


    /**
     * `%<` decreases the indentation level.
     */
    internal data class Unindent(val levels: Int = 1) : CodeArgumentPart()

    /**
     * `%[` begins a statement.
     * For multiline statements, every line after the first line
     * is double-indented.
     */
    internal data object StatementBegin : CodeArgumentPart()

    /**
     * `%]` ends a statement.
     */
    internal data object StatementEnd : CodeArgumentPart()

    /**
     * `%W` emits a space or a newline, depending on its position on the line. This prefers
     * to wrap lines before 100 columns.
     */
    internal data object WrappingSpace : CodeArgumentPart()

    /**
     * `%Z` acts as a zero-width space. This prefers to wrap lines before 100 columns.
     */
    internal data object ZeroWidthSpace : CodeArgumentPart()
}

internal data class CodeSimplePart(val value: String) : CodePart()