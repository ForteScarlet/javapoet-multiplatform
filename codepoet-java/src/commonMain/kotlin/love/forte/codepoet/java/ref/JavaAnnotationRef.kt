package love.forte.codepoet.java.ref

import love.forte.codepoet.common.computeValueIfAbsent
import love.forte.codepoet.common.ref.AnnotationRef
import love.forte.codepoet.java.JavaCodeEmitter
import love.forte.codepoet.java.JavaCodeValue
import love.forte.codepoet.java.naming.JavaClassName
import love.forte.codepoet.java.ref.internal.JavaAnnotationRefImpl

/**
 * A Java annotation ref.
 *
 * @author ForteScarlet
 */
public interface JavaAnnotationRef : AnnotationRef, JavaCodeEmitter {
    override val className: JavaClassName
    override val members: Map<String, List<JavaCodeValue>>
}

public inline fun JavaClassName.javaAnnotationRef(block: JavaAnnotationRefBuilder.() -> Unit = {}): JavaAnnotationRef {
    return JavaAnnotationRefBuilder(this@javaAnnotationRef).apply {
        block()
    }.build()
}

/**
 * Builder for [JavaAnnotationRef].
 */
public class JavaAnnotationRefBuilder(public val className: JavaClassName) :
    JavaAnnotationRefBuildable<JavaAnnotationRefBuilder> {
    private val members: MutableMap<String, MutableList<JavaCodeValue>> = linkedMapOf()

    override fun addMember(name: String, codeValue: JavaCodeValue): JavaAnnotationRefBuilder = apply {
        val values = members.computeValueIfAbsent(name) { mutableListOf() }
        values.add(codeValue)
    }

    public fun build(): JavaAnnotationRef {
        return JavaAnnotationRefImpl(
            className = className,
            members = members.mapValues { it.value.toList() }
        )
    }
}
