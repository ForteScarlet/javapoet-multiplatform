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

import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.nio.file.Path
import kotlin.io.path.bufferedWriter
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory
import kotlin.io.path.notExists

@JvmOverloads
@Throws(IOException::class)
public fun JavaFile.writeTo(directory: Path, charset: Charset = Charsets.UTF_8) {
    require(directory.notExists() || directory.isDirectory()) {
        "Path $directory does not exist or is not a directory."
    }

    var outputDir = directory

    if (packageName.isNotEmpty()) {
        for (packagePart in packageName.split('.')) {
            outputDir = outputDir.resolve(packagePart)
        }
        outputDir.createDirectories()
    }

    val outputPath = outputDir.resolve(type.name + ".java")

    outputPath.bufferedWriter(charset = charset).use { writer ->
        writeTo(writer)
    }
}

@JvmOverloads
@Throws(IOException::class)
public fun JavaFile.writeTo(directory: File, charset: Charset = Charsets.UTF_8) {
    writeTo(directory.toPath(), charset)
}
