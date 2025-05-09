package love.forte.codepoet.java.apt

import love.forte.codegentle.java.JavaFile
import java.io.ByteArrayInputStream
import java.io.IOException
import java.io.InputStream
import java.net.URI
import java.nio.charset.StandardCharsets
import javax.annotation.processing.Filer
import javax.tools.JavaFileObject
import javax.tools.SimpleJavaFileObject


public fun JavaFile.toJavaFileObject(): JavaFileObject {
    val name = if (packageName.isEmpty()) {
        type.name
    } else {
        packageName.replace('.', '/') + '/' + type.name
    }

    val uri = URI.create(name + JavaFileObject.Kind.SOURCE.extension)

    val lastModified = System.currentTimeMillis()

    return object : SimpleJavaFileObject(uri, JavaFileObject.Kind.SOURCE) {
        override fun getCharContent(ignoreEncodingErrors: Boolean): String {
            return this@toJavaFileObject.toString()
        }

        override fun openInputStream(): InputStream {
            return ByteArrayInputStream(getCharContent(true).toByteArray(StandardCharsets.UTF_8))
        }

        override fun getLastModified(): Long = lastModified
    }
}


@Throws(IOException::class)
public fun JavaFile.writeTo(filer: Filer) {
    val fileName = if (packageName.isEmpty()) {
        type.name
    } else {
        packageName + "." + type.name
    }

    val filerSourceFile = filer.createSourceFile(fileName)

    try {
        filerSourceFile.openWriter().use { writer ->
            writeTo(writer)
        }
    } catch (e: Exception) {
        runCatching {
            filerSourceFile.delete()
        }

        throw e
    }
}
