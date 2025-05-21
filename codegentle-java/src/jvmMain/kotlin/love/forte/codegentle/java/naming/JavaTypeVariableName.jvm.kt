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
@file:JvmName("JavaTypeVariableNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import love.forte.codegentle.common.computeValue
import love.forte.codegentle.java.ref.javaRef
import java.lang.reflect.Type
import java.lang.reflect.TypeVariable
import javax.lang.model.element.TypeParameterElement

public fun TypeVariable<*>.toJavaTypeVariableName(): JavaTypeVariableName =
    toJavaTypeVariableName(linkedMapOf())

internal fun TypeVariable<*>.toJavaTypeVariableName(map: MutableMap<Type, JavaTypeVariableName>): JavaTypeVariableName {
    val type = this
    return map.computeValue(type) { _, old ->
        old ?: JavaTypeVariableName(
            name = type.name,
            bounds = bounds.mapNotNull { bound ->
                bound.takeIf { it != Object::class.java }?.toJavaTypeName(map)?.javaRef()
            }
        )
    }!!
}

public fun javax.lang.model.type.TypeVariable.toJavaTypeVariableName(): JavaTypeVariableName =
    (asElement() as TypeParameterElement).toJavaTypeVariableName()

public fun TypeParameterElement.toJavaTypeVariableName(): JavaTypeVariableName {
    return JavaTypeVariableName(
        name = simpleName.toString(),
        bounds = bounds.map { it.toJavaTypeName().javaRef() }
    )
}


internal fun javax.lang.model.type.TypeVariable.toJavaTypeVariableName(
    typeVariables: MutableMap<TypeParameterElement, JavaTypeVariableName>
): JavaTypeVariableName {
    val element = asElement() as TypeParameterElement
    return typeVariables.computeValue(element) { _, old ->
        old ?: JavaTypeVariableName(
            name = element.simpleName.toString(),
            bounds = element.bounds.mapNotNull { typeMirror ->
                typeMirror.toJavaTypeName(typeVariables)
                    .takeIf { it != JavaClassNames.OBJECT }
                    ?.javaRef()
            }
        )
    }!!
}
