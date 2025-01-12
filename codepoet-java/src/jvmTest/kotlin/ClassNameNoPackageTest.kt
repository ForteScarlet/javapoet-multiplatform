/*
 * Copyright (C) 2019 Square, Inc.
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

import love.forte.codepoet.java.ClassName
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull


class ClassNameNoPackageTest {

    @Test
    fun shouldSupportClassInDefaultPackage() {
        val className = ClassName(ClassNameNoPackageTest::class)
        assertNull(className.packageName)
        assertEquals("ClassNameNoPackageTest", className.simpleName)
        assertEquals("ClassNameNoPackageTest", className.toString())
    }

}