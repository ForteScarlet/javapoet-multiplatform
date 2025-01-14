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

@file:JvmName("ClassNames")
@file:JvmMultifileClass

package love.forte.codepoet.java

import kotlin.reflect.KClass

@JvmName("of")
public fun ClassName(type: KClass<*>): ClassName {
    return ClassName(type.java)
}

@JvmName("of")
public fun ClassName(type: Class<*>): ClassName {
    var java = type
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

        return ClassName(packageName, name)
    }

    return ClassName(java.enclosingClass).nestedClass(name)
}
