/*
 * Copyright (C) 2014 Google, Inc.
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

@file:JvmName("JavaClassNames")
@file:JvmMultifileClass

package love.forte.codegentle.java.naming

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.SimpleElementVisitor8
import kotlin.reflect.KClass

/**
 * Create a [love.forte.codepoet.java.naming.JavaClassName] from [KClass].
 *
 * @see Class.toClassName
 */
public fun KClass<*>.toClassName(): JavaClassName {
    return java.toClassName()
}

/**
 * Create a [JavaClassName] from [Class].
 */
public fun Class<*>.toClassName(): JavaClassName {
    var java = this
    require(!java.isPrimitive) { "Primitive types cannot be represented as a ClassName" }
    require(Void.TYPE != java) { "'void' type cannot be represented as a ClassName" }
    require(!java.isArray) { "Array types cannot be represented as a ClassName" }

    var anonymousSuffix = ""
    while (java.isAnonymousClass) {
        val lastDollar: Int = java.getName().lastIndexOf('$')
        anonymousSuffix = java.getName().substring(lastDollar) + anonymousSuffix
        java = java.getEnclosingClass()
    }
    val name: String = java.getSimpleName() + anonymousSuffix

    if (java.getEnclosingClass() == null) {
        // Avoid unreliable Class.getPackage(). https://github.com/square/javapoet/issues/295
        val lastDot: Int = java.getName().lastIndexOf('.')
        val packageName: String? = if (lastDot != -1) java.getName().substring(0, lastDot) else null

        return JavaClassName(packageName, name)
    }

    return java.enclosingClass.toClassName().nestedClass(name)
}

// javax.lang.model

public fun TypeElement.toClassName(): JavaClassName {
    val simpleName = simpleName.toString()
    val visitor = object : SimpleElementVisitor8<JavaClassName, Void?>() {
        override fun visitPackage(packageElement: PackageElement, p: Void?): JavaClassName {
            return JavaClassName(packageElement.qualifiedName.toString(), simpleName)
        }

        override fun visitType(typeElement: TypeElement, p: Void?): JavaClassName {
            return typeElement.toClassName().nestedClass(simpleName)
        }

        override fun visitUnknown(unknown: Element?, p: Void?): JavaClassName? {
            return JavaClassName("", simpleName)
        }

        override fun defaultAction(element: Element?, p: Void?): JavaClassName? {
            throw IllegalArgumentException("Unexpected type nesting: $element")
        }
    }

    return enclosingElement.accept(visitor, null)
}
