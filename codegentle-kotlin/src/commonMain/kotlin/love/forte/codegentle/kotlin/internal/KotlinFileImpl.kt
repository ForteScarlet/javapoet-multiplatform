package love.forte.codegentle.kotlin.internal

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.isEmpty
import love.forte.codegentle.common.computeValueIfAbsent
import love.forte.codegentle.common.naming.*
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinFile
import love.forte.codegentle.kotlin.naming.KotlinStdPackageName
import love.forte.codegentle.kotlin.ref.KotlinTypeNameRefStatus
import love.forte.codegentle.kotlin.spec.KotlinContextParameterSpec
import love.forte.codegentle.kotlin.spec.KotlinFunctionSpec
import love.forte.codegentle.kotlin.spec.KotlinPropertySpec
import love.forte.codegentle.kotlin.spec.KotlinTypeSpec
import love.forte.codegentle.kotlin.spec.KotlinValueParameterSpec
import love.forte.codegentle.kotlin.spec.internal.emitTo
import love.forte.codegentle.kotlin.strategy.KotlinWriteStrategy
import love.forte.codegentle.kotlin.strategy.ToStringKotlinWriteStrategy
import love.forte.codegentle.kotlin.writer.KotlinCodeWriter
import love.forte.codegentle.kotlin.writer.inPackage

/**
 * Concrete implementation of the [KotlinFile] interface
 */
internal class KotlinFileImpl(
    override val fileComment: CodeValue,
    override val packageName: PackageName,
    override val types: List<KotlinTypeSpec>,
    override val skipKotlinImports: Boolean,
    override val staticImports: Set<String>,
    override val alwaysQualify: Set<String>,
    override val indent: String
) : KotlinFile {
    override fun writeTo(out: Appendable, strategy: KotlinWriteStrategy) {
        // Step 1: Collect types that need to be imported
        val suggestedImports = linkedMapOf<String, ClassName>()
        val classImportVisitor = ClassImportVisitor(alwaysQualify, suggestedImports)

        // 遍历所有类型收集导入
        for (typeSpec in types) {
            classImportVisitor.visitTypeSpec(typeSpec)
        }

        // 创建代码写入器
        val codeWriter = KotlinCodeWriter.create(
            strategy = strategy,
            out = out,
            indent = indent,
            importedTypes = suggestedImports,
            staticImports = staticImports,
            alwaysQualify = alwaysQualify
        )

        // 发射代码
        emit(codeWriter)
    }

    override fun emit(codeWriter: KotlinCodeWriter) {
        codeWriter.inPackage(packageName) {
            if (!fileComment.isEmpty) {
                codeWriter.emitComment(fileComment)
            }

            if (packageName.isNotEmpty) {
                codeWriter.emit("package $packageName")
                codeWriter.emit("\n\n")
            }

            if (staticImports.isNotEmpty()) {
                for (signature in staticImports) {
                    codeWriter.emit("import $signature")
                    codeWriter.emit("\n")
                }
                codeWriter.emit("\n")
            }

            var importedTypesCount = 0
            val importedTypeSet = LinkedHashSet(codeWriter.importedTypes.values)

            for (className in importedTypeSet) {
                // 跳过 kotlin.* 包的导入
                if (skipKotlinImports
                    && className.packageName == KotlinStdPackageName
                    && !alwaysQualify.contains(className.simpleName)
                ) {
                    continue
                }

                // 检查是否为静态导入
                if (className.enclosingClassName != null) {
                    codeWriter.emit("import ${className.canonicalName}")
                    codeWriter.emit("\n")
                } else {
                    codeWriter.emit("import ${className.canonicalName}")
                    codeWriter.emit("\n")
                }
                importedTypesCount++
            }

            if (importedTypesCount > 0) {
                codeWriter.emit("\n")
            }

            // 发射所有类型
            var first = true
            for (typeSpec in types) {
                if (!first) {
                    codeWriter.emit("\n\n")
                }
                // 使用 emitTo 扩展函数
                typeSpec.emitTo(codeWriter)
                first = false
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is KotlinFileImpl) return false

        if (skipKotlinImports != other.skipKotlinImports) return false
        if (fileComment != other.fileComment) return false
        if (packageName != other.packageName) return false
        if (types != other.types) return false
        if (staticImports != other.staticImports) return false
        if (alwaysQualify != other.alwaysQualify) return false
        if (indent != other.indent) return false

        return true
    }

    override fun hashCode(): Int {
        var result = skipKotlinImports.hashCode()
        result = 31 * result + fileComment.hashCode()
        result = 31 * result + packageName.hashCode()
        result = 31 * result + types.hashCode()
        result = 31 * result + staticImports.hashCode()
        result = 31 * result + alwaysQualify.hashCode()
        result = 31 * result + indent.hashCode()
        return result
    }

    override fun toString(): String {
        return buildString {
            writeTo(this, ToStringKotlinWriteStrategy)
        }
    }
}

/**
 * 用于收集需要导入的类型的访问器
 */
private class ClassImportVisitor(
    val alwaysQualify: Set<String>,
    val importableTypes: LinkedHashMap<String, ClassName>
) {
    fun visitTypeSpec(type: KotlinTypeSpec) {
        // 父类和接口
        type.superclass?.also { visitTypeName(it) }
        type.superinterfaces.forEach { visitTypeName(it) }

        // 注解
        for (ref in type.annotations) {
            visitAnnotationRef(ref)
        }

        // 类型变量
        for (ref in type.typeVariables) {
            visitTypeRef(ref)
        }

        // 代码值
        visitCodeValue(type.initializerBlock)
        visitCodeValue(type.kDoc)

        // 属性
        for (property in type.properties) {
            visitPropertySpec(property)
        }

        // 函数
        for (function in type.functions) {
            visitFunctionSpec(function)
        }

        // 子类型
        for (subtype in type.subtypes) {
            visitTypeSpec(subtype)
        }
    }

    fun visitFunctionSpec(function: KotlinFunctionSpec) {
        // 注解
        for (ref in function.annotations) {
            visitAnnotationRef(ref)
        }

        // 代码值
        visitCodeValue(function.code)
        visitCodeValue(function.kDoc)

        // 类型变量
        for (ref in function.typeVariables) {
            visitTypeRef(ref)
        }

        // 返回类型
        function.returnType.also { visitTypeRef(it) }

        // 参数
        for (parameter in function.parameters) {
            visitValueParameterSpec(parameter)
        }

        // 上下文参数
        for (contextParam in function.contextParameters) {
            visitContextParameterSpec(contextParam)
        }

        // 扩展接收者
        function.receiver?.also { visitTypeRef(it) }
    }

    fun visitPropertySpec(property: KotlinPropertySpec) {
        // 类型
        property.typeRef.also { visitTypeRef(it) }

        // 注解
        for (annotationRef in property.annotations) {
            visitAnnotationRef(annotationRef)
        }

        // 代码
        property.initializer?.also { visitCodeValue(it) }
        visitCodeValue(property.kDoc)
    }

    fun visitValueParameterSpec(parameter: KotlinValueParameterSpec) {
        // 类型
        visitTypeRef(parameter.typeRef)

        // 注解
        for (annotationRef in parameter.annotations) {
            visitAnnotationRef(annotationRef)
        }

        // 代码
        parameter.defaultValue?.also { visitCodeValue(it) }
    }

    fun visitContextParameterSpec(parameter: KotlinContextParameterSpec) {
        // 类型
        visitTypeRef(parameter.typeRef)
    }

    fun visitCodeValue(codeValue: CodeValue) {
        for (part in codeValue.parts) {
            when (part) {
                is CodeArgumentPart.Type -> visitTypeName(part.type)
                is CodeArgumentPart.TypeRef -> visitTypeRef(part.type)
                is CodeArgumentPart.OtherCodeValue -> visitCodeValue(part.value)
                is CodeArgumentPart.Literal -> {
                    val value = part.value
                    when (value) {
                        is KotlinTypeSpec -> visitTypeSpec(value)
                        is KotlinFunctionSpec -> visitFunctionSpec(value)
                        is KotlinPropertySpec -> visitPropertySpec(value)
                        is KotlinValueParameterSpec -> visitValueParameterSpec(value)
                        is AnnotationRef -> visitAnnotationRef(value)
                    }
                }
                else -> {
                    // Do nothing.
                }
            }
        }
    }

    fun visitAnnotationRef(annotationRef: AnnotationRef) {
        visitTypeName(annotationRef.typeName)
        for (memberValue in annotationRef.members.values) {
            for (value in memberValue) {
                visitCodeValue(value)
            }
        }
    }

    fun visitTypeRef(typeRef: TypeRef<*>) {
        (typeRef.status as? KotlinTypeNameRefStatus)?.also { status ->
            for (annotationRef in status.annotations) {
                visitAnnotationRef(annotationRef)
            }
        }
        visitTypeName(typeRef.typeName)
    }

    fun visitTypeName(typeName: TypeName) {
        when (typeName) {
            is ClassName -> importable(typeName)
            is ParameterizedTypeName -> importable(typeName.rawType)
            is TypeVariableName -> {
                for (ref in typeName.bounds) {
                    visitTypeRef(ref)
                }
            }
            is ArrayTypeName -> {
                visitTypeRef(typeName.componentType)
            }
            is WildcardTypeName -> {
                for (ref in typeName.bounds) {
                    visitTypeRef(ref)
                }
            }
        }
    }

    private fun importable(className: ClassName) {
        val packageName = className.packageName
        if (packageName == null || packageName.isEmpty) {
            // null, or is empty.
            return
        } else if (alwaysQualify.contains(className.simpleName)) {
            return
        }
        importableTypes.computeValueIfAbsent(className.simpleName) { className }
    }
}
