/*
 * Copyright (C) 2014 Google, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

@file:JvmName("ClassNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.common.codepoint.codePointAt
import love.forte.codepoet.common.codepoint.isLowerCase
import love.forte.codepoet.common.codepoint.isUpperCase
import love.forte.codepoet.java.internal.ClassNameImpl
import kotlin.jvm.JvmField
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


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
        get() = enclosingClassName?.canonicalName?.plus(".")?.plus(simpleName)
            ?: packageName?.takeUnless { it.isEmpty() }?.plus(".")?.plus(simpleName)
            ?: simpleName

    /**
     * Returns the package name, like `"java.util"` for [Map.Entry].
     * Returns the empty string for the default package.
     */
    public val packageName: String?

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
            ?: packageName?.takeUnless { it.isEmpty() }?.plus(".")?.plus(simpleName)
            ?: simpleName

    public val simpleNames: List<String>

    override val isPrimitive: Boolean
        get() = false

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

    public object Builtins {
        internal const val JAVA_LANG_PACKAGE = "java.lang"

        @JvmField
        public val OBJECT: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, "Object")

        @JvmField
        public val STRING: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, "String")

        // primitives

        internal const val BOXED_VOID_SIMPLE_NAME = "Void"


        @JvmField
        public val BOXED_VOID: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_VOID_SIMPLE_NAME)

        internal const val BOXED_BOOLEAN_SIMPLE_NAME = "Boolean"

        @JvmField
        public val BOXED_BOOLEAN: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_BOOLEAN_SIMPLE_NAME)

        internal const val BOXED_BYTE_SIMPLE_NAME = "Byte"

        @JvmField
        public val BOXED_BYTE: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_BYTE_SIMPLE_NAME)

        internal const val BOXED_SHORT_SIMPLE_NAME = "Short"

        @JvmField
        public val BOXED_SHORT: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_SHORT_SIMPLE_NAME)

        internal const val BOXED_INT_SIMPLE_NAME = "Integer"

        @JvmField
        public val BOXED_INT: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_INT_SIMPLE_NAME)

        internal const val BOXED_LONG_SIMPLE_NAME = "Long"

        @JvmField
        public val BOXED_LONG: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_LONG_SIMPLE_NAME)

        internal const val BOXED_CHAR_SIMPLE_NAME = "Character"

        @JvmField
        public val BOXED_CHAR: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_CHAR_SIMPLE_NAME)

        internal const val BOXED_FLOAT_SIMPLE_NAME = "Float"

        @JvmField
        public val BOXED_FLOAT: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_FLOAT_SIMPLE_NAME)

        internal const val BOXED_DOUBLE_SIMPLE_NAME = "Double"

        @JvmField
        public val BOXED_DOUBLE: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, BOXED_DOUBLE_SIMPLE_NAME)

        // Annotations

        @JvmField
        public val OVERRIDE: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, "Override")

        @JvmField
        public val SUPPRESS_WARNINGS: ClassName = ClassNameImpl(JAVA_LANG_PACKAGE, null, "SuppressWarnings")
    }
}


/**
 * Returns a class name created from the given parts. For example, calling this with package name `"java.util"`
 * and simple names `"Map"`, `"Entry"` yields [Map.Entry].
 */
@JvmName("of")
public fun ClassName(packageName: String?, simpleName: String, vararg simpleNames: String): ClassName {
    var className: ClassName = ClassNameImpl(packageName, null, simpleName)
    for (nested in simpleNames) {
        className = className.nestedClass(nested)
    }
    return className
}

/**
 * Returns a new [ClassName] instance for the given fully-qualified class name string. This
 * method assumes that the input is ASCII and follows typical Java style (lowercase package
 * names, UpperCamelCase class names) and may produce incorrect results or throw
 * [IllegalArgumentException] otherwise.
 * For that reason,
 * [ClassName] should be preferred as they can correctly create [ClassName]
 * instances without such restrictions.
 */
@JvmName("of")
public fun ClassName(bestGuessClassNameString: String): ClassName {


    // Add the package name, like "java.util.concurrent", or "" for no package.
    var p = 0
    while (p < bestGuessClassNameString.length && bestGuessClassNameString.codePointAt(p).isLowerCase()) {
        p = bestGuessClassNameString.indexOf('.', p) + 1
        require(p != 0) {
            "couldn't make a guess for $bestGuessClassNameString"
        }
    }

    val packageName: String? =
        if (p == 0) null else bestGuessClassNameString.substring(0, p - 1)

    // Add class names like "Map" and "Entry".
    var className: ClassName? = null

    for (simpleName in bestGuessClassNameString.substring(p).split("\\.".toRegex()).toTypedArray()) {
        require(simpleName.isNotEmpty() && simpleName.codePointAt(0).isUpperCase()) {
            "couldn't make a guess for $bestGuessClassNameString"
        }
        className = ClassNameImpl(packageName, className, simpleName)
    }

    return className!!
}
