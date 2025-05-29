package love.forte.codegentle.java.spec

import love.forte.codegentle.common.naming.TypeName
import love.forte.codegentle.common.spec.NamedSpec
import love.forte.codegentle.java.spec.internal.JavaAnnotationTypeSpecImpl

/**
 * A generated annotation type.
 * ```java
 * public @interface Anno {
 * }
 * ```
 */
@SubclassOptInRequired(CodeGentleJavaSpecImplementation::class)
public interface JavaAnnotationTypeSpec : NamedSpec, JavaTypeSpec {
    override val name: String

    override val superclass: TypeName?
        get() = null

    override val superinterfaces: List<TypeName>
        get() = emptyList()

}

public class JavaAnnotationTypeSpecBuilder @PublishedApi internal constructor(
    name: String,
) : JavaTypeSpecBuilder<JavaAnnotationTypeSpecBuilder, JavaAnnotationTypeSpec>(JavaTypeSpec.Kind.ANNOTATION, name) {

    override val self: JavaAnnotationTypeSpecBuilder
        get() = this

    override fun build(): JavaAnnotationTypeSpec {
        // TODO check method must be public abstract?
        // TODO check field must be public static (no private)?

        check(superclass == null) {
            "`superclass` must be null for annotation type."
        }

        check(superinterfaces.isEmpty()) {
            "`superinterfaces` must be empty for annotation type."
        }

        return JavaAnnotationTypeSpecImpl(
            name = name!!,
            kind = kind,
            javadoc = javadoc.build(),
            annotations = annotationRefs.toList(),
            modifiers = modifierSet.copy(),
            typeVariables = typeVariableRefs.toList(),
            fields = fields.toList(),
            staticBlock = staticBlock.build(),
            initializerBlock = initializerBlock.build(),
            methods = methods,
            subtypes = subtypes.toList(),
        )
    }
}


public inline fun JavaAnnotationTypeSpec(
    name: String,
    block: JavaAnnotationTypeSpecBuilder.() -> Unit = {},
): JavaAnnotationTypeSpec {
    return JavaAnnotationTypeSpecBuilder(name).also(block).build()
}
