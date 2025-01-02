package love.forte.javapoet

import kotlin.reflect.KClass


/**
 * A fully-qualified class name for top-level and member classes.
 */
public interface ClassName : TypeName, Comparable<ClassName> {
    /**
     * Returns the simple name of this class, like `"Entry"` for `Map.Entry`.
     */
    public val simpleName: String

    /**
     * Returns the full class name of this class. Like `"java.util.Map.Entry"` for [Map.Entry].
     */
    public val canonicalName: String

    /**
     * Returns the package name, like `"java.util"` for [Map.Entry].
     * Returns the empty string for the default package.
     */
    public val packageName: String

    /**
     * Returns the enclosing class, like `Map` for `Map.Entry`.
     * Returns null if this class is not nested in another class.
     */
    public val enclosingClassName: ClassName?

    /**
     * Returns the top class in this nesting group.
     * Equivalent to chained calls to [enclosingClassName] until the result's enclosing class is null.
     */
    public val topLevelClassName: ClassName
        get() = enclosingClassName?.topLevelClassName ?: this

    /**
     * Return the binary name of a class.
     */
    public val reflectionName: String
        get() = enclosingClassName?.reflectionName?.plus("$")?.plus(simpleName)
            ?: packageName.takeUnless { it.isEmpty() }?.plus(".")?.plus(simpleName)
            ?: simpleName

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

    public companion object {
        // OBJECT?

    }
}

public fun ClassName(clz: KClass<*>): ClassName = TODO()

/**
 * Returns a class name created from the given parts. For example, calling this with package name `"java.util"`
 * and simple names `"Map"`, `"Entry"` yields [Map.Entry].
 */
public fun ClassName(packageName: String, simpleName: String, vararg simpleNames: String): ClassName = TODO()

/**
 * Returns a new [ClassName] instance for the given fully-qualified class name string. This
 * method assumes that the input is ASCII and follows typical Java style (lowercase package
 * names, UpperCamelCase class names) and may produce incorrect results or throw
 * [IllegalArgumentException] otherwise.
 * For that reason,
 * [ClassName] should be preferred as they can correctly create [ClassName]
 * instances without such restrictions.
 */
public fun bestGuessClassName(classNameString: String): ClassName = TODO()
