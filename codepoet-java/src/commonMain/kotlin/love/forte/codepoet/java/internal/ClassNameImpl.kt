package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*


/**
 *
 * @author ForteScarlet
 */
internal class ClassNameImpl(
    override val packageName: String?,
    override val enclosingClassName: ClassName?,
    override val simpleName: String,
    override val annotations: List<AnnotationSpec> = emptyList(),
) : ClassName {

    private lateinit var _simpleNames: List<String>

    override val simpleNames: List<String>
        get() {
            if (::_simpleNames.isInitialized) {
                return _simpleNames
            }

            _simpleNames = enclosingClassName?.simpleNames?.plus(simpleName)
                ?: listOf(simpleName)

            return _simpleNames
        }

    override fun peerClass(name: String): ClassName {
        return ClassNameImpl(packageName, enclosingClassName, name)
    }

    override fun nestedClass(name: String): ClassName {
        return ClassNameImpl(packageName, this, name)
    }

    override fun annotated(annotations: List<AnnotationSpec>): TypeName {
        return ClassNameImpl(packageName, enclosingClassName, simpleName, this.annotations + annotations)
    }

    override fun withoutAnnotations(): TypeName {
        return ClassNameImpl(packageName, enclosingClassName, simpleName)
    }

    override fun emit(codeWriter: CodeWriter) {
        fun enclosingClasses(): List<ClassName> {
            val result = mutableListOf<ClassName>()
            var c: ClassName? = this
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
            } else if (className.isAnnotated || className === this) {
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

            if (className.isAnnotated) {
                if (charsEmitted) codeWriter.emit(" ")
                className.emitAnnotations(codeWriter)
            }

            codeWriter.emit(simpleName)
            charsEmitted = true
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassNameImpl) return false

        if (packageName != other.packageName) return false
        if (enclosingClassName != other.enclosingClassName) return false
        if (simpleName != other.simpleName) return false
        if (annotations != other.annotations) return false

        return true
    }

    override fun hashCode(): Int {
        var result = packageName?.hashCode() ?: 0
        result = 31 * result + (enclosingClassName?.hashCode() ?: 0)
        result = 31 * result + simpleName.hashCode()
        result = 31 * result + annotations.hashCode()
        return result
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
private fun CodeWriter.lookupName(className: ClassName): String {
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
    var c: ClassName? = className
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
    if (!javadoc) {
        importableType(className)
    }

    return className.canonicalName
}

private fun CodeWriter.importableType(className: ClassName) {
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

    if (className.packageName?.isEmpty() != false) {
        // null, or is empty.
        return
    } else if (alwaysQualify.contains(className.simpleName)) {
        // TODO what about nested types like java.util.Map.Entry?
        return
    }
    val topLevelClassName: ClassName = className.topLevelClassName
    val simpleName: String = topLevelClassName.simpleName
    val replaced: ClassName? = importableTypes.put(simpleName, topLevelClassName)
    if (replaced != null) {
        importableTypes[simpleName] = replaced // On collision, prefer the first inserted.
    }
}

/**
 * Returns the class referenced by `simpleName`, using the current nesting context and
 * imports.
 */
// TODO also honor superclass members when resolving names.
private fun CodeWriter.resolve(simpleName: String): ClassName? {
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
        val typeSpec: TypeSpec = typeSpecStack[i]
        if (typeSpec.nestedTypesSimpleNames.contains(simpleName)) {
            return stackClassName(i, simpleName)
        }
    }

    // Match the top-level class.
    if (typeSpecStack.size > 0 && typeSpecStack[0].name == simpleName) {
        return ClassName(packageName, simpleName)
    }

    // Match an imported type.
    val importedType: ClassName? = importedTypes[simpleName]
    if (importedType != null) return importedType

    // No match.
    return null
}

/** Returns the class named `simpleName` when nested in the class at `stackDepth`.  */
private fun CodeWriter.stackClassName(stackDepth: Int, simpleName: String): ClassName {
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

    var className = ClassName(packageName, typeSpecStack[0].name!!)
    for (i in 1..stackDepth) {
        className = className.nestedClass(typeSpecStack[i].name!!)
    }
    return className.nestedClass(simpleName)
}