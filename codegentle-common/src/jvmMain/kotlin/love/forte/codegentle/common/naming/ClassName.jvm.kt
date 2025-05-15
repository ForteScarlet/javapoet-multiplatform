package love.forte.codegentle.common.naming

import javax.lang.model.element.Element
import javax.lang.model.element.PackageElement
import javax.lang.model.element.TypeElement
import javax.lang.model.util.SimpleElementVisitor8
import kotlin.reflect.KClass


/**
 * Create a [love.forte.codegentle.common.naming.ClassName] from [KClass].
 *
 * @see Class.toJavaClassName
 */
public fun KClass<*>.toClassName(): ClassName {
    return java.toClassName()
}

/**
 * Create a [ClassName] from [Class].
 */
public fun Class<*>.toClassName(): ClassName {
    var java = this
    require(!java.isPrimitive) { "Primitive types cannot be represented as a ClassName" }
    require(Void.TYPE != java) { "'void' type cannot be represented as a ClassName" }
    require(!java.isArray) { "Array types cannot be represented as a ClassName" }

    var anonymousSuffix = ""
    while (java.isAnonymousClass) {
        val lastDollar: Int = java.getName().lastIndexOf('$')
        anonymousSuffix = java.getName().substring(lastDollar) + anonymousSuffix
        java = java.getEnclosingClass()
    }
    val name: String = java.getSimpleName() + anonymousSuffix

    if (java.getEnclosingClass() == null) {
        // Avoid unreliable Class.getPackage(). https://github.com/square/javapoet/issues/295
        val lastDot: Int = java.getName().lastIndexOf('.')
        val packageName: String? = if (lastDot != -1) java.getName().substring(0, lastDot) else null

        return ClassName(packageName, name)
    }

    return java.enclosingClass.toClassName().nestedClass(name)
}

public fun TypeElement.toClassName(): ClassName {
    val simpleName = simpleName.toString()
    val visitor = object : SimpleElementVisitor8<ClassName, Void?>() {
        override fun visitPackage(packageElement: PackageElement, p: Void?): ClassName {
            return ClassName(packageElement.qualifiedName.toString(), simpleName)
        }

        override fun visitType(typeElement: TypeElement, p: Void?): ClassName {
            return typeElement.toClassName().nestedClass(simpleName)
        }

        override fun visitUnknown(unknown: Element?, p: Void?): ClassName? {
            return ClassName("", simpleName)
        }

        override fun defaultAction(element: Element?, p: Void?): ClassName? {
            throw IllegalArgumentException("Unexpected type nesting: $element")
        }
    }

    return enclosingElement.accept(visitor, null)
}
