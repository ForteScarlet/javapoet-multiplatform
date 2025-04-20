package love.forte.codepoet.java

import love.forte.codepoet.java.CodePart.Companion.literal
import javax.lang.model.SourceVersion
import javax.lang.model.element.AnnotationMirror
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import javax.lang.model.util.SimpleAnnotationValueVisitor8

/*
  public static AnnotationSpec get(AnnotationMirror annotation) {
    TypeElement element = (TypeElement) annotation.getAnnotationType().asElement();
    AnnotationSpec.Builder builder = AnnotationSpec.builder(ClassName.get(element));
    Visitor visitor = new Visitor(builder);
    for (ExecutableElement executableElement : annotation.getElementValues().keySet()) {
      String name = executableElement.getSimpleName().toString();
      AnnotationValue value = annotation.getElementValues().get(executableElement);
      value.accept(visitor, name);
    }
    return builder.build();
  }
 */

public fun AnnotationMirror.toAnnotationSpec(): AnnotationSpec {
    val element = annotationType.asElement() as TypeElement



    return AnnotationSpec(element.toClassName()) {
        TODO()
    }
}

private class AnnotationMirrorVisitor(
    val builder: AnnotationSpec.Builder,
) : SimpleAnnotationValueVisitor8<AnnotationSpec.Builder, String>() {
    override fun defaultAction(o: Any, name: String): AnnotationSpec.Builder {
        return builder.addMemberForValue(name, o)
    }

    override fun visitAnnotation(annotationMirror: AnnotationMirror, name: String): AnnotationSpec.Builder {
        return builder.addMember(name, "%V") {
            literal(annotationMirror.toAnnotationSpec())
        }
    }

    override fun visitEnumConstant(c: VariableElement, name: String): AnnotationSpec.Builder {
        return builder.addMember(name, "%V.%V") {
            // TODO
            //  type(c.asType()) // TypeMirror
            type(c.asType().toTypeName())
            literal(c.simpleName)
        }
    }
}

internal fun AnnotationSpec.Builder.addMemberForValue(memberName: String, value: Any): AnnotationSpec.Builder {
    check(SourceVersion.isName(memberName)) {
        "not a valid name: $memberName"
    }

    return when (value) {
        is Class<*> -> {
            addMember(memberName, "%V.class") {
                // TODO type(Class)
                type(value.toClassName())
            }
        }
        is Enum<*> -> {
            addMember(memberName, "%V.%V") {
                // TODO type(Class)
                type(value.javaClass.toClassName())
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
            addMember(memberName, CodeValue(literal(value)))
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
