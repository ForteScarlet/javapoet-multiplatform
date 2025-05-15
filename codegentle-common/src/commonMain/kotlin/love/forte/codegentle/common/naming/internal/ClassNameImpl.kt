package love.forte.codegentle.common.naming.internal

import love.forte.codegentle.common.naming.ClassName
import love.forte.codegentle.common.naming.canonicalName
import love.forte.codegentle.common.naming.contentEquals
import love.forte.codegentle.common.naming.contentHashCode

/**
 *
 * @author ForteScarlet
 */
internal data class ClassNameImpl(
    override val packageName: String?,
    override val enclosingClassName: ClassName?,
    override val simpleName: String,
) : ClassName {
    override val topLevelClassName: ClassName
        get() = enclosingClassName?.topLevelClassName ?: this


    override fun peerClass(name: String): ClassName {
        return ClassNameImpl(packageName, enclosingClassName, name)
    }

    override fun nestedClass(name: String): ClassName {
        return ClassNameImpl(packageName, this, name)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is ClassName) return false

        return this contentEquals other
    }

    override fun hashCode(): Int {
        return contentHashCode()
    }

    override fun toString(): String {
        return canonicalName
    }
}
