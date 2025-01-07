package love.forte.codepoet.java.internal

import love.forte.codepoet.java.*

internal inline fun TypeSpec.doEmit(
    codeWriter: CodeWriter,
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

private fun CodeWriter.newLineIfNot(condition: Boolean) {
    if (!condition) {
        emit("\n")
    }
}

internal fun TypeSpec.emitSupers(codeWriter: CodeWriter) {
    val extendsTypes: List<TypeName>
    val implementsTypes: List<TypeName>

    if (kind == TypeSpec.Kind.INTERFACE) {
        extendsTypes = superinterfaces
        implementsTypes = emptyList()
    } else {
        extendsTypes = superclass?.let {
            if (it != ClassName.Builtins.OBJECT) {
                listOf(it)
            } else null
        } ?: emptyList()

        implementsTypes = superinterfaces
    }

    codeWriter.emitExtends(extendsTypes)
    codeWriter.emitImplements(implementsTypes)
}

internal fun CodeWriter.emitExtends(extendsTypes: List<TypeName>) {
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

internal fun CodeWriter.emitImplements(implementsTypes: List<TypeName>) {
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

internal inline fun TypeSpec.emitMembers(
    codeWriter: CodeWriter,
    isRecord: Boolean = false,
    block: (firstMember: Boolean, notFirst: () -> Unit) -> Unit = { _, _ -> }
) {
    codeWriter.pushType(this)
    codeWriter.indent()
    var firstMember = true

    block(firstMember) { firstMember = false }

    // Static fields
    for (field in fields) {
        if (!field.hasModifier(Modifier.STATIC)) continue
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
        if (field.hasModifier(Modifier.STATIC)) continue
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
    for (type in types) {
        codeWriter.newLineIfNot(firstMember)
        type.emit(codeWriter, kind.implicitFieldModifiers)
        firstMember = false
    }

    codeWriter.unindent()
    codeWriter.popType()
}


internal fun TypeSpec.toVirtualTypeSpec(name: String) =
    SimpleTypeSpecImpl(
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
        types = emptyList(),
    )
