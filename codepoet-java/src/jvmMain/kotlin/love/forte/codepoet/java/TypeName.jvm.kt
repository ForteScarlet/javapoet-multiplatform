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

@file:JvmName("TypeNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.java.internal.toTypeName
import java.lang.reflect.Type
import javax.lang.model.element.TypeElement
import javax.lang.model.element.TypeParameterElement
import javax.lang.model.type.ArrayType
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.ErrorType
import javax.lang.model.type.PrimitiveType
import javax.lang.model.type.TypeKind
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.TypeVariable
import javax.lang.model.type.WildcardType
import javax.lang.model.util.SimpleTypeVisitor8

public fun Type.toTypeName(): TypeName = toTypeName(linkedMapOf())

// javax.model.element

public fun TypeMirror.toTypeName(): TypeName = toTypeName(linkedMapOf())

internal fun TypeMirror.toTypeName(typeVariables: MutableMap<TypeParameterElement, TypeVariableName>): TypeName {
    val visitor = object : SimpleTypeVisitor8<TypeName, Void?>() {
        override fun visitPrimitive(t: PrimitiveType, p: Void?): TypeName {
            return when (t.kind) {
                TypeKind.BOOLEAN -> TypeName.Builtins.BOOLEAN
                TypeKind.BYTE -> TypeName.Builtins.BYTE
                TypeKind.SHORT -> TypeName.Builtins.SHORT
                TypeKind.INT -> TypeName.Builtins.INT
                TypeKind.LONG -> TypeName.Builtins.LONG
                TypeKind.CHAR -> TypeName.Builtins.CHAR
                TypeKind.FLOAT -> TypeName.Builtins.FLOAT
                TypeKind.DOUBLE -> TypeName.Builtins.DOUBLE
                else -> throw AssertionError()
            }
        }

        override fun visitDeclared(t: DeclaredType, p: Void?): TypeName {
            val asElement = t.asElement()
            val rawType = (asElement as TypeElement).toClassName()
            val enclosingType = t.enclosingType
            val enclosing = if (enclosingType.kind != TypeKind.NONE
                && !asElement.modifiers.contains(javax.lang.model.element.Modifier.STATIC)) {
                enclosingType.accept(this, null)
            } else {
                null
            }
            if (t.typeArguments.isEmpty() && enclosing !is ParameterizedTypeName) {
                return rawType
            }

            val typeArgumentNames = mutableListOf<TypeName>()
            for (mirror in t.typeArguments) {
                mirror.toTypeName(typeVariables)
                typeArgumentNames.add(mirror.toTypeName(typeVariables))
            }

            return if (enclosing is ParameterizedTypeName) {
                enclosing.nestedClass(rawType.simpleName, typeArgumentNames)
            } else {
                ParameterizedTypeName(rawType, typeArgumentNames)
            }
        }

        override fun visitError(t: ErrorType, p: Void?): TypeName {
            return visitDeclared(t, p)
        }

        override fun visitArray(t: ArrayType, p: Void?): TypeName {
            TODO("return t.toArrayTypeName(typeVariables)")
        }

        override fun visitTypeVariable(t: TypeVariable, p: Void?): TypeName {
            TODO("return t.toTypeVariableName(typeVariables)")
        }

        override fun visitWildcard(t: WildcardType, p: Void?): TypeName {
            TODO("return t.toWildcardTypeName(typeVariables)")
        }

        override fun visitNoType(t: javax.lang.model.type.NoType, p: Void?): TypeName {
            return when (t.kind) {
                TypeKind.VOID -> TypeName.Builtins.VOID
                else -> super.visitUnknown(t, p)
            }
        }

        override fun defaultAction(e: TypeMirror?, p: Void?): TypeName {
            throw IllegalArgumentException("Unexpected type mirror: $e")
        }
    }
    return accept(visitor, null)
}
