package love.forte.codegentle.java.naming.internal

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.contentEquals
import love.forte.codegentle.java.InternalJavaCodeGentleApi
import love.forte.codegentle.java.naming.JavaClassName
import love.forte.codegentle.java.writer.JavaCodeWriter

/**
 *
 * @author ForteScarlet
 */
public data class JavaClassNameImpl(private val className: ClassName) :
    JavaClassName, ClassName by className {

    @InternalJavaCodeGentleApi
    override fun emit(codeWriter: JavaCodeWriter) {
        emitTo(codeWriter)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassName) return false

        return this contentEquals other
    }

    override fun hashCode(): Int {
        return className.hashCode()
    }


}
