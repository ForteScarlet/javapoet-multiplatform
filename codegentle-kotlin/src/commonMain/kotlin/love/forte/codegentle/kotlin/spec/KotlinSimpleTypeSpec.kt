package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.spec.internal.KotlinSimpleTypeSpecBuilderImpl

/**
 * 表示一个简单的 Kotlin 类型规范，如类、接口等。
 *
 * @property kind 类型的种类
 * @property name 类型的名称
 */
@SubclassOptInRequired(CodeGentleKotlinSpecImplementation::class)
public interface KotlinSimpleTypeSpec : KotlinTypeSpec {
    // TODO 类型拆分开
    // TODO SimpleType 支持:
    //  - 普通的class
    //  - data class

    /**
     * 创建一个简单的 Kotlin 类型规范的构建器。
     *
     * @param kind 类型的种类
     * @param name 类型的名称
     * @return 新的构建器实例
     */
    public companion object {
        /**
         * Create a builder for a simple Kotlin type spec.
         *
         * @param kind the type kind
         * @param name the type name
         * @return a new builder
         */
        public fun builder(
            kind: KotlinTypeSpec.Kind,
            name: String
        ): Builder {
            return KotlinSimpleTypeSpecBuilderImpl(kind, name)
        }
    }

    /**
     * 简单 Kotlin 类型规范的构建器。
     */
    public interface Builder {
        /**
         * 类型的种类。
         */
        public val kind: KotlinTypeSpec.Kind

        /**
         * 类型的名称。
         */
        public val name: String

        /**
         * 添加KDoc。
         */
        public fun addKDoc(codeValue: CodeValue): Builder

        /**
         * 添加KDoc。
         */
        public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): Builder

        /**
         * 设置父类。
         */
        public fun superclass(superclass: TypeName): Builder

        /**
         * 添加初始化块。
         */
        public fun addInitializerBlock(codeValue: CodeValue): Builder

        /**
         * 添加初始化块。
         */
        public fun addInitializerBlock(format: String, vararg argumentParts: CodeArgumentPart): Builder

        /**
         * 添加注解引用。
         */
        public fun addAnnotationRef(ref: AnnotationRef): Builder

        /**
         * 添加多个注解引用。
         */
        public fun addAnnotationRefs(refs: Iterable<AnnotationRef>): Builder

        /**
         * 添加多个修饰符。
         */
        public fun addModifiers(vararg modifiers: KotlinModifier): Builder

        /**
         * 添加多个修饰符。
         */
        public fun addModifiers(modifiers: Iterable<KotlinModifier>): Builder

        /**
         * 添加修饰符。
         */
        public fun addModifier(modifier: KotlinModifier): Builder

        /**
         * 添加多个类型变量引用。
         */
        public fun addTypeVariableRefs(vararg typeVariables: TypeRef<TypeVariableName>): Builder

        /**
         * 添加多个类型变量引用。
         */
        public fun addTypeVariableRefs(typeVariables: Iterable<TypeRef<TypeVariableName>>): Builder

        /**
         * 添加类型变量引用。
         */
        public fun addTypeVariableRef(typeVariable: TypeRef<TypeVariableName>): Builder

        /**
         * 添加多个父接口。
         */
        public fun addSuperinterfaces(vararg superinterfaces: TypeName): Builder

        /**
         * 添加多个父接口。
         */
        public fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): Builder

        /**
         * 添加父接口。
         */
        public fun addSuperinterface(superinterface: TypeName): Builder

        /**
         * 添加多个属性。
         */
        public fun addProperties(vararg properties: KotlinPropertySpec): Builder

        /**
         * 添加多个属性。
         */
        public fun addProperties(properties: Iterable<KotlinPropertySpec>): Builder

        /**
         * 添加属性。
         */
        public fun addProperty(property: KotlinPropertySpec): Builder

        /**
         * 添加多个函数。
         */
        public fun addFunctions(functions: Iterable<KotlinFunctionSpec>): Builder

        /**
         * 添加多个函数。
         */
        public fun addFunctions(vararg functions: KotlinFunctionSpec): Builder

        /**
         * 添加函数。
         */
        public fun addFunction(function: KotlinFunctionSpec): Builder

        /**
         * 添加多个子类型。
         */
        public fun addSubtypes(types: Iterable<KotlinTypeSpec>): Builder

        /**
         * 添加多个子类型。
         */
        public fun addSubtypes(vararg types: KotlinTypeSpec): Builder

        /**
         * 添加子类型。
         */
        public fun addSubtype(type: KotlinTypeSpec): Builder

        /**
         * 构建 [KotlinSimpleTypeSpec] 实例。
         *
         * @return 新的 [KotlinSimpleTypeSpec] 实例
         */
        public fun build(): KotlinSimpleTypeSpec
    }
}

public inline fun KotlinSimpleTypeSpec.Builder.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinSimpleTypeSpec.Builder = addKDoc(CodeValue(format, block))

public inline fun KotlinSimpleTypeSpec.Builder.addInitializerBlock(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): KotlinSimpleTypeSpec.Builder = addInitializerBlock(CodeValue(format, block))

public inline fun KotlinSimpleTypeSpec.Builder.addProperty(
    name: String,
    type: TypeRef<*>,
    block: KotlinPropertySpec.Builder.() -> Unit = {}
): KotlinSimpleTypeSpec.Builder = addProperty(KotlinPropertySpec(name, type, block))

public inline fun KotlinSimpleTypeSpec.Builder.addFunction(
    name: String,
    type: TypeRef<*>,
    block: KotlinFunctionSpec.Builder.() -> Unit = {}
): KotlinSimpleTypeSpec.Builder = addFunction(KotlinFunctionSpec(name, type, block))

