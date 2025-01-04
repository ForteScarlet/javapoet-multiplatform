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

package love.forte.javapoet

import love.forte.javapoet.TypeSpec.Kind
import love.forte.javapoet.internal.isSourceName
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
public sealed interface TypeSpec {
    public val name: String
    public val kind: Kind
    public val anonymousTypeArguments: CodeBlock
    public val javadoc: CodeBlock
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

    public val staticBlock: CodeBlock

    public val initializerBlock: CodeBlock

    public val methods: List<MethodSpec>

    // subtypes
    public val types: List<TypeName>

    // val nestedTypesSimpleNames: Set<String>? = null
    // val alwaysQualifiedNames: Set<String>? = null


    /**
     * Type kind
     */
    public enum class Kind(
        internal val states: States = States(0),
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
            )
        ),
        ENUM(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            )
        ),
        ANNOTATION(
            states = States(
            )
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
            )
        ),

        // non-sealed interface Service permits Car, Truck
        NON_SEALED_INTERFACE(
            states = States(
                State.SUPERINTERFACES_SUPPORT,
            )
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
        public val anonymousTypeArguments: CodeBlock,
    ) {
        init {
            check(name == null || name.isSourceName()) { "Invalid `name`: $name" }
        }

        internal val javadoc = CodeBlock.builder()
        internal var superclass: TypeName? = null
        internal val staticBlock = CodeBlock.builder()
        internal val initializerBlock = CodeBlock.builder()

        // enumConstants
        public val annotations: MutableList<AnnotationSpec> = mutableListOf()
        public val modifiers: MutableSet<Modifier> = linkedSetOf()
        public val typeVariables: MutableList<TypeVariableName> = mutableListOf()
        public val superinterfaces: MutableList<TypeName> = mutableListOf()
        public val fields: MutableList<FieldSpec> = mutableListOf()
        public val methods: MutableList<MethodSpec> = mutableListOf()
        public val types: MutableList<TypeSpec> = mutableListOf()

        protected abstract val self: B

        public open fun addJavadoc(format: String, vararg args: Any?): B = self.apply {
            javadoc.add(format, *args)
        }

        public open fun addJavadoc(block: CodeBlock): B = self.apply {
            javadoc.add(block)
        }

        public open fun superclass(superclass: TypeName): B = self.apply {
            check(kind.states.superclassSupport) { "`superclass` is not supported for kind $kind" }
            require(!superclass.isPrimitive) { "`superclass` can't be primitive." }
            this.superclass = superclass
        }

        public open fun addStaticBlock(format: String, vararg args: Any?): B = self.apply {
            this.staticBlock.add(format, *args)
        }

        public open fun addStaticBlock(block: CodeBlock): B = self.apply {
            this.staticBlock.add(block)
        }

        public open fun addInitializerBlock(format: String, vararg args: Any?): B = self.apply {
            this.initializerBlock.add(format, *args)
        }

        public open fun addInitializerBlock(block: CodeBlock): B = self.apply {
            this.initializerBlock.add(block)
        }

        public open fun addAnnotations(annotations: Iterable<AnnotationSpec>): B = self.apply {
            this.annotations.addAll(annotations)
        }

        public open fun addAnnotations(vararg annotations: AnnotationSpec): B = self.apply {
            this.annotations.addAll(annotations)
        }

        public open fun addAnnotation(annotation: AnnotationSpec): B = self.apply {
            this.annotations.add(annotation)
        }

        public open fun addAnnotation(annotation: ClassName): B =
            addAnnotation(AnnotationSpec(annotation))

        public open fun addModifiers(vararg modifiers: Modifier): B = self.apply {
            this.modifiers.addAll(modifiers)
        }

        public open fun addModifiers(modifiers: Iterable<Modifier>): B = self.apply {
            this.modifiers.addAll(modifiers)
        }

        public open fun addModifier(modifier: Modifier): B = self.apply {
            this.modifiers.add(modifier)
        }

        public open fun addTypeVariables(vararg typeVariables: TypeVariableName): B = self.apply {
            this.typeVariables.addAll(typeVariables)
        }

        public open fun addTypeVariables(typeVariables: Iterable<TypeVariableName>): B = self.apply {
            this.typeVariables.addAll(typeVariables)
        }

        public open fun addTypeVariable(typeVariable: TypeVariableName): B = self.apply {
            this.typeVariables.add(typeVariable)
        }

        public open fun addSuperinterfaces(vararg superinterfaces: TypeName): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.addAll(superinterfaces)
        }

        public open fun addSuperinterfaces(superinterfaces: Iterable<TypeName>): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.addAll(superinterfaces)
        }

        public open fun addSuperinterface(superinterface: TypeName): B = self.apply {
            checkSuperinterfaceSupport()
            this.superinterfaces.add(superinterface)
        }

        private fun checkSuperinterfaceSupport() {
            check(kind.states.superinterfacesSupport) { "`superinterface` is not supported for kind $kind" }
        }

        public open fun addFields(vararg fields: FieldSpec): B = self.apply {
            this.fields.addAll(fields)
        }

        public open fun addFields(fields: Iterable<FieldSpec>): B = self.apply {
            this.fields.addAll(fields)
        }

        public open fun addField(field: FieldSpec): B = self.apply {
            this.fields.add(field)
        }

        // TODO check field?

        public open fun addMethods(methods: Iterable<MethodSpec>): B = self.apply {
            this.methods.addAll(methods)
        }

        public open fun addMethods(vararg methods: MethodSpec): B = self.apply {
            this.methods.addAll(methods)
        }

        public open fun addMethod(method: MethodSpec): B = self.apply {
            this.methods.add(method)
        }

        public open fun addTypes(types: Iterable<TypeSpec>): B = self.apply {
            this.types.addAll(types)
        }

        public open fun addTypes(vararg types: TypeSpec): B = self.apply {
            this.types.addAll(types)
        }

        public open fun addType(type: TypeSpec): B = self.apply {
            this.types.add(type)
        }


        public abstract fun build(): T
    }

    public companion object
}

/**
 * A generated `class` or `interface`.
 */
public interface SimpleTypeSpec : TypeSpec {

    public class Builder(
        kind: Kind,
        name: String?,
        anonymousTypeArguments: CodeBlock,
    ) : TypeSpec.Builder<Builder, SimpleTypeSpec>(kind, name, anonymousTypeArguments) {
        init {
            check(kind == Kind.CLASS || kind == Kind.INTERFACE) {
                "Invalid simple type `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this


        override fun build(): SimpleTypeSpec {
            TODO("Not yet implemented")
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
    public class Builder(
        name: String?,
        anonymousTypeArguments: CodeBlock,
    ) : TypeSpec.Builder<Builder, AnnotationTypeSpec>(Kind.ANNOTATION, name, anonymousTypeArguments) {

        override val self: Builder
            get() = this

        override fun build(): AnnotationTypeSpec {
            TODO("Not yet implemented")
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
    override val superclass: TypeName?
        get() = null

    public val enumConstants: Map<String, TypeSpec>

    public class Builder(
        name: String?,
        anonymousTypeArguments: CodeBlock,
    ) : TypeSpec.Builder<Builder, EnumTypeSpec>(Kind.ENUM, name, anonymousTypeArguments) {
        override val self: Builder
            get() = this

        private val enumConstants = linkedMapOf<String, TypeSpec>()

        public fun addEnumConstant(name: String, type: TypeSpec): Builder = apply {
            enumConstants[name] = type
        }

        override fun build(): EnumTypeSpec {
            TODO("Not yet implemented")
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
    public class Builder(
        kind: Kind,
        name: String?,
        anonymousTypeArguments: CodeBlock,
    ) : TypeSpec.Builder<Builder, NonSealedTypeSpec>(kind, name, anonymousTypeArguments) {
        init {
            check(kind == Kind.NON_SEALED_CLASS || kind == Kind.NON_SEALED_INTERFACE) {
                "Invalid non-sealed `kind`: $kind"
            }
        }

        override val self: Builder
            get() = this

        override fun build(): NonSealedTypeSpec {
            TODO("Not yet implemented")
        }
    }
}
// non-sealed class, non-sealed interface

/**
 * A generated `sealed class/interface`.
 */
public interface SealedTypeSpec : TypeSpec {
    // sealed class, sealed interface
    public val permits: List<TypeName>

    public class Builder(
        kind: Kind,
        name: String?,
        anonymousTypeArguments: CodeBlock,
    ) : TypeSpec.Builder<Builder, SealedTypeSpec>(kind, name, anonymousTypeArguments) {
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
            TODO("Not yet implemented")
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
    override val superclass: TypeName?
        get() = null

    public val mainConstructor: MethodSpec

    public class Builder(
        name: String?,
        anonymousTypeArguments: CodeBlock,
    ) : TypeSpec.Builder<Builder, SealedTypeSpec>(Kind.RECORD, name, anonymousTypeArguments) {
        override val self: Builder
            get() = this

        private var mainConstructor: MethodSpec? = null

        public fun mainConstructor(method: MethodSpec): Builder = apply {
            mainConstructor = method
        }

        override fun build(): SealedTypeSpec {
            TODO("Not yet implemented")
        }
    }
}
