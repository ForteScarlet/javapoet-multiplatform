
@file:JvmName("JavaAnnotationSpecs")
@file:JvmMultifileClass

package love.forte.codegentle.java.spec

import love.forte.codegentle.common.code.CodePart.Companion.literal
import love.forte.codegentle.java.*
import love.forte.codegentle.java.naming.toJavaClassName
import love.forte.codegentle.java.naming.toJavaTypeName
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.SimpleAnnotationValueVisitor8

public fun AnnotationMirror.toAnnotationSpec(): JavaAnnotationSpec {
    val element = annotationType.asElement() as TypeElement
    return JavaAnnotationSpec(element.toJavaClassName()) {
        val visitor = AnnotationMirrorVisitor(this)
        for (executableElement in this@toAnnotationSpec.elementValues.keys) {
            val name = executableElement.simpleName.toString()
            this@toAnnotationSpec.elementValues[executableElement]?.accept(visitor, name)
        }
    }
}

private class AnnotationMirrorVisitor(
    val builder: JavaAnnotationSpecBuilder,
) : SimpleAnnotationValueVisitor8<JavaAnnotationSpecBuilder, String>() {
    override fun defaultAction(o: Any, name: String): JavaAnnotationSpecBuilder {
        return builder.addMemberForValue(name, o)
    }

    override fun visitAnnotation(annotationMirror: AnnotationMirror, name: String): JavaAnnotationSpecBuilder {
        return builder.addMember(name, "%V") {
            literal(annotationMirror.toAnnotationSpec())
        }
    }

    override fun visitEnumConstant(c: VariableElement, name: String): JavaAnnotationSpecBuilder {
        return builder.addMember(name, "%V.%V") {
            type(c.asType().toJavaTypeName())
            literal(c.simpleName)
        }
    }
}

internal fun JavaAnnotationSpecBuilder.addMemberForValue(memberName: String, value: Any): JavaAnnotationSpecBuilder {
    check(SourceVersion.isName(memberName)) {
        "not a valid name: $memberName"
    }

    return when (value) {
        is Class<*> -> {
            addMember(memberName, "%V.class") {
                // TODO type(Class)
                type(value.toJavaClassName())
            }
        }

        is Enum<*> -> {
            addMember(memberName, "%V.%V") {
                // TODO type(Class)
                type(value.javaClass.toJavaClassName())
                literal(value.name)
            }
        }

        is String -> {
            addMember(memberName, "%V") {
                string(value)
            }
        }

        is Float -> {
            addMember(memberName, "%Vf") {
                literal(value)
            }
        }

        is Long -> {
            addMember(memberName, "%VL") {
                literal(value)
            }
        }

        is Char -> {
            addMember(memberName, "'%L'") {
                literal((value.characterLiteralWithoutSingleQuotes()))
            }
        }

        else -> {
            addMember(memberName, JavaCodeValue(literal(value)))
        }
    }
}

/*
private static class Visitor extends SimpleAnnotationValueVisitor8<Builder, String> {
    final Builder builder;
    @Override public Builder visitEnumConstant(VariableElement c, String name) {
      return builder.addMember(name, "$T.$L", c.asType(), c.getSimpleName());
    }

    @Override public Builder visitType(TypeMirror t, String name) {
      return builder.addMember(name, "$T.class", t);
    }

    @Override public Builder visitArray(List<? extends AnnotationValue> values, String name) {
      for (AnnotationValue value : values) {
        value.accept(this, name);
      }
      return builder;
    }
  }
 */
