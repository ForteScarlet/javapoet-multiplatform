package love.forte.codegentle.java.internal

import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.spec.internal.JavaSimpleTypeSpecImpl
import love.forte.codegentle.java.type
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit

internal inline fun JavaTypeSpec.doEmit(
    codeWriter: JavaCodeWriter,
    block: () -> Unit
) {
    // Nested classes interrupt wrapped line indentation. Stash the current wrapping state and put
    // it back afterwards when this type is complete.
    val previousStatementLine = codeWriter.statementLine
    codeWriter.statementLine = -1

    try {
        block()
    } finally {
        codeWriter.statementLine = previousStatementLine
    }
}

private fun JavaCodeWriter.newLineIfNot(condition: Boolean) {
    if (!condition) {
        emit("\n")
    }
}

internal fun JavaTypeSpec.emitSupers(codeWriter: JavaCodeWriter) {
    val extendsTypes: List<JavaTypeName>
    val implementsTypes: List<JavaTypeName>

    if (kind == JavaTypeSpec.Kind.INTERFACE) {
        extendsTypes = superinterfaces
        implementsTypes = emptyList()
    } else {
        extendsTypes = superclass?.let {
            if (it != JavaClassName.Builtins.OBJECT) {
                listOf(it)
            } else null
        } ?: emptyList()

        implementsTypes = superinterfaces
    }

    codeWriter.emitExtends(extendsTypes)
    codeWriter.emitImplements(implementsTypes)
}

internal fun JavaCodeWriter.emitExtends(extendsTypes: List<JavaTypeName>) {
    if (extendsTypes.isNotEmpty()) {
        emit(" extends")
        var firstType = true
        for (extendsType in extendsTypes) {
            if (!firstType) {
                emit(",")
            }
            emit(" %V") { type(extendsType) }
            firstType = false
        }
    }
}

internal fun JavaCodeWriter.emitImplements(implementsTypes: List<JavaTypeName>) {
    if (implementsTypes.isNotEmpty()) {
        emit(" implements")
        var firstType = true
        for (implementsType in implementsTypes) {
            if (!firstType) {
                emit(",")
            }
            emit(" %V") { type(implementsType) }
            firstType = false
        }
    }
}

internal inline fun JavaTypeSpec.emitMembers(
    codeWriter: JavaCodeWriter,
    isRecord: Boolean = false,
    block: (firstMember: Boolean, notFirst: () -> Unit) -> Unit = { _, _ -> }
) {
    codeWriter.pushType(this)
    codeWriter.indent()
    var firstMember = true

    block(firstMember) { firstMember = false }

    // Static fields
    for (field in fields) {
        if (!field.hasModifier(JavaModifier.STATIC)) continue
        codeWriter.newLineIfNot(firstMember)
        field.emit(codeWriter, kind.implicitFieldModifiers)
        firstMember = false
    }

    // Static block
    if (!staticBlock.isEmpty) {
        codeWriter.newLineIfNot(firstMember)
        staticBlock.emit(codeWriter)
        firstMember = false
    }

    // Non-static fields
    for (field in fields) {
        if (field.hasModifier(JavaModifier.STATIC)) continue
        codeWriter.newLineIfNot(firstMember)
        field.emit(codeWriter, kind.implicitFieldModifiers)
        firstMember = false
    }

    // Initializer block
    if (!initializerBlock.isEmpty) {
        if (!isRecord) {
            codeWriter.newLineIfNot(firstMember)
            initializerBlock.emit(codeWriter)
        } else {
            //


        }
        firstMember = false
    }

    // Constructors
    for (method in methods) {
        if (!method.isConstructor) continue
        codeWriter.newLineIfNot(firstMember)
        method.emit(codeWriter, name, kind.implicitMethodModifiers)
        firstMember = false
    }

    // Methods
    for (method in methods) {
        if (method.isConstructor) continue
        codeWriter.newLineIfNot(firstMember)
        method.emit(codeWriter, null, kind.implicitMethodModifiers)
        firstMember = false
    }

    // Types
    for (type in subtypes) {
        codeWriter.newLineIfNot(firstMember)
        type.emit(codeWriter, kind.implicitFieldModifiers)
        firstMember = false
    }

    codeWriter.unindent()
    codeWriter.popType()
}


internal fun JavaTypeSpec.toVirtualTypeSpec(name: String) =
    JavaSimpleTypeSpecImpl(
        name = name,
        kind = kind,
        javadoc = javadoc,
        annotations = emptyList(),
        modifiers = emptySet(),
        typeVariables = emptyList(),
        superclass = null,
        superinterfaces = emptyList(),
        fields = emptyList(),
        staticBlock = staticBlock,
        initializerBlock = initializerBlock,
        methods = emptyList(),
        subtypes = emptyList(),
    )
