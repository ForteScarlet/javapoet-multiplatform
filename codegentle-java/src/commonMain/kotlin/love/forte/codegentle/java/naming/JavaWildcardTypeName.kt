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
package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.LowerWildcardTypeName
import love.forte.codegentle.common.naming.UpperWildcardTypeName
import love.forte.codegentle.common.naming.WildcardTypeName
import love.forte.codegentle.java.type
import love.forte.codegentle.java.writer.JavaCodeWriter
import love.forte.codegentle.java.writer.emit


//
//
// public sealed interface JavaWildcardTypeName : JavaTypeName {
//     public val bounds: List<TypeRef<*>>
//     // public val upperBounds: List<JavaTypeName> // ? extends T1 & T2
//     // public val lowerBounds: List<JavaTypeName> // ? super T1 & T2
// }
//
// public interface JavaSubtypeWildcardTypeName : JavaWildcardTypeName, UpperWildcardTypeName {
//     // lowerBounds, ? super A
//     public val lowerBounds: List<TypeRef<*>>
//         get() = bounds
// }
//
// public interface JavaSupertypeWildcardTypeName : JavaWildcardTypeName, LowerWildcardTypeName {
//     // upperBounds, ? extends A & B,
//     // 接口在后，类在前
//     public val upperBounds: List<TypeRef<*>>
//         get() = bounds
// }
//
// @JvmName("of")
// public fun JavaSubtypeWildcardTypeName(upperBound: TypeRef<*>): JavaSubtypeWildcardTypeName =
//     JavaSubtypeWildcardTypeName(listOf(upperBound))
//
// @JvmName("of")
// public fun JavaSupertypeWildcardTypeName(lowerBound: TypeRef<*>): JavaSupertypeWildcardTypeName =
//     JavaSupertypeWildcardTypeName(listOf(lowerBound))
//
// @JvmName("of")
// public fun JavaSubtypeWildcardTypeName(upperBounds: List<TypeRef<*>>): JavaSubtypeWildcardTypeName =
//     JavaSubtypeWildcardTypeNameImpl(upperBounds)
//
// @JvmName("of")
// public fun JavaSupertypeWildcardTypeName(lowerBounds: List<TypeRef<*>>): JavaSupertypeWildcardTypeName =
//     JavaSupertypeWildcardTypeNameImpl(lowerBounds)


internal fun WildcardTypeName.emitTo(codeWriter: JavaCodeWriter) {
    when (this) {
        is LowerWildcardTypeName -> emitTo(codeWriter)
        is UpperWildcardTypeName -> emitTo(codeWriter)
    }
}

internal fun LowerWildcardTypeName.emitTo(codeWriter: JavaCodeWriter) {
    if (bounds.isNotEmpty()) {
        var extends = false
        bounds.forEachIndexed { index, upperBound ->
            if (index == 0) {
                // first
                codeWriter.emit("?")
            }

            if (upperBound.typeName == JavaClassNames.OBJECT) {
                // continue
                return@forEachIndexed
            }

            if (!extends) {
                codeWriter.emit(" extends %V") { type(upperBound) }
                extends = true
            } else {
                codeWriter.emit(" & %V") { type(upperBound) }
            }
        }
    }
}

internal fun UpperWildcardTypeName.emitTo(codeWriter: JavaCodeWriter) {
    bounds.forEachIndexed { index, lowerBound ->
        if (index == 0) {
            // first
            codeWriter.emit("? super %V") {
                type(lowerBound)
            }
        } else {
            codeWriter.emit(" & %V") {
                type(lowerBound)
            }
        }
    }
}
