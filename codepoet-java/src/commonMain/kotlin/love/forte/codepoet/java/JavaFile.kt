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

@file:JvmName("JavaFiles")
@file:JvmMultifileClass

package love.forte.codepoet.java

import love.forte.codepoet.common.BuilderDsl
import love.forte.codepoet.common.code.CodeArgumentPart
import love.forte.codepoet.common.naming.canonicalName
import love.forte.codepoet.java.JavaFile.Builder
import love.forte.codepoet.java.internal.JavaFileImpl
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.spec.JavaTypeSpec
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 *
 * @author ForteScarlet
 */
public interface JavaFile : JavaCodeEmitter {

    public val fileComment: JavaCodeValue
    public val packageName: String

    // TODO types?

    public val type: JavaTypeSpec

    /**
     * Call this to omit imports for classes in `java.lang`, such as `java.lang.String`.
     *
     * By default, JavaPoet explicitly imports types in `java.lang` to defend against
     * naming conflicts. Suppose an (ill-advised) class is named `com.example.String`. When
     * `java.lang` imports are skipped, generated code in `com.example` that references
     * `java.lang.String` will get `com.example.String` instead.
     */
    public val skipJavaLangImports: Boolean

    public val staticImports: Set<String>
    public val alwaysQualify: Set<String>

    public val indent: String

    public fun writeTo(out: Appendable)

    public class Builder internal constructor(
        public val packageName: String,
        public val type: JavaTypeSpec,
    ) : BuilderDsl {
        private val fileComment = JavaCodeValue.builder()
        private var skipJavaLangImports: Boolean = true
        private var indent: String = "    "
        private val staticImports = linkedSetOf<String>()

        public fun addFileComment(format: String, vararg argumentParts: CodeArgumentPart): Builder = apply {
            addFileComment(JavaCodeValue(format, *argumentParts))
        }

        public fun addFileComment(codeValue: JavaCodeValue): Builder = apply {
            fileComment.add(codeValue)
        }

        public fun addStaticImport(import: String): Builder = apply {
            staticImports.add(import)
        }

        public fun addStaticImport(className: JavaClassName, vararg names: String): Builder = apply {
            require(names.isNotEmpty()) { "`names` is empty" }
            for (name in names) {
                staticImports.add(className.canonicalName + "." + name)
            }
        }

        public fun addStaticImport(className: JavaClassName, names: Iterable<String>): Builder = apply {
            val iter = names.iterator()
            require(!iter.hasNext()) { "`names` is empty" }

            for (name in iter) {
                staticImports.add(className.canonicalName + "." + name)
            }
        }

        public fun skipJavaLangImports(skipJavaLangImports: Boolean): Builder = apply {
            this.skipJavaLangImports = skipJavaLangImports
        }

        public fun indent(indent: String): Builder = apply {
            this.indent = indent
        }

        public fun build(): JavaFile {
            val alwaysQualify = linkedSetOf<String>()

            // TODO alwaysQualify.addAll(type.alwaysQualifiedNames)
            //  for (nested in spec.typeSpecs) {
            //    fillAlwaysQualifiedNames(nested, alwaysQualifiedNames)
            //  }


            return JavaFileImpl(
                fileComment = fileComment.build(),
                packageName = packageName,
                type = type,
                skipJavaLangImports = skipJavaLangImports,
                staticImports = LinkedHashSet(staticImports),
                alwaysQualify = alwaysQualify,
                indent = indent
            )
        }
    }

    public companion object {

        @JvmStatic
        public fun builder(packageName: String, type: JavaTypeSpec): Builder =
            Builder(packageName, type)

    }
}

public inline fun Builder.addFileComment(format: String, block: CodeValueSingleFormatBuilderDsl = {}): Builder = apply {
    addFileComment(JavaCodeValue(format, block))
}

public inline fun JavaFile(packageName: String, type: JavaTypeSpec, block: Builder.() -> Unit = {}): JavaFile =
    JavaFile.builder(packageName, type).also(block).build()
