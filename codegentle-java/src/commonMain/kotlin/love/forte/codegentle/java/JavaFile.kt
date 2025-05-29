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

package love.forte.codegentle.java

import love.forte.codegentle.common.BuilderDsl
import love.forte.codegentle.common.code.CodeArgumentPart
import love.forte.codegentle.common.code.CodeValue
import love.forte.codegentle.common.code.CodeValueSingleFormatBuilderDsl
import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.PackageName
import love.forte.codegentle.common.naming.canonicalName
import love.forte.codegentle.common.naming.parseToPackageName
import love.forte.codegentle.java.internal.JavaFileImpl
import love.forte.codegentle.java.spec.JavaTypeSpec
import love.forte.codegentle.java.strategy.JavaWriteStrategy
import love.forte.codegentle.java.writer.JavaCodeEmitter
import kotlin.jvm.JvmMultifileClass
import kotlin.jvm.JvmName
import kotlin.jvm.JvmStatic


/**
 *
 * @author ForteScarlet
 */
public interface JavaFile : JavaCodeEmitter {

    public val fileComment: CodeValue
    public val packageName: PackageName

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

    public fun writeTo(out: Appendable, strategy: JavaWriteStrategy)

    public companion object {

        @JvmStatic
        public fun builder(packageName: PackageName, type: JavaTypeSpec): JavaFileBuilder =
            JavaFileBuilder(packageName, type)

    }
}

public class JavaFileBuilder internal constructor(
    public val packageName: PackageName,
    public val type: JavaTypeSpec,
) : BuilderDsl {
    private val fileComment = CodeValue.builder()
    private var skipJavaLangImports: Boolean = true
    private var indent: String = "    "
    private val staticImports = linkedSetOf<String>()

    public fun addFileComment(format: String, vararg argumentParts: CodeArgumentPart): JavaFileBuilder = apply {
        addFileComment(CodeValue(format, *argumentParts))
    }

    public fun addFileComment(codeValue: CodeValue): JavaFileBuilder = apply {
        fileComment.add(codeValue)
    }

    public fun addStaticImport(import: String): JavaFileBuilder = apply {
        staticImports.add(import)
    }

    public fun addStaticImport(className: ClassName, vararg names: String): JavaFileBuilder = apply {
        require(names.isNotEmpty()) { "`names` is empty" }
        for (name in names) {
            staticImports.add(className.canonicalName + "." + name)
        }
    }

    public fun addStaticImport(className: ClassName, names: Iterable<String>): JavaFileBuilder = apply {
        val iter = names.iterator()
        require(!iter.hasNext()) { "`names` is empty" }

        for (name in iter) {
            staticImports.add(className.canonicalName + "." + name)
        }
    }

    public fun skipJavaLangImports(skipJavaLangImports: Boolean): JavaFileBuilder = apply {
        this.skipJavaLangImports = skipJavaLangImports
    }

    public fun indent(indent: String): JavaFileBuilder = apply {
        this.indent = indent
    }

    public fun build(): JavaFile {
        val alwaysQualify = linkedSetOf<String>()

        // TODO
        //  alwaysQualify.addAll(type.alwaysQualifiedName)
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

public inline fun JavaFileBuilder.addFileComment(
    format: String,
    block: CodeValueSingleFormatBuilderDsl = {}
): JavaFileBuilder = apply {
    addFileComment(CodeValue(format, block))
}

public inline fun JavaFile(
    packageName: PackageName,
    type: JavaTypeSpec,
    block: JavaFileBuilder.() -> Unit = {}
): JavaFile =
    JavaFile.builder(packageName, type).also(block).build()

public inline fun JavaFile(
    packageNamePaths: String,
    type: JavaTypeSpec,
    block: JavaFileBuilder.() -> Unit = {}
): JavaFile =
    JavaFile.builder(packageNamePaths.parseToPackageName(), type).also(block).build()
