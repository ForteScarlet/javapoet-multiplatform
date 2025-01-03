package love.forte.javapoet.internal

// See javax.lang.model.SourceVersion.java
private val keywords = setOf(
    "abstract", "continue", "for", "new", "switch",
    "assert", "default", "if", "package", "synchronized",
    "boolean", "do", "goto", "private", "this",
    "break", "double", "implements", "protected", "throw",
    "byte", "else", "import", "public", "throws",
    "case", "enum", "instanceof", "return", "transient",
    "catch", "extends", "int", "short", "try",
    "char", "final", "interface", "static", "void",
    "class", "finally", "long", "strictfp", "volatile",
    "const", "float", "native", "super", "while",
    // literals
    "null", "true", "false"
)

internal fun CharSequence.isSourceKeyword() = toString() in keywords

internal fun CharSequence.isSourceName(): Boolean {
    val id = toString()

    for (s in id.split("\\.".toRegex()).toTypedArray()) {
        if (!s.isSourceIdentifier() || s.isSourceKeyword()) return false
    }
    return true
}

internal fun CharSequence.isSourceIdentifier(): Boolean {
    val id: String = toString()

    if (id.isEmpty()) {
        return false
    }
    var cp = id.codePointAt(0)
    if (!cp.isJavaIdentifierStart()) {
        return false
    }

    var i: Int = cp.charCount()
    while (i < id.length) {
        cp = id.codePointAt(i)

        if (!cp.isJavaIdentifierPart()) {
            return false
        }

        i += cp.charCount()
    }

    return true
}
