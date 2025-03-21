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
package love.forte.codepoet.java

import love.forte.codepoet.java.internal.LineWrapper
import love.forte.codepoet.java.internal.isSourceIdentifier


@InternalApi
public class CodeWriter private constructor(
    internal val indentValue: String,
    internal val out: LineWrapper,

    internal val staticImportClassNames: Set<String> = emptySet(),
    internal val staticImports: Set<String> = emptySet(),
    internal val alwaysQualify: Set<String> = emptySet(),
    internal val importedTypes: Map<String, ClassName> = emptyMap(),

    ) {
    internal var indentLevel = 0
    internal var javadoc = false
    private var comment = false
    internal var packageName: String? = null // com.squareup.javapoet.CodeWriter.NO_PACKAGE

    internal val importableTypes: MutableMap<String, ClassName> = linkedMapOf()
    public val referencedNames: MutableSet<String> = linkedSetOf()

    // private val typeSpecStack: MutableList<TypeSpec> = mutableListOf()
    internal val typeSpecStack = ArrayDeque<TypeSpec>()
    internal val currentTypeVariables: Multiset<String> = Multiset()

    private var trailingNewline = false

    /**
     * When emitting a statement, this is the line of the statement currently being written. The first
     * line of a statement is indented normally and subsequent wrapped lines are double-indented. This
     * is -1 when the currently-written line isn't part of a statement.
     */
    internal var statementLine: Int = -1

    internal fun indent() {
        indent(1)
    }

    internal fun indent(levels: Int) {
        indentLevel += levels
    }

    internal fun unindent() {
        unindent(1)
    }

    internal fun unindent(levels: Int) {
        check(indentLevel - levels >= 0) { "cannot unindent $levels from $indentLevel" }
        indentLevel -= levels
    }


    internal fun pushPackage(packageName: String) {
        check(this.packageName == null) { "package already set: ${this.packageName}" }
        this.packageName = packageName
    }

    internal fun popPackage() {
        check(this.packageName != null) { "package not set" }
        this.packageName = null
    }

    internal fun pushType(type: TypeSpec) {
        this.typeSpecStack.addLast(type)
    }

    internal fun popType() {
        this.typeSpecStack.removeLast()
    }

    internal fun emitComment(codeBlock: CodeValue) {
        trailingNewline = true
        comment = true
        try {
            codeBlock.emit(this)
            emit("\n")
        } finally {
            comment = false
        }
    }

    internal fun emitJavadoc(javadoc: CodeValue) {
        if (javadoc.isEmpty) return

        emit("/**\n")
        this.javadoc = true
        try {
            javadoc.emit(this, true)
        } finally {
            this.javadoc = false
        }

        emit(" */\n")
    }

    internal fun emitAnnotations(annotations: Iterable<AnnotationSpec>, inline: Boolean) {
        for (annotation in annotations) {
            annotation.emit(this, inline)
            emit(if (inline) " " else "\n")
        }
    }

    internal fun emitModifiers(modifiers: Set<Modifier>, implicitModifiers: Set<Modifier> = emptySet()) {
        if (modifiers.isEmpty()) return

        for (modifier in modifiers) {
            if (modifier in implicitModifiers) continue
            emitAndIndent(modifier.name.lowercase())
            emitAndIndent(" ")
        }
    }

    internal fun emitTypeVariables(typeVariables: List<TypeVariableName>) {
        if (typeVariables.isEmpty()) return

        typeVariables.forEach { typeVariable -> currentTypeVariables.add(typeVariable.name) }

        emit("<")
        var firstTypeVariable = true
        for (typeVariable in typeVariables) {
            if (!firstTypeVariable) emit(", ")
            emitAnnotations(typeVariable.annotations, true)
            emit("%V") { literal(typeVariable.name) }
            var firstBound = true
            for (bound in typeVariable.bounds) {
                emit(if (firstBound) " extends %V" else " & %V") {
                    type(bound)
                }
                firstBound = false
            }
            firstTypeVariable = false
        }
        emit(">")
    }

    internal fun popTypeVariables(typeVariables: List<TypeVariableName>) {
        typeVariables.forEach { typeVariable -> currentTypeVariables.remove(typeVariable.name) }
    }

    internal fun emitWrappingSpace() {
        out.wrappingSpace(indentLevel + 2)
    }

    internal fun extractMemberName(part: String): String {
        require(part[0].isJavaIdentifierStart()) { "not an identifier: $part" }
        for (i in 1..part.length) {
            if (!part.substring(0, i).isSourceIdentifier()) {
                return part.substring(0, i - 1)
            }
        }
        return part
    }

    internal fun emitStaticImportMember(canonical: String, part: String): Boolean {
        val partWithoutLeadingDot = part.substring(1)
        if (partWithoutLeadingDot.isEmpty()) return false
        val first = partWithoutLeadingDot[0]
        if (!first.isJavaIdentifierStart()) return false
        val explicit = canonical + "." + extractMemberName(partWithoutLeadingDot)
        val wildcard = "$canonical.*"
        if (staticImports.contains(explicit) || staticImports.contains(wildcard)) {
            emitAndIndent(partWithoutLeadingDot)
            return true
        }
        return false
    }

    internal fun emitLiteral(value: Any?) {
        when (value) {
            is AnnotationSpec -> {
                value.emit(this, inline = true)
            }

            is CodeEmitter -> {
                value.emit(this)
            }

            else -> emitAndIndent(value.toString())
        }
    }

    internal fun emit(s: String) {
        emitAndIndent(s)
    }

    internal fun emit(codeValue: CodeValue) {
        codeValue.emit(this)
    }

    internal fun emitAndIndent(s: String) {
        var first = true
        val lines = s.lines()
        for (line in lines) {
            // Emit a newline character. Make sure blank lines in Javadoc & comments look good.
            if (!first) {
                if ((javadoc || comment) && trailingNewline) {
                    emitIndentation()
                    out.append(if (javadoc) " *" else "//")
                }
                out.append("\n")
                trailingNewline = true
                if (statementLine != -1) {
                    if (statementLine == 0) {
                        indent(2) // Begin multiple-line statement. Increase the indentation level.
                    }
                    statementLine++
                }
            }

            first = false
            if (line.isEmpty()) continue  // Don't indent empty lines.

            // Emit indentation and comment prefix if necessary.
            if (trailingNewline) {
                emitIndentation()
                if (javadoc) {
                    out.append(" * ")
                } else if (comment) {
                    out.append("// ")
                }
            }

            out.append(line)
            trailingNewline = false
        }
    }

    internal fun emitIndentation() {
        repeat(indentLevel) {
            out.append(indentValue)
        }
    }

    internal fun suggestedImports(): Map<String, ClassName> {
        val result = LinkedHashMap(importableTypes)
        result.keys.removeAll(referencedNames)
        return result
    }

    // TODO

    public companion object {
        private const val DEFAULT_INDENT = "    "

        internal fun create(out: Appendable): CodeWriter {
            return create(out, DEFAULT_INDENT, emptySet(), emptySet())
        }

        internal fun create(
            out: Appendable,
            indent: String,
            staticImports: Set<String>,
            alwaysQualify: Set<String>,
        ): CodeWriter {
            return create(
                out,
                indent,
                emptyMap(),
                staticImports,
                alwaysQualify
            )
        }

        internal fun create(
            out: Appendable,
            indent: String,
            importedTypes: Map<String, ClassName>,
            staticImports: Set<String>,
            alwaysQualify: Set<String>
        ): CodeWriter {
            return CodeWriter(
                indentValue = indent,
                out = LineWrapper.create(out, indent, 100),
                importedTypes = importedTypes,
                staticImports = staticImports,
                alwaysQualify = alwaysQualify,
                staticImportClassNames = staticImports.mapTo(linkedSetOf()) {
                    it.substringBeforeLast('.')
                },
            )
        }
    }
}

private data class IntWrapper(var value: Int = 0)

internal class Multiset<T> {
    private val map = linkedMapOf<T, IntWrapper>()

    fun add(t: T) {
        map.computeValue(t) { _, v ->
            if (v != null) {
                v.value += 1
                v
            } else {
                IntWrapper(1)
            }
        }
    }

    fun remove(t: T) {
        map.computeValue(t) { _, v ->
            if (v != null) {
                v.value -= 1
                if (v.value == 0) null else v
            } else {
                // not in the multiset
                null
            }
        }
    }

    fun contains(t: T): Boolean {
        return (map[t]?.value ?: 0) > 0
    }
}


internal inline fun CodeWriter.inPackage(packageName: String, block: () -> Unit) {
    pushPackage(packageName)
    block()
    popPackage()
}

internal inline fun CodeWriter.emit(ensureTrailingNewline: Boolean, format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    CodeValue(format, block).emit(this, ensureTrailingNewline)
}

internal inline fun CodeWriter.emit(format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    CodeValue(format, block).emit(this)
}

internal fun CodeEmitter.emitToString(): String =
    buildString { emit(CodeWriter.create(this)) }
