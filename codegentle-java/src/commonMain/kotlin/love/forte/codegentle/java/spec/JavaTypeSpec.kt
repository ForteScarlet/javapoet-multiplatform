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

package love.forte.codegentle.java.spec

import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.*
import love.forte.codegentle.java.internal.isSourceName
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.naming.JavaTypeName
import love.forte.codegentle.java.naming.JavaTypeVariableName
import love.forte.codegentle.java.naming.isPrimitive
import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.spec.internal.*
import kotlin.jvm.JvmInline
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName


/**
 * A generated class, interface, or enum declaration.
 *
 * @see JavaSimpleTypeSpec
 * @see JavaAnnotationTypeSpec
 * @see JavaEnumTypeSpec
 * @see JavaNonSealedTypeSpec
 * @see JavaSealedTypeSpec
 * @see JavaRecordTypeSpec
 *
 * @author ForteScarlet
 */
public sealed interface JavaTypeSpec : JavaSpec {
    public val name: String?
    public val kind: Kind
    public val javadoc: JavaCodeValue
    public val annotations: List<JavaAnnotationSpec>
    public val modifiers: Set<JavaModifier>

    public fun hasModifier(modifier: JavaModifier): Boolean = modifier in modifiers

    public val typeVariables: List<JavaTypeVariableName>

    // TODO super class:
    //  `extends` One if is class,
    //  Nothing if is record, enum, annotation, interface.

    public val superclass: JavaTypeName?


    // TODO super interfaces:
    //  `extends` if is interface,
    //  `implements` others.

    public val superinterfaces: List<JavaTypeName>

    public val fields: List<FieldSpec>

    public val staticBlock: JavaCodeValue

    public val initializerBlock: JavaCodeValue

    public val methods: List<JavaMethodSpec>

    // subtypes
    public val types: List<JavaTypeSpec>

    // val nestedTypesSimpleNames: Set<String>? = null
    // val alwaysQualifiedNames: Set<String>? = null

    override fun emit(codeWriter: JavaCodeWriter) {
        emit(codeWriter, emptySet())
    }

    public fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier> = emptySet())

    /**
     * Type kind
     */
    public enum class Kind(
        internal val states: States = States(0),
        internal val implicitFieldModifiers: Set<JavaModifier> = emptySet(),
        internal val implicitMethodModifiers: Set<JavaModifier> = emptySet(),
        internal val implicitTypeModifiers: Set<JavaModifier> = emptySet(),
        internal val asMemberModifiers: Set<JavaModifier> = emptySet(),
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
            implicitFieldModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC, JavaModifier.FINAL),
            implicitMethodModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT),
            implicitTypeModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC),
            asMemberModifiers = setOf(JavaModifier.STATIC),
        ),
        ENUM(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            ),
            asMemberModifiers = setOf(JavaModifier.STATIC),
        ),
        ANNOTATION(
            states = States(),
            implicitFieldModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC, JavaModifier.FINAL),
            implicitMethodModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT),
            implicitTypeModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC),
            asMemberModifiers = setOf(JavaModifier.STATIC),
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
            implicitFieldModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC, JavaModifier.FINAL),
            implicitMethodModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT),
            implicitTypeModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC),
            asMemberModifiers = setOf(JavaModifier.STATIC),
        ),

        // non-sealed interface Service permits Car, Truck
        NON_SEALED_INTERFACE(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            ),
            implicitFieldModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC, JavaModifier.FINAL),
            implicitMethodModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.ABSTRACT),
            implicitTypeModifiers = setOf(JavaModifier.PUBLIC, JavaModifier.STATIC),
            asMemberModifiers = setOf(JavaModifier.STATIC),
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
     * @see JavaSimpleTypeSpec.Builder
     * @see JavaAnnotationTypeSpec.Builder
     * @see JavaEnumTypeSpec.Builder
     * @see JavaNonSealedTypeSpec.Builder
     * @see JavaSealedTypeSpec.Builder
     * @see JavaRecordTypeSpec.Builder
     */
    public sealed class Builder<B : Builder<B, T>, T : JavaTypeSpec>(
        public val kind: Kind,
        public val name: String?,
    ) : ModifierBuilderContainer {
        init {
            check(name == null || name.isSourceName()) { "Invalid `name`: $name" }
        }

        internal val javadoc = JavaCodeValue.builder()
        internal var superclass: JavaTypeName? = null
        internal val staticBlock = JavaCodeValue.builder()
        internal val initializerBlock = JavaCodeValue.builder()

        public val annotations: MutableList<JavaAnnotationSpec> = mutableListOf()
        public val modifiers: MutableSet<JavaModifier> = linkedSetOf()
        public val typeVariables: MutableList<JavaTypeVariableName> = mutableListOf()
        public val superinterfaces: MutableList<JavaTypeName> = mutableListOf()
        public val fields: MutableList<FieldSpec> = mutableListOf()
        public val methods: MutableList<JavaMethodSpec> = mutableListOf()
        public val types: MutableList<JavaTypeSpec> = mutableListOf()

        protected abstract val self: B

        public fun addJavadoc(codeValue: JavaCodeValue): B = self.apply {
            javadoc.add(codeValue)
        }

        public fun superclass(superclass: JavaTypeName): B = self.apply {
            check(kind.states.superclassSupport) { "`superclass` is not supported for kind $kind" }
            require(!superclass.isPrimitive) { "`superclass` can't be primitive." }
            this.superclass = superclass
        }

        public fun addStaticBlock(codeValue: JavaCodeValue): B = self.apply {
            this.staticBlock.add(codeValue)
        }

        public fun addInitializerBlock(codeValue: JavaCodeValue): B = self.apply {
            this.initializerBlock.add(codeValue)
        }

        public fun addAnnotations(annotations: Iterable<JavaAnnotationSpec>): B = self.apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotations(vararg annotations: JavaAnnotationSpec): B = self.apply {
            this.annotations.addAll(annotations)
        }

        public fun addAnnotation(annotation: JavaAnnotationSpec): B = self.apply {
            this.annotations.add(annotation)
        }

        public fun addAnnotation(annotation: JavaClassName): B =
            addAnnotation(JavaAnnotationSpec(annotation))

        override fun addModifiers(vararg modifiers: JavaModifier): B = self.apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifiers(modifiers: Iterable<JavaModifier>): B = self.apply {
            this.modifiers.addAll(modifiers)
        }

        override fun addModifier(modifier: JavaModifier): B = self.apply {
            this.modifiers.add(modifier)
        }

        public fun addTypeVariables(vararg typeVariables: JavaTypeVariableName): B = self.apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addTypeVariables(typeVariables: Iterable<JavaTypeVariableName>): B = self.apply {
            this.typeVariables.addAll(typeVariables)
        }

        public fun addTypeVariable(typeVariable: JavaTypeVariableName): B = self.apply {
            this.typeVariables.add(typeVariable)
        }

        public fun addSuperinterfaces(vararg superinterfaces: JavaTypeName): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.addAll(superinterfaces)
        }

        public fun addSuperinterfaces(superinterfaces: Iterable<JavaTypeName>): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.addAll(superinterfaces)
        }

        public fun addSuperinterface(superinterface: JavaTypeName): B = self.apply {
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

        public fun addMethods(methods: Iterable<JavaMethodSpec>): B = self.apply {
            this.methods.addAll(methods)
        }

        public fun addMethods(vararg methods: JavaMethodSpec): B = self.apply {
            this.methods.addAll(methods)
        }

        public fun addMethod(method: JavaMethodSpec): B = self.apply {
            this.methods.add(method)
        }

        public fun addTypes(types: Iterable<JavaTypeSpec>): B = self.apply {
            this.types.addAll(types)
        }

        public fun addTypes(vararg types: JavaTypeSpec): B = self.apply {
            this.types.addAll(types)
        }

        public fun addType(type: JavaTypeSpec): B = self.apply {
            this.types.add(type)
        }


        public abstract fun build(): T
    }

    public companion object
}

public val JavaTypeSpec.nestedTypesSimpleNames: Set<String>
    get() = types.mapTo(linkedSetOf()) { it.name!! }

/**
 * A generated `class` or `interface`.
 */
public interface JavaSimpleTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    public class Builder(
        kind: JavaTypeSpec.Kind,
        name: String,
    ) : JavaTypeSpec.Builder<Builder, JavaSimpleTypeSpec>(kind, name) {
        init {
            require(kind == JavaTypeSpec.Kind.CLASS || kind == JavaTypeSpec.Kind.INTERFACE) {
                "Invalid simple type `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this


        override fun build(): JavaSimpleTypeSpec {
            return JavaSimpleTypeSpecImpl(
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
 * Also used in enum constants, see [JavaEnumTypeSpec.enumConstants].
 *
 */
public interface JavaAnonymousClassTypeSpec : JavaTypeSpec {
    override val name: String?
        get() = null

    public val anonymousTypeArguments: JavaCodeValue

    override fun emit(codeWriter: JavaCodeWriter, implicitModifiers: Set<JavaModifier>) {
        emit(codeWriter, null, implicitModifiers)
    }

    public fun emit(codeWriter: JavaCodeWriter, enumName: String? = null, implicitModifiers: Set<JavaModifier> = emptySet())

    public class Builder(
        public val anonymousTypeArguments: JavaCodeValue,
    ) : JavaTypeSpec.Builder<Builder, JavaAnonymousClassTypeSpec>(JavaTypeSpec.Kind.CLASS, null) {
        override val self: Builder
            get() = this

        override fun build(): JavaAnonymousClassTypeSpec {
            return JavaAnonymousClassTypeSpecImpl(
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
public interface JavaAnnotationTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    override val superclass: JavaTypeName?
        get() = null

    override val superinterfaces: List<JavaTypeName>
        get() = emptyList()

    public class Builder(
        name: String,
    ) : JavaTypeSpec.Builder<Builder, JavaAnnotationTypeSpec>(JavaTypeSpec.Kind.ANNOTATION, name) {

        override val self: Builder
            get() = this

        override fun build(): JavaAnnotationTypeSpec {
            // TODO check method must be public abstract
            // TODO check field must be public static (no private)

            check(superclass == null) {
                "`superclass` must be null for annotation type."
            }

            check(superinterfaces.isEmpty()) {
                "`superinterfaces` must be empty for annotation type."
            }

            return JavaAnnotationTypeSpecImpl(
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
public interface JavaEnumTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    override val superclass: JavaTypeName?
        get() = null

    public val enumConstants: Map<String, JavaTypeSpec>

    public class Builder(
        name: String?,
    ) : JavaTypeSpec.Builder<Builder, JavaEnumTypeSpec>(JavaTypeSpec.Kind.ENUM, name) {
        override val self: Builder
            get() = this

        // TODO superclass must be null

        private val enumConstants = linkedMapOf<String, JavaAnonymousClassTypeSpec>()

        public fun addEnumConstant(name: String, type: JavaAnonymousClassTypeSpec): Builder = apply {
            enumConstants[name] = type
        }

        override fun build(): JavaEnumTypeSpec {
            return JavaEnumTypeSpecImpl(
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
public interface JavaNonSealedTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    public class Builder(
        kind: JavaTypeSpec.Kind,
        name: String,
    ) : JavaTypeSpec.Builder<Builder, JavaNonSealedTypeSpec>(kind, name) {
        init {
            check(kind == JavaTypeSpec.Kind.NON_SEALED_CLASS || kind == JavaTypeSpec.Kind.NON_SEALED_INTERFACE) {
                "Invalid non-sealed `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this

        override fun build(): JavaNonSealedTypeSpec {
            return JavaNonSealedTypeSpecImpl(
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
public interface JavaSealedTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    // sealed class, sealed interface
    public val permits: List<JavaTypeName>

    public class Builder(
        kind: JavaTypeSpec.Kind,
        name: String,
    ) : JavaTypeSpec.Builder<Builder, JavaSealedTypeSpec>(kind, name) {
        init {
            check(kind == JavaTypeSpec.Kind.SEALED_CLASS || kind == JavaTypeSpec.Kind.SEALED_INTERFACE) {
                "Invalid sealed `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this

        private val permits = mutableListOf<JavaTypeName>()

        public fun addPermits(permits: Iterable<JavaTypeName>): Builder = apply {
            this.permits.addAll(permits)
        }

        public fun addPermits(vararg permits: JavaTypeName): Builder = apply {
            this.permits.addAll(permits)
        }

        public fun addPermit(permit: JavaTypeName): Builder = apply {
            permits.add(permit)
        }

        override fun build(): JavaSealedTypeSpec {
            return JavaSealedTypeSpecImpl(
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
public interface JavaRecordTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    override val superclass: JavaTypeName?
        get() = null

    public val mainConstructorParameters: List<JavaParameterSpec>

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
    ) : JavaTypeSpec.Builder<Builder, JavaRecordTypeSpec>(JavaTypeSpec.Kind.RECORD, name) {
        override val self: Builder
            get() = this

        private var mainConstructorParameters = mutableListOf<JavaParameterSpec>()

        public fun addMainConstructorParameter(mainConstructorParameter: JavaParameterSpec): Builder = apply {
            mainConstructorParameters.add(mainConstructorParameter)
        }

        public fun addMainConstructorParameters(vararg mainConstructorParams: JavaParameterSpec): Builder = apply {
            mainConstructorParameters.addAll(mainConstructorParams)
        }

        public fun addMainConstructorParameters(mainConstructorParams: Iterable<JavaParameterSpec>): Builder = apply {
            mainConstructorParameters.addAll(mainConstructorParams)
        }

        override fun build(): JavaRecordTypeSpec {
            return JavaRecordTypeSpecImpl(
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

public inline fun JavaSimpleTypeSpec(
    kind: JavaTypeSpec.Kind,
    name: String,
    block: JavaSimpleTypeSpec.Builder.() -> Unit = {}
): JavaSimpleTypeSpec {
    return JavaSimpleTypeSpec.Builder(kind, name).also(block).build()
}

public inline fun JavaAnonymousClassTypeSpec(
    anonymousTypeArguments: JavaCodeValue,
    block: JavaAnonymousClassTypeSpec.Builder.() -> Unit = {},
): JavaAnonymousClassTypeSpec {
    return JavaAnonymousClassTypeSpec.Builder(anonymousTypeArguments).also(block).build()
}

public inline fun JavaAnnotationTypeSpec(
    name: String,
    block: JavaAnnotationTypeSpec.Builder.() -> Unit = {},
): JavaAnnotationTypeSpec {
    return JavaAnnotationTypeSpec.Builder(name).also(block).build()
}

public inline fun JavaEnumTypeSpec(
    name: String?,
    block: JavaEnumTypeSpec.Builder.() -> Unit = {},
): JavaEnumTypeSpec {
    return JavaEnumTypeSpec.Builder(name).also(block).build()
}

public inline fun JavaNonSealedTypeSpec(
    kind: JavaTypeSpec.Kind,
    name: String,
    block: JavaNonSealedTypeSpec.Builder.() -> Unit = {},
): JavaNonSealedTypeSpec {
    return JavaNonSealedTypeSpec.Builder(kind, name).also(block).build()
}

public inline fun JavaSealedTypeSpec(
    kind: JavaTypeSpec.Kind,
    name: String,
    block: JavaSealedTypeSpec.Builder.() -> Unit = {},
): JavaSealedTypeSpec {
    return JavaSealedTypeSpec.Builder(kind, name).also(block).build()
}

public inline fun JavaRecordTypeSpec(
    name: String,
    block: JavaRecordTypeSpec.Builder.() -> Unit = {},
): JavaRecordTypeSpec {
    return JavaRecordTypeSpec.Builder(name).also(block).build()
}

// extensions

public inline fun JavaTypeSpec.Builder<*, *>.addJavadoc(format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    addJavadoc(JavaCodeValue(format, block))
}


public inline fun JavaTypeSpec.Builder<*, *>.addStaticBlock(format: String, block: CodeValueSingleFormatBuilderDsl = {}) {
    addStaticBlock(JavaCodeValue(format, block))
}


public inline fun JavaTypeSpec.Builder<*, *>.addInitializerBlock(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
) {
    addInitializerBlock(JavaCodeValue(format, block))
}

public inline fun JavaTypeSpec.Builder<*, *>.addAnnotation(
    annotation: JavaClassName,
    block: JavaAnnotationSpecBuilder.() -> Unit = {}
) {
    addAnnotation(JavaAnnotationSpec(annotation, block))
}

public inline fun JavaTypeSpec.Builder<*, *>.addField(
    type: JavaTypeName,
    name: String,
    block: FieldSpecBuilder.() -> Unit = {}
) {
    addField(FieldSpec(type, name, block))
}

public inline fun JavaTypeSpec.Builder<*, *>.addMethod(
    name: String,
    block: JavaMethodSpecBuilder.() -> Unit = {}
) {
    addMethod(JavaMethodSpec(name, block))
}

public inline fun JavaTypeSpec.Builder<*, *>.addMethod(
    block: JavaMethodSpecBuilder.() -> Unit = {}
) {
    addMethod(JavaMethodSpec(block))
}

public inline fun JavaEnumTypeSpec.Builder.addEnumConstant(
    name: String,
    anonymousTypeArguments: JavaCodeValue,
    block: JavaAnonymousClassTypeSpec.Builder.() -> Unit = {}
) {
    addEnumConstant(
        name,
        JavaAnonymousClassTypeSpec(anonymousTypeArguments, block)
    )
}

public inline fun JavaRecordTypeSpec.Builder.addMainConstructorParameter(
    type: JavaTypeRef<*>,
    name: String,
    block: JavaParameterSpecBuilder.() -> Unit = {}
) {
    addMainConstructorParameter(
        JavaParameterSpec(type, name, block)
    )
}


