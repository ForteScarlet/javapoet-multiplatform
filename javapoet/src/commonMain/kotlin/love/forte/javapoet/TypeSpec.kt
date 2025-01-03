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
    public enum class Kind {
        CLASS,
        INTERFACE,
        ENUM,
        ANNOTATION,
        RECORD,
        SEALED_CLASS, // abstract sealed class Vehicle permits Car, Truck
        NON_SEALED_CLASS, // non-sealed class Car extends Vehicle implements Service
        SEALED_INTERFACE, // sealed interface Service permits Car, Truck
        NON_SEALED_INTERFACE, // non-sealed interface Service permits Car, Truck
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

        // addJavadoc(s)
        // addAnnotation(s)


        public abstract fun build(): T
    }

    public companion object {

    }
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

        override fun build(): SealedTypeSpec {
            TODO("Not yet implemented")
        }
    }
}
