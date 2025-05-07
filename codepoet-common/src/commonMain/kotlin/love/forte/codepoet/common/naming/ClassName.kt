package love.forte.codepoet.common.naming

/**
 * A JVM-based, fully qualified class name for top-level and member classes.
 *
 * @author ForteScarlet
 */
public interface ClassName : Named, TypeName, Comparable<ClassName> {
    /**
     * Returns the simple name of this class, like `"Entry"` for `Map.Entry`.
     */
    public val simpleName: String

    /**
     * Same as [simpleName].
     */
    override val name: String
        get() = simpleName

    /**
     * Returns the package name, like `"java.util"` for [Map.Entry].
     * Returns null for the default package.
     */
    public val packageName: String?

    /**
     * Returns the enclosing class, like `Map` for `Map.Entry`.
     * Returns null if this class is not nested in another class.
     */
    public val enclosingClassName: ClassName?

    /**
     * Returns the top class in this nesting group.
     * Equivalent to chained calls to [ClassName.enclosingClassName] until the result's enclosing class is null.
     */
    public val topLevelClassName: ClassName

    /**
     * Returns a class that shares the same enclosing package or class.
     * If this class is enclosed by another class, this is equivalent to `enclosingClassName().nestedClass(name)`.
     * Otherwise, it is equivalent to `get(packageName(), name)`.
     */
    public fun peerClass(name: String): ClassName

    /**
     * Returns a new `ClassName` instance for the specified `name` as nested inside this class.
     */
    public fun nestedClass(name: String): ClassName


    override fun compareTo(other: ClassName): Int = canonicalName.compareTo(other.canonicalName)

    // TODO BuiltIns
}

/**
 * Returns the full class name of this class. Like `"java.util.Map.Entry"` for [Map.Entry].
 */
public val ClassName.canonicalName: String
    get() = buildString { appendCanonicalNameTo(this) }

public fun <A : Appendable> ClassName.appendCanonicalNameTo(appendable: A): A {
    val enclosingClassName = enclosingClassName
    if (enclosingClassName != null) {
        enclosingClassName.appendCanonicalNameTo(appendable).append('.').append(simpleName)
    } else {
        val packageName = packageName
        if (packageName != null) {
            appendable.append(packageName).append('.')
        }
        appendable.append(simpleName)
    }

    return appendable
}

/**
 * Return the binary name of a class.
 *
 * `java.util.Map$Entry`
 */
public val ClassName.reflectionName: String
    get() = buildString { appendReflectionNameTo(this) }

public fun <A : Appendable> ClassName.appendReflectionNameTo(appendable: A): A {
    val enclosingClassName = enclosingClassName
    if (enclosingClassName != null) {
        enclosingClassName.appendReflectionNameTo(appendable).append('$').append(simpleName)
    } else {
        val packageName = packageName
        if (packageName != null) {
            appendable.append(packageName).append('.')
        }
        appendable.append(simpleName)
    }

    return appendable
}

public val ClassName.simpleNames: List<String>
    get() = enclosingClassName?.simpleNames?.plus(simpleName)
        ?: listOf(simpleName)
