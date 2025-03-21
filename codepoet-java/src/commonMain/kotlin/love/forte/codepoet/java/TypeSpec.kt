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

@file:JvmName("TypeSpecs")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.java.TypeSpec.Kind
import love.forte.codepoet.java.internal.*
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 * A generated class, interface, or enum declaration.
 *
 * @see SimpleTypeSpec
 * @see AnnotationTypeSpec
 * @see EnumTypeSpec
 * @see NonSealedTypeSpec
 * @see SealedTypeSpec
 * @see RecordTypeSpec
 *
 * @author ForteScarlet
 */
public sealed interface TypeSpec : CodeEmitter {
    public val name: String?
    public val kind: Kind
    public val javadoc: CodeValue
    public val annotations: List<AnnotationSpec>
    public val modifiers: Set<Modifier>

    public fun hasModifier(modifier: Modifier): Boolean = modifier in modifiers

    public val typeVariables: List<TypeVariableName>

    // TODO super class:
    //  `extends` One if is class,
    //  Nothing if is record, enum, annotation, interface.

    public val superclass: TypeName?


    // TODO super interfaces:
    //  `extends` if is interface,
    //  `implements` others.

    public val superinterfaces: List<TypeName>

    public val fields: List<FieldSpec>

    public val staticBlock: CodeValue

    public val initializerBlock: CodeValue

    public val methods: List<MethodSpec>

    // subtypes
    public val types: List<TypeSpec>

    // val nestedTypesSimpleNames: Set<String>? = null
    // val alwaysQualifiedNames: Set<String>? = null

    override fun emit(codeWriter: CodeWriter) {
        emit(codeWriter, emptySet())
    }

    public fun emit(codeWriter: CodeWriter, implicitModifiers: Set<Modifier> = emptySet())

    /**
     * Type kind
     */
    public enum class Kind(
        internal val states: States = States(0),
        internal val implicitFieldModifiers: Set<Modifier> = emptySet(),
        internal val implicitMethodModifiers: Set<Modifier> = emptySet(),
        internal val implicitTypeModifiers: Set<Modifier> = emptySet(),
        internal val asMemberModifiers: Set<Modifier> = emptySet(),
    ) {
        CLASS(
            states = States(
                State.SUPERCLASS_SUPPORT,
                State.SUPERINTERFACES_SUPPORT,
            )
        ),
        INTERFACE(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            ),
            implicitFieldModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
            implicitMethodModifiers = setOf(Modifier.PUBLIC, Modifier.ABSTRACT),
            implicitTypeModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC),
            asMemberModifiers = setOf(Modifier.STATIC),
        ),
        ENUM(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            ),
            asMemberModifiers = setOf(Modifier.STATIC),
        ),
        ANNOTATION(
            states = States(),
            implicitFieldModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
            implicitMethodModifiers = setOf(Modifier.PUBLIC, Modifier.ABSTRACT),
            implicitTypeModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC),
            asMemberModifiers = setOf(Modifier.STATIC),
        ),
        RECORD(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            )
        ),

        // abstract sealed class Vehicle permits Car, Truck
        SEALED_CLASS(
            states = States(
                State.SUPERCLASS_SUPPORT,
                State.SUPERINTERFACES_SUPPORT
            )
        ),

        // non-sealed class Car extends Vehicle implements Service
        NON_SEALED_CLASS(
            states = States(
                State.SUPERCLASS_SUPPORT
            )
        ),

        // sealed interface Service permits Car, Truck
        SEALED_INTERFACE(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            ),
            implicitFieldModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
            implicitMethodModifiers = setOf(Modifier.PUBLIC, Modifier.ABSTRACT),
            implicitTypeModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC),
            asMemberModifiers = setOf(Modifier.STATIC),
        ),

        // non-sealed interface Service permits Car, Truck
        NON_SEALED_INTERFACE(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            ),
            implicitFieldModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC, Modifier.FINAL),
            implicitMethodModifiers = setOf(Modifier.PUBLIC, Modifier.ABSTRACT),
            implicitTypeModifiers = setOf(Modifier.PUBLIC, Modifier.STATIC),
            asMemberModifiers = setOf(Modifier.STATIC),
        );

        @JvmInline
        internal value class States(private val bits: Int) {
            constructor(vararg states: State) : this(
                states.fold(0) { acc, state -> acc or state.bit }
            )

            operator fun contains(state: State): Boolean =
                bits and state.bit != 0

            inline val superclassSupport: Boolean
                get() = bits and State.SUPERCLASS_SUPPORT.bit != 0

            inline val superinterfacesSupport: Boolean
                get() = bits and State.SUPERINTERFACES_SUPPORT.bit != 0
        }

        internal enum class State {
            SUPERCLASS_SUPPORT,
            SUPERINTERFACES_SUPPORT,
            ;

            val bit: Int get() = 1 shl ordinal
        }
    }


    /**
     * @see SimpleTypeSpec.Builder
     * @see AnnotationTypeSpec.Builder
     * @see EnumTypeSpec.Builder
     * @see NonSealedTypeSpec.Builder
     * @see SealedTypeSpec.Builder
     * @see RecordTypeSpec.Builder
     */
    public sealed class Builder<B : Builder<B, T>, T : TypeSpec>(
        public val kind: Kind,
        public val name: String?,
    ) : ModifierBuilderContainer {
        init {
            check(name == null || name.isSourceName()) { "Invalid `name`: $name" }
        }

        internal val javadoc = CodeValue.builder()
        internal var superclass: TypeName? = null
        internal val staticBlock = CodeValue.builder()
        internal val initializerBlock = CodeValue.builder()

        public val annotations: MutableList<AnnotationSpec> = mutableListOf()
        public val modifiers: MutableSet<Modifier> = linkedSetOf()
        public val typeVariables: MutableList<TypeVariableName> = mutableListOf()
        public val superinterfaces: MutableList<TypeName> = mutableListOf()
        public val fields: MutableList<FieldSpec> = mutableListOf()
        public val methods: MutableList<MethodSpec> = mutableListOf()
        public val types: MutableList<TypeSpec> = mutableListOf()

        protected abstract val self: B

        public fun addJavadoc(codeValue: CodeValue): B = self.apply {
            javadoc.add(codeValue)
        }

        public fun superclass(superclass: TypeName): B = self.apply {
            check(kind.states.superclassSupport) { "`superclass` is not supported for kind $kind" }
            require(!superclass.isPrimitive) { "`superclass` can't be primitive." }
            this.superclass = superclass
        }

        public fun addStaticBlock(codeValue: CodeValue): B = self.apply {
            this.staticBlock.add(codeValue)
        }

        public fun addInitializerBlock(codeValue: CodeValue): B = self.apply {
            this.initializerBlock.add(codeValue)
        }

        public fun addAnnotations(annotations: Iterable<AnnotationSpec>): B = self.apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotations(vararg annotations: AnnotationSpec): B = self.apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotation(annotation: AnnotationSpec): B = self.apply {
            this.annotations.add(annotation)
        }

        public fun addAnnotation(annotation: ClassName): B =
            addAnnotation(AnnotationSpec(annotation))

        override fun addModifiers(vararg modifiers: Modifier): B = self.apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifiers(modifiers: Iterable<Modifier>): B = self.apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifier(modifier: Modifier): B = self.apply {
            this.modifiers.add(modifier)
        }

        public fun addTypeVariables(vararg typeVariables: TypeVariableName): B = self.apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addTypeVariables(typeVariables: Iterable<TypeVariableName>): B = self.apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addTypeVariable(typeVariable: TypeVariableName): B = self.apply {
            this.typeVariables.add(typeVariable)
        }

        public fun addSuperinterfaces(vararg superinterfaces: TypeName): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.addAll(superinterfaces)
        }

        public fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.addAll(superinterfaces)
        }

        public fun addSuperinterface(superinterface: TypeName): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.add(superinterface)
        }

        private fun checkSuperinterfaceSupport() {
            check(kind.states.superinterfacesSupport) { "`superinterface` is not supported for kind $kind" }
        }

        public fun addFields(vararg fields: FieldSpec): B = self.apply {
            this.fields.addAll(fields)
        }

        public fun addFields(fields: Iterable<FieldSpec>): B = self.apply {
            this.fields.addAll(fields)
        }

        public fun addField(field: FieldSpec): B = self.apply {
            this.fields.add(field)
        }

        // TODO check field?

        public fun addMethods(methods: Iterable<MethodSpec>): B = self.apply {
            this.methods.addAll(methods)
        }

        public fun addMethods(vararg methods: MethodSpec): B = self.apply {
            this.methods.addAll(methods)
        }

        public fun addMethod(method: MethodSpec): B = self.apply {
            this.methods.add(method)
        }

        public fun addTypes(types: Iterable<TypeSpec>): B = self.apply {
            this.types.addAll(types)
        }

        public fun addTypes(vararg types: TypeSpec): B = self.apply {
            this.types.addAll(types)
        }

        public fun addType(type: TypeSpec): B = self.apply {
            this.types.add(type)
        }


        public abstract fun build(): T
    }

    public companion object
}

public val TypeSpec.nestedTypesSimpleNames: Set<String>
    get() = types.mapTo(linkedSetOf()) { it.name!! }

/**
 * A generated `class` or `interface`.
 */
public interface SimpleTypeSpec : TypeSpec {
    override val name: String

    public class Builder(
        kind: Kind,
        name: String,
    ) : TypeSpec.Builder<Builder, SimpleTypeSpec>(kind, name) {
        init {
            require(kind == Kind.CLASS || kind == Kind.INTERFACE) {
                "Invalid simple type `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this


        override fun build(): SimpleTypeSpec {
            return SimpleTypeSpecImpl(
                name = name!!,
                kind = kind,
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                superclass = superclass,
                superinterfaces = superinterfaces.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}

/**
 * A generated anonymous class.
 * ```java
 * new java.lang.Object() {
 * }
 * ////
 * new HashMap<String, String>(1) {
 * // `anonymousTypeArguments` 👆
 * }
 * ```
 *
 * Also used in enum constants, see [EnumTypeSpec.enumConstants].
 *
 */
public interface AnonymousClassTypeSpec : TypeSpec {
    override val name: String?
        get() = null

    public val anonymousTypeArguments: CodeValue

    override fun emit(codeWriter: CodeWriter, implicitModifiers: Set<Modifier>) {
        emit(codeWriter, null, implicitModifiers)
    }

    public fun emit(codeWriter: CodeWriter, enumName: String? = null, implicitModifiers: Set<Modifier> = emptySet())

    public class Builder(
        public val anonymousTypeArguments: CodeValue,
    ) : TypeSpec.Builder<Builder, AnonymousClassTypeSpec>(Kind.CLASS, null) {
        override val self: Builder
            get() = this

        override fun build(): AnonymousClassTypeSpec {
            return AnonymousClassTypeSpecImpl(
                kind = kind,
                anonymousTypeArguments = anonymousTypeArguments,
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                superclass = superclass,
                superinterfaces = superinterfaces.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}

// Class, interface

/**
 * A generated annotation type.
 * ```java
 * public @interface Anno {
 * }
 * ```
 */
public interface AnnotationTypeSpec : TypeSpec {
    override val name: String

    override val superclass: TypeName?
        get() = null

    override val superinterfaces: List<TypeName>
        get() = emptyList()

    public class Builder(
        name: String,
    ) : TypeSpec.Builder<Builder, AnnotationTypeSpec>(Kind.ANNOTATION, name) {

        override val self: Builder
            get() = this

        override fun build(): AnnotationTypeSpec {
            // TODO check method must be public abstract
            // TODO check field must be public static (no private)

            check(superclass == null) {
                "`superclass` must be null for annotation type."
            }

            check(superinterfaces.isEmpty()) {
                "`superinterfaces` must be empty for annotation type."
            }

            return AnnotationTypeSpecImpl(
                name = name!!,
                kind = kind,
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}
// @interface, no fields

/**
 * A generated `enum` type.
 *
 * ```java
 * public enum EnumType {
 * }
 * ```
 *
 */
public interface EnumTypeSpec : TypeSpec {
    override val name: String

    override val superclass: TypeName?
        get() = null

    public val enumConstants: Map<String, TypeSpec>

    public class Builder(
        name: String?,
    ) : TypeSpec.Builder<Builder, EnumTypeSpec>(Kind.ENUM, name) {
        override val self: Builder
            get() = this

        // TODO superclass must be null

        private val enumConstants = linkedMapOf<String, AnonymousClassTypeSpec>()

        public fun addEnumConstant(name: String, type: AnonymousClassTypeSpec): Builder = apply {
            enumConstants[name] = type
        }

        override fun build(): EnumTypeSpec {
            return EnumTypeSpecImpl(
                name = name!!,
                kind = kind,
                enumConstants = enumConstants.toMap(linkedMapOf()),
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                superinterfaces = superinterfaces.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}

/**
 * A generated `non-sealed class/interface`.
 *
 * ```java
 * public non-sealed class NonSealedClass extends SealedType {
 * }
 * ```
 *
 * ```java
 * public non-sealed interface NonSealedInterface extends SealedType {
 * }
 * ```
 */
public interface NonSealedTypeSpec : TypeSpec {
    override val name: String

    public class Builder(
        kind: Kind,
        name: String,
    ) : TypeSpec.Builder<Builder, NonSealedTypeSpec>(kind, name) {
        init {
            check(kind == Kind.NON_SEALED_CLASS || kind == Kind.NON_SEALED_INTERFACE) {
                "Invalid non-sealed `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this

        override fun build(): NonSealedTypeSpec {
            return NonSealedTypeSpecImpl(
                name = name!!,
                kind = kind,
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                superclass = superclass,
                superinterfaces = superinterfaces.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}
// non-sealed class, non-sealed interface

/**
 * A generated `sealed class/interface`.
 */
public interface SealedTypeSpec : TypeSpec {
    override val name: String

    // sealed class, sealed interface
    public val permits: List<TypeName>

    public class Builder(
        kind: Kind,
        name: String,
    ) : TypeSpec.Builder<Builder, SealedTypeSpec>(kind, name) {
        init {
            check(kind == Kind.SEALED_CLASS || kind == Kind.SEALED_INTERFACE) {
                "Invalid sealed `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this

        private val permits = mutableListOf<TypeName>()

        public fun addPermits(permits: Iterable<TypeName>): Builder = apply {
            this.permits.addAll(permits)
        }

        public fun addPermits(vararg permits: TypeName): Builder = apply {
            this.permits.addAll(permits)
        }

        public fun addPermit(permit: TypeName): Builder = apply {
            permits.add(permit)
        }

        override fun build(): SealedTypeSpec {
            return SealedTypeSpecImpl(
                name = name!!,
                kind = kind,
                permits = permits.toList(),
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                superclass = superclass,
                superinterfaces = superinterfaces.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}

/**
 * A generated `record` type.
 *
 * ```java
 * public record RecordClass(int value) {
 * }
 * ```
 */
public interface RecordTypeSpec : TypeSpec {
    override val name: String

    override val superclass: TypeName?
        get() = null

    public val mainConstructorParameters: List<ParameterSpec>

    /*
     * Init constructor:
     *
     * ```java
     * public record Student(String name, int age) {
     *   // initializerBlock
     *   public Student {
     *     require(age > 0, "...")
     *   }
     *
     *   static {  }
     *
     *   // Other constructor
     *   public Student(String name) {
     *     this(name, 24)
     *   }
     * }
     * ```
     */

    public class Builder(
        name: String,
    ) : TypeSpec.Builder<Builder, RecordTypeSpec>(Kind.RECORD, name) {
        override val self: Builder
            get() = this

        private var mainConstructorParameters = mutableListOf<ParameterSpec>()

        public fun addMainConstructorParameter(mainConstructorParameter: ParameterSpec): Builder = apply {
            mainConstructorParameters.add(mainConstructorParameter)
        }

        public fun addMainConstructorParameters(vararg mainConstructorParams: ParameterSpec): Builder = apply {
            mainConstructorParameters.addAll(mainConstructorParams)
        }

        public fun addMainConstructorParameters(mainConstructorParams: Iterable<ParameterSpec>): Builder = apply {
            mainConstructorParameters.addAll(mainConstructorParams)
        }

        override fun build(): RecordTypeSpec {
            return RecordTypeSpecImpl(
                name = name!!,
                kind = kind,
                mainConstructorParameters = mainConstructorParameters.toList(),
                javadoc = javadoc.build(),
                annotations = annotations.toList(),
                modifiers = LinkedHashSet(modifiers),
                typeVariables = typeVariables.toList(),
                superinterfaces = superinterfaces.toList(),
                fields = fields.toList(),
                staticBlock = staticBlock.build(),
                initializerBlock = initializerBlock.build(),
                methods = methods,
                types = types.toList(),
            )
        }
    }
}

// factories

public inline fun SimpleTypeSpec(
    kind: Kind,
    name: String,
    block: SimpleTypeSpec.Builder.() -> Unit = {}
): SimpleTypeSpec {
    return SimpleTypeSpec.Builder(kind, name).also(block).build()
}

public inline fun AnonymousClassTypeSpec(
    anonymousTypeArguments: CodeValue,
    block: AnonymousClassTypeSpec.Builder.() -> Unit = {},
): AnonymousClassTypeSpec {
    return AnonymousClassTypeSpec.Builder(anonymousTypeArguments).also(block).build()
}

public inline fun AnnotationTypeSpec(
    name: String,
    block: AnnotationTypeSpec.Builder.() -> Unit = {},
): AnnotationTypeSpec {
    return AnnotationTypeSpec.Builder(name).also(block).build()
}

public inline fun EnumTypeSpec(
    name: String?,
    block: EnumTypeSpec.Builder.() -> Unit = {},
): EnumTypeSpec {
    return EnumTypeSpec.Builder(name).also(block).build()
}

public inline fun NonSealedTypeSpec(
    kind: Kind,
    name: String,
    block: NonSealedTypeSpec.Builder.() -> Unit = {},
): NonSealedTypeSpec {
    return NonSealedTypeSpec.Builder(kind, name).also(block).build()
}

public inline fun SealedTypeSpec(
    kind: Kind,
    name: String,
    block: SealedTypeSpec.Builder.() -> Unit = {},
): SealedTypeSpec {
    return SealedTypeSpec.Builder(kind, name).also(block).build()
}

public inline fun RecordTypeSpec(
    name: String,
    block: RecordTypeSpec.Builder.() -> Unit = {},
): RecordTypeSpec {
    return RecordTypeSpec.Builder(name).also(block).build()
}

// extensions

public inline fun TypeSpec.Builder<*, *>.addJavadoc(format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    addJavadoc(CodeValue(format, block))
}


public inline fun TypeSpec.Builder<*, *>.addStaticBlock(format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    addStaticBlock(CodeValue(format, block))
}


public inline fun TypeSpec.Builder<*, *>.addInitializerBlock(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
) {
    addInitializerBlock(CodeValue(format, block))
}

public inline fun TypeSpec.Builder<*, *>.addAnnotation(
    annotation: ClassName,
    block: AnnotationSpec.Builder.() -> Unit = {}
) {
    addAnnotation(AnnotationSpec(annotation, block))
}

public inline fun TypeSpec.Builder<*, *>.addField(
    type: TypeName,
    name: String,
    block: FieldSpec.Builder.() -> Unit = {}
) {
    addField(FieldSpec(type, name, block))
}

public inline fun TypeSpec.Builder<*, *>.addMethod(
    name: String,
    block: MethodSpec.Builder.() -> Unit = {}
) {
    addMethod(MethodSpec(name, block))
}

public inline fun TypeSpec.Builder<*, *>.addMethod(
    block: MethodSpec.Builder.() -> Unit = {}
) {
    addMethod(MethodSpec(block))
}

public inline fun EnumTypeSpec.Builder.addEnumConstant(
    name: String,
    anonymousTypeArguments: CodeValue,
    block: AnonymousClassTypeSpec.Builder.() -> Unit = {}
) {
    addEnumConstant(
        name,
        AnonymousClassTypeSpec(anonymousTypeArguments, block)
    )
}

public inline fun RecordTypeSpec.Builder.addMainConstructorParameter(
    type: TypeName,
    name: String,
    block: ParameterSpec.Builder.() -> Unit = {}
) {
    addMainConstructorParameter(
        ParameterSpec(type, name, block)
    )
}


