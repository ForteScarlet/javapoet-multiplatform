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
package love.forte.codegentle.java.writer

import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.common.computeValue
import love.forte.codegentle.common.writer.CodeWriter
import love.forte.codegentle.java.*
import love.forte.codegentle.java.internal.LineWrapper
import love.forte.codegentle.java.internal.emit0
import love.forte.codegentle.java.internal.isSourceIdentifier
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaTypeVariableName
import love.forte.codegentle.java.ref.JavaAnnotationRef
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.ref.internal.emitTo
import love.forte.codegentle.java.spec.JavaAnnotationSpec
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.strategy.DefaultJavaWriteStrategy
import love.forte.codegentle.java.strategy.JavaWriteStrategy
import love.forte.codegentle.java.strategy.ToStringJavaWriteStrategy


@InternalJavaCodeGentleApi
public class JavaCodeWriter private constructor(
    override val strategy: JavaWriteStrategy,
    override val indentValue: String,
    internal val out: LineWrapper,

    override val staticImportClassNames: Set<JavaClassName> = emptySet(),
    override val staticImports: Set<String> = emptySet(),
    override val alwaysQualify: Set<String> = emptySet(),
    internal val importedTypes: Map<String, JavaClassName> = emptyMap()
) : CodeWriter {

    internal enum class CommentType(
        val isJavadoc: Boolean = false,
    ) {
        JAVADOC(isJavadoc = true),
        COMMENT;
    }


    internal var indentLevel = 0
    internal var commentType: CommentType? = null

    // internal var javadoc = false
    // private var comment = false
    internal var packageName: String? = null // com.squareup.javapoet.CodeWriter.NO_PACKAGE

    // simple name -> class name
    internal val importableTypes: MutableMap<String, JavaClassName> = linkedMapOf()
    internal val referencedNames: MutableSet<String> = linkedSetOf()

    // private val typeSpecStack: MutableList<TypeSpec> = mutableListOf()
    internal val typeSpecStack = ArrayDeque<JavaTypeSpec>()
    internal val currentTypeVariables: Multiset<String> = Multiset()

    private var trailingNewline = false

    /**
     * When emitting a statement, this is the line of the statement currently being written. The first
     * line of a statement is indented normally and subsequent wrapped lines are double-indented. This
     * is -1 when the currently-written line isn't part of a statement.
     */
    internal var statementLine: Int = -1

    internal fun indent(levels: Int = 1) {
        indentLevel += levels
    }

    internal fun unindent(levels: Int = 1) {
        check(indentLevel - levels >= 0) { "cannot unindent $levels from $indentLevel" }
        indentLevel -= levels
    }

    internal inline fun withIndent(level: Int = 1, block: () -> Unit) {
        indent(level)
        block()
        unindent(level)
    }

    internal fun pushPackage(packageName: String) {
        check(this.packageName == null) { "package already set: ${this.packageName}" }
        this.packageName = packageName
    }

    internal fun popPackage() {
        check(this.packageName != null) { "package not set" }
        this.packageName = null
    }

    internal fun pushType(type: JavaTypeSpec) {
        this.typeSpecStack.addLast(type)
    }

    internal fun popType() {
        this.typeSpecStack.removeLast()
    }

    internal fun emitComment(codeBlock: JavaCodeValue) {
        trailingNewline = true
        commentType = CommentType.COMMENT
        // comment = true
        try {
            codeBlock.emit(this)
            emit("\n")
        } finally {
            commentType = null
            // comment = false
        }
    }

    internal fun emitJavadoc(javadoc: CodeValue) {
        if (javadoc.isEmpty) return

        emit("/**\n")
        this.commentType = CommentType.JAVADOC
        // this.javadoc = true
        try {
            javadoc.emit0(this, true)
        } finally {
            this.commentType = null
            // this.javadoc = false
        }

        emit(" */\n")
    }

    internal fun emitAnnotations(annotations: Iterable<JavaAnnotationSpec>, inline: Boolean) {
        for (annotation in annotations) {
            annotation.emit(this, inline)
            emit(if (inline) " " else "\n")
        }
    }

    internal fun emitAnnotationRefs(annotations: Iterable<JavaAnnotationRef>, inline: Boolean) {
        for (annotation in annotations) {
            annotation.emitTo(this, inline)
            emit(if (inline) " " else "\n")
        }
    }

    internal fun emitModifiers(modifiers: Set<JavaModifier>, implicitModifiers: Set<JavaModifier> = emptySet()) {
        if (modifiers.isEmpty()) return

        for (modifier in modifiers) {
            if (modifier in implicitModifiers) continue
            emitAndIndent(modifier.name.lowercase())
            emitAndIndent(" ")
        }
    }

    internal fun emitTypeVariables(typeVariables: List<JavaTypeVariableName>) {
        if (typeVariables.isEmpty()) return

        typeVariables.forEach { typeVariable -> currentTypeVariables.add(typeVariable.name) }

        emit("<")
        var firstTypeVariable = true
        for (typeVariable in typeVariables) {
            if (!firstTypeVariable) emit(", ")
            // TODO typeVariableRef
            // emitAnnotations(typeVariable.annotations, true)
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

    internal fun emitTypeVariableRefs(typeVariables: List<JavaTypeRef<JavaTypeVariableName>>) {
        if (typeVariables.isEmpty()) return

        typeVariables.forEach { typeVariable -> currentTypeVariables.add(typeVariable.typeName.name) }

        emit("<")
        var firstTypeVariable = true
        for (typeVariable in typeVariables) {
            if (!firstTypeVariable) emit(", ")
            // TODO typeVariableRef
            emitAnnotationRefs(typeVariable.status.annotations, true)
            emit("%V") { literal(typeVariable.typeName.name) }
            var firstBound = true
            for (bound in typeVariable.typeName.bounds) {
                emit(if (firstBound) " extends %V" else " & %V") {
                    type(bound)
                }
                firstBound = false
            }
            firstTypeVariable = false
        }
        emit(">")
    }

    internal fun popTypeVariables(typeVariables: List<JavaTypeVariableName>) {
        typeVariables.forEach { typeVariable -> currentTypeVariables.remove(typeVariable.name) }
    }

    internal fun popTypeVariableRefs(typeVariableRefs: List<JavaTypeRef<JavaTypeVariableName>>) {
        typeVariableRefs.forEach { typeVariableRef -> currentTypeVariables.remove(typeVariableRef.typeName.name) }
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
            is JavaAnnotationSpec -> {
                value.emit(this, inline = true)
            }

            is JavaCodeEmitter -> {
                value.emit(this)
            }

            else -> emitAndIndent(value.toString())
        }
    }

    internal fun emit(s: String) {
        emitAndIndent(s)
    }

    internal fun emit(codeValue: CodeValue) {
        codeValue.emit0(this)
    }

    internal fun emitNewline() {
        out.append(strategy.newline())
        trailingNewline = true
    }

    internal fun emitNewlineAndIndent() {
        emitNewline()
        emitIndentation()
    }

    internal fun emitAndIndent(s: String) {
        var first = true
        val lines = s.lines()
        for (line in lines) {
            // Emit a newline character. Make sure blank lines in Javadoc & comments look good.
            if (!first) {
                // if ((javadoc || comment) && trailingNewline) {
                if (commentType != null && trailingNewline) {
                    emitIndentation()
                    out.append(if (commentType?.isJavadoc == true) " *" else "//")
                    // out.append(if (javadoc) " *" else "//")
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
                when (commentType) {
                    CommentType.JAVADOC -> {
                        out.append(" * ")
                    }

                    CommentType.COMMENT -> {
                        out.append("// ")
                    }

                    null -> {}
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

    internal fun suggestedImports(): Map<String, JavaClassName> {
        val result = LinkedHashMap(importableTypes)
        result.keys.removeAll(referencedNames)
        return result
    }

    public companion object {
        private const val DEFAULT_COLUMN_LIMIT = 100
        private const val DEFAULT_INDENT = "    "

        internal fun create(
            out: Appendable,
            dialect: JavaWriteStrategy = DefaultJavaWriteStrategy()
        ): JavaCodeWriter {
            return create(
                dialect = dialect,
                out = out,
                indent = DEFAULT_INDENT,
                staticImports = emptySet(),
                alwaysQualify = emptySet()
            )
        }

        internal fun create(
            dialect: JavaWriteStrategy,
            out: Appendable,
            indent: String,
            staticImports: Set<String>,
            alwaysQualify: Set<String>,
        ): JavaCodeWriter {
            return create(
                dialect = dialect,
                out = out,
                indent = indent,
                importedTypes = emptyMap(),
                staticImports = staticImports,
                alwaysQualify = alwaysQualify
            )
        }

        internal fun create(
            dialect: JavaWriteStrategy,
            out: Appendable,
            indent: String,
            importedTypes: Map<String, JavaClassName>,
            // TODO sealed class StaticImports { className, propertyName, functionName }
            staticImports: Set<String>,
            alwaysQualify: Set<String>
        ): JavaCodeWriter {
            return JavaCodeWriter(
                strategy = dialect,
                indentValue = indent,
                out = LineWrapper.create(out, indent, DEFAULT_COLUMN_LIMIT),
                importedTypes = importedTypes,
                staticImports = staticImports,
                alwaysQualify = alwaysQualify,
                staticImportClassNames = staticImports.mapTo(linkedSetOf()) {
                    // static import 不止可以import class，还有字段、方法啥的
                    JavaClassName(it)
                    // TODO("")
                    // it.substringBeforeLast('.')
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


internal inline fun JavaCodeWriter.inPackage(packageName: String, block: () -> Unit) {
    pushPackage(packageName)
    block()
    popPackage()
}

internal inline fun JavaCodeWriter.emit(
    ensureTrailingNewline: Boolean,
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
) {
    JavaCodeValue(format, block).emit(this, ensureTrailingNewline)
}

internal inline fun JavaCodeWriter.emit(format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    JavaCodeValue(format, block).emit(this)
}

internal fun JavaCodeEmitter.emitToString(): String =
    buildString {
        emit(
            JavaCodeWriter.create(
                out = this,
                dialect = ToStringJavaWriteStrategy
            )
        )
    }
