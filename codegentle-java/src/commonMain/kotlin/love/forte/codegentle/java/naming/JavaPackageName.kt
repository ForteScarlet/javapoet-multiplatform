package love.forte.codegentle.java.naming

import love.forte.codegentle.common.naming.PackageName
import love.forte.codegentle.common.naming.appendTo
import love.forte.codegentle.common.writer.InternalWriterApi
import love.forte.codegentle.java.writer.JavaCodeWriter

internal val JavaLangPackage: PackageName = PackageName(listOf("java", "lang"))

internal val PackageName.isJavaLang: Boolean get() = this == JavaLangPackage


@OptIn(InternalWriterApi::class)
internal fun PackageName.emitTo(codeWriter: JavaCodeWriter) {
    appendTo(codeWriter.out)
}
