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

@file:JvmName("JavaTypeNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.java.ref.JavaTypeRef
import love.forte.codegentle.java.ref.ref
import java.lang.reflect.GenericArrayType
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.*
import javax.lang.model.util.SimpleTypeVisitor8

public fun Type.toJavaTypeName(): JavaTypeName = toJavaTypeName(linkedMapOf())


private val VOID_CLASS = Void::class.javaPrimitiveType!!
private val BOOLEAN_CLASS = Boolean::class.javaPrimitiveType!!
private val BYTE_CLASS = Byte::class.javaPrimitiveType!!
private val SHORT_CLASS = Short::class.javaPrimitiveType!!
private val INT_CLASS = Int::class.javaPrimitiveType!!
private val LONG_CLASS = Long::class.javaPrimitiveType!!
private val CHAR_CLASS = Char::class.javaPrimitiveType!!
private val FLOAT_CLASS = Float::class.javaPrimitiveType!!

internal val VOID_BOXED_CLASS = Void::class.javaObjectType
internal val BOOLEAN_BOXED_CLASS = Boolean::class.javaObjectType
internal val BYTE_BOXED_CLASS = Byte::class.javaObjectType
internal val SHORT_BOXED_CLASS = Short::class.javaObjectType
internal val INT_BOXED_CLASS = Int::class.javaObjectType
internal val LONG_BOXED_CLASS = Long::class.javaObjectType
internal val CHAR_BOXED_CLASS = Char::class.javaObjectType
internal val FLOAT_BOXED_CLASS = Float::class.javaObjectType

internal val OBJECT_CLASS = Object::class.java
internal val STRING_CLASS = String::class.java

@PublishedApi
internal fun Type.toJavaTypeName(map: MutableMap<Type, JavaTypeVariableName>): JavaTypeName {
    return when (val type = this) {
        is Class<*> -> {
            when (type) {
                VOID_CLASS -> return JavaTypeName.Builtins.VOID
                BOOLEAN_CLASS -> return JavaTypeName.Builtins.BOOLEAN
                BYTE_CLASS -> return JavaTypeName.Builtins.BYTE
                SHORT_CLASS -> return JavaTypeName.Builtins.SHORT
                INT_CLASS -> return JavaTypeName.Builtins.INT
                LONG_CLASS -> return JavaTypeName.Builtins.LONG
                CHAR_CLASS -> return JavaTypeName.Builtins.CHAR
                FLOAT_CLASS -> return JavaTypeName.Builtins.FLOAT
                VOID_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_VOID
                BOOLEAN_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_BOOLEAN
                BYTE_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_BYTE
                SHORT_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_SHORT
                INT_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_INT
                LONG_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_LONG
                CHAR_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_CHAR
                FLOAT_BOXED_CLASS -> return JavaClassName.Builtins.BOXED_FLOAT
                OBJECT_CLASS -> return JavaClassName.Builtins.OBJECT
                STRING_CLASS -> return JavaClassName.Builtins.STRING
            }

            if (type.isArray) {
                JavaArrayTypeName(type.componentType.toJavaTypeName(map).ref())
            } else {
                type.toJavaClassName()
            }
        }

        is ParameterizedType -> type.toJavaParameterizedTypeName(map)

        is java.lang.reflect.WildcardType -> {
            val lowerBounds = type.lowerBounds

            if (lowerBounds.isNotEmpty()) {
                JavaSupertypeWildcardTypeName(lowerBounds.map { it.toJavaTypeName().ref() })
            } else {
                JavaSubtypeWildcardTypeName(
                    type.upperBounds
                        .map { it.toJavaTypeName().ref() }
                        .ifEmpty { listOf(JavaClassName.Builtins.OBJECT.ref()) }
                )
            }
        }

        is java.lang.reflect.TypeVariable<*> -> type.toJavaTypeVariableName(map)

        is GenericArrayType -> type.toJavaArrayTypeName(map) {}

        else -> throw IllegalArgumentException("Unexpected type $type")
    }
}


// javax.model.element

public fun TypeMirror.toJavaTypeName(): JavaTypeName = toJavaTypeName(linkedMapOf())

internal fun TypeMirror.toJavaTypeName(typeVariables: MutableMap<TypeParameterElement, JavaTypeVariableName>): JavaTypeName {
    val visitor = object : SimpleTypeVisitor8<JavaTypeName, Void?>() {
        override fun visitPrimitive(t: PrimitiveType, p: Void?): JavaTypeName {
            return when (t.kind) {
                TypeKind.BOOLEAN -> JavaTypeName.Builtins.BOOLEAN
                TypeKind.BYTE -> JavaTypeName.Builtins.BYTE
                TypeKind.SHORT -> JavaTypeName.Builtins.SHORT
                TypeKind.INT -> JavaTypeName.Builtins.INT
                TypeKind.LONG -> JavaTypeName.Builtins.LONG
                TypeKind.CHAR -> JavaTypeName.Builtins.CHAR
                TypeKind.FLOAT -> JavaTypeName.Builtins.FLOAT
                TypeKind.DOUBLE -> JavaTypeName.Builtins.DOUBLE
                else -> throw AssertionError()
            }
        }

        override fun visitDeclared(t: DeclaredType, p: Void?): JavaTypeName {
            val asElement = t.asElement()
            val rawType = (asElement as TypeElement).toJavaClassName()
            val enclosingType = t.enclosingType
            val enclosing = if (enclosingType.kind != TypeKind.NONE
                && !asElement.modifiers.contains(Modifier.STATIC)
            ) {
                enclosingType.accept(this, null)
            } else {
                null
            }
            if (t.typeArguments.isEmpty() && enclosing !is JavaParameterizedTypeName) {
                return rawType
            }

            val typeArgumentNameRefs = mutableListOf<JavaTypeRef<*>>()
            for (mirror in t.typeArguments) {
                mirror.toJavaTypeName(typeVariables)
                typeArgumentNameRefs.add(mirror.toJavaTypeName(typeVariables).ref())
            }

            return if (enclosing is JavaParameterizedTypeName) {
                enclosing.nestedClass(rawType.simpleName, typeArgumentNameRefs)
            } else {
                JavaParameterizedTypeName(rawType, typeArgumentNameRefs)
            }
        }

        override fun visitError(t: ErrorType, p: Void?): JavaTypeName {
            return visitDeclared(t, p)
        }

        override fun visitArray(t: ArrayType, p: Void?): JavaTypeName {
            TODO("return t.toArrayTypeName(typeVariables)")
        }

        override fun visitTypeVariable(t: TypeVariable, p: Void?): JavaTypeName {
            TODO("return t.toTypeVariableName(typeVariables)")
        }

        override fun visitWildcard(t: WildcardType, p: Void?): JavaTypeName {
            TODO("return t.toWildcardTypeName(typeVariables)")
        }

        override fun visitNoType(t: NoType, p: Void?): JavaTypeName {
            return when (t.kind) {
                TypeKind.VOID -> JavaTypeName.Builtins.VOID
                else -> super.visitUnknown(t, p)
            }
        }

        override fun defaultAction(e: TypeMirror?, p: Void?): JavaTypeName {
            throw IllegalArgumentException("Unexpected type mirror: $e")
        }
    }
    return accept(visitor, null)
}
