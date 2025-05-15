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

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.java.naming.internal.JavaClassNameImpl
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName

public interface JavaClassName : ClassName, JavaTypeName

public fun ClassName.java(): JavaClassName =
    this as? JavaClassName ?: JavaClassNameImpl(this)

