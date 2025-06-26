package love.forte.codegentle.kotlin.spec

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.naming.TypeVariableName
import love.forte.codegentle.common.ref.AnnotationRef
import love.forte.codegentle.common.ref.AnnotationRefCollectable
import love.forte.codegentle.common.ref.TypeRef
import love.forte.codegentle.kotlin.KotlinModifier
import love.forte.codegentle.kotlin.KotlinModifierBuilderContainer
import love.forte.codegentle.kotlin.KotlinModifierContainer
import love.forte.codegentle.kotlin.MutableKotlinModifierSet

/**
 * A Kotlin type.
 */
public sealed interface KotlinTypeSpec : KotlinSpec, KotlinModifierContainer {
    /**
     * 获取类型的种类。
     *
     * @return 类型的种类
     */
    public val kind: Kind

    /**
     * 获取类型的名称。
     *
     * @return 类型的名称
     */
    public val name: String

    public val kDoc: CodeValue
    public val annotations: List<AnnotationRef>
    override val modifiers: Set<KotlinModifier>
    public val typeVariables: List<TypeRef<TypeVariableName>>

    // super class:
    //  `extends` One if is class,
    //  Nothing if is enum, annotation, interface.

    /**
     * Super class.
     *
     * @see love.forte.codegentle.kotlin.naming.KotlinDelegatedClassName
     */
    public val superclass: TypeName?

    /**
     * Super interfaces.
     *
     * @see love.forte.codegentle.kotlin.naming.KotlinDelegatedClassName
     */
    public val superinterfaces: List<TypeName>

    public val properties: List<KotlinPropertySpec>

    public val initializerBlock: CodeValue

    public val functions: List<KotlinFunctionSpec>

    public val subtypes: List<KotlinTypeSpec>

    public enum class Kind {
        /**
         * 类
         */
        CLASS,

        /**
         * 接口
         */
        INTERFACE,

        /**
         * 对象
         */
        OBJECT,

        /**
         * 伴生对象
         */
        COMPANION_OBJECT,

        /**
         * 枚举类
         */
        ENUM,

        /**
         * 注解类
         */
        ANNOTATION,

        /**
         * 密封类
         */
        SEALED,

        /**
         * 数据类
         */
        DATA,

        /**
         * 值类
         */
        VALUE,

        /**
         * 函数式接口
         */
        FUN_INTERFACE,

        /**
         * 类型别名
         */
        TYPE_ALIAS
    }

    public companion object {
        /**
         * Create a builder for a class.
         *
         * @param name the class name
         * @return a new builder
         */
        public fun classBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.CLASS, name)
        }

        /**
         * Create a builder for an interface.
         *
         * @param name the interface name
         * @return a new builder
         */
        public fun interfaceBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.INTERFACE, name)
        }

        /**
         * Create a builder for an object.
         *
         * @param name the object name
         * @return a new builder
         */
        public fun objectBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.OBJECT, name)
        }

        /**
         * Create a builder for a companion object.
         *
         * @return a new builder
         */
        public fun companionObjectBuilder(): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.COMPANION_OBJECT, "Companion")
        }

        /**
         * Create a builder for an enum class.
         *
         * @param name the enum class name
         * @return a new builder
         */
        public fun enumBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.ENUM, name)
        }

        /**
         * Create a builder for an annotation class.
         *
         * @param name the annotation class name
         * @return a new builder
         */
        public fun annotationBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.ANNOTATION, name)
        }

        /**
         * Create a builder for a sealed class.
         *
         * @param name the sealed class name
         * @return a new builder
         */
        public fun sealedClassBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.SEALED, name)
        }

        /**
         * Create a builder for a data class.
         *
         * @param name the data class name
         * @return a new builder
         */
        public fun dataClassBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.DATA, name)
        }

        /**
         * Create a builder for a value class.
         *
         * @param name the value class name
         * @return a new builder
         */
        public fun valueClassBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.VALUE, name)
        }

        /**
         * Create a builder for a functional interface.
         *
         * @param name the functional interface name
         * @return a new builder
         */
        public fun funInterfaceBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.FUN_INTERFACE, name)
        }

        /**
         * Create a builder for a type alias.
         *
         * @param name the type alias name
         * @return a new builder
         */
        public fun typeAliasBuilder(name: String): KotlinSimpleTypeSpec.Builder {
            return KotlinSimpleTypeSpec.builder(Kind.TYPE_ALIAS, name)
        }
    }
}

public sealed class KotlinTypeSpecBuilder<B : KotlinTypeSpecBuilder<B, T>, T : KotlinTypeSpec>(
    public val kind: KotlinTypeSpec.Kind,
    public val name: String?,
) : KotlinModifierBuilderContainer,
    AnnotationRefCollectable<B>,
    BuilderDsl {

    internal val kDoc = CodeValue.builder()
    internal var superclass: TypeName? = null
    internal val initializerBlock = CodeValue.builder()

    internal val annotationRefs: MutableList<AnnotationRef> = mutableListOf()
    internal val modifierSet = MutableKotlinModifierSet.empty()
    internal val typeVariableRefs: MutableList<TypeRef<TypeVariableName>> = mutableListOf()
    internal val superinterfaces: MutableList<TypeName> = mutableListOf()
    internal val properties: MutableList<KotlinPropertySpec> = mutableListOf()
    internal val functions: MutableList<KotlinFunctionSpec> = mutableListOf()
    internal val subtypes: MutableList<KotlinTypeSpec> = mutableListOf()

    protected abstract val self: B

    public fun addKDoc(codeValue: CodeValue): B = self.apply {
        kDoc.add(codeValue)
    }

    public fun addKDoc(format: String, vararg argumentParts: CodeArgumentPart): B = self.apply {
        kDoc.add(format, *argumentParts)
    }

    public fun superclass(superclass: TypeName): B = self.apply {
        this.superclass = superclass
    }

    public fun addInitializerBlock(codeValue: CodeValue): B = self.apply {
        this.initializerBlock.add(codeValue)
    }

    public fun addInitializerBlock(format: String, vararg argumentParts: CodeArgumentPart): B = self.apply {
        this.initializerBlock.add(format, *argumentParts)
    }

    override fun addAnnotationRef(ref: AnnotationRef): B = self.apply {
        annotationRefs.add(ref)
    }

    override fun addAnnotationRefs(refs: Iterable<AnnotationRef>): B = self.apply {
        annotationRefs.addAll(refs)
    }

    override fun addModifiers(vararg modifiers: KotlinModifier): B = self.apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifiers(modifiers: Iterable<KotlinModifier>): B = self.apply {
        this.modifierSet.addAll(modifiers)
    }

    override fun addModifier(modifier: KotlinModifier): B = self.apply {
        this.modifierSet.add(modifier)
    }

    public fun addTypeVariableRefs(vararg typeVariables: TypeRef<TypeVariableName>): B = self.apply {
        this.typeVariableRefs.addAll(typeVariables)
    }

    public fun addTypeVariableRefs(typeVariables: Iterable<TypeRef<TypeVariableName>>): B = self.apply {
        this.typeVariableRefs.addAll(typeVariables)
    }

    public fun addTypeVariableRef(typeVariable: TypeRef<TypeVariableName>): B = self.apply {
        this.typeVariableRefs.add(typeVariable)
    }

    public fun addSuperinterfaces(vararg superinterfaces: TypeName): B = self.apply {
        this.superinterfaces.addAll(superinterfaces)
    }

    public fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): B = self.apply {
        this.superinterfaces.addAll(superinterfaces)
    }

    public fun addSuperinterface(superinterface: TypeName): B = self.apply {
        this.superinterfaces.add(superinterface)
    }

    public fun addProperties(vararg properties: KotlinPropertySpec): B = self.apply {
        this.properties.addAll(properties)
    }

    public fun addProperties(properties: Iterable<KotlinPropertySpec>): B = self.apply {
        this.properties.addAll(properties)
    }

    public fun addProperty(property: KotlinPropertySpec): B = self.apply {
        this.properties.add(property)
    }

    public fun addFunctions(functions: Iterable<KotlinFunctionSpec>): B = self.apply {
        this.functions.addAll(functions)
    }

    public fun addFunctions(vararg functions: KotlinFunctionSpec): B = self.apply {
        this.functions.addAll(functions)
    }

    public fun addFunction(function: KotlinFunctionSpec): B = self.apply {
        this.functions.add(function)
    }

    public fun addSubtypes(types: Iterable<KotlinTypeSpec>): B = self.apply {
        this.subtypes.addAll(types)
    }

    public fun addSubtypes(vararg types: KotlinTypeSpec): B = self.apply {
        this.subtypes.addAll(types)
    }

    public fun addSubtype(type: KotlinTypeSpec): B = self.apply {
        this.subtypes.add(type)
    }

    public abstract fun build(): T
}

// extensions

public inline fun KotlinTypeSpecBuilder<*, *>.addKDoc(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
) {
    addKDoc(CodeValue(format, block))
}

public inline fun KotlinTypeSpecBuilder<*, *>.addInitializerBlock(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
) {
    addInitializerBlock(CodeValue(format, block))
}

public inline fun KotlinTypeSpecBuilder<*, *>.addProperty(
    name: String,
    type: TypeRef<*>,
    block: KotlinPropertySpecBuilder.() -> Unit = {}
) {
    addProperty(KotlinPropertySpec.builder(name, type).apply(block).build())
}

/**
 * Add a function
 */
public inline fun KotlinTypeSpecBuilder<*, *>.addFunction(
    name: String,
    type: TypeRef<*>,
    block: KotlinFunctionSpecBuilder.() -> Unit = {}
) {
    addFunction(KotlinFunctionSpec(name, type, block))
}
