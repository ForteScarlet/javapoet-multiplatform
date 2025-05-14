package love.forte.codegentle.java.naming.internal

import love.forte.codegentle.common.naming.canonicalName
import love.forte.codegentle.common.naming.simpleNames
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.spec.nestedTypesSimpleNames
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emitToString


/**
 *
 * @author ForteScarlet
 */
internal data class JavaClassNameImpl(
    override val packageName: String?,
    override val enclosingClassName: JavaClassName?,
    override val simpleName: String,
    // override val annotations: List<JavaAnnotationSpec> = emptyList(),
) : JavaClassName {
    override val topLevelClassName: JavaClassName
        get() = enclosingClassName?.topLevelClassName ?: this

    override fun peerClass(name: String): JavaClassName {
        return JavaClassNameImpl(packageName, enclosingClassName, name)
    }

    override fun nestedClass(name: String): JavaClassName {
        return JavaClassNameImpl(packageName, this, name)
    }

    // override fun annotated(annotations: List<JavaAnnotationSpec>): JavaTypeName {
    //     if (annotations.isEmpty()) return this
    //     return JavaClassNameImpl(packageName, enclosingClassName, simpleName, this.annotations + annotations)
    // }
    //
    // override fun withoutAnnotations(): JavaTypeName {
    //     return JavaClassNameImpl(packageName, enclosingClassName, simpleName)
    // }

    override fun unbox(): JavaTypeName {
        if (packageName == JavaClassName.Builtins.JAVA_LANG_PACKAGE && enclosingClassName == null) {
            return when (simpleName) {
                JavaClassName.Builtins.BOXED_VOID_SIMPLE_NAME -> JavaTypeName.Builtins.VOID
                JavaClassName.Builtins.BOXED_BOOLEAN_SIMPLE_NAME -> JavaTypeName.Builtins.BOOLEAN
                JavaClassName.Builtins.BOXED_BYTE_SIMPLE_NAME -> JavaTypeName.Builtins.BYTE
                JavaClassName.Builtins.BOXED_SHORT_SIMPLE_NAME -> JavaTypeName.Builtins.SHORT
                JavaClassName.Builtins.BOXED_INT_SIMPLE_NAME -> JavaTypeName.Builtins.INT
                JavaClassName.Builtins.BOXED_LONG_SIMPLE_NAME -> JavaTypeName.Builtins.LONG
                JavaClassName.Builtins.BOXED_CHAR_SIMPLE_NAME -> JavaTypeName.Builtins.CHAR
                JavaClassName.Builtins.BOXED_FLOAT_SIMPLE_NAME -> JavaTypeName.Builtins.FLOAT
                JavaClassName.Builtins.BOXED_DOUBLE_SIMPLE_NAME -> JavaTypeName.Builtins.DOUBLE
                else -> throw IllegalStateException("Can't unbox $this")
            } // .annotated(annotations)
        }

        throw IllegalStateException("Can't unbox $this")
    }

    override fun emit(codeWriter: JavaCodeWriter) {
        fun enclosingClasses(): List<JavaClassName> {
            val result = mutableListOf<JavaClassName>()
            var c: JavaClassName? = this
            while (c != null) {
                result.add(c)
                c = c.enclosingClassName
            }
            result.reverse()
            return result
        }

        var charsEmitted = false

        for (className in enclosingClasses()) {
            val simpleName: String
            if (charsEmitted) {
                // We've already emitted an enclosing class. Emit as we go.
                codeWriter.emit(".")
                simpleName = className.simpleName
            } else if (className === this) {
                // We encountered the first enclosing class that must be emitted.
                val qualifiedName: String = codeWriter.lookupName(className)
                val dot = qualifiedName.lastIndexOf('.')
                if (dot != -1) {
                    codeWriter.emitAndIndent(qualifiedName.substring(0, dot + 1))
                    simpleName = qualifiedName.substring(dot + 1)
                    charsEmitted = true
                } else {
                    simpleName = qualifiedName
                }
            } else {
                // Don't emit this enclosing type. Keep going so we can be more precise.
                continue
            }

            codeWriter.emit(simpleName)
            charsEmitted = true
        }
    }

    override fun toString(): String {
        return emitToString()
    }
}


/**
 * Returns the best name to identify `className` with in the current context. This uses the
 * available imports and the current scope to find the shortest name available. It does not honor
 * names visible due to inheritance.
 */
private fun JavaCodeWriter.lookupName(className: JavaClassName): String {
    /*
     * Copyright (C) 2015 Square, Inc.
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

    // If the top level simple name is masked by a current type variable, use the canonical name.
    val topLevelSimpleName: String = className.topLevelClassName.simpleName
    if (currentTypeVariables.contains(topLevelSimpleName)) {
        return className.canonicalName
    }

    // Find the shortest suffix of className that resolves to className. This uses both local type
    // names (so `Entry` in `Map` refers to `Map.Entry`). Also uses imports.
    var nameResolved = false
    var c: JavaClassName? = className
    while (c != null) {
        val resolved = resolve(c.simpleName)
        nameResolved = resolved != null

        if (resolved != null && resolved.canonicalName == c.canonicalName) {
            val suffixOffset: Int = c.simpleNames.size - 1
            return className.simpleNames.subList(
                suffixOffset, className.simpleNames.size
            ).joinToString(".")
        }

        c = c.enclosingClassName
    }


    // If the name resolved but wasn't a match, we're stuck with the fully qualified name.
    if (nameResolved) {
        return className.canonicalName
    }

    // If the class is in the same package, we're done.
    if (packageName == className.packageName) {
        referencedNames.add(topLevelSimpleName)
        return className.simpleNames.joinToString(".")
    }


    // We'll have to use the fully-qualified name. Mark the type as importable for a future pass.
    // if (!javadoc) {
    if (commentType?.isJavadoc != true) {
        importableType(className)
    }

    return className.canonicalName
}

private fun JavaCodeWriter.importableType(className: JavaClassName) {
    /*
     * Copyright (C) 2015 Square, Inc.
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

    val packageName = className.packageName
    if (packageName == null || packageName.isEmpty()) {
        // null, or is empty.
        return
    } else if (alwaysQualify.contains(className.simpleName)) {
        // TODO what about nested types like java.util.Map.Entry?
        return
    }
    val topLevelClassName: JavaClassName = className.topLevelClassName
    val simpleName: String = topLevelClassName.simpleName
    val replaced: JavaClassName? = importableTypes.put(simpleName, topLevelClassName)
    if (replaced != null) {
        importableTypes[simpleName] = replaced // On collision, prefer the first inserted.
    }
}

/**
 * Returns the class referenced by `simpleName`, using the current nesting context and
 * imports.
 */
// TODO also honor superclass members when resolving names.
private fun JavaCodeWriter.resolve(simpleName: String): JavaClassName? {
    /*
     * Copyright (C) 2015 Square, Inc.
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

    // Match a child of the current (potentially nested) class.
    for (i in typeSpecStack.indices.reversed()) {
        val typeSpec: JavaTypeSpec = typeSpecStack[i]
        if (typeSpec.nestedTypesSimpleNames.contains(simpleName)) {
            return stackClassName(i, simpleName)
        }
    }

    // Match the top-level class.
    if (typeSpecStack.size > 0 && typeSpecStack[0].name == simpleName) {
        return JavaClassName(packageName, simpleName)
    }

    // Match an imported type.
    val importedType: JavaClassName? = importedTypes[simpleName]
    if (importedType != null) return importedType

    // No match.
    return null
}

/** Returns the class named `simpleName` when nested in the class at `stackDepth`.  */
private fun JavaCodeWriter.stackClassName(stackDepth: Int, simpleName: String): JavaClassName {
    /*
     * Copyright (C) 2015 Square, Inc.
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

    var className = JavaClassName(packageName, typeSpecStack[0].name!!)
    for (i in 1..stackDepth) {
        className = className.nestedClass(typeSpecStack[i].name!!)
    }
    return className.nestedClass(simpleName)
}
