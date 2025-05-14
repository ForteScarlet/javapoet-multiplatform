@file:JvmName("JavaMethodSpecs")
@file:JvmMultifileClass

package love.forte.codegentle.java.spec

import love.forte.codegentle.java.JavaModifier
import love.forte.codegentle.java.naming.JavaAnnotationNames
import love.forte.codegentle.java.naming.toJavaTypeName
import love.forte.codegentle.java.naming.toJavaTypeVariableName
import love.forte.codegentle.java.ref.annotationRef
import love.forte.codegentle.java.ref.ref
import javax.lang.model.element.ExecutableElement
import javax.lang.model.element.Modifier
import javax.lang.model.type.TypeVariable

public inline fun ExecutableElement.toJavaMethodSpecOverring(
    block: JavaMethodSpecBuilder.() -> Unit = {}
): JavaMethodSpec {
    val method = this

    val name = method.simpleName.toString()
    val enclosingElement = method.enclosingElement
    require(Modifier.FINAL !in enclosingElement.modifiers) {
        "Cannot override method $name on final class $enclosingElement"
    }
    val methodModifiers = method.modifiers
    require(Modifier.FINAL !in methodModifiers) {
        "Cannot override final method $name on class $enclosingElement"
    }
    require(Modifier.PRIVATE !in methodModifiers) {
        "Cannot override private method $name on class $enclosingElement"
    }
    require(Modifier.STATIC !in methodModifiers) {
        "Cannot override static method $name on class $enclosingElement"
    }

    return JavaMethodSpec(name) {
        addAnnotationRef(JavaAnnotationNames.Override.annotationRef())

        // copy modifiers
        val newModifierSet = mutableSetOf<JavaModifier>()
        newModifierSet.addAll(methodModifiers)
        newModifierSet.remove(JavaModifier.ABSTRACT)
        newModifierSet.remove(JavaModifier.DEFAULT)
        addModifiers(newModifierSet)

        for (typeParameterElement in method.typeParameters) {
            val typeParameterElementAsType = typeParameterElement.asType() as TypeVariable
            addTypeVariable(typeParameterElementAsType.toJavaTypeVariableName())
        }

        returns(method.returnType.toJavaTypeName())
        addParameters(method.javaParameterSpecs)
        varargs(method.isVarArgs)

        addExceptions(method.thrownTypes.map { it.toJavaTypeName().ref() })

        block()
    }
}


